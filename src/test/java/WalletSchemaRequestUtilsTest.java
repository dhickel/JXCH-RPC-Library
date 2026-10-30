import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.wallet.Addition;
import io.mindspice.jxch.rpc.schemas.wallet.CoinAnnouncement;
import io.mindspice.jxch.rpc.schemas.wallet.PuzzleAnnouncement;
import io.mindspice.jxch.rpc.schemas.wallet.nft.MetaData;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RequestUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WalletSchemaRequestUtilsTest {
    private static final Coin COIN = new Coin("11".repeat(32), "22".repeat(32), 50);
    private static final String HASH = "33".repeat(32);

    private static JsonNode wire(Object value) throws Exception {
        return JsonUtils.readTree(JsonUtils.writeString(value));
    }

    private static MetaData metadata() {
        return new RequestUtils.MetaDataBuilder().addUri("https://example.invalid/nft")
                .addHash(HASH).build();
    }

    @Test
    void offerAmountsAreStringsAndDriversAreObjects() throws Exception {
        var request = wire(new RequestUtils.OfferBuilder().addRequestedAsset("1", 100)
                .addOfferedAsset("2", 200).addDriverDict(HASH, "{\"type\":\"CAT\",\"tail\":\"abc\"}")
                .addSolver("proofs", List.of("proof")).setMinValidTime(10).setMaxValidTime(20)
                .setMinValidHeight(30).setMaxValidHeight(40).build());
        assertTrue(request.path("offer").path("1").isTextual());
        assertEquals("100", request.path("offer").path("1").asText());
        assertEquals("-200", request.path("offer").path("2").asText());
        assertTrue(request.path("driver_dict").path(HASH).isObject());
        assertEquals("proof", request.path("solver").path("proofs").get(0).asText());
        assertEquals(10, request.path("min_time").asLong());
        assertEquals(20, request.path("max_time").asLong());
        assertEquals(30, request.path("min_height").asLong());
        assertEquals(40, request.path("max_height").asLong());
    }

    @Test
    void offerRejectsInvalidAmountsAndDriverShapes() {
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.OfferBuilder().addRequestedAsset("1", 0));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.OfferBuilder().addRequestedAsset("1", -1));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.OfferBuilder().addOfferedAsset("1", 0));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.OfferBuilder().addDriverDict(HASH, "CAT"));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.OfferBuilder().addDriverDict(HASH, "[]"));
        assertThrows(IllegalStateException.class, () -> new RequestUtils.OfferBuilder().build());
    }

    @Test
    void offerDriverOverloadsAndOptionalDriverDefault() throws Exception {
        assertFalse(wire(new RequestUtils.OfferBuilder().addOfferedAsset("1", -1).build()).has("driver_dict"));
        var request = wire(new RequestUtils.OfferBuilder().addOfferedAsset("1", Long.MIN_VALUE)
                .addDriverDict(HASH, Map.of("type", "CAT")).build());
        assertEquals(Long.toString(Long.MIN_VALUE), request.path("offer").path("1").asText());
        assertEquals("CAT", request.path("driver_dict").path(HASH).path("type").asText());
    }

    @Test
    void takeOfferIncludesSolver() throws Exception {
        var request = wire(new RequestUtils.TakeOfferBuilder().addOffer("offer1...")
                .addSolver("key", Map.of("value", "answer")).build());
        assertEquals("answer", request.path("solver").path("key").path("value").asText());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.TakeOfferBuilder().build());
    }

    @Test
    void offerSearchUsesNamedSortKeys() throws Exception {
        assertEquals("RELEVANCE", wire(new RequestUtils.OfferSearchBuilder().setSortKey("RELEVANCE")
                .build()).path("sort_key").asText());
        assertEquals("CONFIRMED_AT_HEIGHT", wire(new RequestUtils.OfferSearchBuilder()
                .setSortKey("CONFIRMED_AT_HEIGHT").build()).path("sort_key").asText());
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.OfferSearchBuilder().setSortKey(1));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.OfferSearchBuilder().setSortKey("AMOUNT"));
    }

    @Test
    void transactionExclusionsSupportIdsAndLegacyCoinObjects() throws Exception {
        var ids = wire(new RequestUtils.TransactionBuilder().setWalletId(1).setAmount(5).setAddress("xch1...")
                .setExcludedCoinIds(List.of(HASH)).build());
        assertEquals(HASH, ids.path("excluded_coin_ids").get(0).asText());
        assertFalse(ids.has("excluded_coins_ids"));
        var coins = wire(new RequestUtils.TransactionBuilder().setWalletId(1).setAmount(5).setAddress("xch1...")
                .setExcludedCoinIds(List.of(COIN)).build());
        assertEquals(50, coins.path("excluded_coins").get(0).path("amount").asLong());
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.TransactionBuilder()
                .setExcludedCoinIds(List.of(HASH, COIN)));
        assertThrows(IllegalStateException.class, () -> new RequestUtils.TransactionBuilder().build());
    }

    @Test
    void didCreationUsesMetadataAndStringModes() throws Exception {
        var request = wire(new RequestUtils.DIDWalletBuilder().isRecovery(false).setAmount(1)
                .setMetaData(Map.of("name", "identity")).setBackupDIDS(List.of()).setNumOfBackUpsNeeded(0).build());
        assertEquals("new", request.path("did_type").asText());
        assertEquals("identity", request.path("metadata").path("name").asText());
        assertFalse(request.has("meta_data"));
        assertFalse(request.has("num_of_backup_ids_needed"));
        assertFalse(request.has("backup_dids"));
        assertEquals("did_wallet", request.path("wallet_type").asText());
    }

    @Test
    void didRecoveryRequiresBackupDataNotLegacyRecoveryLists() throws Exception {
        var request = wire(new RequestUtils.DIDWalletBuilder().isRecovery(true).setBackupData("backup").build());
        assertEquals("recovery", request.path("did_type").asText());
        assertEquals("backup", request.path("backup_data").asText());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.DIDWalletBuilder().isRecovery(true).build());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.DIDWalletBuilder().isRecovery(true)
                .setBackupData("backup").setAmount(1).build());
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.DIDWalletBuilder().setBackupDIDS(List.of(HASH)));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.DIDWalletBuilder().setNumOfBackUpsNeeded(1));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.DIDWalletBuilder().setMetaData(Map.of("n", 1)));
    }

    @Test
    void catCreationDistinguishesExistingWalletsAndTestOnlyIssuance() throws Exception {
        var existing = wire(new RequestUtils.CatWalletBuilder().isNewWallet(false).setAssetId(HASH).build());
        assertEquals("existing", existing.path("mode").asText());
        assertEquals(HASH, existing.path("asset_id").asText());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.CatWalletBuilder()
                .isNewWallet(true).setAmount(10).build());
        assertTrue(wire(new RequestUtils.CatWalletBuilder().isNewWallet(true).setTest(true)
                .setAmount(10).build()).path("test").asBoolean());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.CatWalletBuilder()
                .isNewWallet(false).setAssetId(HASH).setAmount(10).build());
    }

    @Test
    void signedAndMultiTransactionsDoNotSendEmptyCoinSelections() throws Exception {
        var signed = wire(new RequestUtils.SignedTransactionBuilder().addAddition(HASH, 1).build());
        assertFalse(signed.has("coins"));
        assertEquals(HASH, signed.path("additions").get(0).path("puzzle_hash").asText());
        var multi = wire(new RequestUtils.MultiTransactionBuilder().setWalletId(1).addAddition(HASH, 1).build());
        assertFalse(multi.has("coins"));
        var selected = wire(new RequestUtils.SignedTransactionBuilder().addAddition(HASH, 1).addCoin(COIN).build());
        assertEquals(50, selected.path("coins").get(0).path("amount").asLong());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.SignedTransactionBuilder().build());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.MultiTransactionBuilder().setWalletId(1).build());
    }

    @Test
    void legacyAnnouncementMorphBytesMoveToRequestLevel() throws Exception {
        var request = wire(new RequestUtils.SignedTransactionBuilder().addAddition(HASH, 1)
                .addCoinAnnouncements(List.of(new CoinAnnouncement("aa", HASH, "bb")))
                .addPuzzleAnnouncements(List.of(new PuzzleAnnouncement("aa", "cc", HASH))).build());
        assertEquals("aa", request.path("morph_bytes").asText());
        assertFalse(request.path("coin_announcements").get(0).has("morph_bytes"));
        assertFalse(request.path("puzzle_announcements").get(0).has("morph_bytes"));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.MultiTransactionBuilder()
                .addCoinAnnouncements(List.of(new CoinAnnouncement("aa", HASH, "bb")))
                .addPuzzleAnnouncements(List.of(new PuzzleAnnouncement("dd", "cc", HASH))));
    }

    @Test
    void bulkMintUsesCurrentCoinAndLineageKeys() throws Exception {
        var request = wire(new RequestUtils.BulkMintBuilder().setWalletId(2).addMetaData(metadata())
                .addXchCoin(COIN).addDidCoin(COIN).setDidLinageParent(HASH).build());
        assertEquals(50, request.path("xch_coins").get(0).path("amount").asLong());
        assertEquals(50, request.path("did_coin").path("amount").asLong());
        assertEquals(HASH, request.path("did_lineage_parent").asText());
        assertTrue(request.path("mint_from_did").asBoolean());
        assertFalse(request.has("xch_coin_list"));
        assertFalse(request.has("did_coin_dict"));
        assertFalse(request.has("did_lineage_parent_hex"));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.BulkMintBuilder().addXchCoin(HASH));
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.BulkMintBuilder().addXchCoin(List.of(HASH)));
        assertEquals(1, wire(new RequestUtils.BulkMintBuilder().setWalletId(2).addMetaData(metadata())
                .addXchCoin(List.of(COIN)).build()).path("xch_coins").size());
    }

    @Test
    void singleMintIncludesRequiredNullableAddressKeysAndOmitsAbsentHashes() throws Exception {
        var request = wire(new RequestUtils.SingleMintBuilder().setWalletId(2).addMetaData(metadata()).build());
        assertTrue(request.has("target_address"));
        assertTrue(request.path("target_address").isNull());
        assertTrue(request.has("royalty_address"));
        assertTrue(request.path("royalty_address").isNull());
        assertFalse(request.has("meta_hash"));
        assertFalse(request.has("license_hash"));
        assertEquals(HASH, request.path("hash").asText());
    }

    @Test
    void metadataSupportsLongEditionsAndOptionalHashes() throws Exception {
        var data = new RequestUtils.MetaDataBuilder().addUri("uri").addHash(HASH)
                .setEditionNumber(3_000_000_000L, 4_000_000_000L).build();
        assertEquals(3_000_000_000L, wire(data).path("edition_number").asLong());
        assertEquals(new java.math.BigInteger("4000000000"), data.cloneSetEdt(1L).editionTotal());
        assertFalse(wire(data).has("meta_hash"));
    }

    @Test
    void bulkDidCanClearOwnerWithoutDidArgument() throws Exception {
        var request = wire(new RequestUtils.SetDidBulkBuilder().addNft(HASH, 2).build());
        assertFalse(request.has("did_id"));
        assertEquals(HASH, request.path("nft_coin_list").get(0).path("nft_coin_id").asText());
    }

    @Test
    void spendBundleRequiresOnlyItsActualFields() throws Exception {
        var bundle = new RequestUtils.SpendBundleBuilder().addAggregatedSignature("signature").build();
        var request = wire(bundle);
        assertTrue(request.path("coin_spends").isArray());
        assertFalse(request.has("puzzle_reveal"));
        assertFalse(request.has("solution"));
        assertThrows(IllegalStateException.class, () -> new RequestUtils.SpendBundleBuilder().build());
        assertThrows(IllegalArgumentException.class, () -> new RequestUtils.SpendBundleBuilder().addSolution("80"));
    }

    @Test
    void catSpendRejectsContradictoryShapesAndPartialDiscrepancy() throws Exception {
        assertEquals(10, wire(new RequestUtils.CatSpendBuilder().setWalletId(2).setAmount(10)
                .setInnerAddress("xch1...").build()).path("amount").asLong());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.CatSpendBuilder().setWalletId(2).build());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.CatSpendBuilder().setWalletId(2)
                .setAmount(10).setInnerAddress("xch1...").setAdditions(List.of(new Addition(HASH, 1))).build());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.CatSpendBuilder().setWalletId(2)
                .setAmount(10).setInnerAddress("xch1...").setExtraDelta("-1").build());
    }

    @Test
    void coinQueriesKeepCurrentFilterAndRangeNames() throws Exception {
        var request = wire(new RequestUtils.CoinRecordBuilder().setCoinIdFilter(List.of(HASH), true)
                .setAmountRange(1, 50).orderBySpendHeight().includeTotalCount(true).build());
        assertEquals(2, request.path("coin_id_filter").path("mode").asInt());
        assertEquals(50, request.path("amount_range").path("stop").asInt());
        assertEquals(2, request.path("order").asInt());
        assertTrue(request.path("include_total_count").asBoolean());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.SelectCoinBuilder().setAmount(1).build());
        assertThrows(IllegalStateException.class, () -> new RequestUtils.SpendableCoinBuilder().build());
    }
}
