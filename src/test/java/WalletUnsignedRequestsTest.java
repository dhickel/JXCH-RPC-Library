import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.NftDataKey;
import io.mindspice.jxch.rpc.http.WalletAPI;
import io.mindspice.jxch.rpc.util.JsonUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WalletUnsignedRequestsTest {
    private static final long UINT32_MAX = 4294967295L;
    private static final BigInteger UINT64_MAX = new BigInteger("18446744073709551615");

    @Test void allDirectWalletIdReadsAcceptTheFullUint32Domain() throws Exception {
        var client = new WalletContractTest.Capture();
        var api = new WalletAPI(client);
        for (String name : List.of("catGetAssetIdAsBytes", "catGetNameAsBytes",
                "didCreateDIDBackupFileAsBytes", "didGetCurrentCoinInfoAsBytes", "didGetDIDAsBytes",
                "didGetMetaDataAsBytes", "didGetPubKeyAsBytes", "didGetWalletNameAsBytes",
                "pwStatusAsBytes", "pwSelfPoolAsBytes", "deleteUnconfirmedTransactionsAsBytes",
                "getTransactionCountAsBytes", "getWalletBalanceAsBytes", "nftCountNftsAsBytes",
                "nftGetWalletDidAsBytes")) {
            WalletAPI.class.getMethod(name, long.class).invoke(api, UINT32_MAX);
            assertEquals(UINT32_MAX, client.payload().path("wallet_id").longValue(), name);
        }
        api.getWalletBalancesAsBytes(List.of(UINT32_MAX));
        assertEquals(UINT32_MAX, client.payload().path("wallet_ids").get(0).longValue());
        api.getWalletBalancesAsBytes(List.of(1));
        assertEquals(1, client.payload().path("wallet_ids").get(0).intValue());
    }

    @Test void directDidNftAndTransactionWritesDoNotNarrowWalletIds() throws Exception {
        var client = new WalletContractTest.Capture();
        var api = new WalletAPI(client);
        api.didSetWalletNameAsBytes(UINT32_MAX, "DID");
        assertWalletId(client);
        api.catSetNameAsBytes(UINT32_MAX, "CAT");
        assertWalletId(client);
        api.catSpendAsBytes(UINT32_MAX, 1, "address");
        assertWalletId(client);
        api.didUpdateMetaDataAsBytes(UINT32_MAX, JsonUtils.newEmptyNode(), 0, false);
        assertWalletId(client);
        api.didMessageSpendAsBytes(UINT32_MAX, List.of(), List.of());
        assertWalletId(client);
        api.didTransferDIDAsBytes(UINT32_MAX, "address", 0, true, false);
        assertWalletId(client);
        api.pwJoinPoolAsBytes(UINT32_MAX, "hash", "pool", UINT32_MAX, 0);
        assertWalletId(client);
        assertEquals(UINT32_MAX, client.payload().path("relative_lock_height").longValue());
        api.pwAbsorbRewardsAsBytes(UINT32_MAX, 0);
        assertWalletId(client);
        api.getNextAddressAsBytes(UINT32_MAX, false);
        assertWalletId(client);
        api.getNextAddressAsBytes(UINT32_MAX, false, false);
        assertWalletId(client);
        api.sendTransactionAsBytes(UINT32_MAX, "address", 1, 0);
        assertWalletId(client);
        api.selectCoinsAsBytes(UINT32_MAX, 1);
        assertWalletId(client);
        api.nftAddUriAsBytes(UINT32_MAX, "uri", NftDataKey.values()[0], "coin", 0, false);
        assertWalletId(client);
        api.nftSetNftDidAsBytes(UINT32_MAX, "coin", "did", 0, false);
        assertWalletId(client);
        api.nftSetNftStatusAsBytes(UINT32_MAX, "coin", false);
        assertWalletId(client);
        api.nftTransferNftAsBytes(UINT32_MAX, "coin", "address", 0, false);
        assertWalletId(client);
        api.splitCoinsAsBytes(UINT32_MAX, 2, 1, "coin", 0);
        assertWalletId(client);
        api.registerRemoteCoinsAsBytes(UINT32_MAX, List.of("coin"));
        assertWalletId(client);
        api.crcatApprovePendingAsBytes(UINT32_MAX, 1, 0);
        assertWalletId(client);
    }

    @Test void fingerprintsHeightsAndPaginationUseUint32NotSignedInt() throws Exception {
        var client = new WalletContractTest.Capture();
        var api = new WalletAPI(client);
        for (String name : List.of("logInAsBytes", "deleteKeyAsBytes", "getPrivateKeyAsBytes")) {
            WalletAPI.class.getMethod(name, long.class).invoke(api, UINT32_MAX);
            assertEquals(UINT32_MAX, client.payload().path("fingerprint").longValue(), name);
        }
        api.checkDeleteKeyAsBytes(UINT32_MAX, 65535);
        assertEquals(UINT32_MAX, client.payload().path("fingerprint").longValue());
        api.getTimestampForHeightAsBytes(UINT32_MAX);
        assertEquals(UINT32_MAX, client.payload().path("height").longValue());
        api.extendDerivationIndexAsBytes(UINT32_MAX);
        assertEquals(UINT32_MAX, client.payload().path("index").longValue());
        api.getCoinRecordsByNamesAsBytes(List.of("coin"), UINT32_MAX, UINT32_MAX, true);
        assertEquals(UINT32_MAX, client.payload().path("start_height").longValue());
        assertEquals(UINT32_MAX, client.payload().path("end_height").longValue());
        api.getTransactionsAsBytes(UINT32_MAX, UINT32_MAX, UINT32_MAX, false);
        assertWalletId(client);
        assertEquals(UINT32_MAX, client.payload().path("start").longValue());
        assertEquals(UINT32_MAX, client.payload().path("end").longValue());
        api.vcGetListAsBytes(UINT32_MAX, UINT32_MAX);
        assertEquals(UINT32_MAX, client.payload().path("start").longValue());
        assertEquals(UINT32_MAX, client.payload().path("end").longValue());
        api.dlHistoryAsBytes("launcher", UINT32_MAX, UINT32_MAX, UINT32_MAX);
        assertEquals(UINT32_MAX, client.payload().path("min_generation").longValue());
    }

    @Test void nullableIntegerNftAdaptersRemainUnambiguousAlongsideWidePrimitiveCalls() throws Exception {
        var client = new WalletContractTest.Capture();
        var api = new WalletAPI(client);
        api.nftCountNftsAsBytes(null);
        assertFalse(client.payload().has("wallet_id"));
        api.nftGetNftsAsBytes(null, UINT32_MAX, UINT32_MAX);
        assertFalse(client.payload().has("wallet_id"));
        Integer legacyWalletId = 1;
        api.nftCountNftsAsBytes(legacyWalletId);
        assertEquals(1, client.payload().path("wallet_id").intValue());
        api.nftGetNftsAsBytes(legacyWalletId, 0, 50);
        assertEquals(1, client.payload().path("wallet_id").intValue());
        api.nftCountNftsAsBytes(UINT32_MAX);
        assertWalletId(client);
        api.nftGetNftsAsBytes(UINT32_MAX, UINT32_MAX, UINT32_MAX);
        assertWalletId(client);
        assertEquals(UINT32_MAX, client.payload().path("start_index").longValue());
        assertEquals(UINT32_MAX, client.payload().path("num").longValue());
    }

    @Test void fullPrecisionJsonEndpointsPreserveAmountsFeesAndInputSnapshots() throws Exception {
        var client = new WalletContractTest.Capture();
        var api = new WalletAPI(client);
        Map<String, String> requests = Map.ofEntries(
                Map.entry("sendTransactionAsBytes", "{\"wallet_id\":4294967295,\"address\":\"address\",\"amount\":18446744073709551615,\"fee\":18446744073709551615}"),
                Map.entry("selectCoinsAsBytes", "{\"wallet_id\":4294967295,\"amount\":18446744073709551615}"),
                Map.entry("splitCoinsAsBytes", "{\"wallet_id\":4294967295,\"number_of_coins\":2,\"amount_per_coin\":18446744073709551615,\"target_coin_id\":\"coin\",\"fee\":18446744073709551615}"),
                Map.entry("catSpendAsBytes", "{\"wallet_id\":4294967295,\"amount\":18446744073709551615,\"inner_address\":\"address\",\"fee\":18446744073709551615}"),
                Map.entry("didTransferDIDAsBytes", "{\"wallet_id\":4294967295,\"inner_address\":\"address\",\"with_recovery_info\":true,\"fee\":18446744073709551615}"),
                Map.entry("nftTransferNftAsBytes", "{\"wallet_id\":4294967295,\"nft_coin_id\":\"coin\",\"target_address\":\"address\",\"fee\":18446744073709551615}"),
                Map.entry("crcatApprovePendingAsBytes", "{\"wallet_id\":4294967295,\"min_amount_to_claim\":18446744073709551615,\"fee\":18446744073709551615}"),
                Map.entry("combineCoinsAsBytes", "{\"wallet_id\":4294967295,\"target_coin_amount\":18446744073709551615,\"fee\":18446744073709551615}"));
        for (var entry : requests.entrySet()) {
            JsonNode request = JsonUtils.readTree(entry.getValue());
            JsonNode before = request.deepCopy();
            WalletAPI.class.getMethod(entry.getKey(), JsonNode.class).invoke(api, request);
            assertEquals(before, request, entry.getKey());
            assertEquals(before, client.payload(), entry.getKey());
        }
        api.setAutoClaimAsBytes(true, UINT64_MAX, UINT64_MAX, 65535);
        assertEquals(UINT64_MAX, client.payload().path("tx_fee").bigIntegerValue());
        assertEquals(UINT64_MAX, client.payload().path("min_amount").bigIntegerValue());
        client.response = "{\"success\":true,\"enabled\":true,\"tx_fee\":18446744073709551615,\"min_amount\":18446744073709551615,\"batch_size\":65535}";
        assertEquals(UINT64_MAX, api.setAutoClaim(true, UINT64_MAX, UINT64_MAX, 65535).data().orElseThrow().txFee());
        api.setAutoClaimAsBytes(true, 1, 2, 3);
        assertEquals(1, client.payload().path("tx_fee").intValue());
    }

    @Test void uint32ScalarResponsesAndNftUint64CountDecodeWithoutOverflow() throws Exception {
        var client = new WalletContractTest.Capture();
        var api = new WalletAPI(client);
        client.response = "{\"success\":true,\"index\":4294967295}";
        assertEquals(UINT32_MAX, api.extendDerivationIndex(UINT32_MAX).data().orElseThrow());
        assertEquals(UINT32_MAX, api.getCurrentDerivationIndex().data().orElseThrow());
        client.response = "{\"success\":true,\"height\":4294967295}";
        assertEquals(UINT32_MAX, api.getHeightInfo().data().orElseThrow());
        client.response = "{\"success\":true,\"wallet_id\":4294967295}";
        assertEquals(UINT32_MAX, api.nftGetByDid("did").data().orElseThrow());
        client.response = "{\"success\":true,\"count\":18446744073709551615}";
        assertEquals(UINT64_MAX, api.nftCountNfts(UINT32_MAX).data().orElseThrow());
        assertEquals(UINT64_MAX, api.nftCountNfts((Integer) null).data().orElseThrow());
    }

    private void assertWalletId(WalletContractTest.Capture client) throws Exception {
        assertEquals(UINT32_MAX, client.payload().path("wallet_id").longValue());
    }
}
