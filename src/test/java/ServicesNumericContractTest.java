import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.http.*;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.farmer.PlotFilter;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServicesNumericContractTest {
    // Pinned 2.7.5 farmer_rpc_api.py PlotInfoRequestData/PlotPathRequestData: uint32.
    // data_layer_rpc_api.py and crawler_rpc_api.py pass Python integers through without narrowing.
    static final long UINT32_MAX = 0xffff_ffffL;
    static final BigInteger HUGE = BigInteger.ONE.shiftLeft(100).add(BigInteger.valueOf(123));
    static final List<Number> NON_INTEGERS = List.of(1.25, 1.0f, Double.NaN, Double.POSITIVE_INFINITY,
            new BigDecimal("1"), new java.util.concurrent.atomic.AtomicLong(1));
    static final List<String> PLOT_METHODS = List.of("getHarvesterPlotsValid", "getHarvesterPlotsInvalid",
            "getHarvesterPlotsKeysMissing", "getHarvesterPlotsDuplicates");

    static Object call(Method method, Object api, Object... args) throws Throwable {
        try { return method.invoke(api, args); }
        catch (InvocationTargetException e) { throw e.getCause(); }
    }

    static JsonNode body(CommonContractTest.Client client) throws Exception {
        return JsonUtils.readTree(client.last.data);
    }

    @Test void allFarmerPaginationMethodsKeepIntCallsAndExposeFullUint32Paths() throws Throwable {
        var client = new CommonContractTest.Client();
        var api = new FarmerAPI(client);
        for (String name : PLOT_METHODS) {
            Method raw = FarmerAPI.class.getMethod(name + "AsBytes", String.class, List.class, long.class, long.class, boolean.class);
            call(raw, api, "node", List.of(), UINT32_MAX, UINT32_MAX, false);
            assertEquals(UINT32_MAX, body(client).get("page").longValue());
            assertEquals(UINT32_MAX, body(client).get("page_size").longValue());
            Method legacyRaw = FarmerAPI.class.getMethod(name + "AsBytes", String.class, List.class, int.class, int.class, boolean.class);
            call(legacyRaw, api, "node", List.of(), 7, 8, true);
            assertEquals(7, body(client).get("page").intValue());
            assertEquals(8, body(client).get("page_size").intValue());
            client.response = "{\"success\":true,\"page\":0,\"page_count\":1,\"plots\":[]}";
            Method parsed = FarmerAPI.class.getMethod(name, String.class, List.class, long.class, boolean.class);
            assertTrue(((ApiResponse<?>) call(parsed, api, "node", List.of(), UINT32_MAX, false)).success());
            assertEquals(UINT32_MAX, body(client).get("page_size").longValue());
            Method legacy = FarmerAPI.class.getMethod(name, String.class, List.class, int.class, boolean.class);
            assertTrue(((ApiResponse<?>) call(legacy, api, "node", List.of(), 5, false)).success());
            for (long invalid : new long[]{-1, UINT32_MAX + 1, Long.MAX_VALUE}) {
                assertThrows(IllegalArgumentException.class, () -> call(raw, api, "node", List.of(), invalid, 1L, false));
                assertThrows(IllegalArgumentException.class, () -> call(raw, api, "node", List.of(), 0L, invalid, false));
                assertThrows(IllegalArgumentException.class, () -> call(parsed, api, "node", List.of(), invalid, false));
            }
        }
        var filter = List.of(new PlotFilter("pool_public_key", null));
        api.getHarvesterPlotsValidAsBytes("node", filter, "file_size", UINT32_MAX, UINT32_MAX, true);
        assertEquals(UINT32_MAX, body(client).get("page").longValue());
        assertEquals(UINT32_MAX, body(client).get("page_size").longValue());
        assertTrue(body(client).at("/filter/0/value").isNull());
        assertEquals("file_size", body(client).get("sort_key").textValue());
        assertTrue(api.getHarvesterPlotsValid("node", filter, "file_size", UINT32_MAX, false).success());
        assertThrows(IllegalArgumentException.class,
                () -> api.getHarvesterPlotsValidAsBytes("node", filter, "file_size", UINT32_MAX + 1, 1L, false));
        assertThrows(IllegalArgumentException.class,
                () -> api.getHarvesterPlotsValid("node", filter, "file_size", UINT32_MAX + 1, false));
    }

    @Test void farmerPaginationDoesNotTruncateResponsePageNumbers() throws Throwable {
        class Pages extends CommonContractTest.Client {
            final ArrayDeque<String> replies = new ArrayDeque<>();
            @Override public byte[] makeRequest(Request request) {
                last = request;
                return replies.remove().getBytes(StandardCharsets.UTF_8);
            }
        }
        var client = new Pages();
        var api = new FarmerAPI(client);
        for (String name : PLOT_METHODS) {
            client.replies.add("{\"success\":true,\"page\":2147483647,\"page_count\":2147483649,\"plots\":[]}");
            client.replies.add("{\"success\":true,\"page\":2147483648,\"page_count\":2147483649,\"plots\":[]}");
            Method parsed = FarmerAPI.class.getMethod(name, String.class, List.class, long.class, boolean.class);
            assertTrue(((ApiResponse<?>) call(parsed, api, "node", List.of(), 1L, false)).success());
            assertEquals(2147483648L, body(client).get("page").longValue());
            assertTrue(client.replies.isEmpty());
            for (String page : List.of("1.5", "4294967296", "-1")) {
                client.replies.add("{\"success\":true,\"page\":" + page + ",\"page_count\":1,\"plots\":[]}");
                assertThrows(RPCException.class, () -> call(parsed, api, "node", List.of(), 1L, false));
            }
        }
    }

    @Test void farmerRewardSearchDepthAcceptsExactPythonIntegersWithoutBreakingLegacyCalls() throws Exception {
        var client = new CommonContractTest.Client();
        var api = new FarmerAPI(client);
        for (Number value : List.of(Integer.MAX_VALUE, UINT32_MAX, HUGE, HUGE.negate())) {
            api.getRewardTargetsAsBytes(false, value);
            assertEquals(new BigInteger(value.toString()), body(client).get("max_ph_to_search").bigIntegerValue());
            assertTrue(api.getRewardTargets(false, value).success());
        }
        api.getRewardTargetsAsBytes(false, 500);
        assertEquals(500, body(client).get("max_ph_to_search").intValue());
        for (Number invalid : NON_INTEGERS)
            assertThrows(IllegalArgumentException.class, () -> api.getRewardTargetsAsBytes(false, invalid));
        assertThrows(IllegalArgumentException.class, () -> api.getRewardTargets(false, (Number) null));
    }

    @Test void dataLayerPaginationAcceptsExactUnboundedIntegersAndRetainsNullDefaults() throws Throwable {
        var client = new CommonContractTest.Client();
        var api = new DataLayerAPI(client);
        for (String name : List.of("getKeys", "getKeysValues", "getKvDiff")) {
            boolean diff = name.equals("getKvDiff");
            Class<?>[] types = diff
                    ? new Class<?>[]{String.class, String.class, String.class, Number.class, Number.class}
                    : new Class<?>[]{String.class, String.class, Number.class, Number.class};
            Method raw = DataLayerAPI.class.getMethod(name + "AsBytes", types);
            Method parsed = DataLayerAPI.class.getMethod(name, types);
            for (Number value : List.of(2147483648L, HUGE, HUGE.negate())) {
                Object[] args = diff ? new Object[]{"id", "hash1", "hash2", value, HUGE}
                        : new Object[]{"id", null, value, HUGE};
                call(raw, api, args);
                assertEquals(new BigInteger(value.toString()), body(client).get("page").bigIntegerValue());
                assertEquals(HUGE, body(client).get("max_page_size").bigIntegerValue());
                assertTrue(((ApiResponse<?>) call(parsed, api, args)).success());
            }
            Object[] defaults = diff ? new Object[]{"id", "hash1", "hash2", null, null}
                    : new Object[]{"id", null, null, null};
            call(raw, api, defaults);
            assertFalse(body(client).has("page"));
            assertFalse(body(client).has("max_page_size"));
            for (Number invalid : NON_INTEGERS) {
                Object[] args = defaults.clone();
                args[args.length - 2] = invalid;
                assertThrows(IllegalArgumentException.class, () -> call(raw, api, args));
                args[args.length - 2] = 0;
                args[args.length - 1] = invalid;
                assertThrows(IllegalArgumentException.class, () -> call(parsed, api, args));
            }
        }
        api.walletLogInAsBytes(UINT32_MAX);
        assertEquals(UINT32_MAX, body(client).get("fingerprint").longValue());
        assertThrows(IllegalArgumentException.class, () -> api.walletLogIn(-1));
        assertThrows(IllegalArgumentException.class, () -> api.walletLogInAsBytes(UINT32_MAX + 1));
    }

    @Test void crawlerAfterOffsetAndLimitAreExactPythonIntegersIncludingNegativeSlices() throws Exception {
        var client = new CommonContractTest.Client();
        var api = new CrawlerAPI(client);
        for (BigInteger value : List.of(HUGE, HUGE.negate())) {
            api.getIpsAfterTimestampAsBytes(value, value, value);
            for (String field : List.of("after", "offset", "limit"))
                assertEquals(value, body(client).get(field).bigIntegerValue());
            assertTrue(api.getIpsAfterTimestamp(value, value, value).success());
        }
        api.getIpsAfterTimestampAsBytes(HUGE);
        assertEquals(HUGE, body(client).get("after").bigIntegerValue());
        assertEquals(0, body(client).get("offset").intValue());
        assertEquals(10000, body(client).get("limit").intValue());
        assertTrue(api.getIpsAfterTimestamp(HUGE).success());
        for (Number invalid : NON_INTEGERS) {
            assertThrows(IllegalArgumentException.class, () -> api.getIpsAfterTimestampAsBytes(invalid, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> api.getIpsAfterTimestampAsBytes(0, invalid, 1));
            assertThrows(IllegalArgumentException.class, () -> api.getIpsAfterTimestamp(0, 0, invalid));
        }
        assertThrows(IllegalArgumentException.class, () -> api.getIpsAfterTimestampAsBytes(null));
        assertThrows(IllegalArgumentException.class, () -> api.getIpsAfterTimestampAsBytes(0, null, 1));
        assertThrows(IllegalArgumentException.class, () -> api.getIpsAfterTimestampAsBytes(0, 0, null));
    }

    @Test void integerResponseFieldsRejectFractionalWireValuesRatherThanTruncating() {
        var client = new CommonContractTest.Client();
        var dl = new DataLayerAPI(client);
        client.response = "{\"success\":true,\"keys\":[],\"total_pages\":1.5}";
        assertThrows(RPCException.class, () -> dl.getKeys("id", null, 0, null));
        client.response = "{\"success\":true,\"keys_values\":[],\"total_bytes\":1.5}";
        assertThrows(RPCException.class, () -> dl.getKeysValues("id", null, 0, null));
        client.response = "{\"success\":true,\"diff\":[],\"total_bytes\":1.5}";
        assertThrows(RPCException.class, () -> dl.getKvDiff("id", "hash1", "hash2", 0, null));
        client.response = "{\"success\":true,\"timestamp\":1.5}";
        assertThrows(RPCException.class, () -> dl.getRoot("id"));
        client.response = "{\"success\":true,\"root_hashes\":[{\"timestamp\":1.5}]}";
        assertThrows(RPCException.class, () -> dl.getRoots(List.of("id")));
        client.response = "{\"success\":true,\"root_history\":[{\"timestamp\":1.5}]}";
        assertThrows(RPCException.class, () -> dl.getRootHistory("id"));
        client.response = "{\"success\":true,\"mirrors\":[{\"amount\":1.5}]}";
        assertThrows(RPCException.class, () -> dl.getMirrors("id"));
        client.response = "{\"success\":true,\"valid\":true,\"fee\":1.5}";
        assertThrows(RPCException.class, () -> dl.verifyOffer(DataLayerContractTest.OFFER, null));
        client.response = "{\"success\":true,\"root\":{\"generation\":1.5}}";
        assertThrows(RPCException.class, () -> dl.clearPendingRoots("id"));
        client.response = "{\"success\":true,\"sync_status\":{\"generation\":1.5}}";
        assertThrows(RPCException.class, () -> dl.getSyncStatus("id"));
        var crawler = new CrawlerAPI(client);
        client.response = "{\"success\":true,\"ips\":[],\"total\":1.5}";
        assertThrows(RPCException.class, () -> crawler.getIpsAfterTimestamp(0));
        client.response = "{\"success\":true,\"peer_counts\":{\"total_last_5_days\":1.5}}";
        assertThrows(RPCException.class, crawler::getPeerCounts);
        client.response = "{\"success\":true,\"peer_counts\":{\"versions\":{\"2.7.5\":1.5}}}";
        assertThrows(RPCException.class, crawler::getPeerCounts);
    }

    @Test void dataLayerAndCrawlerPythonIntegerResponsesAreLossless() throws Exception {
        var client = new CommonContractTest.Client();
        var dl = new DataLayerAPI(client);
        String totals = "\"total_pages\":" + HUGE + ",\"total_bytes\":" + HUGE;
        client.response = "{\"success\":true,\"keys\":[]," + totals + ",\"root_hash\":null}";
        var keys = dl.getKeys("id", null, 0, HUGE).data().orElseThrow();
        assertEquals(HUGE, keys.totalPages());
        assertEquals(HUGE, keys.totalBytes());
        assertNull(keys.rootHash());
        client.response = "{\"success\":true,\"keys_values\":[]," + totals + ",\"root_hash\":null}";
        var values = dl.getKeysValues("id", null, 0, HUGE).data().orElseThrow();
        assertEquals(HUGE, values.totalPages());
        assertEquals(HUGE, values.totalBytes());
        assertNull(values.rootHash());
        client.response = "{\"success\":true,\"diff\":[]," + totals + "}";
        var diff = dl.getKvDiff("id", "hash1", "hash2", 0, HUGE).data().orElseThrow();
        assertEquals(HUGE, diff.totalPages());
        assertEquals(HUGE, diff.totalBytes());
        client.response = "{\"success\":true,\"sync_status\":{\"generation\":-1,\"target_generation\":" + HUGE + "}}";
        var sync = dl.getSyncStatus("id").data().orElseThrow();
        assertEquals(BigInteger.valueOf(-1), sync.generation());
        assertEquals(HUGE, sync.targetGeneration());
        client.response = "{\"success\":true,\"root\":{\"node_hash\":null,\"generation\":" + HUGE + ",\"status\":3}}";
        var root = dl.clearPendingRoots("id").data().orElseThrow();
        assertEquals(HUGE, root.generation());
        assertNull(root.nodeHash());
        var crawler = new CrawlerAPI(client);
        client.response = "{\"success\":true,\"ips\":[],\"total\":" + HUGE + "}";
        assertEquals(HUGE, crawler.getIpsAfterTimestamp(0).data().orElseThrow().total());
        client.response = "{\"success\":true,\"peer_counts\":{\"total_last_5_days\":" + HUGE
                + ",\"reliable_nodes\":" + HUGE + ",\"ipv4_last_5_days\":" + HUGE + ",\"ipv6_last_5_days\":" + HUGE
                + ",\"versions\":{\"2.7.5\":" + HUGE + "}}}";
        var counts = crawler.getPeerCounts().data().orElseThrow();
        assertEquals(HUGE, counts.totalLast5Days());
        assertEquals(HUGE, counts.reliableNodes());
        assertEquals(HUGE, counts.ipv4Last5Days());
        assertEquals(HUGE, counts.ipv6Last5Days());
        assertEquals(HUGE, counts.versions().get("2.7.5"));
    }
}
