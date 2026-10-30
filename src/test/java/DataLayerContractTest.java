import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.DataLayer;
import io.mindspice.jxch.rpc.http.DataLayerAPI;
import io.mindspice.jxch.rpc.http.RPCClient;
import io.mindspice.jxch.rpc.http.Request;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.datalayer.DataLayerSchemas.*;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DataLayerContractTest {
    @org.junit.jupiter.api.Test void collectionsAreImmutableSnapshots() {
        var source = new java.util.ArrayList<>(java.util.List.of("one"));
        var keys = new io.mindspice.jxch.rpc.schemas.datalayer.DataLayerSchemas.Keys(source, null, null, null);
        source.add("two");
        org.junit.jupiter.api.Assertions.assertEquals(java.util.List.of("one"), keys.keys());
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class, () -> keys.keys().add("three"));
        var map = new java.util.HashMap<String, io.mindspice.jxch.rpc.schemas.datalayer.DataLayerSchemas.PluginInfo>();
        map.put("one", null);
        var status = new io.mindspice.jxch.rpc.schemas.datalayer.DataLayerSchemas.PluginStatus(map, null);
        map.clear();
        org.junit.jupiter.api.Assertions.assertTrue(status.uploaders().containsKey("one"));
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class, () -> status.uploaders().clear());
    }
    // Contract source: chia/data_layer/data_layer_rpc_api.py and data_layer_util.py,
    // chia-blockchain ff71448437a137c7092775923d5ecbbe104433e7 (2.7.5).
    static final String ID = "ab".repeat(32);
    static final String HASH = "0x" + "cd".repeat(32);
    static final String HASH2 = "ef".repeat(32);
    static final String TRADE = "12".repeat(32);
    static final String KEY = "0x00ff";
    static final String VALUE = "ff0080";
    static final String URL = "https://mirror.invalid/data";
    static final List<Change> CHANGES = List.of(
            new Change("insert", KEY, VALUE, HASH, 0),
            new Change("delete", "", null, null, null));
    static final String CHANGES_JSON = "[{\"action\":\"insert\",\"key\":\"@key\",\"value\":\"@value\","
            + "\"reference_node_hash\":\"@hash\",\"side\":0},{\"action\":\"delete\",\"key\":\"\"}]";
    static final OfferStore OFFER_STORE = new OfferStore(ID, List.of(new KeyValue("00ff", VALUE)));
    static final String STORE_JSON = "{\"store_id\":\"@id\",\"inclusions\":[{\"key\":\"00ff\",\"value\":\"@value\"}]}";
    static final Offer OFFER = new Offer(TRADE, "deadbeef00", List.of(OFFER_STORE), List.of(
            new StoreProofs(ID, List.of(new OfferProof("00ff", VALUE, HASH2,
                    List.of(new OfferLayer("left", HASH2, HASH2)))))));
    static final String OFFER_JSON = "{\"trade_id\":\"@trade\",\"offer\":\"deadbeef00\",\"taker\":[" + STORE_JSON
            + "],\"maker\":[{\"store_id\":\"@id\",\"proofs\":[{\"key\":\"00ff\",\"value\":\"@value\","
            + "\"node_hash\":\"@hash2\",\"layers\":[{\"other_hash_side\":\"left\",\"other_hash\":\"@hash2\","
            + "\"combined_hash\":\"@hash2\"}]}]}]}";
    static final DLProof PROOF = new DLProof(new StoreProofsHashes("0x" + ID, List.of(
            new HashOnlyProof(HASH, HASH, HASH, List.of(new ProofLayer(1, HASH, HASH))))), HASH, HASH);
    static final String PROOF_JSON = "{\"store_proofs\":{\"store_id\":\"0x@id\",\"proofs\":[{"
            + "\"key_clvm_hash\":\"@hash\",\"value_clvm_hash\":\"@hash\",\"node_hash\":\"@hash\",\"layers\":[{"
            + "\"other_hash_side\":1,\"other_hash\":\"@hash\",\"combined_hash\":\"@hash\"}]}]},"
            + "\"coin_id\":\"@hash\",\"inner_puzzle_hash\":\"@hash\"}";

    static class Stub extends RPCClient {
        Request request;
        int calls;
        String response;
        RPCException failure;
        Stub(String response) { super(new NodeConfig.Builder().build()); this.response = expand(response); }
        @Override public byte[] makeRequest(Request request) throws RPCException {
            this.request = request;
            calls++;
            if (failure != null) throw failure;
            return response.getBytes(StandardCharsets.UTF_8);
        }
    }

    record Fixture(DataLayer route, String method, Object[] args, String payload, Object[] defaults,
                   String defaultPayload, String response, Object expected) {
        Method parsed() {
            return Arrays.stream(DataLayerAPI.class.getDeclaredMethods())
                    .filter(m -> m.getName().equals(method)).findFirst().orElseThrow();
        }
        Object invoke(DataLayerAPI api, boolean bytes, boolean useDefaults) throws Throwable {
            Method method = bytes ? DataLayerAPI.class.getMethod(parsed().getName() + "AsBytes", parsed().getParameterTypes())
                    : parsed();
            try { return method.invoke(api, useDefaults ? defaults : args); }
            catch (InvocationTargetException e) { throw e.getCause(); }
        }
    }

    static Object[] a(Object... args) { return args; }
    static Fixture f(DataLayer route, String method, Object[] args, String payload, Object[] defaults,
                     String defaultPayload, String response, Object expected) {
        return new Fixture(route, method, args, payload, defaults, defaultPayload, response, expected);
    }
    static String expand(String json) {
        return json.replace("@id", ID).replace("@hash2", HASH2).replace("@hash", HASH)
                .replace("@trade", TRADE).replace("@key", KEY).replace("@value", VALUE).replace("@url", URL);
    }
    static JsonNode json(String json) throws Exception { return JsonUtils.readTree(expand(json)); }
    static String ok(String fields) { return "{\"success\":true" + (fields.isEmpty() ? "" : "," + fields) + "}"; }

    static List<Fixture> fixtures() throws Exception {
        return List.of(
                f(DataLayer.WALLET_LOG_IN, "walletLogIn", a(4294967295L), "{\"fingerprint\":4294967295}",
                        a(4294967295L), "{\"fingerprint\":4294967295}", ok(""), true),
                f(DataLayer.CREATE_DATA_STORE, "createDataStore", a(7L, true), "{\"fee\":7,\"verbose\":true}",
                        a(null, null), "{}", ok("\"id\":\"@id\",\"txs\":[{\"name\":\"@hash\",\"spend_bundle\":{\"coin_spends\":[],\"aggregated_signature\":\"0x1234\"},\"future_field\":[1,null]}]"),
                        new DataStore(ID, List.of(json("{\"name\":\"@hash\",\"spend_bundle\":{\"coin_spends\":[],\"aggregated_signature\":\"0x1234\"},\"future_field\":[1,null]}")))),
                f(DataLayer.GET_OWNED_STORES, "getOwnedStores", a(), "{}", a(), "{}",
                        ok("\"store_ids\":[\"@id\"]"), new StoreIds(List.of(ID))),
                f(DataLayer.BATCH_UPDATE, "batchUpdate", a(ID, CHANGES, 7L, true),
                        "{\"id\":\"@id\",\"changelist\":" + CHANGES_JSON + ",\"fee\":7,\"submit_on_chain\":true}",
                        a(ID, CHANGES, null, null), "{\"id\":\"@id\",\"changelist\":" + CHANGES_JSON + "}",
                        ok("\"tx_id\":\"@hash\""), new TransactionId(HASH)),
                f(DataLayer.MULTISTORE_BATCH_UPDATE, "multistoreBatchUpdate", a(List.of(new StoreUpdate(ID, CHANGES)), 7L, true),
                        "{\"store_updates\":[{\"store_id\":\"@id\",\"changelist\":" + CHANGES_JSON + "}],\"fee\":7,\"submit_on_chain\":true}",
                        a(List.of(new StoreUpdate(ID, CHANGES)), null, null),
                        "{\"store_updates\":[{\"store_id\":\"@id\",\"changelist\":" + CHANGES_JSON + "}]}",
                        ok("\"tx_id\":[\"@hash\",\"@hash2\"]"), new TransactionIds(List.of(HASH, HASH2))),
                f(DataLayer.SUBMIT_PENDING_ROOT, "submitPendingRoot", a(ID, 7L), "{\"id\":\"@id\",\"fee\":7}",
                        a(ID, null), "{\"id\":\"@id\"}", ok("\"tx_id\":\"@hash\""), new TransactionId(HASH)),
                f(DataLayer.SUBMIT_ALL_PENDING_ROOTS, "submitAllPendingRoots", a(7L), "{\"fee\":7}", a((Object) null), "{}",
                        ok("\"tx_id\":[\"@hash\"]"), new TransactionIds(List.of(HASH))),
                f(DataLayer.GET_VALUE, "getValue", a(ID, KEY, HASH), "{\"id\":\"@id\",\"key\":\"@key\",\"root_hash\":\"@hash\"}",
                        a(ID, KEY, null), "{\"id\":\"@id\",\"key\":\"@key\"}", ok("\"value\":\"@value\""), new Value(VALUE)),
                f(DataLayer.GET_KEYS, "getKeys", a(ID, HASH, 0, 1024),
                        "{\"id\":\"@id\",\"root_hash\":\"@hash\",\"page\":0,\"max_page_size\":1024}",
                        a(ID, null, null, null), "{\"id\":\"@id\"}",
                        ok("\"keys\":[\"@key\",\"0x\"],\"total_pages\":2,\"total_bytes\":4294967296,\"root_hash\":\"@hash\""),
                        new Keys(List.of(KEY, "0x"), BigInteger.TWO, BigInteger.valueOf(4294967296L), HASH)),
                f(DataLayer.GET_KEYS_VALUES, "getKeysValues", a(ID, HASH, 0, 1024),
                        "{\"id\":\"@id\",\"root_hash\":\"@hash\",\"page\":0,\"max_page_size\":1024}",
                        a(ID, null, null, null), "{\"id\":\"@id\"}",
                        ok("\"keys_values\":[{\"hash\":\"@hash\",\"key\":\"@key\",\"value\":\"0x@value\",\"atom\":null}],\"total_pages\":1,\"total_bytes\":5,\"root_hash\":\"@hash\""),
                        new KeysValues(List.of(new TerminalNode(HASH, KEY, "0x" + VALUE, null)), BigInteger.ONE, BigInteger.valueOf(5), HASH)),
                f(DataLayer.GET_ANCESTORS, "getAncestors", a(ID, HASH), "{\"id\":\"@id\",\"hash\":\"@hash\"}",
                        a(ID, HASH), "{\"id\":\"@id\",\"hash\":\"@hash\"}",
                        ok("\"ancestors\":[{\"hash\":\"@hash\",\"left_hash\":\"@hash\",\"right_hash\":\"0x@hash2\",\"left\":null,\"right\":null}]"),
                        new Ancestors(List.of(new InternalNode(HASH, HASH, "0x" + HASH2)))),
                f(DataLayer.GET_ROOT, "getRoot", a(ID), "{\"id\":\"@id\"}", a(ID), "{\"id\":\"@id\"}",
                        ok("\"hash\":\"@hash\",\"confirmed\":true,\"timestamp\":4294967296"), new Root(HASH, true, BigInteger.valueOf(4294967296L))),
                f(DataLayer.GET_LOCAL_ROOT, "getLocalRoot", a(ID), "{\"id\":\"@id\"}", a(ID), "{\"id\":\"@id\"}",
                        ok("\"hash\":\"@hash\""), new LocalRoot(HASH)),
                f(DataLayer.GET_ROOTS, "getRoots", a(List.of(ID)), "{\"ids\":[\"@id\"]}", a(List.of(ID)), "{\"ids\":[\"@id\"]}",
                        ok("\"root_hashes\":[{\"id\":\"0x@id\",\"hash\":\"@hash\",\"confirmed\":false,\"timestamp\":0}]"),
                        new Roots(List.of(new StoreRoot("0x" + ID, HASH, false, BigInteger.ZERO)))),
                f(DataLayer.DELETE_KEY, "deleteKey", a(ID, KEY, 7L), "{\"id\":\"@id\",\"key\":\"@key\",\"fee\":7}",
                        a(ID, KEY, null), "{\"id\":\"@id\",\"key\":\"@key\"}", ok("\"tx_id\":\"@hash\""), new TransactionId(HASH)),
                f(DataLayer.INSERT, "insert", a(ID, KEY, VALUE, 7L), "{\"id\":\"@id\",\"key\":\"@key\",\"value\":\"@value\",\"fee\":7}",
                        a(ID, KEY, VALUE, null), "{\"id\":\"@id\",\"key\":\"@key\",\"value\":\"@value\"}",
                        ok("\"tx_id\":\"@hash\""), new TransactionId(HASH)),
                f(DataLayer.SUBSCRIBE, "subscribe", a(ID, List.of(URL)), "{\"id\":\"@id\",\"urls\":[\"@url\"]}",
                        a(ID, null), "{\"id\":\"@id\"}", ok(""), true),
                f(DataLayer.UNSUBSCRIBE, "unsubscribe", a(ID, true), "{\"id\":\"@id\",\"retain\":true}",
                        a(ID, null), "{\"id\":\"@id\"}", ok(""), true),
                f(DataLayer.ADD_MIRROR, "addMirror", a(ID, List.of(URL), 42L, 7L), "{\"id\":\"@id\",\"urls\":[\"@url\"],\"amount\":42,\"fee\":7}",
                        a(ID, List.of(URL), 42L, null), "{\"id\":\"@id\",\"urls\":[\"@url\"],\"amount\":42}", ok(""), true),
                f(DataLayer.DELETE_MIRROR, "deleteMirror", a(HASH, 7L), "{\"coin_id\":\"@hash\",\"fee\":7}",
                        a(HASH, null), "{\"coin_id\":\"@hash\"}", ok(""), true),
                f(DataLayer.GET_MIRRORS, "getMirrors", a(ID), "{\"id\":\"@id\"}", a(ID), "{\"id\":\"@id\"}",
                        ok("\"mirrors\":[{\"coin_id\":\"@hash\",\"launcher_id\":\"0x@id\",\"amount\":18446744073709551615,\"urls\":[\"@url\"],\"ours\":true,\"confirmed_at_height\":4294967295}]"),
                        new Mirrors(List.of(new Mirror(HASH, "0x" + ID, new BigInteger("18446744073709551615"), List.of(URL), true, 4294967295L)))),
                f(DataLayer.REMOVE_SUBSCRIPTIONS, "removeSubscriptions", a(ID, List.of(URL)), "{\"id\":\"@id\",\"urls\":[\"@url\"]}",
                        a(ID, List.of()), "{\"id\":\"@id\",\"urls\":[]}", ok(""), true),
                f(DataLayer.SUBSCRIPTIONS, "subscriptions", a(), "{}", a(), "{}", ok("\"store_ids\":[\"@id\"]"), new StoreIds(List.of(ID))),
                f(DataLayer.GET_KV_DIFF, "getKvDiff", a(ID, HASH, HASH2, 0, 1024),
                        "{\"id\":\"@id\",\"hash_1\":\"@hash\",\"hash_2\":\"@hash2\",\"page\":0,\"max_page_size\":1024}",
                        a(ID, HASH, HASH2, null, null), "{\"id\":\"@id\",\"hash_1\":\"@hash\",\"hash_2\":\"@hash2\"}",
                        ok("\"diff\":[{\"type\":\"INSERT\",\"key\":\"00ff\",\"value\":\"@value\"},{\"type\":\"DELETE\",\"key\":\"\",\"value\":\"\"}],\"total_pages\":2,\"total_bytes\":5"),
                        new KvDiff(List.of(new Diff("INSERT", "00ff", VALUE), new Diff("DELETE", "", "")), BigInteger.TWO, BigInteger.valueOf(5))),
                f(DataLayer.GET_ROOT_HISTORY, "getRootHistory", a(ID), "{\"id\":\"@id\"}", a(ID), "{\"id\":\"@id\"}",
                        ok("\"root_history\":[{\"root_hash\":\"@hash\",\"confirmed\":true,\"timestamp\":2},{\"root_hash\":\"0x@hash2\",\"confirmed\":true,\"timestamp\":1}]"),
                        new RootHistory(List.of(new RootHistoryEntry(HASH, true, BigInteger.TWO), new RootHistoryEntry("0x" + HASH2, true, BigInteger.ONE)))),
                f(DataLayer.ADD_MISSING_FILES, "addMissingFiles", a(List.of(ID), true, "/tmp/data files"),
                        "{\"ids\":[\"@id\"],\"overwrite\":true,\"foldername\":\"/tmp/data files\"}", a(null, null, null), "{}", ok(""), true),
                f(DataLayer.MAKE_OFFER, "makeOffer", a(List.of(OFFER_STORE), List.of(OFFER_STORE), 7L),
                        "{\"maker\":[" + STORE_JSON + "],\"taker\":[" + STORE_JSON + "],\"fee\":7}",
                        a(List.of(OFFER_STORE), List.of(OFFER_STORE), null),
                        "{\"maker\":[" + STORE_JSON + "],\"taker\":[" + STORE_JSON + "],\"fee\":null}",
                        ok("\"offer\":" + OFFER_JSON), OFFER),
                f(DataLayer.TAKE_OFFER, "takeOffer", a(OFFER, 7L), "{\"offer\":" + OFFER_JSON + ",\"fee\":7}",
                        a(OFFER, null), "{\"offer\":" + OFFER_JSON + ",\"fee\":null}",
                        ok("\"trade_id\":\"@trade\""), new TradeId(TRADE)),
                f(DataLayer.VERIFY_OFFER, "verifyOffer", a(OFFER, 7L), "{\"offer\":" + OFFER_JSON + ",\"fee\":7}",
                        a(OFFER, null), "{\"offer\":" + OFFER_JSON + ",\"fee\":null}",
                        ok("\"valid\":true,\"error\":null,\"fee\":7"), new OfferVerification(true, null, BigInteger.valueOf(7))),
                f(DataLayer.CANCEL_OFFER, "cancelOffer", a(TRADE, true, 7L), "{\"trade_id\":\"@trade\",\"secure\":true,\"fee\":7}",
                        a(TRADE, false, null), "{\"trade_id\":\"@trade\",\"secure\":false,\"fee\":null}", ok(""), true),
                f(DataLayer.GET_SYNC_STATUS, "getSyncStatus", a(ID), "{\"id\":\"@id\"}", a(ID), "{\"id\":\"@id\"}",
                        ok("\"sync_status\":{\"root_hash\":\"@id\",\"generation\":3,\"target_root_hash\":\"@hash2\",\"target_generation\":4}"),
                        new SyncStatus(ID, BigInteger.valueOf(3), HASH2, BigInteger.valueOf(4))),
                f(DataLayer.CHECK_PLUGINS, "checkPlugins", a(), "{}", a(), "{}",
                        ok("\"plugin_status\":{\"uploaders\":{\"@url\":{\"status\":200,\"response\":{\"name\":\"plugin\",\"custom\":{\"formats\":[1,null,\"v2\"]}}}},\"downloaders\":{\"@url\":{\"error\":\"TimeoutError: timed out\"}}}"),
                        new PluginStatus(Map.of(URL, new PluginInfo(200,
                                json("{\"name\":\"plugin\",\"custom\":{\"formats\":[1,null,\"v2\"]}}"), null, null, null, null)),
                                Map.of(URL, new PluginInfo(null, null, "TimeoutError: timed out", null, null, null)))),
                f(DataLayer.CLEAR_PENDING_ROOTS, "clearPendingRoots", a(ID), "{\"store_id\":\"@id\"}", a(ID), "{\"store_id\":\"@id\"}",
                        ok("\"root\":{\"tree_id\":\"@id\",\"node_hash\":\"@hash2\",\"generation\":3,\"status\":2}"),
                        new PendingRoot(ID, HASH2, BigInteger.valueOf(3), 2)),
                f(DataLayer.GET_PROOF, "getProof", a(ID, List.of(KEY, "")), "{\"store_id\":\"@id\",\"keys\":[\"@key\",\"\"]}",
                        a(ID, List.of()), "{\"store_id\":\"@id\",\"keys\":[]}", ok("\"proof\":" + PROOF_JSON), PROOF),
                f(DataLayer.VERIFY_PROOF, "verifyProof", a(PROOF), PROOF_JSON, a(PROOF), PROOF_JSON,
                        ok("\"verified_clvm_hashes\":{\"store_id\":\"0x@id\",\"inclusions\":[{\"key_clvm_hash\":\"@hash\",\"value_clvm_hash\":\"@hash\"}]},\"current_root\":false"),
                        new ProofVerification(new ProofResultInclusions("0x" + ID, List.of(new KeyValueHashes(HASH, HASH))), false))
        );
    }

    static void assertRequest(Fixture f, Stub client, String expected) throws Exception {
        assertEquals(f.route().getPath(), client.request.endpoint);
        assertEquals(ChiaService.DATA_LAYER, client.request.service);
        assertEquals(json(expected), JsonUtils.readTree(client.request.data));
    }

    @Test void exactUpstreamRouteSetAndConcreteTypedPairs() throws Exception {
        Set<String> upstream = Set.of("wallet_log_in", "create_data_store", "get_owned_stores", "batch_update",
                "multistore_batch_update", "submit_pending_root", "submit_all_pending_roots", "get_value", "get_keys",
                "get_keys_values", "get_ancestors", "get_root", "get_local_root", "get_roots", "delete_key", "insert",
                "subscribe", "unsubscribe", "add_mirror", "delete_mirror", "get_mirrors", "remove_subscriptions",
                "subscriptions", "get_kv_diff", "get_root_history", "add_missing_files", "make_offer", "take_offer",
                "verify_offer", "cancel_offer", "get_sync_status", "check_plugins", "clear_pending_roots", "get_proof", "verify_proof");
        assertEquals(35, upstream.size());
        assertEquals(upstream.stream().map(s -> "/" + s).collect(Collectors.toSet()),
                Arrays.stream(DataLayer.values()).map(DataLayer::getPath).collect(Collectors.toSet()));
        assertEquals(Set.of(DataLayer.values()), fixtures().stream().map(Fixture::route).collect(Collectors.toSet()));
        assertEquals(35, fixtures().size());
        assertEquals(70, Arrays.stream(DataLayerAPI.class.getDeclaredMethods())
                .filter(m -> java.lang.reflect.Modifier.isPublic(m.getModifiers())).count());
        for (Fixture f : fixtures()) {
            assertEquals(ChiaService.DATA_LAYER, f.route().getService());
            Method method = f.parsed();
            assertEquals(ApiResponse.class, method.getReturnType());
            String result = method.getGenericReturnType().getTypeName();
            assertFalse(result.contains("JsonNode"), result);
            assertFalse(result.contains("java.lang.Object"), result);
            assertFalse(Arrays.asList(method.getParameterTypes()).contains(JsonNode.class), method.toString());
            assertEquals(byte[].class, DataLayerAPI.class.getMethod(f.method() + "AsBytes", method.getParameterTypes()).getReturnType());
        }
    }

    @TestFactory Stream<DynamicTest> routePayloadAndTypedFixtures() throws Exception {
        return fixtures().stream().map(f -> DynamicTest.dynamicTest(f.route().name(), () -> {
            Stub client = new Stub(f.response());
            DataLayerAPI api = new DataLayerAPI(client);
            assertArrayEquals(client.response.getBytes(StandardCharsets.UTF_8), (byte[]) f.invoke(api, true, false));
            assertRequest(f, client, f.payload());
            var parsed = (ApiResponse<?>) f.invoke(api, false, false);
            assertRequest(f, client, f.payload());
            assertEquals(2, client.calls);
            assertTrue(parsed.success());
            assertEquals(f.expected(), parsed.data().orElseThrow());
            assertEquals("", parsed.error());
            assertEquals(f.route(), parsed.endpoint());
            assertEquals(client.getAddressFor(ChiaService.DATA_LAYER) + f.route().getPath(), parsed.requestURI());
            assertEquals(client.getAddress(), parsed.address());
        }));
    }

    @TestFactory Stream<DynamicTest> defaultPayloads() throws Exception {
        return fixtures().stream().map(f -> DynamicTest.dynamicTest(f.route().name(), () -> {
            Stub client = new Stub(f.response());
            DataLayerAPI api = new DataLayerAPI(client);
            f.invoke(api, true, true);
            assertRequest(f, client, f.defaultPayload());
            f.invoke(api, false, true);
            assertRequest(f, client, f.defaultPayload());
        }));
    }

    @TestFactory Stream<DynamicTest> serverErrorsPreserveEnvelopeWithoutDecodingData() throws Exception {
        return fixtures().stream().map(f -> DynamicTest.dynamicTest(f.route().name(), () -> {
            Stub client = new Stub("{\"success\":false,\"error\":\"upstream failure\",\"value\":[],\"root\":17,\"tx_id\":{},\"offer\":false}");
            var response = (ApiResponse<?>) f.invoke(new DataLayerAPI(client), false, false);
            assertFalse(response.success());
            assertTrue(response.data().isEmpty());
            assertEquals("upstream failure", response.error());
            assertEquals(f.route(), response.endpoint());
            assertEquals(client.getAddressFor(ChiaService.DATA_LAYER) + f.route().getPath(), response.requestURI());
        }));
    }

    @TestFactory Stream<DynamicTest> malformedEnvelopesAreRpcErrors() throws Exception {
        return fixtures().stream().map(f -> DynamicTest.dynamicTest(f.route().name(), () -> {
            for (String malformed : List.of("not json", "[]", "null", "{}", "{\"success\":\"true\"}")) {
                Stub client = new Stub(malformed);
                var error = assertThrows(RPCException.class, () -> f.invoke(new DataLayerAPI(client), false, false));
                assertNotNull(error.getCause());
            }
        }));
    }

    @Test void everyFeeAndMirrorAmountPreservesUint64AndRejectsInvalidNumbers() throws Throwable {
        BigInteger max = new BigInteger("18446744073709551615");
        int feeRoutes = 0;
        for (Fixture f : fixtures()) {
            JsonNode original = json(f.payload());
            if (!original.has("fee")) continue;
            feeRoutes++;
            Object[] args = f.args().clone();
            int feeIndex = -1;
            Class<?>[] types = f.parsed().getParameterTypes();
            for (int i = 0; i < types.length; i++) if (types[i] == Number.class) feeIndex = i;
            assertTrue(feeIndex >= 0, f.method());
            args[feeIndex] = max;
            Fixture updated = new Fixture(f.route(), f.method(), args, f.payload(), f.defaults(), f.defaultPayload(), f.response(), f.expected());
            Stub client = new Stub(f.response());
            updated.invoke(new DataLayerAPI(client), true, false);
            assertEquals(max, JsonUtils.readTree(client.request.data).get("fee").bigIntegerValue());
            for (Number bad : List.of(-1L, max.add(BigInteger.ONE), 1.5, new java.math.BigDecimal("1"))) {
                args[feeIndex] = bad;
                assertThrows(IllegalArgumentException.class, () -> updated.invoke(new DataLayerAPI(client), true, false));
            }
        }
        assertEquals(13, feeRoutes);
        Stub client = new Stub(ok(""));
        DataLayerAPI api = new DataLayerAPI(client);
        api.addMirrorAsBytes(ID, List.of(), max, null);
        assertEquals(max, JsonUtils.readTree(client.request.data).get("amount").bigIntegerValue());
        for (Number bad : List.of(-1L, max.add(BigInteger.ONE), 1.5))
            assertThrows(IllegalArgumentException.class, () -> api.addMirrorAsBytes(ID, List.of(), bad, null));
        assertThrows(IllegalArgumentException.class, () -> api.addMirrorAsBytes(ID, List.of(), null, null));
    }

    @Test void nullableAndEmptyHexValuesRemainDistinct() throws Exception {
        Stub client = new Stub(ok("\"value\":null"));
        DataLayerAPI api = new DataLayerAPI(client);
        assertNull(api.getValue(ID, "", null).data().orElseThrow().value());
        assertEquals(json("{\"id\":\"@id\",\"key\":\"\"}"), JsonUtils.readTree(client.request.data));
        client.response = ok("\"value\":\"\"");
        assertEquals("", api.getValue("0x" + ID, KEY, HASH).data().orElseThrow().value());
        assertEquals("0x" + ID, JsonUtils.readTree(client.request.data).get("id").asText());
        client.response = ok("\"hash\":null");
        assertNull(api.getLocalRoot(ID).data().orElseThrow().hash());
    }

    @Test void nonVerboseCreationDoesNotInventTransactions() throws Exception {
        Stub client = new Stub(ok("\"id\":\"@id\""));
        DataStore store = new DataLayerAPI(client).createDataStore(0L, false).data().orElseThrow();
        assertEquals(ID, store.id());
        assertNull(store.txs());
        assertEquals(json("{\"fee\":0,\"verbose\":false}"), JsonUtils.readTree(client.request.data));
    }

    @Test void offChainUpdatesSucceedWithoutTransactionIds() throws Exception {
        Stub client = new Stub(ok(""));
        DataLayerAPI api = new DataLayerAPI(client);
        var single = api.batchUpdate(ID, CHANGES, 0L, false);
        assertTrue(single.success());
        assertNull(single.data().orElseThrow().txId());
        assertFalse(JsonUtils.readTree(client.request.data).get("submit_on_chain").asBoolean());
        assertEquals(0, JsonUtils.readTree(client.request.data).get("fee").asInt());
        var multi = api.multistoreBatchUpdate(List.of(new StoreUpdate(ID, CHANGES)), null, false);
        assertTrue(multi.success());
        assertNull(multi.data().orElseThrow().txId());
        assertFalse(JsonUtils.readTree(client.request.data).get("submit_on_chain").asBoolean());
        assertFalse(JsonUtils.readTree(client.request.data).has("fee"));
    }

    @Test void nonPaginatedAndEmptyPagesHaveNullableMetadata() throws Exception {
        Stub client = new Stub(ok("\"keys\":[]"));
        DataLayerAPI api = new DataLayerAPI(client);
        assertEquals(new Keys(List.of(), null, null, null), api.getKeys(ID, null, null, null).data().orElseThrow());
        String zeroRoot = "0x" + "00".repeat(32);
        client.response = ok("\"keys\":[],\"total_pages\":1,\"total_bytes\":0,\"root_hash\":null");
        assertEquals(new Keys(List.of(), BigInteger.ONE, BigInteger.ZERO, null), api.getKeys(ID, zeroRoot, 0, null).data().orElseThrow());
        JsonNode payload = JsonUtils.readTree(client.request.data);
        assertEquals(zeroRoot, payload.get("root_hash").asText());
        assertEquals(0, payload.get("page").asInt());
        assertFalse(payload.has("max_page_size"));
        client.response = ok("\"keys_values\":[]");
        assertEquals(new KeysValues(List.of(), null, null, null), api.getKeysValues(ID, null, null, null).data().orElseThrow());
        client.response = ok("\"diff\":[]");
        assertEquals(new KvDiff(List.of(), null, null), api.getKvDiff(ID, HASH, HASH2, null, null).data().orElseThrow());
    }

    @Test void emptyListsAreNotAbsentAndRootOrderIsNotChanged() throws Exception {
        Stub client = new Stub(ok("\"root_hashes\":[]"));
        DataLayerAPI api = new DataLayerAPI(client);
        assertEquals(new Roots(List.of()), api.getRoots(List.of()).data().orElseThrow());
        assertEquals(json("{\"ids\":[]}"), JsonUtils.readTree(client.request.data));
        client.response = ok("\"tx_id\":[]");
        assertEquals(new TransactionIds(List.of()), api.submitAllPendingRoots(null).data().orElseThrow());
        client.response = ok("\"root_history\":[]");
        assertEquals(new RootHistory(List.of()), api.getRootHistory(ID).data().orElseThrow());
        client.response = ok("\"ancestors\":[]");
        assertEquals(new Ancestors(List.of()), api.getAncestors(ID, HASH).data().orElseThrow());
        client.response = ok("\"store_ids\":[]");
        assertEquals(new StoreIds(List.of()), api.getOwnedStores().data().orElseThrow());
        assertEquals(new StoreIds(List.of()), api.subscriptions().data().orElseThrow());
    }

    @Test void rootTimestampsPreserveTheFullUint64Range() throws Exception {
        BigInteger max = new BigInteger("18446744073709551615");
        Stub client = new Stub(ok("\"hash\":\"@hash\",\"confirmed\":true,\"timestamp\":18446744073709551615"));
        DataLayerAPI api = new DataLayerAPI(client);
        assertEquals(max, api.getRoot(ID).data().orElseThrow().timestamp());
        client.response = expand(ok("\"root_hashes\":[{\"id\":\"0x@id\",\"hash\":\"@hash\",\"confirmed\":true,\"timestamp\":18446744073709551615}]"));
        assertEquals(max, api.getRoots(List.of(ID)).data().orElseThrow().rootHashes().get(0).timestamp());
        client.response = expand(ok("\"root_history\":[{\"root_hash\":\"@hash\",\"confirmed\":true,\"timestamp\":18446744073709551615}]"));
        assertEquals(max, api.getRootHistory(ID).data().orElseThrow().rootHistory().get(0).timestamp());
    }

    @Test void pendingEmptyRootAndNothingToClearAreDifferentResults() throws Exception {
        Stub client = new Stub(ok("\"root\":{\"tree_id\":\"@id\",\"node_hash\":null,\"generation\":0,\"status\":3}"));
        DataLayerAPI api = new DataLayerAPI(client);
        assertEquals(new PendingRoot(ID, null, BigInteger.ZERO, 3), api.clearPendingRoots(ID).data().orElseThrow());
        client.response = "{\"success\":false,\"root\":null}";
        var empty = api.clearPendingRoots(ID);
        assertFalse(empty.success());
        assertTrue(empty.data().isEmpty());
        assertEquals("", empty.error());
    }

    @Test void invalidOfferIsSuccessfulRpcWithTypedFailureReason() throws Exception {
        Stub client = new Stub(ok("\"valid\":false,\"error\":\"offer integrity mismatch\",\"fee\":null"));
        DataLayerAPI api = new DataLayerAPI(client);
        var invalid = api.verifyOffer(OFFER, null);
        assertTrue(invalid.success());
        assertEquals(new OfferVerification(false, "offer integrity mismatch", null), invalid.data().orElseThrow());
        assertEquals("offer integrity mismatch", invalid.error());
        assertTrue(JsonUtils.readTree(client.request.data).get("fee").isNull());
        client.response = ok("\"valid\":true,\"error\":null,\"fee\":18446744073709551615");
        assertEquals(new BigInteger("18446744073709551615"), api.verifyOffer(OFFER, 0L).data().orElseThrow().fee());
    }

    @Test void offerAndProofRoundTripsUseDifferentLayerSideEncodings() throws Exception {
        Stub client = new Stub(ok("\"offer\":" + OFFER_JSON));
        DataLayerAPI api = new DataLayerAPI(client);
        Offer made = api.makeOffer(List.of(OFFER_STORE), List.of(OFFER_STORE), null).data().orElseThrow();
        client.response = expand(ok("\"trade_id\":\"@trade\""));
        assertEquals(TRADE, api.takeOffer(made, null).data().orElseThrow().tradeId());
        JsonNode offerBody = JsonUtils.readTree(client.request.data);
        assertEquals(json(OFFER_JSON), offerBody.get("offer"));
        assertTrue(offerBody.at("/offer/maker/0/proofs/0/layers/0/other_hash_side").isTextual());
        client.response = expand(ok("\"proof\":" + PROOF_JSON));
        DLProof proof = api.getProof(ID, List.of(KEY)).data().orElseThrow();
        client.response = expand(ok("\"verified_clvm_hashes\":{\"store_id\":\"0x@id\",\"inclusions\":[]},\"current_root\":true"));
        var verified = api.verifyProof(proof).data().orElseThrow();
        assertTrue(verified.currentRoot());
        assertEquals(new ProofResultInclusions("0x" + ID, List.of()), verified.verifiedClvmHashes());
        JsonNode proofBody = JsonUtils.readTree(client.request.data);
        assertEquals(json(PROOF_JSON), proofBody);
        assertFalse(proofBody.has("proof"));
        assertTrue(proofBody.at("/store_proofs/proofs/0/layers/0/other_hash_side").isInt());
    }

    @Test void zeroLayerProofsRemainFullyTyped() throws Exception {
        Stub client = new Stub(ok("\"proof\":{\"store_proofs\":{\"store_id\":\"0x@id\",\"proofs\":[{\"key_clvm_hash\":\"@hash\",\"value_clvm_hash\":\"@hash\",\"node_hash\":\"@hash\",\"layers\":[]}]},\"coin_id\":\"@hash\",\"inner_puzzle_hash\":\"@hash\"}"));
        DLProof proof = new DataLayerAPI(client).getProof(ID, List.of(KEY)).data().orElseThrow();
        assertEquals(List.of(), proof.storeProofs().proofs().get(0).layers());
        assertEquals(HASH, proof.storeProofs().proofs().get(0).nodeHash());
    }

    @Test void pluginHttpErrorsAndFallbackHaveTypedEnvelopes() throws Exception {
        Stub client = new Stub(ok("\"plugin_status\":{\"uploaders\":{\"@url\":{\"status\":503}},\"downloaders\":{\"@url\":{\"name\":\"unknown\",\"version\":\"unknown\",\"instance\":\"unknown\"}}}"));
        PluginStatus status = new DataLayerAPI(client).checkPlugins().data().orElseThrow();
        assertEquals(new PluginInfo(503, null, null, null, null, null), status.uploaders().get(URL));
        assertEquals(new PluginInfo(null, null, null, "unknown", "unknown", "unknown"), status.downloaders().get(URL));
    }

    @Test void mirrorUnconfirmedHeightIsNullable() throws Exception {
        Stub client = new Stub(ok("\"mirrors\":[{\"coin_id\":\"@hash\",\"launcher_id\":\"0x@id\",\"amount\":0,\"urls\":[],\"ours\":false,\"confirmed_at_height\":null}]"));
        Mirror mirror = new DataLayerAPI(client).getMirrors(ID).data().orElseThrow().mirrors().get(0);
        assertNull(mirror.confirmedAtHeight());
        assertEquals(BigInteger.ZERO, mirror.amount());
        assertEquals(List.of(), mirror.urls());
        assertFalse(mirror.ours());
    }

    @Test void explicitFalseAndEmptyOptionsDoNotBecomeDefaults() throws Exception {
        Stub client = new Stub(ok(""));
        DataLayerAPI api = new DataLayerAPI(client);
        api.unsubscribe(ID, false);
        assertEquals(json("{\"id\":\"@id\",\"retain\":false}"), JsonUtils.readTree(client.request.data));
        api.subscribe(ID, List.of());
        assertEquals(json("{\"id\":\"@id\",\"urls\":[]}"), JsonUtils.readTree(client.request.data));
        api.addMissingFiles(List.of(), false, "");
        assertEquals(json("{\"ids\":[],\"overwrite\":false,\"foldername\":\"\"}"), JsonUtils.readTree(client.request.data));
        api.cancelOffer(TRADE, false, 0L);
        assertEquals(json("{\"trade_id\":\"@trade\",\"secure\":false,\"fee\":0}"), JsonUtils.readTree(client.request.data));
    }

    @Test void malformedTypedDataBecomesRpcException() {
        Stub client = new Stub(ok("\"value\":{\"not\":\"hex\"}"));
        DataLayerAPI api = new DataLayerAPI(client);
        assertNotNull(assertThrows(RPCException.class, () -> api.getValue(ID, KEY, null)).getCause());
        client.response = ok("\"tx_id\":[]");
        assertThrows(RPCException.class, () -> api.submitPendingRoot(ID, null));
        client.response = ok("\"tx_id\":\"not an array\"");
        assertThrows(RPCException.class, () -> api.submitAllPendingRoots(null));
    }

    @TestFactory Stream<DynamicTest> rawMethodsDoNotParseResponseBodies() throws Exception {
        return fixtures().stream().map(f -> DynamicTest.dynamicTest(f.route().name(), () -> {
            Stub client = new Stub("this is deliberately not JSON");
            assertArrayEquals(client.response.getBytes(StandardCharsets.UTF_8),
                    (byte[]) f.invoke(new DataLayerAPI(client), true, false));
            assertRequest(f, client, f.payload());
        }));
    }

    @TestFactory Stream<DynamicTest> transportErrorsPropagateUnchanged() throws Exception {
        return fixtures().stream().map(f -> DynamicTest.dynamicTest(f.route().name(), () -> {
            Stub client = new Stub("{}");
            client.failure = new RPCException("transport unavailable");
            DataLayerAPI api = new DataLayerAPI(client);
            assertSame(client.failure, assertThrows(RPCException.class, () -> f.invoke(api, true, false)));
            assertSame(client.failure, assertThrows(RPCException.class, () -> f.invoke(api, false, false)));
        }));
    }
}
