package io.mindspice.jxch.rpc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.jxch.rpc.enums.CoinType;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.CoinSpend;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;
import io.mindspice.jxch.rpc.schemas.wallet.Addition;
import io.mindspice.jxch.rpc.schemas.wallet.CoinAnnouncement;
import io.mindspice.jxch.rpc.schemas.wallet.PuzzleAnnouncement;
import io.mindspice.jxch.rpc.schemas.wallet.nft.MetaData;
import org.apache.http.annotation.Experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RequestUtils {

    public static class CatSpendBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();

        public CatSpendBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public CatSpendBuilder setAdditions(List<Addition> additions) {
            node.putPOJO("additions", additions);
            return this;
        }

        public CatSpendBuilder setAmount(long amount) {
            node.put("amount", amount);
            return this;
        }

        public CatSpendBuilder setInnerAddress(String address) {
            node.put("inner_address", address);
            return this;
        }

        public CatSpendBuilder setMemos(List<String> memos) {
            node.putPOJO("memos", memos);
            return this;
        }

        public CatSpendBuilder setCoins(List<Coin> coins) {
            node.putPOJO("coins", coins);
            return this;
        }

        public CatSpendBuilder setMinCoinAmount(long amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public CatSpendBuilder setMaxCoinAmount(long amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public CatSpendBuilder setExcludedCoinAmounts(List<Long> amounts) {
            node.putPOJO("exclude_coin_amounts", amounts);
            return this;
        }

        public CatSpendBuilder setExcludedCoinIds(List<String> coinIds) {
            node.putPOJO("exclude_coin_ids", coinIds);
            return this;
        }

        public CatSpendBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public CatSpendBuilder setExtraDelta(String extraDelta) {
            node.put("extra_delta", extraDelta);
            return this;
        }

        public CatSpendBuilder setTailReveal(String tailReveal) {
            node.put("tail_reveal", tailReveal);
            return this;
        }

        public CatSpendBuilder setTailSolution(String tailSolution) {
            node.put("tail_solution", tailSolution);
            return this;
        }

        public CatSpendBuilder setReusePuzzleHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public JsonNode build() {
            return node;
        }
    }


    public static class OfferBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();
        private final ObjectNode offerNode = JsonUtils.newEmptyNode();
        private final ObjectNode driverNode = JsonUtils.newEmptyNode();
        private ObjectNode solver;

        public OfferBuilder addRequestedAsset(String asset, int value) {
            if (value < 0) { throw new IllegalArgumentException("Value must be positive for requested assets."); }
            offerNode.put(asset, value);
            return this;
        }

        public OfferBuilder addOfferedAsset(String asset, int value) {
            offerNode.put(asset, value > 0 ? -value : value);
            return this;
        }

        public OfferBuilder addDriverDict(String prop, String value) {
            driverNode.put(prop, value);
            return this;
        }

        public <T> OfferBuilder addSolver(String prop, T value) {
            if (solver == null) { solver = JsonUtils.newEmptyNode(); }
            solver.putPOJO(prop, value);
            return this;
        }

        public OfferBuilder isValidateOnly(boolean bool) {
            node.put("validate_only", bool);
            return this;
        }

        public OfferBuilder setMinCoinAmount(long amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public OfferBuilder setMaxCoinAmount(long amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public OfferBuilder setReusePuzzleHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public OfferBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        /**
         * Not Implemented in chia client yet
         **/
        @Experimental
        public OfferBuilder setMaxValidHeight(long height) {
            node.put("max_height", height);
            return this;
        }

        public OfferBuilder setMaxValidTime(long unixTime) {
            node.put("max_time", unixTime);
            return this;
        }

        /**
         * Not Implemented in chia client yet
         **/
        @Experimental
        public OfferBuilder setMinValidHeight(long height) {
            node.put("min_height", height);
            return this;
        }

        /**
         * Not Implemented in chia client yet
         **/
        @Experimental
        public OfferBuilder setMinValidTime(long unixTime) {
            node.put("min_time", unixTime);
            return this;
        }

        public JsonNode build() {
            node.putPOJO("offer", offerNode);
            node.putPOJO("driver_dict", driverNode);
            if (solver != null) { node.putPOJO("solver", solver); }
            return node;
        }
    }


    public static class OfferSearchBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();

        public OfferSearchBuilder setStart(int start) {
            node.put("start", start);
            return this;
        }

        public OfferSearchBuilder setEnd(int end) {
            node.put("end", end);
            return this;
        }

        public OfferSearchBuilder excludeMadeOffers(boolean bool) {
            node.put("exclude_my_offers", bool);
            return this;
        }

        public OfferSearchBuilder excludeTakenOffers(boolean bool) {
            node.put("exclude_taken_offers", bool);
            return this;
        }

        public OfferSearchBuilder includeCompletedOffers(boolean bool) {
            node.put("include_completed", bool);
            return this;
        }

        public OfferSearchBuilder setSortKey(int sortKey) {
            node.put("sort_key", sortKey);
            return this;
        }

        public OfferSearchBuilder reverse(boolean bool) {
            node.put("reverse", bool);
            return this;
        }

        public OfferSearchBuilder displayFileContents(boolean bool) {
            node.put("file_contents", bool);
            return this;
        }

        public JsonNode build() {
            return node;
        }
    }


    public static class SelectCoinBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();

        public SelectCoinBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public SelectCoinBuilder setAmount(long amount) {
            node.put("amount", amount);
            return this;
        }

        public SelectCoinBuilder setMinCoinAmount(long amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public SelectCoinBuilder setMaxCoinAmount(long amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public SelectCoinBuilder setExcludedCoinAmounts(List<Long> amounts) {
            node.putPOJO("excluded_coin_amounts", amounts);
            return this;
        }

        public SelectCoinBuilder setExcludedCoins(List<Coin> coins) {
            node.putPOJO("excluded_coins", coins);
            return this;
        }

        public ObjectNode build() {
            if (!node.has("wallet_id") || !node.has("amount")) {
                throw new IllegalStateException("Must have both wallet id and amount");
            }
            return node;
        }
    }


    public static class TakeOfferBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();
        private ObjectNode solver;

        public TakeOfferBuilder addOffer(String offer) {
            node.put("offer", offer);
            return this;
        }

        public TakeOfferBuilder setMinCoinAmount(long amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public TakeOfferBuilder setMaxCoinAmount(long amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public TakeOfferBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public TakeOfferBuilder setReusePuzzleHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public <T> TakeOfferBuilder addSolver(String prop, T value) {
            if (solver == null) { solver = JsonUtils.newEmptyNode(); }
            solver.putPOJO(prop, value);
            return this;
        }

        public JsonNode build() {
            if (!node.has("offer")) {
                throw new IllegalStateException("Must include an offer");
            }
            return node;
        }
    }


    public static class CatWalletBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public CatWalletBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public CatWalletBuilder setName(String name) {
            node.put("name", name);
            return this;
        }

        public CatWalletBuilder isNewWallet(boolean bool) {
            node.put("mode", bool ? "new" : "existing");
            return this;
        }

        public CatWalletBuilder setAmount(long amount) {
            node.put("amount", amount);
            return this;
        }

        public CatWalletBuilder setAssetId(String assetId) {
            node.put("asset_id", assetId);
            return this;
        }

        public JsonNode build() {
            node.put("wallet_type", "cat_wallet");
            return node;
        }
    }


    public static class DIDWalletBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public DIDWalletBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public DIDWalletBuilder setName(String name) {
            node.put("wallet_name", name);
            return this;
        }

        public DIDWalletBuilder isRecovery(boolean bool) {
            node.put("did_type", bool);
            return this;
        }

        public DIDWalletBuilder setBackupDIDS(List<String> dids) {
            if (dids.isEmpty()) { throw new IllegalArgumentException("Empty backup did list"); }
            node.putPOJO("backup_dids", dids);
            return this;
        }

        public <T, U> DIDWalletBuilder setMetaData(Map<T, U> metaDict) {
            node.putPOJO("meta_data", metaDict);
            return this;
        }

        public DIDWalletBuilder setMetaData(JsonNode metaDict) {
            node.putPOJO("meta_data", metaDict);
            return this;
        }

        public DIDWalletBuilder setNumOfBackUpsNeeded(int num) {
            if (num < 1) { throw new IllegalArgumentException("Backups needed must be more than 0"); }
            node.put("num_of_backup_ids_needed", num);
            return this;
        }

        public DIDWalletBuilder setAmount(long amount) {
            node.put("amount", amount);
            return this;
        }

        public DIDWalletBuilder isNewWallet(boolean bool) {
            node.put("did_type", bool ? "new" : "recovery");
            return this;
        }

        public JsonNode build() {
            node.put("wallet_type", "did_wallet");
            return node;
        }
    }


    public static class NFTWalletBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public NFTWalletBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public NFTWalletBuilder setDID(String didId) {
            node.put("did_id", didId);
            return this;
        }

        public NFTWalletBuilder setName(String name) {
            node.put("name", name);
            return this;
        }

        public JsonNode build() {
            node.put("wallet_type", "nft_wallet");
            return node;
        }
    }


    public static class SignedTransactionBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<Addition> additions = new ArrayList<>();
        final List<Coin> coins = new ArrayList<>();

        public SignedTransactionBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public SignedTransactionBuilder addAddition(String coinPuzzleHash, long amount) {
            additions.add(new Addition(coinPuzzleHash, amount));
            return this;
        }

        public SignedTransactionBuilder addAddition(String coinPuzzleHash, long amount, List<String> memos) {
            additions.add(new Addition(coinPuzzleHash, amount, memos));
            return this;
        }

        public SignedTransactionBuilder addAdditions(List<Addition> additions) {
            this.additions.addAll(additions);
            return this;
        }

        public SignedTransactionBuilder setMinCoinAmount(long amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public SignedTransactionBuilder setMaxCoinAmount(long amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public SignedTransactionBuilder setExcludedCoinAmounts(List<Long> amounts) {
            node.putPOJO("exclude_coin_amounts", amounts);
            return this;
        }

        public SignedTransactionBuilder setExcludedCoins(List<Coin> coins) {
            node.putPOJO("exclude_coins", coins);
            return this;
        }

        public SignedTransactionBuilder addCoin(String coinPuzzleHash, String parentPuzzleHash, long amount) {
            coins.add(new Coin(parentPuzzleHash, coinPuzzleHash, amount));
            return this;
        }

        public SignedTransactionBuilder addCoin(Coin coin) {
            coins.add(coin);
            return this;
        }

        public SignedTransactionBuilder addCoin(List<Coin> coins) {
            this.coins.addAll(coins);
            return this;
        }

        public SignedTransactionBuilder addCoinAnnouncements(List<CoinAnnouncement> coinAnnouncements) {
            node.putPOJO("coin_announcements", coinAnnouncements);
            return this;
        }

        public SignedTransactionBuilder addPuzzleAnnouncements(List<PuzzleAnnouncement> puzzleAnnouncements) {
            node.putPOJO("puzzle_announcements", puzzleAnnouncements);
            return this;
        }

        public SignedTransactionBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public JsonNode build() {
            node.putPOJO("additions", additions);
            node.putPOJO("coins", coins);
            return node;
        }
    }


    public static class SpendableCoinBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public SpendableCoinBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public SpendableCoinBuilder setMinCoinAmount(long amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public SpendableCoinBuilder setMaxCoinAmount(long amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public SpendableCoinBuilder setExcludedCoinAmounts(List<Long> amounts) {
            node.putPOJO("excluded_coin_amounts", amounts);
            return this;
        }

        public SpendableCoinBuilder setExcludedCoins(List<Coin> coins) {
            node.putPOJO("excluded_coins", coins);
            return this;
        }

        public SpendableCoinBuilder setExcludedCoinIds(List<String> coinIds) {
            node.putPOJO("excluded_coin_ids", coinIds);
            return this;
        }

        public ObjectNode build() {
            if (!node.has("wallet_id")) {
                throw new IllegalStateException("Must set a wallet id");
            }

            return node;
        }
    }


    public static class TransactionBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public TransactionBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public TransactionBuilder setMinCoinAmount(long amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public TransactionBuilder setAddress(String address) {
            node.put("address", address);
            return this;
        }

        public TransactionBuilder setAmount(long amount) {
            node.put("amount", amount);
            return this;
        }

        public TransactionBuilder setMaxCoinAmount(long amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public TransactionBuilder setExcludedCoinAmounts(List<Long> amounts) {
            node.putPOJO("exclude_coin_amounts", amounts);
            return this;
        }

        public TransactionBuilder setExcludedCoinIds(List<Coin> coinIds) {
            node.putPOJO("exclude_coins_ids", coinIds);
            return this;
        }

        public TransactionBuilder setReusePuzHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public TransactionBuilder addMemos(List<String> memos) {
            node.putPOJO("memos", memos);
            return this;
        }

        public TransactionBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public JsonNode build() {
            return node;
        }
    }


    public static class MultiTransactionBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<Addition> additions = new ArrayList<>();
        final List<Coin> coins = new ArrayList<>();

        public MultiTransactionBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public MultiTransactionBuilder addAddition(String coinPuzzleHash, long amount) {
            additions.add(new Addition(coinPuzzleHash, amount));
            return this;
        }

        public MultiTransactionBuilder addAddition(String coinPuzzleHash, long amount, List<String> memos) {
            additions.add(new Addition(coinPuzzleHash, amount, memos));
            return this;
        }

        public MultiTransactionBuilder addAdditions(List<Addition> additions) {
            this.additions.addAll(additions);
            return this;
        }

        public MultiTransactionBuilder addCoin(String coinPuzzleHash, String parentPuzzleHash, long amount) {
            coins.add(new Coin(parentPuzzleHash, coinPuzzleHash, amount));
            return this;
        }

        public MultiTransactionBuilder addCoin(Coin coin) {
            coins.add(coin);
            return this;
        }

        public MultiTransactionBuilder addCoin(List<Coin> coins) {
            this.coins.addAll(coins);
            return this;
        }

        public MultiTransactionBuilder addCoinAnnouncements(List<CoinAnnouncement> coinAnnouncements) {
            node.putPOJO("coin_announcements", coinAnnouncements);
            return this;
        }

        public MultiTransactionBuilder addPuzzleAnnouncements(List<PuzzleAnnouncement> puzzleAnnouncements) {
            node.putPOJO("puzzle_announcements", puzzleAnnouncements);
            return this;
        }

        public MultiTransactionBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public JsonNode build() {
            node.putPOJO("additions", additions);
            node.putPOJO("coins", coins);
            return node;
        }
    }


    public static class SpendBundleBuilder {
        final List<CoinSpend> coinSpends = new ArrayList<>();
        String aggregatedSignature;
        String solution;
        String puzzleReveal;

        public SpendBundleBuilder addCoinSpend(CoinSpend coinSpend) {
            coinSpends.add(coinSpend);
            return this;
        }

        public SpendBundleBuilder addCoinSpends(List<CoinSpend> coinSpends) {
            this.coinSpends.addAll(coinSpends);
            return this;
        }

        public SpendBundleBuilder addPuzzleReveal(String puzzleReveal) {
            this.puzzleReveal = puzzleReveal;
            return this;
        }

        public SpendBundleBuilder addSolution(String solution) {
            this.solution = solution;
            return this;
        }

        public SpendBundleBuilder addAggregatedSignature(String aggSig) {
            this.aggregatedSignature = aggSig;
            return this;
        }

        public SpendBundle build() {
            if (coinSpends.isEmpty() || aggregatedSignature == null
                    || solution == null || puzzleReveal == null) {
                throw new IllegalStateException(
                        "Must set all fields: coinSpends, puzzleReveal, solution and aggregatedSignature"
                );
            }

            return new SpendBundle(
                    aggregatedSignature,
                    coinSpends,
                    puzzleReveal,
                    solution
            );
        }
    }


    public static class BulkMintBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<MetaData> metaList = new ArrayList<>();
        final List<String> targetList = new ArrayList<>();
        final List<String> coinList = new ArrayList<>();

        public BulkMintBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public BulkMintBuilder addMetaData(MetaData metaData) {
            metaList.add(metaData);
            return this;
        }

        public BulkMintBuilder addMetaData(List<MetaData> metaData) {
            metaList.addAll(metaData);
            return this;
        }

        public BulkMintBuilder mintFromDid(boolean mintFromDid) {
            node.put("mint_from_did", mintFromDid);
            return this;
        }

        public BulkMintBuilder setRoyaltyPercentage(int royaltyPct) {
            node.put("royalty_percentage", royaltyPct);
            return this;
        }

        public BulkMintBuilder setRoyaltyAddress(String address) {
            node.put("royalty_address", address);
            return this;
        }

        public BulkMintBuilder addTargetAddress(String address) {
            targetList.add(address);
            return this;
        }

        public BulkMintBuilder addTargetAddress(List<String> addresses) {
            targetList.addAll(addresses);
            return this;
        }

        public BulkMintBuilder setMintNumberStart(int start) {
            node.put("mint_number_start", start);
            return this;
        }

        public BulkMintBuilder setMintTotal(int total) {
            node.put("mint_total", total);
            return this;
        }

        public BulkMintBuilder addXchCoin(String coinId) {
            coinList.add(coinId);
            return this;
        }

        public BulkMintBuilder addXchCoin(List<String> coinIds) {
            coinList.addAll(coinIds);
            return this;
        }

        public BulkMintBuilder setChangeTarget(String targetAddress) {
            node.put("xch_change_target", targetAddress);
            return this;
        }

        public BulkMintBuilder setNewInnerPuzzleHash(String puzzleHash) {
            node.put("new_innerpuzhash", puzzleHash);
            return this;
        }

        public BulkMintBuilder setNewP2PuzzleHash(String puzzleHash) {
            node.put("new_p2_puzhash", puzzleHash);
            return this;
        }

        public BulkMintBuilder addDidCoin(Coin coin) {
            node.putPOJO("did_coin_dict", coin);
            node.put("mint_from_did", true);
            return this;
        }

        public BulkMintBuilder setDidLinageParent(String linageParentHex) {
            node.put("did_lineage_parent_hex", linageParentHex);
            return this;
        }

        public BulkMintBuilder addFee(long amont) {
            node.put("fee", amont);
            return this;
        }

        public BulkMintBuilder setReusePuzHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public JsonNode build() {
            if (!node.has("wallet_id")) { throw new IllegalStateException("Must Set Wallet Id"); }
            if (metaList.isEmpty()) { throw new IllegalStateException("Must Add Meta Data To Mint"); }
            node.putPOJO("metadata_list", metaList);
            if (!targetList.isEmpty()) { node.putPOJO("target_list", targetList); }
            if (!coinList.isEmpty()) { node.putPOJO("xch_coin_list", coinList); }

            return node;
        }
    }


    public static class MetaDataBuilder {
        final List<String> uris = new ArrayList<>();
        final List<String> metaUris = new ArrayList<>();
        final List<String> licenseUris = new ArrayList<>();
        String hash;
        String metaHash;
        String licenseHash;
        int editionNumber = 1;
        int editionTotal = 1;

        public MetaDataBuilder addUri(String uri) {
            uris.add(uri);
            return this;
        }

        public MetaDataBuilder addUris(List<String> uris) {
            this.uris.addAll(uris);
            return this;
        }

        public MetaDataBuilder addMetaUri(String uri) {
            metaUris.add(uri);
            return this;
        }

        public MetaDataBuilder addMetaUris(List<String> uris) {
            metaUris.addAll(uris);
            return this;
        }

        public MetaDataBuilder addLicenseUri(String uri) {
            licenseUris.add(uri);
            return this;
        }

        public MetaDataBuilder addLicenseUris(List<String> uris) {
            licenseUris.addAll(uris);
            return this;
        }

        public MetaDataBuilder addHash(String hash) {
            this.hash = hash;
            return this;
        }

        public MetaDataBuilder addMetaDataHash(String hash) {
            this.metaHash = hash;
            return this;
        }

        public MetaDataBuilder addLicenseHash(String hash) {
            this.licenseHash = hash;
            return this;
        }

        public MetaDataBuilder setEditionNumber(int edition, int total) {
            editionNumber = edition;
            editionTotal = total;
            return this;
        }

        public MetaData build() {
            if (uris.isEmpty()) {
                throw new IllegalStateException("Must include at least one uri");
            }

            if (hash == null) {
                throw new IllegalStateException("Must include a uri hash");
            }

            return new MetaData(
                    uris,
                    metaUris,
                    licenseUris,
                    hash,
                    metaHash,
                    licenseHash,
                    editionNumber,
                    editionTotal
            );
        }
    }


    public static class SingleMintBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        MetaData metaData;
        int walletId = -1;

        public SingleMintBuilder setWalletId(int id) {
            walletId = id;
            return this;
        }

        public SingleMintBuilder addMetaData(MetaData metaData) {
            this.metaData = metaData;
            return this;
        }

        public SingleMintBuilder setDid(String did) {
            node.put("did_id", did);
            return this;
        }

        public SingleMintBuilder addRoyaltyAddress(String address) {
            node.put("royalty_address", address);
            return this;
        }

        public SingleMintBuilder setRoyaltyPercentage(int amount) {
            node.put("royalty_percentage", amount);
            return this;
        }

        public SingleMintBuilder setTargetAddress(String address) {
            node.put("target_address", address);
            return this;
        }

        public SingleMintBuilder addFee(long amount) {
            node.put("fee", amount);
            return this;
        }

        public SingleMintBuilder setReusePuzHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public JsonNode build() {
            if (metaData == null) { throw new IllegalStateException("Must Set Meta Data"); }
            if (walletId == -1) { throw new IllegalStateException("Must Set Wallet Id"); }
            node.put("wallet_id", walletId);
            return JsonUtils.merge(node, JsonUtils.getMapper().valueToTree(metaData));
        }
    }


    public static class SetDidBulkBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<NftCoin> nftCoins = new ArrayList<>();

        public SetDidBulkBuilder addNft(String coinId, int walletId) {
            nftCoins.add(new NftCoin(coinId, walletId));
            return this;
        }

        public SetDidBulkBuilder setDid(String did) {
            node.put("did_id", did);
            return this;
        }

        public SetDidBulkBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public SetDidBulkBuilder setReusePuzHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public JsonNode build() {
            if (!node.has("did_id")) { throw new IllegalStateException("Must Set a DID"); }
            if (nftCoins.isEmpty()) { throw new IllegalStateException("Must Add NFTs"); }
            node.putPOJO("nft_coin_list", nftCoins);
            return node;
        }


    }


    public static class NftBulkTransferBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<NftCoin> nftCoins = new ArrayList<>();

        public NftBulkTransferBuilder addNft(String coinId, int walletId) {
            nftCoins.add(new NftCoin(coinId, walletId));
            return this;
        }

        public NftBulkTransferBuilder setTargetAddress(String address) {
            node.put("target_address", address);
            return this;
        }

        public NftBulkTransferBuilder addFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public NftBulkTransferBuilder setReusePuzHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public JsonNode build() {
            if (nftCoins.isEmpty()) { throw new IllegalStateException("Must Add NFT Coins"); }
            if (!node.has("target_address")) { throw new IllegalStateException("Must Set Target Address"); }
            node.putPOJO("nft_coin_list", nftCoins);
            return node;
        }

    }


    private record NftCoin(
            String nft_coin_id,
            int wallet_id
    ) { }


    public static class CoinRecordBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public CoinRecordBuilder setOffset(int offset) {
            node.put("offset", offset);
            return this;
        }

        public CoinRecordBuilder setLimit(int limit) {
            node.put("limit", limit);
            return this;
        }

        public CoinRecordBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public CoinRecordBuilder setCoinType(int coinType) {
            node.put("coin_type", coinType);
            return this;
        }

        public CoinRecordBuilder setCoinType(CoinType coinType) {
            node.put("coin_type", coinType.enumValue);
            return this;
        }

        public CoinRecordBuilder setCoinIdFilter(List<String> coinIds, boolean exclude) {
            node.putPOJO("coin_id_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", coinIds)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setPuzzleHashFilter(List<String> puzzleHashes, boolean exclude) {
            node.putPOJO("puzzle_hash_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", puzzleHashes)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setParentCoinIdFilter(List<String> parentCoinIds, boolean exclude) {
            node.putPOJO("parent_coin_id_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", parentCoinIds)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setAmountFilter(List<Long> amounts, boolean exclude) {
            node.putPOJO("amount_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", amounts)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setAmountRange(long start, long end) {
            node.putPOJO("amount_range",
                    new JsonUtils.ObjectBuilder()
                            .put("start", start)
                            .put("stop", end)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setConfirmedRange(long start, long end) {
            node.putPOJO("confirmed_range",
                    new JsonUtils.ObjectBuilder()
                            .put("start", start)
                            .put("stop", end)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setSpentRange(long start, long end) {
            node.putPOJO("spent_range",
                    new JsonUtils.ObjectBuilder()
                            .put("start", start)
                            .put("stop", end)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder orderByConfirmHeight() {
            node.put("order", 1);
            return this;
        }

        public CoinRecordBuilder orderBySpendHeight() {
            node.put("order", 2);
            return this;
        }

        public CoinRecordBuilder reversed(boolean reversed) {
            node.put("reverse", reversed);
            return this;
        }

        public CoinRecordBuilder includeTotalCount(boolean includeTotalCount) {
            node.put("include_total_count", includeTotalCount);
            return this;
        }

        public JsonNode build() {
            return node;
        }
    }


    public static class HarvesterConfigBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public HarvesterConfigBuilder useGpuHarvesting(boolean bool) {
            node.put("use_gpu_harvesting", bool);
            return this;
        }

        public HarvesterConfigBuilder gpuIndex(int index) {
            node.put("gpu_index", index);
            return this;
        }

        public HarvesterConfigBuilder enforceGpuIndex(boolean bool) {
            node.put("enforce_gpu_index", bool);
            return this;
        }

        public HarvesterConfigBuilder disableCpuAffinity(boolean bool) {
            node.put("disable_cpu_affinity", bool);
            return this;
        }

        public HarvesterConfigBuilder parallelDecompressorCount(int count) {
            node.put("parallel_decompressor_count", count);
            return this;
        }

        public HarvesterConfigBuilder decompressorThreadCount(int count) {
            node.put("decompressor_thread_count", count);
            return this;
        }

        public HarvesterConfigBuilder recursivePlotScan(boolean bool) {
            node.put("recursive_plot_scan", bool);
            return this;
        }

        public HarvesterConfigBuilder refreshParameterIntervalCount(int count) {
            node.put("refresh_parameter_interval_seconds", count);
            return this;
        }

        public JsonNode build() {
            return node;
        }

    }
}