import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.CoinType;
import io.mindspice.jxch.rpc.enums.WalletType;
import io.mindspice.jxch.rpc.schemas.clawback.AutoClaim;
import io.mindspice.jxch.rpc.schemas.wallet.*;
import io.mindspice.jxch.rpc.schemas.wallet.did.DIDCoin;
import io.mindspice.jxch.rpc.schemas.wallet.did.DIDInfo;
import io.mindspice.jxch.rpc.schemas.wallet.did.NewWallet;
import io.mindspice.jxch.rpc.schemas.wallet.nft.NftBundle;
import io.mindspice.jxch.rpc.schemas.wallet.nft.NftInfo;
import io.mindspice.jxch.rpc.schemas.wallet.offers.Offer;
import io.mindspice.jxch.rpc.schemas.wallet.offers.OfferSummary;
import io.mindspice.jxch.rpc.schemas.wallet.offers.Offers;
import io.mindspice.jxch.rpc.schemas.wallet.offers.TradeRecord;
import io.mindspice.jxch.rpc.util.JsonUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WalletSchemaContractsTest {
    private static final String TRANSACTION = """
            {"confirmed_at_height":4,"created_at_time":3000000000,"to_puzzle_hash":"hash",
             "amount":100,"fee_amount":5,"confirmed":true,"sent":1,"spend_bundle":null,
             "additions":[],"removals":[],"wallet_id":1,"sent_to":[["peer",1,null]],
             "to_address":"xch1...","trade_id":null,"type":1,"name":"transaction",
             "memos":{"coin":["00"]},"valid_times":{"min_time":3000000000,"max_height":4000000000},
             "metadata":{"coin_id":"coin","spent":false,"time_lock":100}}
            """;

    private static JsonNode wire(Object value) throws Exception {
        return JsonUtils.readTree(JsonUtils.writeString(value));
    }

    @Test
    void walletTypesUsePinnedValuesRatherThanJavaOrdinals() throws Exception {
        for (int value : new int[]{0, 2, 6, 8, 9, 13, 57, 132, 205, 209}) {
            var type = JsonUtils.readValue(Integer.toString(value), WalletType.class);
            assertEquals(value, type.enumValue);
            assertEquals(value, wire(type).asInt());
        }
        assertEquals(WalletType.DECENTRALIZED_ID, WalletType.fromValue(8));
        assertEquals(8, WalletType.DISTRIBUTED_ID.enumValue);
        assertThrows(IllegalStateException.class, () -> WalletType.fromValue(1));
        assertEquals(CoinType.CRCAT_PENDING, JsonUtils.readValue("2", CoinType.class));
        assertEquals(3, wire(CoinType.CRCAT).asInt());
    }

    @Test
    void balancesKeepUint128AmountsOptionalFingerprintAndPendingApproval() throws Exception {
        var balance = JsonUtils.readValue("""
                {"wallet_id":3,"wallet_type":57,"fingerprint":null,
                 "confirmed_wallet_balance":340282366920938463463374607431768211455,
                 "unconfirmed_wallet_balance":18446744073709551616,
                 "spendable_balance":18446744073709551616,"max_send_amount":18446744073709551616,
                 "pending_approval_balance":9223372036854775808,"pending_change":1}
                """, WalletBalance.class);
        assertEquals(WalletType.CRCAT, balance.walletType());
        assertNull(balance.fingerprint());
        assertEquals(new BigInteger("340282366920938463463374607431768211455"), balance.confirmedWalletBalance());
        assertEquals(new BigInteger("9223372036854775808"), balance.pendingApprovalBalance());
        assertEquals(57, wire(balance).path("wallet_type").asInt());
        assertEquals(BigInteger.TEN, new WalletBalance(1, 0, 0, 1, 0, 10, 0, null, 10, 10, 10)
                .confirmedWalletBalance());
    }

    @Test
    void transactionRetainsLongTimeNestedPeerStatusesAndTimelocks() throws Exception {
        var transaction = JsonUtils.readValue(TRANSACTION, Transaction.class);
        assertEquals(new BigInteger("3000000000"), transaction.createdAtTime());
        assertEquals(1, transaction.sentTo().get(0).get(1));
        assertNull(transaction.sentTo().get(0).get(2));
        assertEquals(4_000_000_000L, transaction.validTimes().maxHeight());
        assertEquals("coin", transaction.metadata().path("coin_id").asText());
        assertTrue(wire(transaction).path("sent_to").get(0).get(1).isInt());
        var signed = JsonUtils.readValue(TRANSACTION, SignedTransaction.class);
        assertEquals(new BigInteger("3000000000"), signed.createdAtTime());
        assertEquals(1, signed.sentTo().get(0).get(1));
        assertEquals("xch1...", signed.toAddress());
        assertEquals(4_000_000_000L, signed.validTimes().maxHeight());
    }

    @Test
    void tradeRecordAcceptsNullableAcceptanceAndNestedPeerStatuses() throws Exception {
        var record = JsonUtils.readValue("""
                {"accepted_at_time":null,"created_at_time":3000000000,"sent_to":[["peer",2,"error"]],
                 "summary":{"fees":5000000000,"requested":{"xch":"18446744073709551616"},"offered":{}},
                 "valid_times":{"max_time":3000000010},"status":"PENDING_ACCEPT"}
                """, TradeRecord.class);
        assertNull(record.acceptedAtTime());
        assertEquals(new BigInteger("3000000000"), record.createdAtTime());
        assertEquals(2, record.sentTo().get(0).get(1));
        assertEquals(new BigInteger("5000000000"), record.summary().fees());
        assertEquals("18446744073709551616", record.summary().requested().get("xch"));
        assertEquals(new BigInteger("3000000010"), record.validTimes().maxTime());
    }

    @Test
    void summaryRetainsCoinIdsAndAbsoluteValidity() throws Exception {
        var summary = JsonUtils.readValue("""
                {"fees":5000000000,"requested":{"xch":"123"},"offered":{"cat":"-1"},
                 "infos":{},"additions":["addition"],"removals":["removal"],
                 "valid_times":{"min_height":4000000000}}
                """, OfferSummary.class);
        assertEquals(List.of("addition"), summary.additions());
        assertEquals(List.of("removal"), summary.removals());
        assertEquals(4_000_000_000L, summary.validTimes().minHeight());
        assertTrue(wire(summary).path("requested").path("xch").isTextual());
    }

    @Test
    void offerAndTransactionEnvelopesRetainTransactionResults() throws Exception {
        String response = "{\"success\":true,\"transactions\":[" + TRANSACTION
                + "],\"unsigned_transactions\":[{\"transaction_info\":{}}],\"offer\":\"offer1...\",\"trade_record\":null}";
        var offer = JsonUtils.readValue(response, Offer.class);
        assertEquals(1, offer.transactions().size());
        assertEquals(1, offer.unsignedTransactions().size());
        assertNull(offer.tradeRecord());
        var status = JsonUtils.readValue(response, TransactionStatus.class);
        assertEquals(1, status.transactions().size());
        var nft = JsonUtils.readValue(response, NftBundle.class);
        assertEquals(1, nft.transactions().size());
    }

    @Test
    void offersPreserveNullFileContentsRatherThanInventingEmptyResults() throws Exception {
        var offers = JsonUtils.readValue("{\"success\":true,\"offers\":null,\"trade_records\":[]}", Offers.class);
        assertNull(offers.offers());
        assertTrue(offers.tradeRecords().isEmpty());
    }

    @Test
    void walletMetadataAndNullableFingerprintSurviveParsing() throws Exception {
        var wallets = JsonUtils.readValue("""
                {"success":true,"fingerprint":null,"wallets":[{"id":4,"name":"restricted",
                 "type":57,"data":"","authorized_providers":["did"],"flags_needed":["flag"]}]}
                """, Wallets.class);
        assertNull(wallets.fingerprint());
        assertEquals(List.of("did"), wallets.wallets().get(0).authorizedProviders());
        assertEquals(List.of("flag"), wallets.wallets().get(0).flagsNeeded());
        var data = JsonUtils.readValue("""
                {"backup_ids":["backup"],"parent_info":[["parent",{"parent_name":"name","amount":1}]]}
                """, Wallet.Data.class);
        assertEquals(1, data.backupIds().size());
        assertEquals(1, data.parentInfo().size());
        assertTrue(wire(data).path("parent_info").get(0).get(1).isObject());
    }

    @Test
    void poolStatusKeepsCurrentTransactionsAndDoesNotEmitRemovedCurrentInner() throws Exception {
        var status = JsonUtils.readValue("{\"success\":true,\"unconfirmed_transactions\":[" + TRANSACTION
                + "],\"state\":{\"current_inner\":\"legacy\",\"launcher_id\":\"launcher\",\"current\":{\"state\":3}}}",
                PoolWalletStatus.class);
        assertEquals(1, status.unconfirmedTransactions().size());
        assertEquals("launcher", status.state().launcherId());
        assertEquals(3, status.state().poolInfo().state());
        assertFalse(wire(status).path("state").has("current_inner"));
    }

    @Test
    void didSchemasKeepCurrentIdentifierAndLargeCoinAmount() throws Exception {
        var info = JsonUtils.readValue("{\"did_id\":\"did:chia:example\",\"num_verification\":0}", DIDInfo.class);
        assertEquals("did:chia:example", info.didId());
        var coin = JsonUtils.readValue("{\"did_amount\":5000000000}", DIDCoin.class);
        assertEquals(new BigInteger("5000000000"), coin.didAmount());
        var wallet = JsonUtils.readValue("{\"type\":\"DECENTRALIZED_ID\",\"wallet_id\":4,\"my_did\":\"did\"}", NewWallet.class);
        assertEquals("DECENTRALIZED_ID", wallet.type());
        assertEquals("did", wallet.info());
        assertEquals("DECENTRALIZED_ID", new NewWallet("did", 8, 4, true).type());
    }

    @Test
    void nftSchemaRetainsNftIdConfirmationAndNullableRoyalty() throws Exception {
        var info = JsonUtils.readValue("""
                {"nft_id":"nft1...","nft_coin_id":"coin","nft_coin_confirmation_height":4000000000,
                 "edition_number":3000000000,"edition_total":4000000000,"royalty_percentage":null}
                """, NftInfo.class);
        assertEquals("nft1...", info.nftId());
        assertEquals(4_000_000_000L, info.nftCoinConfirmationHeight());
        assertEquals(new BigInteger("3000000000"), info.editionNumber());
        assertNull(info.royaltyPercentage());
    }

    @Test
    void farmerAndAutoClaimSchemasKeepCurrentPoolAndClaimFields() throws Exception {
        var farmed = JsonUtils.readValue("""
                {"pool_reward_amount":20,"last_time_farmed":3000000000,"blocks_won":5}
                """, FarmedAmount.class);
        assertEquals(BigInteger.valueOf(20), farmed.poolRewardAmount());
        assertEquals(new BigInteger("3000000000"), farmed.lastTimeFarmed());
        assertEquals(5, farmed.blocksWon());
        var claim = JsonUtils.readValue("{\"enabled\":true,\"tx_fee\":5000000000,\"min_amount\":20,\"batch_size\":50}",
                AutoClaim.class);
        assertTrue(claim.enabled());
        assertEquals(new BigInteger("5000000000"), claim.txFee());
        assertEquals(50, claim.batchSize());
    }

    @Test
    void unsignedAmountsAndHeightsDoNotOverflowSignedJavaPrimitives() throws Exception {
        var max = new BigInteger("18446744073709551615");
        var transaction = JsonUtils.readValue(TRANSACTION.replace("\"amount\":100", "\"amount\":" + max)
                .replace("\"created_at_time\":3000000000", "\"created_at_time\":" + max)
                .replace("\"confirmed_at_height\":4", "\"confirmed_at_height\":4000000000"), Transaction.class);
        assertEquals(max, transaction.amount());
        assertEquals(max, transaction.createdAtTime());
        assertEquals(4_000_000_000L, transaction.confirmedAtHeight());
        var addition = JsonUtils.readValue("{\"puzzle_hash\":\"hash\",\"amount\":" + max + "}", Addition.class);
        assertEquals(max, addition.amount());
        assertEquals(max, wire(addition).path("amount").bigIntegerValue());
        var notification = JsonUtils.readValue("{\"amount\":" + max + ",\"height\":4000000000}", Notification.class);
        assertEquals(max, notification.amount());
        assertEquals(4_000_000_000L, notification.height());
        var claim = JsonUtils.readValue("{\"tx_fee\":" + max + ",\"min_amount\":" + max + "}", AutoClaim.class);
        assertEquals(max, claim.txFee());
        assertEquals(max, claim.minAmount());
    }

    @Test
    void additionsWithoutMemosHaveNullSafeEquality() {
        assertEquals(new Addition("hash", 1), new Addition("hash", 1));
        assertEquals(new Addition(), new Addition());
    }

    @Test
    void simpleTransactionCoinCollectionsAreArraysButLegacyConstructorRemains() throws Exception {
        var coin = new io.mindspice.jxch.rpc.schemas.object.Coin("parent", "puzzle", 1);
        var transaction = new SimpleTransaction(null, coin, coin);
        assertEquals(1, wire(transaction).path("additions").size());
        assertTrue(wire(transaction).path("removals").isArray());
    }
}
