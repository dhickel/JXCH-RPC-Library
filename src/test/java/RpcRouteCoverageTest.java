import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.http.*;
import io.mindspice.jxch.rpc.util.JsonUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RpcRouteCoverageTest {
    private static final Map<String, Class<? extends ChiaAPI>> APIS = Map.of(
            "data_layer", DataLayerAPI.class, "wallet", WalletAPI.class,
            "full_node", FullNodeAPI.class, "farmer", FarmerAPI.class,
            "harvester", HarvesterAPI.class, "crawler", CrawlerAPI.class,
            "solver", SolverAPI.class, "timelord", TimelordAPI.class);

    @Test
    void everyStableHttpRouteHasACallableRawMethod() throws Exception {
        JsonNode inventory;
        try (var input = getClass().getResourceAsStream("/chia-2.7.5-routes.json")) {
            assertNotNull(input);
            inventory = JsonUtils.readTree(input.readAllBytes());
        }
        assertEquals("ff71448437a137c7092775923d5ecbbe104433e7", inventory.get("commit").asText());
        for (var entry : APIS.entrySet()) {
            var client = new CapturingClient();
            var api = entry.getValue().getConstructor(RPCClient.class).newInstance(client);
            var expected = new TreeSet<String>();
            inventory.get("shared").forEach(route -> expected.add("/" + route.asText()));
            inventory.get(entry.getKey()).forEach(route -> expected.add("/" + route.asText()));
            var observed = new TreeSet<String>();
            for (Method method : entry.getValue().getMethods()) {
                if (method.getReturnType() != byte[].class || method.isAnnotationPresent(Deprecated.class)) {
                    continue;
                }
                client.request = null;
                Object[] args = Arrays.stream(method.getParameterTypes()).map(RpcRouteCoverageTest::sample).toArray();
                method.invoke(api, args);
                assertNotNull(client.request, method.toString());
                assertEquals(ChiaService.valueOf(entry.getKey().toUpperCase(Locale.ROOT)), client.request.service,
                        method.toString());
                assertTrue(JsonUtils.readTree(client.request.data).isObject(), method.toString());
                observed.add(client.request.endpoint);
            }
            var missing = new TreeSet<>(expected);
            missing.removeAll(observed);
            assertTrue(missing.isEmpty(), entry.getKey() + " has no callable raw method for " + missing);
            var obsolete = new TreeSet<>(observed);
            obsolete.removeAll(expected);
            assertTrue(obsolete.isEmpty(), entry.getKey() + " exposes non-deprecated obsolete routes " + obsolete);
        }
    }

    @Test
    void endpointPathsDoNotDependOnDefaultLocale() throws Exception {
        var original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            for (String name : List.of("Common", "DataLayer", "Wallet", "FullNode", "Farmer", "Harvester", "Crawler", "Solver")) {
                var type = Class.forName("io.mindspice.jxch.rpc.enums.endpoints." + name);
                for (Object value : type.getEnumConstants()) {
                    var endpoint = (io.mindspice.jxch.rpc.enums.endpoints.Endpoint) value;
                    assertEquals("/" + ((Enum<?>) value).name().toLowerCase(Locale.ROOT), endpoint.getPath());
                }
            }
        } finally {
            Locale.setDefault(original);
        }
    }

    private static Object sample(Class<?> type) {
        if (type == String.class) return "00".repeat(32);
        if (type == int.class || type == Integer.class) return 1;
        if (type == long.class || type == Long.class) return 1L;
        if (type == boolean.class || type == Boolean.class) return true;
        if (type == double.class || type == Double.class) return 1.0;
        if (type == BigInteger.class || type == Number.class || type == Object.class) return BigInteger.ONE;
        if (JsonNode.class.isAssignableFrom(type)) return JsonUtils.newEmptyNode();
        if (List.class.isAssignableFrom(type)) return List.of();
        if (Map.class.isAssignableFrom(type)) return Map.of();
        if (type.isEnum()) return type.getEnumConstants()[0];
        if (type.isRecord()) {
            try {
                var components = type.getRecordComponents();
                var types = Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
                return type.getDeclaredConstructor(types).newInstance(Arrays.stream(types)
                        .map(RpcRouteCoverageTest::sample).toArray());
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("Cannot create request fixture for " + type, e);
            }
        }
        throw new IllegalArgumentException("Add an explicit request fixture for " + type);
    }

    private static final class CapturingClient extends RPCClient {
        Request request;

        CapturingClient() {
            super(new NodeConfig.Builder().build());
        }

        @Override
        public byte[] makeRequest(Request request) {
            this.request = request;
            return "{\"success\":false,\"error\":\"contract fixture\"}".getBytes(StandardCharsets.UTF_8);
        }
    }
}
