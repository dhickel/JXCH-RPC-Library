import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.wallet.Addition;
import io.mindspice.jxch.rpc.schemas.wallet.nft.MetaData;
import io.mindspice.jxch.rpc.util.RequestUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class WalletUnsignedBuildersTest {
    private static final BigInteger UINT32_MAX = BigInteger.valueOf(0xffff_ffffL);
    private static final BigInteger UINT64_MAX = new BigInteger("18446744073709551615");
    private static final String HASH = "11".repeat(32);

    @Test
    void buildersAcceptPinnedUnsignedDomains() {
        var cat = new RequestUtils.CatSpendBuilder()
                .setWalletId(UINT32_MAX.longValueExact())
                .setAmount(UINT64_MAX)
                .setMinCoinAmount(UINT64_MAX)
                .setMaxCoinAmount(UINT64_MAX)
                .setExcludedCoinAmounts(List.of(UINT64_MAX))
                .setInnerAddress("xch1address").build();
        assertEquals(UINT32_MAX, cat.path("wallet_id").bigIntegerValue());
        assertEquals(UINT64_MAX, cat.path("amount").bigIntegerValue());
        assertEquals(UINT64_MAX, cat.path("excluded_coin_amounts").get(0).bigIntegerValue());

        var offer = new RequestUtils.OfferBuilder()
                .addRequestedAsset("1", UINT64_MAX)
                .addOfferedAsset("2", BigInteger.valueOf(Long.MIN_VALUE))
                .setMinValidTime(UINT64_MAX)
                .setMaxValidTime(UINT64_MAX)
                .setMinValidHeight(UINT32_MAX.longValueExact())
                .setMaxValidHeight(UINT32_MAX.longValueExact())
                .build();
        assertEquals(UINT64_MAX.toString(), offer.path("offer").path("1").asText());
        assertEquals("-9223372036854775808", offer.path("offer").path("2").asText());
        assertEquals(UINT64_MAX, offer.path("max_time").bigIntegerValue());
        assertEquals(UINT32_MAX, offer.path("max_height").bigIntegerValue());

        var excludedAmounts = new ArrayList<>(List.of(UINT64_MAX));
        var recordsBuilder = new RequestUtils.CoinRecordBuilder()
                .setOffset(UINT32_MAX.longValueExact()).setLimit(UINT32_MAX.longValueExact())
                .setWalletId(UINT32_MAX.longValueExact())
                .setAmountFilter(excludedAmounts, false)
                .setAmountRange(UINT64_MAX, UINT64_MAX)
                .setConfirmedRange(UINT32_MAX.longValueExact(), UINT32_MAX.longValueExact());
        excludedAmounts.set(0, BigInteger.ONE);
        var records = recordsBuilder.build();
        assertEquals(UINT32_MAX, records.path("offset").bigIntegerValue());
        assertEquals(UINT64_MAX, records.path("amount_filter").path("values").get(0).bigIntegerValue());
        assertEquals(UINT64_MAX, records.path("amount_range").path("stop").bigIntegerValue());
    }

    @Test
    void nftCoinAndMetadataKeepFullPrecision() {
        var metadataUris = new ArrayList<>(List.of("uri"));
        var metadata = new MetaData(metadataUris, List.of(), List.of(), HASH, null, null,
                UINT64_MAX, UINT64_MAX);
        metadataUris.add("mutated");

        var coin = new Coin("22".repeat(32), "33".repeat(32), UINT64_MAX);
        var request = new RequestUtils.BulkMintBuilder()
                .setWalletId(UINT32_MAX.longValueExact())
                .addMetaData(metadata)
                .setMintNumberStart(UINT32_MAX.longValueExact())
                .setMintTotal(UINT32_MAX.longValueExact())
                .addXchCoin(coin)
                .build();

        assertEquals(UINT64_MAX, request.path("metadata_list").get(0).path("edition_number").bigIntegerValue());
        assertEquals(1, request.path("metadata_list").get(0).path("uris").size());
        assertEquals(UINT64_MAX, request.path("xch_coins").get(0).path("amount").bigIntegerValue());
        assertEquals(UINT32_MAX, request.path("mint_number_start").bigIntegerValue());
    }

    @Test
    void didTransactionAndNestedNftBuildersRetainMaximumAmountsAndIds() {
        var did = new RequestUtils.DIDWalletBuilder().isNewWallet(true)
                .setAmount(UINT64_MAX).addFee(UINT64_MAX).build();
        assertEquals(UINT64_MAX, did.path("amount").bigIntegerValue());
        assertEquals(UINT64_MAX, did.path("fee").bigIntegerValue());
        var transaction = new RequestUtils.TransactionBuilder().setWalletId(UINT32_MAX.longValueExact())
                .setAddress("address").setAmount(UINT64_MAX).addFee(UINT64_MAX).build();
        assertEquals(UINT64_MAX, transaction.path("amount").bigIntegerValue());
        assertEquals(UINT64_MAX, transaction.path("fee").bigIntegerValue());
        var bulkDid = new RequestUtils.SetDidBulkBuilder().addNft("coin", UINT32_MAX.longValueExact())
                .addFee(UINT64_MAX).build();
        assertEquals(UINT32_MAX, bulkDid.path("nft_coin_list").get(0).path("wallet_id").bigIntegerValue());
        assertEquals(UINT64_MAX, bulkDid.path("fee").bigIntegerValue());
        var bulkTransfer = new RequestUtils.NftBulkTransferBuilder().addNft("coin", UINT32_MAX.longValueExact())
                .setTargetAddress("address").addFee(UINT64_MAX).build();
        assertEquals(UINT32_MAX, bulkTransfer.path("nft_coin_list").get(0).path("wallet_id").bigIntegerValue());
        assertEquals(UINT64_MAX, bulkTransfer.path("fee").bigIntegerValue());
        var metadataBuilder = new RequestUtils.MetaDataBuilder().addUri("uri").addHash(HASH)
                .setEditionNumber(UINT64_MAX, UINT64_MAX);
        var metadata = metadataBuilder.build();
        metadataBuilder.addUri("later");
        assertEquals(List.of("uri"), metadata.uris());
        var single = new RequestUtils.SingleMintBuilder().setWalletId(UINT32_MAX.longValueExact())
                .addMetaData(metadata).addFee(UINT64_MAX).build();
        assertEquals(UINT32_MAX, single.path("wallet_id").bigIntegerValue());
        assertEquals(UINT64_MAX, single.path("edition_number").bigIntegerValue());
        assertEquals(UINT64_MAX, single.path("fee").bigIntegerValue());
    }

    @Test
    void buildsAreDetachedFromInputsBuilderAndPreviousResults() {
        var memos = new ArrayList<>(List.of("original"));
        var builder = new RequestUtils.SignedTransactionBuilder()
                .setWalletId(UINT32_MAX.longValueExact())
                .addAddition(HASH, UINT64_MAX, memos);
        var first = (ObjectNode) builder.build();
        memos.set(0, "changed-input");
        ((ObjectNode) first.withArray("additions").get(0)).withArray("memos").set(0, "changed-result");

        builder.addAddition(HASH, BigInteger.ONE);
        var second = (ObjectNode) builder.build();

        assertNotSame(first, second);
        assertEquals("changed-result", first.path("additions").get(0).path("memos").get(0).asText());
        assertEquals("original", second.path("additions").get(0).path("memos").get(0).asText());
        assertEquals(2, second.path("additions").size());
    }
}
