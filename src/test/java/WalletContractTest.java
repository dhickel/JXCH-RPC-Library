import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.NftDataKey;
import io.mindspice.jxch.rpc.http.*;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;
import io.mindspice.jxch.rpc.schemas.wallet.WalletRpcResponses.*;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class WalletContractTest {
    static class Capture extends RPCClient {
        Request request;
        String response = "{\"success\":false,\"error\":\"fixture error\"}";
        Capture() { super(new NodeConfig.Builder().setPortOverride(ChiaService.WALLET, 12345).build()); }
        @Override public byte[] makeRequest(Request request) {
            this.request = request;
            return response.getBytes(StandardCharsets.UTF_8);
        }
        JsonNode payload() throws Exception { return JsonUtils.readTree(request.data); }
    }

    private Object[] arguments(Method method) {
        return Arrays.stream(method.getParameterTypes()).map(type -> {
            if (type == String.class) return "false";
            if (type == int.class || type == Integer.class) return 1;
            if (type == long.class || type == Long.class) return 1L;
            if (type == BigInteger.class) return BigInteger.ONE;
            if (type == boolean.class || type == Boolean.class) return true;
            if (type == JsonNode.class) return JsonUtils.newEmptyNode();
            if (type == List.class) return List.of();
            if (type == Map.class) return Map.of();
            if (type == NftDataKey.class) return NftDataKey.values()[0];
            if (type == SpendBundle.class) return JsonUtils.getMapper().convertValue(Map.of(), SpendBundle.class);
            throw new AssertionError("Unhandled parameter " + type + " for " + method);
        }).toArray();
    }

    @Test void everyStableRouteHasPairedMethodsAndCapturesWalletRequests() throws Exception {
        var client = new Capture();
        var api = new WalletAPI(client);
        Set<String> byteRoutes = new TreeSet<>();
        Set<String> typedRoutes = new TreeSet<>();
        for (Method method : WalletAPI.class.getDeclaredMethods()) {
            if (!java.lang.reflect.Modifier.isPublic(method.getModifiers()) || method.isAnnotationPresent(Deprecated.class)) continue;
            client.request = null;
            if (method.getReturnType() == byte[].class) {
                assertTrue(method.getName().endsWith("AsBytes"), method.toString());
                method.invoke(api, arguments(method));
                assertNotNull(client.request, method.toString());
                assertEquals(ChiaService.WALLET, client.request.service, method.toString());
                assertTrue(client.payload().isObject(), method.toString());
                byteRoutes.add(client.request.endpoint.substring(1));
            } else if (method.getReturnType() == ApiResponse.class) {
                var result = (ApiResponse<?>) method.invoke(api, arguments(method));
                assertFalse(result.success(), method.toString());
                assertTrue(result.data().isEmpty(), method.toString());
                assertEquals("fixture error", result.error(), method.toString());
                assertEquals(ChiaService.WALLET, client.request.service);
                assertTrue(result.requestURI().startsWith("https://localhost:12345/"));
                typedRoutes.add(client.request.endpoint.substring(1));
            }
        }
        JsonNode inventory;
        try (var input = getClass().getResourceAsStream("/chia-2.7.5-routes.json")) {
            inventory = JsonUtils.getMapper().readTree(input);
        }
        Set<String> expected = new TreeSet<>();
        inventory.path("wallet").forEach(route -> expected.add(route.asText()));
        assertEquals(122, expected.size());
        assertTrue(byteRoutes.containsAll(expected), () -> "Missing bytes routes " + difference(expected, byteRoutes));
        assertTrue(typedRoutes.containsAll(expected), () -> "Missing typed routes " + difference(expected, typedRoutes));
    }

    private Set<String> difference(Set<String> expected, Set<String> actual) {
        var missing = new TreeSet<>(expected); missing.removeAll(actual); return missing;
    }

    @Test void correctedLegacyPayloadsAndResults() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        api.getTransactionsAsBytes(4, 0, 20, true, "CONFIRMED_AT_HEIGHT");
        assertTrue(client.payload().path("reverse").asBoolean());
        assertFalse(client.payload().has("reversed"));
        api.getCoinRecordsByNamesAsBytes(List.of("coin"), 0, 20, false);
        assertFalse(client.payload().path("include_spent_coins").asBoolean());
        assertFalse(client.payload().has("include_spend_coins"));
        api.nftSetNftStatusAsBytes(4, "coin", true);
        assertEquals("coin", client.payload().path("coin_id").asText());
        assertFalse(client.payload().has("nft_coin_id"));
        api.didUpdateMetaDataAsBytes(4, JsonUtils.readTree("{\"name\":\"did\"}"), 5, true);
        assertEquals("did", client.payload().path("metadata").path("name").asText());
        api.didTransferDIDAsBytes(4, "address", 5, true, false);
        assertEquals(5, client.payload().path("fee").asInt());
        api.sendNotificationAsBytes("target", "abcd", 4, 5);
        assertEquals("target", client.payload().path("target").asText());
        assertFalse(client.payload().has("address"));
        api.getOfferAsBytes("trade", true);
        assertTrue(client.payload().path("file_contents").isBoolean());
        client.response = "{\"success\":true,\"name\":\"CAT\"}";
        assertEquals("CAT", api.catGetName(4).data().orElseThrow());
        client.response = "{\"success\":true,\"transactions\":[{\"name\":\"tx\"}]}";
        assertEquals("tx", api.getTransactions(4, 0, 20, false, null).data().orElseThrow().get(0).name());
        client.response = "{\"success\":true,\"wallet_balances\":{\"4\":{\"wallet_id\":4,\"confirmed_wallet_balance\":42}}}";
        assertEquals(1, api.getWalletBalances(List.of(4)).data().orElseThrow().size());
        assertTrue(client.payload().has("wallet_ids"));
        assertFalse(client.payload().has("wallet_id"));
    }

    @Test void removedRecoveryAndIncompleteTransactionsNeverReachTransport() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        assertThrows(RPCException.class, () -> api.didCreateAttestAsBytes(1, "coin", "pk", "ph"));
        assertThrows(RPCException.class, () -> api.didRecoverySpendAsBytes(1, List.of(), "pk", "ph"));
        assertThrows(RPCException.class, () -> api.didGetRecoveryListAsBytes(1));
        assertThrows(RPCException.class, () -> api.didGetInformationNeededForRecoveryAsBytes(1));
        assertThrows(RPCException.class, () -> api.didUpdateRecoveryIdsAsBytes(1, List.of(), 1, true));
        assertThrows(RPCException.class, () -> api.didTransferDIDAsBytes(1, "address", 0, false, true));
        assertThrows(RPCException.class, () -> api.pushTransactionsAsBytes(List.of()));
        assertNull(client.request);
    }

    @Test void nullableAndDefaultRequestsDoNotInventValues() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        api.dlHistoryAsBytes("launcher", null, null, null);
        assertEquals(JsonUtils.readTree("{\"launcher_id\":\"launcher\"}"), client.payload());
        api.vcMintAsBytes("did", null, 0);
        assertFalse(client.payload().has("target_address"));
        api.nftGetNftsAsBytes((Integer) null, 0, 50);
        assertFalse(client.payload().has("wallet_id"));
        api.nftCountNftsAsBytes((Integer) null);
        assertEquals(JsonUtils.newEmptyNode(), client.payload());
        api.getWalletBalancesAsBytes(null);
        assertTrue(client.payload().path("wallet_ids").isNull());
        api.verifySignatureAsBytes("pk", "msg", "sig", null, null);
        assertFalse(client.payload().has("address"));
        assertFalse(client.payload().has("signing_mode"));
        client.response = "{\"success\":true,\"singleton\":null}";
        assertTrue(api.dlLatestSingleton("launcher", false).data().isEmpty());
        client.response = "{\"success\":true,\"vc_record\":null}";
        assertTrue(api.vcGet("id").data().isEmpty());
    }

    @Test void transactionConfigurationAndSigningRoundTrip() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        var request = JsonUtils.readTree("{\"wallet_id\":1,\"number_of_coins\":3,\"min_coin_amount\":2,\"max_coin_amount\":100,\"excluded_coin_ids\":[\"excluded\"],\"included_coin_ids\":[\"included\"],\"reuse_puzhash\":true,\"push\":false,\"sign\":false,\"allow_unsynced\":true,\"fee\":4,\"valid_at\":10,\"max_time\":20,\"extra_conditions\":[{\"opcode\":80,\"args\":[\"10\"]}]}");
        client.response = "{\"success\":true,\"unsigned_transactions\":[{\"transaction_info\":{\"spends\":[]},\"signing_instructions\":{\"key_hints\":{\"sum_hints\":[],\"path_hints\":[]},\"targets\":[{\"fingerprint\":\"abcd\",\"message\":\"msg\",\"hook\":\"hook\"}]}}],\"transactions\":[{\"name\":\"tx\"}]}";
        var result = api.combineCoins(request).data().orElseThrow();
        assertEquals(request, client.payload());
        assertEquals("hook", result.unsignedTransactions().get(0).signingInstructions().targets().get(0).hook());
        assertEquals("tx", result.transactions().get(0).name());
        api.dlUpdateMultipleAsBytes(Map.of("launcher", "root"), 4);
        assertEquals("root", client.payload().path("updates").path("launcher").asText());
        api.vcAddProofsAsBytes(Map.of("key", "value"));
        assertEquals("value", client.payload().path("proofs").path("key").asText());
        client.response = "{\"success\":true,\"signed_transactions\":[{\"transaction_info\":{\"spends\":[]},\"signatures\":[{\"type\":\"bls_12381_aug_scheme\",\"signature\":\"abcd\"}]}]}";
        assertEquals("abcd", api.applySignatures(JsonUtils.newEmptyNode()).data().orElseThrow().get(0).signatures().get(0).signature());
        client.response = "{\"success\":true,\"mempool_ids\":[\"id\"]}";
        assertEquals(List.of("id"), api.submitTransactions(JsonUtils.newEmptyNode()).data().orElseThrow());
        client.response = "{\"success\":true,\"signing_responses\":[{\"signature\":\"sig\",\"hook\":\"hook\"}]}";
        assertEquals("hook", api.executeSigningInstructions(JsonUtils.newEmptyNode()).data().orElseThrow().get(0).hook());
    }

    @Test void dataLayerCredentialAndUint64Fixtures() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        client.response = "{\"success\":true,\"history\":[{\"coin_id\":\"coin\",\"launcher_id\":\"launcher\",\"root\":\"root\",\"generation\":5,\"timestamp\":18446744073709551615,\"lineage_proof\":{\"parent_name\":\"parent\",\"amount\":1}}],\"count\":1}";
        var history = api.dlHistory("launcher", 0L, 5L, 1L).data().orElseThrow();
        assertEquals(5, history.history().get(0).generation());
        assertEquals(new BigInteger("18446744073709551615"), history.history().get(0).timestamp());
        client.response = "{\"success\":true,\"mirrors\":[{\"coin_id\":\"mirror\",\"urls\":[\"https://mirror\"],\"ours\":true,\"confirmed_at_height\":null}]}";
        assertNull(api.dlGetMirrors("launcher").data().orElseThrow().get(0).confirmedAtHeight());
        client.response = "{\"success\":true,\"vc_records\":[{\"vc\":{\"launcher_id\":\"vc\",\"proof_hash\":null},\"confirmed_at_height\":0,\"coin_id\":\"coin\"}],\"proofs\":{\"root\":{\"key\":\"value\"},\"missing\":null}}";
        var credentials = api.vcGetList(0, 50).data().orElseThrow();
        assertEquals("vc", credentials.vcRecords().get(0).vc().launcherId());
        assertEquals("value", credentials.proofs().get("root").get("key"));
        assertNull(credentials.proofs().get("missing"));
        client.response = "{\"success\":true,\"verified_clvm_hashes\":{\"store_id\":\"store\",\"inclusions\":[{\"key_clvm_hash\":\"key\",\"value_clvm_hash\":\"value\"}]},\"current_root\":false}";
        var proof = api.dlVerifyProof(JsonUtils.newEmptyNode()).data().orElseThrow();
        assertFalse(proof.currentRoot());
        assertEquals("key", proof.verifiedClvmHashes().inclusions().get(0).keyClvmHash());
        client.response = "{\"success\":true,\"fee_per_cost\":18446744073709551615}";
        assertEquals(new BigInteger("18446744073709551615"), api.getFeeEstimate().data().orElseThrow());
    }

    @Test void newTransactionRoutesDecodeFullTransactionEnvelopes() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        client.response = "{\"success\":true,\"transactions\":[{\"name\":\"transaction\"}],\"unsigned_transactions\":[]}";
        for (String name : List.of("splitCoins", "combineCoins", "dlUpdateMultiple", "dlNewMirror",
                "dlDeleteMirror", "vcSpend", "vcRevoke", "crcatApprovePending", "cancelOffer",
                "cancelOffers", "pushTransactions")) {
            var response = (ApiResponse<?>) WalletAPI.class.getMethod(name, JsonNode.class)
                    .invoke(api, JsonUtils.newEmptyNode());
            assertTrue(response.success(), name);
            var result = (TransactionResult) response.data().orElseThrow();
            assertEquals("transaction", result.transactions().get(0).name(), name);
            assertTrue(result.unsignedTransactions().isEmpty(), name);
        }
        client.response = "{\"success\":true,\"launcher_id\":\"launcher\",\"transactions\":[],\"unsigned_transactions\":[]}";
        assertEquals("launcher", api.createNewDl("root", 5).data().orElseThrow().launcherId());
        assertEquals("root", client.payload().path("root").asText());
        assertEquals(5, client.payload().path("fee").asInt());
        client.response = "{\"success\":true,\"tx_record\":{\"name\":\"update\"},\"transactions\":[],\"unsigned_transactions\":[]}";
        assertEquals("update", api.dlUpdateRoot("launcher", "newRoot", 2).data().orElseThrow().txRecord().name());
        assertEquals("newRoot", client.payload().path("new_root").asText());
        client.response = "{\"success\":true,\"vc_record\":{\"vc\":{\"launcher_id\":\"credential\",\"proof_hash\":null},\"confirmed_at_height\":0},\"transactions\":[],\"unsigned_transactions\":[]}";
        assertEquals("credential", api.vcMint("did", "target", 1).data().orElseThrow().vcRecord().vc().launcherId());
        assertEquals("target", client.payload().path("target_address").asText());
    }

    @Test void newReadAndSigningRoutesDecodeStableFields() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        client.response = "{\"success\":true,\"count\":18446744073709551615}";
        assertEquals(new BigInteger("18446744073709551615"), api.nftCountNfts(1).data().orElseThrow());
        assertEquals(new BigInteger("18446744073709551615"), api.nftCountNfts((Integer) null).data().orElseThrow());
        client.response = "{\"success\":true,\"peer_count\":18446744073709551615}";
        assertEquals(new BigInteger("18446744073709551615"), api.getFullNodePeerCount().data().orElseThrow());
        client.response = "{\"success\":true,\"private_key\":{\"fingerprint\":4294967295,\"sk\":\"secret\",\"pk\":\"public\",\"seed\":null}}";
        var key = api.getPrivateKey(4294967295L).data().orElseThrow();
        assertEquals(4294967295L, key.fingerprint());
        assertNull(key.seed());
        client.response = "{\"success\":true,\"puzzle_reveal\":\"ff01\",\"solution\":\"80\"}";
        assertEquals("ff01", api.getPuzzleAndSolution("coin").data().orElseThrow().puzzleReveal());
        client.response = "{\"success\":true,\"singletons\":[]}";
        assertEquals(List.of(), api.dlSingletonsByRoot("launcher", "root").data().orElseThrow());
        client.response = "{\"success\":true,\"singletons\":[],\"count\":0}";
        assertEquals(0, api.dlOwnedSingletons().data().orElseThrow().count());
        client.response = "{\"success\":true,\"proofs\":{\"key\":\"value\"}}";
        assertEquals(Map.of("key", "value"), api.vcGetProofsForRoot("root").data().orElseThrow());
        client.response = "{\"success\":true,\"signing_instructions\":{\"key_hints\":{\"sum_hints\":[],\"path_hints\":[{\"root_fingerprint\":\"abcd\",\"path\":[12381,8444,0]}]},\"targets\":[]}}";
        var instructions = api.gatherSigningInfo(JsonUtils.readTree("{\"spends\":[]}")).data().orElseThrow();
        assertEquals(BigInteger.valueOf(8444), instructions.keyHints().pathHints().get(0).path().get(1));
        client.response = "{\"success\":true}";
        assertTrue(api.dlTrackNew("launcher").data().orElseThrow());
        assertTrue(api.dlStopTracking("launcher").data().orElseThrow());
        assertTrue(api.registerRemoteCoins(5, List.of("coin")).data().orElseThrow());
        assertEquals("coin", client.payload().path("coin_ids").get(0).asText());
        assertTrue(api.vcAddProofs(Map.of("key", "value")).data().orElseThrow());
    }

    @Test void royaltyAssetMapExcludesTheRpcSuccessEnvelope() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        client.response = "{\"success\":true,\"nft\":[{\"asset\":null,\"address\":\"royalty-address\",\"amount\":18446744073709551615}]}";
        var result = api.nftCalculateRoyalties(null, null).data().orElseThrow();
        assertEquals(Set.of("nft"), result.keySet());
        assertEquals(new BigInteger("18446744073709551615"), result.get("nft").get(0).amount());
        assertNull(result.get("nft").get(0).asset());
        assertThrows(UnsupportedOperationException.class, () -> result.clear());
        client.response = "{\"success\":true}";
        assertTrue(api.nftCalculateRoyalties(null, null).data().orElseThrow().isEmpty());
        client.response = "{\"success\":false,\"error\":\"royalty failure\"}";
        var failed = api.nftCalculateRoyalties(null, null);
        assertFalse(failed.success());
        assertTrue(failed.data().isEmpty());
        assertEquals("royalty failure", failed.error());
    }

    @Test void walletCreationLockedKeysAndWalletCoinRecordsUseStableShapes() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        client.response = "{\"success\":true,\"type\":\"NFT\",\"wallet_id\":4}";
        var created = api.createNewWallet(JsonUtils.readTree("{\"wallet_type\":\"nft_wallet\"}")).data().orElseThrow();
        assertEquals("NFT", created.type());
        assertEquals(4L, created.walletId());
        client.response = "{\"success\":true,\"type\":\"POOLING_WALLET\",\"total_fee\":10,\"launcher_id\":\"launcher\",\"p2_singleton_puzzle_hash\":\"ph\",\"transaction\":{\"name\":\"tx\"},\"transactions\":[],\"unsigned_transactions\":[]}";
        assertEquals("launcher", api.createNewWallet(JsonUtils.newEmptyNode()).data().orElseThrow().launcherId());
        client.response = "{\"success\":true,\"keyring_is_locked\":true,\"public_key_fingerprints\":null}";
        var locked = api.getPublicKeys().data().orElseThrow();
        assertTrue(locked.keyringIsLocked());
        assertNull(locked.publicKeyFingerprints());
        client.response = "{\"success\":true,\"keyring_is_locked\":false,\"public_key_fingerprints\":[4294967295]}";
        assertEquals(List.of(4294967295L), api.getPublicKeys().data().orElseThrow().publicKeyFingerprints());
        client.response = "{\"success\":true,\"coin_records\":[{\"parent_coin_info\":\"parent\",\"puzzle_hash\":\"ph\",\"amount\":18446744073709551615,\"id\":\"coin\",\"type\":1,\"wallet_identifier\":{\"id\":4,\"type\":0},\"confirmed_height\":5,\"spent_height\":0,\"coinbase\":false,\"metadata\":{\"time_lock\":100}}],\"total_count\":1}";
        var coins = api.getCoinRecords(JsonUtils.newEmptyNode()).data().orElseThrow();
        assertEquals(1L, coins.totalCount());
        assertEquals(4, coins.coinRecords().get(0).walletIdentifier().id());
        assertEquals(new BigInteger("18446744073709551615"), coins.coinRecords().get(0).amount());
        assertEquals(100, coins.coinRecords().get(0).metadata().path("time_lock").asInt());
    }

    @Test void offerSummaryKeepsBothAssetAndDataLayerVariants() throws Exception {
        var client = new Capture(); var api = new WalletAPI(client);
        client.response = "{\"success\":true,\"id\":\"asset-offer\",\"summary\":{\"offered\":{\"xch\":\"100\"},\"requested\":{\"cat\":\"5\"},\"fees\":2,\"infos\":{}}}";
        var asset = api.getOfferSummary("offer", true).data().orElseThrow();
        assertEquals("asset-offer", asset.id());
        assertEquals("100", asset.assetSummary().offered().get("xch"));
        assertNull(asset.dataLayerSummary());
        client.response = "{\"success\":true,\"id\":\"dl-offer\",\"summary\":{\"offered\":[{\"launcher_id\":\"launcher\",\"new_root\":\"root\",\"dependencies\":[{\"launcher_id\":\"dependency\",\"values_to_prove\":[\"abcd\"]}]}]}}";
        var dl = api.getOfferSummary("offer", false).data().orElseThrow();
        assertEquals("dl-offer", dl.id());
        assertNull(dl.assetSummary());
        assertEquals("root", dl.dataLayerSummary().offered().get(0).newRoot());
        assertEquals(List.of("abcd"), dl.dataLayerSummary().offered().get(0).dependencies().get(0).valuesToProve());
        client.response = "{\"success\":true,\"timestamp\":18446744073709551615}";
        assertEquals("18446744073709551615", api.getTimestampForHeight(1).data().orElseThrow());
    }

    @Test void malformedResponsesAreReportedAsRpcErrors() {
        var client = new Capture(); var api = new WalletAPI(client);
        client.response = "not json";
        assertThrows(RPCException.class, api::getFeeEstimate);
        assertThrows(RPCException.class, () -> api.vcGet("vc"));
        assertThrows(RPCException.class, () -> api.splitCoinsAsBytes((JsonNode) null));
    }
}
