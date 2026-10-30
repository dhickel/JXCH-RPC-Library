import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Common;
import io.mindspice.jxch.rpc.http.*;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommonContractTest {
    static class Client extends RPCClient {
        Request last;
        String response = "{\"success\":true}";
        boolean failTransport;
        Client() { super(new NodeConfig.Builder().build()); }
        @Override public byte[] makeRequest(Request request) throws RPCException {
            last = request;
            if (failTransport) throw new RPCException("transport failed");
            return response.getBytes(StandardCharsets.UTF_8);
        }
    }
    record Case(String method, String request, String response, Object... args) { }
    static List<Case> cases() {
        return List.of(
                new Case("closeConnection", "{\"node_id\":\"0xabc\"}", "{\"success\":true}", "0xabc"),
                new Case("getConnections", "{}", "{\"success\":true,\"connections\":[]}"),
                new Case("getRoutes", "{}", "{\"success\":true,\"routes\":[\"/healthz\"]}"),
                new Case("healthz", "{}", "{\"success\":true}"),
                new Case("openConnection", "{\"host\":\"example.org\",\"port\":8444}", "{\"success\":true}", "example.org", 8444),
                new Case("stopNode", "{}", "{\"success\":true}"),
                new Case("getNetworkInfo", "{}", "{\"success\":true,\"network_name\":\"mainnet\",\"network_prefix\":\"xch\",\"genesis_challenge\":\"0xab\"}"),
                new Case("getVersion", "{}", "{\"success\":true,\"version\":\"2.7.5\"}"),
                new Case("getLogLevel", "{}", "{\"success\":true,\"level\":\"INFO\",\"available_levels\":[\"INFO\",\"DEBUG\"]}"),
                new Case("setLogLevel", "{\"level\":\"DEBUG\"}", "{\"success\":true,\"level\":\"DEBUG\",\"available_levels\":[\"DEBUG\"],\"errors\":[]}", "DEBUG"),
                new Case("resetLogLevel", "{}", "{\"success\":true,\"level\":\"INFO\",\"available_levels\":[\"INFO\"],\"errors\":[]}")
        );
    }
    static Object invoke(Object api, String name, Object... args) throws Exception {
        var method = Arrays.stream(api.getClass().getMethods()).filter(m -> m.getName().equals(name)
                && m.getParameterCount() == args.length
                && java.util.stream.IntStream.range(0, args.length).allMatch(i -> {
                    var type = m.getParameterTypes()[i];
                    if (args[i] == null) return !type.isPrimitive();
                    if (type == int.class) type = Integer.class;
                    if (type == long.class) type = Long.class;
                    if (type == boolean.class) type = Boolean.class;
                    return type.isInstance(args[i]);
                })).findFirst().orElseThrow();
        try { return method.invoke(api, args); }
        catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception ex) throw ex;
            throw e;
        }
    }
    static String route(String method) { return "/" + method.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT); }

    @Test void allElevenRoutesBytesParsedPayloadsAndFailuresOnEveryScopedService() throws Exception {
        var client = new Client();
        var apis = Map.of(ChiaService.FARMER, new FarmerAPI(client), ChiaService.HARVESTER, new HarvesterAPI(client),
                ChiaService.DATA_LAYER, new DataLayerAPI(client), ChiaService.CRAWLER, new CrawlerAPI(client),
                ChiaService.SOLVER, new SolverAPI(client), ChiaService.TIMELORD, new TimelordAPI(client));
        var expected = new HashSet<String>();
        for (var entry : apis.entrySet()) {
            for (var c : cases()) {
                expected.add(route(c.method));
                client.response = c.response;
                var parsed = (ApiResponse<?>) invoke(entry.getValue(), c.method, c.args);
                assertTrue(parsed.success(), entry.getKey() + ": " + c.method);
                assertTrue(parsed.data().isPresent(), c.method);
                assertEquals(entry.getKey(), client.last.service);
                assertEquals(route(c.method), client.last.endpoint);
                assertEquals(JsonUtils.readTree(c.request), JsonUtils.readTree(client.last.data));
                assertEquals(new NodeConfig.Builder().build().getAddressOf(entry.getKey()) + route(c.method), parsed.requestURI());
                assertEquals(route(c.method), parsed.endpoint().getPath());
                assertEquals(entry.getKey(), parsed.endpoint().getService());
                var bytes = (byte[]) invoke(entry.getValue(), c.method + "AsBytes", c.args);
                assertEquals(c.response, new String(bytes, StandardCharsets.UTF_8));
                assertEquals(entry.getKey(), client.last.service);
                client.response = "{\"success\":false,\"error\":\"failure\"}";
                var failed = (ApiResponse<?>) invoke(entry.getValue(), c.method, c.args);
                assertFalse(failed.success());
                assertTrue(failed.data().isEmpty());
                assertEquals("failure", failed.error());
                client.response = "not-json";
                assertThrows(RPCException.class, () -> invoke(entry.getValue(), c.method, c.args));
            }
        }
        var inventory = JsonUtils.readTree(getClass().getResourceAsStream("/chia-2.7.5-routes.json").readAllBytes());
        var listed = new HashSet<String>();
        inventory.get("shared").forEach(n -> listed.add("/" + n.asText()));
        assertEquals(listed, expected);
        assertEquals(listed, Arrays.stream(Common.values()).map(Common::getPath).collect(java.util.stream.Collectors.toSet()));
    }

    @Test void newCommonRoutesAlsoInheritedByFullNodeAndWallet() throws Exception {
        var client = new Client();
        for (var api : List.of(new FullNodeAPI(client), new WalletAPI(client))) {
            for (var c : cases().subList(6, 11)) {
                client.response = c.response;
                assertTrue(((ApiResponse<?>) invoke(api, c.method, c.args)).success());
                assertEquals(api instanceof FullNodeAPI ? ChiaService.FULL_NODE : ChiaService.WALLET, client.last.service);
            }
        }
    }

    @Test void legacyNetworkTypePreservesGenesis() throws Exception {
        var client = new Client();
        client.response = cases().get(6).response;
        io.mindspice.jxch.rpc.schemas.ApiResponse<io.mindspice.jxch.rpc.schemas.fullnode.Network> wallet = new WalletAPI(client).getNetworkInfo();
        io.mindspice.jxch.rpc.schemas.ApiResponse<io.mindspice.jxch.rpc.schemas.fullnode.Network> node = new FullNodeAPI(client).getNetworkInfo();
        assertEquals("0xab", wallet.data().orElseThrow().genesisChallenge());
        assertEquals("0xab", node.data().orElseThrow().genesisChallenge());
        assertNull(new io.mindspice.jxch.rpc.schemas.fullnode.Network("mainnet", "xch").genesisChallenge());
    }

    @Test void typedNetworkLogsConnectionFilterAndTransportFailures() throws Exception {
        var client = new Client();
        var api = new SolverAPI(client);
        client.response = cases().get(6).response;
        assertEquals("xch", api.getNetworkInfo().data().orElseThrow().networkPrefix());
        client.response = cases().get(8).response;
        assertEquals(List.of("INFO", "DEBUG"), api.getLogLevel().data().orElseThrow().availableLevels());
        client.response = "{\"success\":false,\"errors\":[\"Unknown level\"]}";
        assertTrue(api.setLogLevel("bad").error().contains("Unknown level"));
        client.response = "{\"success\":true,\"connections\":[{\"creation_time\":1700000000.125,\"bytes_read\":5000000000,\"bytes_written\":6000000000,\"last_message_time\":1700000000.75,\"peak_weight\":18446744073709551615}]}";
        var connection = api.getConnections(8).data().orElseThrow().get(0);
        assertEquals(5000000000L, connection.bytesRead());
        assertEquals(1700000000.125, connection.creationTime());
        assertEquals("18446744073709551615", connection.peakWeight().toString());
        assertEquals(JsonUtils.readTree("{\"node_type\":8}"), JsonUtils.readTree(client.last.data));
        for (var malformed : List.of("{}", "null", "[]", "{\"success\":\"true\"}")) {
            client.response = malformed;
            assertThrows(RPCException.class, api::getVersion);
        }
        client.failTransport = true;
        assertEquals("transport failed", assertThrows(RPCException.class, api::getVersion).getMessage());
        assertThrows(IllegalStateException.class, () -> new Request(Common.GET_VERSION, new byte[0]));
    }
}
