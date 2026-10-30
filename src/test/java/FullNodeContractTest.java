import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.FullNode;
import io.mindspice.jxch.rpc.http.FullNodeAPI;
import io.mindspice.jxch.rpc.http.RPCClient;
import io.mindspice.jxch.rpc.http.Request;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FullNodeContractTest {
    private static final String COIN = "{\"parent_coin_info\":\"aa\",\"puzzle_hash\":\"bb\",\"amount\":17}";
    private static final String SPEND = "{\"coin\":" + COIN + ",\"puzzle_reveal\":\"0x80\",\"solution\":\"0x80\"}";

    static class Capture extends RPCClient {
        Request request;
        String response = "{\"success\":false,\"error\":\"denied\"}";
        Capture() { super(new NodeConfig.Builder().build()); }
        @Override public byte[] makeRequest(Request request) {
            this.request = request;
            return response.getBytes(StandardCharsets.UTF_8);
        }
        JsonNode body() throws Exception { return JsonUtils.readTree(request.data); }
    }

    private Object[] arguments(Method method) {
        var types = method.getParameterTypes();
        var args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] == String.class) args[i] = "aa";
            else if (types[i] == int.class) args[i] = 2;
            else if (types[i] == long.class) args[i] = 3L;
            else if (types[i] == boolean.class) args[i] = true;
            else if (types[i] == BigInteger.class || types[i] == Number.class || types[i] == Object.class) args[i] = BigInteger.TEN;
            else if (types[i] == List.class) args[i] = method.getName().contains("FeeEstimate") ? List.of(30, 60) : List.of("aa");
            else if (types[i] == SpendBundle.class) args[i] = new SpendBundle("cc", List.of(), null, null);
            else fail("Uncovered parameter " + types[i]);
        }
        return args;
    }

    @Test
    void everyStableRouteIsCallableWithPairedRawAndStructuredMethods() throws Exception {
        var inventory = JsonUtils.readTree(Objects.requireNonNull(
                getClass().getResourceAsStream("/chia-2.7.5-routes.json")).readAllBytes());
        assertEquals("2.7.5", inventory.get("release").asText());
        assertEquals("ff71448437a137c7092775923d5ecbbe104433e7", inventory.get("commit").asText());
        var expected = new TreeSet<String>();
        inventory.get("full_node").forEach(n -> expected.add("/" + n.asText()));
        inventory.get("shared").forEach(n -> expected.add("/" + n.asText()));
        assertEquals(40, expected.size());
        var enumRoutes = new TreeSet<String>();
        for (var endpoint : FullNode.values()) enumRoutes.add(endpoint.getPath());
        assertEquals(expected, enumRoutes);

        var client = new Capture();
        var api = new FullNodeAPI(client);
        var callable = new TreeSet<String>();
        for (var method : FullNodeAPI.class.getMethods()) {
            if (!method.getName().endsWith("AsBytes")) continue;
            var raw = (byte[]) method.invoke(api, arguments(method));
            assertArrayEquals(client.response.getBytes(StandardCharsets.UTF_8), raw);
            assertEquals(ChiaService.FULL_NODE, client.request.service);
            assertTrue(expected.contains(client.request.endpoint), method.getName());
            assertTrue(client.body().isObject());
            callable.add(client.request.endpoint);
            String typedName = method.getName().substring(0, method.getName().length() - "AsBytes".length());
            var typed = FullNodeAPI.class.getMethod(typedName, method.getParameterTypes());
            var failure = (ApiResponse<?>) typed.invoke(api, arguments(method));
            assertFalse(failure.success(), typedName);
            assertTrue(failure.data().isEmpty(), typedName);
            assertEquals("denied", failure.error(), typedName);
            assertEquals(client.request.endpoint, failure.endpoint().getPath());
            var route = client.request.endpoint;
            client.response = successFixture(route);
            var success = (ApiResponse<?>) typed.invoke(api, arguments(method));
            assertTrue(success.success(), typedName);
            assertTrue(success.data().isPresent(), typedName);
            assertEquals(route, success.endpoint().getPath());
            client.response = "{\"success\":false,\"error\":\"denied\"}";
        }
        assertEquals(expected, callable);
    }

    private String successFixture(String route) {
        String fields = switch (route) {
            case "/get_additions_and_removals" -> "\"additions\":[],\"removals\":[]";
            case "/get_block" -> "\"block\":{}";
            case "/get_blocks" -> "\"blocks\":[]";
            case "/get_block_count_metrics" -> "\"metrics\":{}";
            case "/get_block_record", "/get_block_record_by_height" -> "\"block_record\":{}";
            case "/get_block_records" -> "\"block_records\":[]";
            case "/get_block_spends" -> "\"block_spends\":[]";
            case "/get_block_spends_with_conditions" -> "\"block_spends_with_conditions\":[]";
            case "/get_unfinished_block_headers" -> "\"headers\":[]";
            case "/get_network_space" -> "\"space\":0";
            case "/get_aggsig_additional_data" -> "\"additional_data\":\"aa\"";
            case "/get_constants" -> "\"constants\":{}";
            case "/get_recent_signage_point_or_eos" -> "\"signage_point\":null,\"time_received\":1.25,\"reverted\":false";
            case "/get_coin_record_by_name" -> "\"coin_record\":{}";
            case "/get_coin_records_by_names", "/get_coin_records_by_parent_ids", "/get_coin_records_by_hint",
                    "/get_coin_records_by_puzzle_hash", "/get_coin_records_by_puzzle_hashes" -> "\"coin_records\":[]";
            case "/push_tx" -> "\"status\":\"SUCCESS\"";
            case "/get_puzzle_and_solution" -> "\"coin_solution\":" + SPEND;
            case "/get_all_mempool_tx_ids" -> "\"tx_ids\":[]";
            case "/get_all_mempool_items" -> "\"mempool_items\":{}";
            case "/get_mempool_item_by_tx_id" -> "\"mempool_item\":{}";
            case "/get_mempool_items_by_coin_name" -> "\"mempool_items\":[]";
            case "/create_block_generator" -> "\"generator\":null,\"refs\":[],\"additions\":[],\"removals\":[],\"sig\":\"aa\",\"cost\":0";
            case "/get_fee_estimate" -> "\"estimates\":[],\"target_times\":[]";
            case "/get_blockchain_state" -> "\"blockchain_state\":{\"peak\":null}";
            case "/get_network_info" -> "\"network_name\":\"mainnet\",\"network_prefix\":\"xch\",\"genesis_challenge\":\"aa\"";
            case "/get_connections" -> "\"connections\":[]";
            case "/get_routes" -> "\"routes\":[]";
            case "/get_version" -> "\"version\":\"2.7.5\"";
            case "/get_log_level", "/set_log_level", "/reset_log_level" -> "\"level\":\"INFO\",\"available_levels\":[\"INFO\"],\"errors\":[]";
            case "/close_connection", "/open_connection", "/stop_node", "/healthz" -> "";
            default -> throw new AssertionError("Missing success fixture " + route);
        };
        return "{\"success\":true" + (fields.isEmpty() ? "" : "," + fields) + "}";
    }

    @Test
    void correctedRequestKeysAndFeeSelectorsMatchStableSource() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        api.openConnectionAsBytes("localhost", 8444);
        assertEquals(JsonUtils.readTree("{\"host\":\"localhost\",\"port\":8444}"), client.body());
        api.getConnectionsAsBytes(1);
        assertEquals(JsonUtils.readTree("{\"node_type\":1}"), client.body());
        api.getMempoolItemByTxIdAsBytes("aa", true);
        assertEquals(JsonUtils.readTree("{\"tx_id\":\"aa\",\"include_pending\":true}"), client.body());
        api.getCoinRecordsByNamesAsBytes(List.of("aa"), 1, 9, true);
        assertCoinFilter(client.body(), "names");
        api.getCoinRecordsByPuzzleHashAsBytes("aa", 1, 9, true);
        assertCoinFilter(client.body(), "puzzle_hash");
        api.getCoinRecordsByPuzzleHashesAsBytes(List.of("aa"), 1, 9, true);
        assertCoinFilter(client.body(), "puzzle_hashes");
        api.getCoinRecordsByParentIdsAsBytes(List.of("aa"), 1, 9, true);
        assertCoinFilter(client.body(), "parent_ids");
        api.getCoinRecordsByHintAsBytes("aa", 1, 9, true);
        assertCoinFilter(client.body(), "hint");
        api.getFeeEstimateForCostAsBytes(100, List.of(60, 30), 2);
        assertEquals(100, client.body().get("cost").asInt());
        assertFalse(client.body().has("spend_type"));
        assertFalse(client.body().has("spend_bundle"));
        api.getFeeEstimateForCostAsBytes(new BigInteger("18446744073709551615"), List.of(30), 1);
        assertEquals(new BigInteger("18446744073709551615"), client.body().get("cost").bigIntegerValue());
        api.getFeeEstimateForBundleAsBytes(new SpendBundle("cc", List.of(), null, null), List.of(30), 1);
        assertTrue(client.body().get("spend_bundle").get("coin_spends").isArray());
        assertFalse(client.body().has("cost"));
        api.getFeeEstimateForSpendTypeAsBytes("cat_spend", List.of(30, 60), 2);
        assertEquals(JsonUtils.readTree("{\"spend_type\":\"cat_spend\",\"target_times\":[30,60],\"spend_count\":2}"), client.body());
    }

    @Test
    @SuppressWarnings("deprecation")
    void blockRangesHashesSignageSelectorsAndLegacyAliasRemainCallable() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        api.getBlocksAsBytes(1, 8, true, false);
        assertEquals(JsonUtils.readTree("{\"start\":1,\"end\":8,\"exclude_header_hash\":true,\"exclude_reorged\":false}"), client.body());
        api.getBlockRecordsAsBytes(1, 8);
        assertEquals(JsonUtils.readTree("{\"start\":1,\"end\":8}"), client.body());
        api.getNetworkSpaceAsBytes("aa", "bb");
        assertEquals(JsonUtils.readTree("{\"newer_block_header_hash\":\"aa\",\"older_block_header_hash\":\"bb\"}"), client.body());
        api.getRecentSignagePointOrEOSAsBytes("aa", true);
        assertEquals(JsonUtils.readTree("{\"challenge_hash\":\"aa\"}"), client.body());
        api.getRecentSignagePointOrEOSAsBytes("bb", false);
        assertEquals(JsonUtils.readTree("{\"sp_hash\":\"bb\"}"), client.body());
        api.getRecentSignagePointOrEOSAsByes("aa", true);
        assertEquals("/get_recent_signage_point_or_eos", client.request.endpoint);
        assertEquals(JsonUtils.readTree("{\"challenge_hash\":\"aa\"}"), client.body());
        api.getBlockSpendsAsBytes("aa");
        assertEquals(JsonUtils.readTree("{\"header_hash\":\"aa\"}"), client.body());
        api.getBlockRecordByHeightAsBytes(7);
        assertEquals(JsonUtils.readTree("{\"height\":7}"), client.body());
    }

    @Test
    void coinIdsMatchChiaRsCanonicalIntegerEncoding() {
        String parent = "00".repeat(32);
        String puzzle = "01".repeat(32);
        String[][] vectors = {
                {"0", "5c85955f709283ecce2b74f1b1552918819f390911816e7bb466805a38ab87f3"},
                {"1", "229b40853c7e7c2e8452e9ff9b188327601d52a520845af4e3ebc5822fdc37e8"},
                {"127", "f9ea55aae71a187f5fc338b99a807c751f6d5ab0fafa9664de4ca566bf47cf21"},
                {"128", "e13eb1bc29e9d5c85014e0063c66fada8a68218c4c37be57e0f8fe6e64b6eb81"},
                {"9223372036854775808", "23f0c6cab7ba3ddb3dbc6eb41e7c07d6cb5211fe44ad50199d4d45ddc19fced4"},
                {"18446744073709551615", "06a4bc6b81a22591683893129b20ad0f19ea7f5295c4c3d5c92fb24fdeec3f24"}
        };
        for (var vector : vectors) {
            var coin = new io.mindspice.jxch.rpc.schemas.object.Coin(parent, puzzle, new BigInteger(vector[0]));
            assertEquals(vector[1], io.mindspice.jxch.rpc.util.ChiaUtils.getCoinId(coin), vector[0]);
        }
    }

    @Test
    void heightAndBlockRangeOverloadsCoverUint32AndExclusiveEnd() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        long maxHeight = 4294967295L;
        long exclusiveEnd = maxHeight + 1;
        api.getBlockRecordByHeightAsBytes(maxHeight);
        assertInteger(client.body().get("height"), BigInteger.valueOf(maxHeight));
        assertFalse(api.getBlockRecordByHeight(maxHeight).success());
        assertInteger(client.body().get("height"), BigInteger.valueOf(maxHeight));
        api.getPuzzleAndSolutionAsBytes("aa", maxHeight);
        assertInteger(client.body().get("height"), BigInteger.valueOf(maxHeight));
        assertFalse(api.getPuzzleAndSolution("aa", maxHeight).success());
        assertInteger(client.body().get("height"), BigInteger.valueOf(maxHeight));
        api.getBlocksAsBytes(maxHeight, exclusiveEnd, true, false);
        var blocksBody = client.body();
        assertInteger(blocksBody.get("start"), BigInteger.valueOf(maxHeight));
        assertInteger(blocksBody.get("end"), BigInteger.valueOf(exclusiveEnd));
        assertFalse(api.getBlocks(maxHeight, exclusiveEnd, true, false).success());
        assertEquals(blocksBody, client.body());
        api.getBlockRecordsAsBytes(maxHeight, exclusiveEnd);
        var recordsBody = client.body();
        assertInteger(recordsBody.get("start"), BigInteger.valueOf(maxHeight));
        assertInteger(recordsBody.get("end"), BigInteger.valueOf(exclusiveEnd));
        assertFalse(api.getBlockRecords(maxHeight, exclusiveEnd).success());
        assertEquals(recordsBody, client.body());
    }

    @Test
    void everyCoinFilterAcceptsUint32BoundsOnRawAndStructuredPaths() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        var selectors = Map.of(
                "Names", "names", "Hint", "hint", "ParentIds", "parent_ids",
                "PuzzleHash", "puzzle_hash", "PuzzleHashes", "puzzle_hashes");
        for (var entry : selectors.entrySet()) {
            boolean single = entry.getKey().equals("Hint") || entry.getKey().equals("PuzzleHash");
            Class<?> selectorType = single ? String.class : List.class;
            var raw = FullNodeAPI.class.getMethod("getCoinRecordsBy" + entry.getKey() + "AsBytes",
                    selectorType, long.class, long.class, boolean.class);
            var typed = FullNodeAPI.class.getMethod("getCoinRecordsBy" + entry.getKey(),
                    selectorType, long.class, long.class, boolean.class);
            for (long start : new long[]{0, 2147483648L, 4294967295L}) {
                Object[] args = {single ? "aa" : List.of("aa"), start, 4294967295L, true};
                raw.invoke(api, args);
                var body = client.body();
                assertTrue(body.has(entry.getValue()));
                assertInteger(body.get("start_height"), BigInteger.valueOf(start));
                assertInteger(body.get("end_height"), BigInteger.valueOf(4294967295L));
                assertFalse(((ApiResponse<?>) typed.invoke(api, args)).success());
                assertEquals(body, client.body());
            }
        }
    }

    @Test
    void everyFeeOverloadPreservesUnboundedIntegerTargetsAndWideSpendCounts() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        var huge = BigInteger.ONE.shiftLeft(160).add(BigInteger.ONE);
        List<Number> targets = List.of((byte) 0, (short) 1, 30, 2147483648L, huge);
        var expected = List.of(BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(30),
                BigInteger.valueOf(2147483648L), huge);
        int checked = 0;
        for (var method : FullNodeAPI.class.getDeclaredMethods()) {
            if (!method.getName().startsWith("getFeeEstimate")) continue;
            Object[] args = arguments(method);
            args[1] = targets;
            long count = method.getParameterTypes()[2] == long.class ? 1962062653890L : Integer.MAX_VALUE;
            if (method.getParameterTypes()[2] == long.class) args[2] = count;
            else args[2] = (int) count;
            method.invoke(api, args);
            var body = client.body();
            assertEquals("/get_fee_estimate", client.request.endpoint);
            for (int i = 0; i < expected.size(); i++) {
                assertInteger(body.get("target_times").get(i), expected.get(i));
            }
            assertInteger(body.get("spend_count"), BigInteger.valueOf(count));
            checked++;
        }
        assertEquals(16, checked);
        List<Integer> legacyTargets = List.of(30, 60);
        api.getFeeEstimateForCostAsBytes(100L, legacyTargets, 1);
        assertEquals(JsonUtils.readTree("[30,60]"), client.body().get("target_times"));
        List<Long> longTargets = List.of(4294967295L);
        api.getFeeEstimateForBundleAsBytes(new SpendBundle("cc", List.of()), longTargets, 1);
        assertInteger(client.body().get("target_times").get(0), BigInteger.valueOf(4294967295L));
        List<BigInteger> bigTargets = List.of(huge);
        api.getFeeEstimateForSpendTypeAsBytes("send_xch_transaction", bigTargets, 1962062653890L);
        assertInteger(client.body().get("target_times").get(0), huge);
        assertTrue(BigInteger.valueOf(1962062653890L).multiply(BigInteger.valueOf(9401710))
                .compareTo(BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE)) <= 0);
        api.getFeeEstimateForCostAsBytes(BigInteger.ZERO, List.of(), 0);
        assertTrue(client.body().get("target_times").isEmpty());
        assertEquals(targets, List.of((byte) 0, (short) 1, 30, 2147483648L, huge));
    }

    @Test
    void everyFeeOverloadRejectsInvalidTargetTimesBeforeSending() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        Object[] invalidTargets = {null, Arrays.asList((Number) null), List.of(-1), List.of(-1L),
                List.of(BigInteger.ONE.shiftLeft(100).negate()), List.of(0.25), List.of(30.0),
                List.of(Float.NaN), List.of(Double.POSITIVE_INFINITY), List.of(new java.math.BigDecimal("1.5"))};
        for (var method : FullNodeAPI.class.getDeclaredMethods()) {
            if (!method.getName().startsWith("getFeeEstimate")) continue;
            for (var targets : invalidTargets) {
                Object[] args = arguments(method);
                args[1] = targets;
                var error = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> method.invoke(api, args));
                assertInstanceOf(IllegalArgumentException.class, error.getCause(), method.getName());
                assertNull(client.request);
            }
        }
    }

    @Test
    void spendBundleRequestsPreserveUint64CoinAmounts() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        var max = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
        var coin = new io.mindspice.jxch.rpc.schemas.object.Coin("aa", "bb", max);
        var spend = new io.mindspice.jxch.rpc.schemas.object.CoinSpend(coin, "0x80", "0x80");
        var bundle = new SpendBundle("cc", List.of(spend));
        api.pushTxAsBytes(bundle);
        assertInteger(client.body().at("/spend_bundle/coin_spends/0/coin/amount"), max);
        assertFalse(api.pushTx(bundle).success());
        assertInteger(client.body().at("/spend_bundle/coin_spends/0/coin/amount"), max);
        api.getFeeEstimateForBundleAsBytes(bundle, List.of(BigInteger.ONE.shiftLeft(100)), 1);
        assertInteger(client.body().at("/spend_bundle/coin_spends/0/coin/amount"), max);
        api.openConnectionAsBytes("localhost", 65535);
        assertInteger(client.body().get("port"), BigInteger.valueOf(65535));
    }

    private void assertInteger(JsonNode node, BigInteger expected) {
        assertTrue(node.isIntegralNumber(), node.toString());
        assertEquals(expected, node.bigIntegerValue());
    }

    private void assertCoinFilter(JsonNode body, String selector) {
        assertTrue(body.has(selector));
        assertEquals(1, body.get("start_height").asInt());
        assertEquals(9, body.get("end_height").asInt());
        assertTrue(body.get("include_spent_coins").asBoolean());
        assertFalse(body.has("include_spend_coins"));
        assertEquals(4, body.size());
    }

    @Test
    void responseKeysListsAndPuzzleSolutionUseStableShapes() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        client.response = "{\"success\":true,\"block_record\":{\"header_hash\":\"aa\"}}";
        assertEquals("aa", api.getBlockRecord("aa").data().orElseThrow().headerHash());
        assertEquals("aa", api.getBlockRecordByHeight(1).data().orElseThrow().headerHash());
        client.response = "{\"success\":true,\"headers\":[]}";
        assertEquals(List.of(), api.getUnfinishedBlockHeaders().data().orElseThrow());
        client.response = "{\"success\":true,\"coin_solution\":" + SPEND + "}";
        var solution = api.getPuzzleAndSolution("aa", 2).data().orElseThrow();
        assertEquals("0x80", solution.puzzleReveal());
        assertEquals(BigInteger.valueOf(17), solution.coin().amount());
        assertEquals(JsonUtils.readTree("{\"coin_id\":\"aa\",\"height\":2}"), client.body());
        client.response = "{\"success\":true,\"block_spends\":[" + SPEND + "]}";
        assertEquals(1, api.getBlockSpends("aa").data().orElseThrow().size());
        client.response = "{\"success\":true,\"mempool_item\":{\"spend_bundle_name\":\"cc\"}}";
        assertEquals("cc", api.getMempoolItemByTxId("cc", false).data().orElseThrow().spendBundleName());
        client.response = "{\"success\":true,\"mempool_items\":[{\"spend_bundle_name\":\"cc\"},{\"spend_bundle_name\":\"dd\"}]}";
        assertEquals(2, api.getMempoolItemsByCoinName("aa").data().orElseThrow().size());
        client.response = "{\"success\":true,\"mempool_items\":{\"cc\":{\"spend_bundle_name\":\"cc\"}}}";
        assertEquals("cc", api.getAllMempoolItems().data().orElseThrow().get("cc").spendBundleName());
    }

    @Test
    void newRoutesPreserveConditionsGeneratorAndConstants() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        client.response = "{\"success\":true,\"block_spends_with_conditions\":[{\"coin_spend\":" + SPEND
                + ",\"conditions\":[[51,[\"aa\",\"11\"]]]}]}";
        var spends = api.getBlockSpendsWithConditions("aa").data().orElseThrow();
        assertEquals(1, spends.size());
        assertEquals(51, spends.get(0).conditions().get(0).get(0).asInt());
        assertEquals("aa", spends.get(0).coinSpend().coin().parentCoinInfo());
        assertEquals(JsonUtils.readTree("{\"header_hash\":\"aa\"}"), client.body());
        client.response = "{\"success\":true,\"generator\":null,\"refs\":[],\"additions\":[],\"removals\":[],\"sig\":\"cc\",\"cost\":0}";
        assertNotNull(api.createBlockGenerator().data().orElseThrow());
        assertEquals("{}", new String(client.request.data, StandardCharsets.UTF_8));
        client.response = "{\"success\":true,\"additional_data\":\"abcdef\"}";
        assertEquals("abcdef", api.getAggsigAdditionalData().data().orElseThrow());
        client.response = "{\"success\":true,\"constants\":{\"DIFFICULTY_CONSTANT_FACTOR\":340282366920938463463374607431768211455,\"GENESIS_CHALLENGE\":\"aa\"}}";
        assertEquals(new BigInteger("340282366920938463463374607431768211455"),
                api.getConstants().data().orElseThrow().get("DIFFICULTY_CONSTANT_FACTOR").bigIntegerValue());
    }

    @Test
    void uint128SpaceNullAndMalformedResponsesAreNotSilentlyTruncated() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        client.response = "{\"success\":true,\"space\":340282366920938463463374607431768211455}";
        assertEquals(new BigInteger("340282366920938463463374607431768211455"),
                api.getNetworkSpace("aa", "bb").data().orElseThrow());
        client.response = "{\"success\":true,\"coin_record\":null}";
        assertTrue(api.getCoinRecordByName("aa").data().isEmpty());
        client.response = "{\"success\":true,\"additional_data\":null}";
        assertTrue(api.getAggsigAdditionalData().data().isEmpty());
        client.response = "not json";
        assertThrows(RPCException.class, api::getConstants);
    }

    @Test
    void feeEstimateIntegerMojosAndGeneratorReferenceHeightsStayExact() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        client.response = "{\"success\":true,\"estimates\":[18446744073709551615],\"target_times\":[30],"
                + "\"current_fee_rate\":0.125,\"fee_rate_last_block\":0.75,\"mempool_fees\":18446744073709551615,"
                + "\"last_peak_timestamp\":null,\"full_node_synced\":false}";
        var estimate = api.getFeeEstimateForCost(BigInteger.TEN, List.of(30), 1).data().orElseThrow();
        assertEquals(new BigInteger("18446744073709551615"), estimate.estimates().get(0));
        assertEquals(0.125, estimate.currentFeeRate());
        assertEquals(new BigInteger("18446744073709551615"), estimate.mempoolFees());
        client.response = "{\"success\":true,\"generator\":\"0x80\",\"refs\":[4294967295],\"additions\":[],\"removals\":[],\"sig\":\"cc\",\"cost\":1}";
        assertEquals(List.of(4294967295L), api.createBlockGenerator().data().orElseThrow().refs());
    }

    @Test
    void sharedVersionNetworkAndLogRoutesUseCorrectEnvelopes() throws Exception {
        var client = new Capture();
        var api = new FullNodeAPI(client);
        client.response = "{\"success\":true,\"version\":\"2.7.5\"}";
        assertEquals("2.7.5", api.getVersion().data().orElseThrow());
        client.response = "{\"success\":true,\"network_name\":\"mainnet\",\"network_prefix\":\"xch\",\"genesis_challenge\":\"aa\"}";
        assertEquals("mainnet", api.getNetworkInfo().data().orElseThrow().networkName());
        client.response = "{\"success\":true,\"level\":\"INFO\",\"available_levels\":[\"INFO\",\"DEBUG\"],\"errors\":[]}";
        assertEquals("INFO", api.getLogLevel().data().orElseThrow().level());
        assertEquals("INFO", api.setLogLevel("INFO").data().orElseThrow().level());
        assertEquals(JsonUtils.readTree("{\"level\":\"INFO\"}"), client.body());
        assertTrue(api.resetLogLevel().success());
    }
}
