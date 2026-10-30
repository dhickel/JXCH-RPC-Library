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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RequestUtils {

    private static <T> void setValue(ObjectNode node, String field, T value) {
        node.set(field, JsonUtils.getMapper().valueToTree(value));
    }

    private static ObjectNode snapshot(ObjectNode node) {
        return node.deepCopy();
    }

    private static void putAnnouncements(ObjectNode node, String field, List<?> announcements) {
        var array = JsonUtils.getMapper().createArrayNode();
        for (Object announcement : announcements) {
            ObjectNode entry = JsonUtils.getMapper().valueToTree(announcement);
            JsonNode morphBytes = entry.remove("morph_bytes");
            if (morphBytes != null && !morphBytes.isNull()) {
                if (node.hasNonNull("morph_bytes") && !node.get("morph_bytes").equals(morphBytes)) {
                    throw new IllegalArgumentException("All announcements must use the same morph_bytes");
                }
                node.set("morph_bytes", morphBytes);
            }
            array.add(entry);
        }
        node.set(field, array);
    }

    public static class CatSpendBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();

        public CatSpendBuilder setWalletId(long walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public CatSpendBuilder setAdditions(List<Addition> additions) {
            setValue(node, "additions", additions);
            return this;
        }

        public CatSpendBuilder setAmount(long amount) {
            return setAmount(BigInteger.valueOf(amount));
        }

        public CatSpendBuilder setAmount(BigInteger amount) {
            node.put("amount", amount);
            return this;
        }

        public CatSpendBuilder setInnerAddress(String address) {
            node.put("inner_address", address);
            return this;
        }

        public CatSpendBuilder setMemos(List<String> memos) {
            setValue(node, "memos", memos);
            return this;
        }

        public CatSpendBuilder setCoins(List<Coin> coins) {
            setValue(node, "coins", coins);
            return this;
        }

        public CatSpendBuilder setMinCoinAmount(long amount) {
            return setMinCoinAmount(BigInteger.valueOf(amount));
        }

        public CatSpendBuilder setMinCoinAmount(BigInteger amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public CatSpendBuilder setMaxCoinAmount(long amount) {
            return setMaxCoinAmount(BigInteger.valueOf(amount));
        }

        public CatSpendBuilder setMaxCoinAmount(BigInteger amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public CatSpendBuilder setExcludedCoinAmounts(List<? extends Number> amounts) {
            setValue(node, "excluded_coin_amounts", amounts);
            return this;
        }

        public CatSpendBuilder setExcludedCoinIds(List<String> coinIds) {
            setValue(node, "excluded_coin_ids", coinIds);
            return this;
        }

        public CatSpendBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public CatSpendBuilder addFee(BigInteger fee) {
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
            if (!node.hasNonNull("wallet_id")) { throw new IllegalStateException("Must set a wallet ID"); }
            boolean additions = node.hasNonNull("additions");
            boolean single = node.hasNonNull("amount") || node.hasNonNull("inner_address") || node.hasNonNull("memos");
            if (additions == single || (!additions && (!node.hasNonNull("amount") || !node.hasNonNull("inner_address")))) {
                throw new IllegalStateException("Specify additions or amount and inner_address, but not both");
            }
            int discrepancyFields = (node.hasNonNull("extra_delta") ? 1 : 0)
                    + (node.hasNonNull("tail_reveal") ? 1 : 0) + (node.hasNonNull("tail_solution") ? 1 : 0);
            if (discrepancyFields != 0 && discrepancyFields != 3) {
                throw new IllegalStateException("Specify extra_delta, tail_reveal and tail_solution together");
            }
            return snapshot(node);
        }
    }


    public static class OfferBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();
        private final ObjectNode offerNode = JsonUtils.newEmptyNode();
        private final ObjectNode driverNode = JsonUtils.newEmptyNode();
        private ObjectNode solver;

        public OfferBuilder addRequestedAsset(String asset, long value) {
            return addRequestedAsset(asset, BigInteger.valueOf(value));
        }

        public OfferBuilder addRequestedAsset(String asset, BigInteger value) {
            if (value.signum() <= 0) { throw new IllegalArgumentException("Value must be positive for requested assets."); }
            offerNode.put(asset, value.toString());
            return this;
        }

        public OfferBuilder addOfferedAsset(String asset, long value) {
            return addOfferedAsset(asset, BigInteger.valueOf(value));
        }

        public OfferBuilder addOfferedAsset(String asset, BigInteger value) {
            if (value.signum() == 0) { throw new IllegalArgumentException("Offered amount cannot be zero"); }
            offerNode.put(asset, value.abs().negate().toString());
            return this;
        }

        public OfferBuilder addDriverDict(String prop, String value) {
            try {
                return addDriverDict(prop, JsonUtils.readTree(value));
            } catch (java.io.IOException e) {
                throw new IllegalArgumentException("Driver information must be a JSON object", e);
            }
        }

        public OfferBuilder addDriverDict(String prop, JsonNode value) {
            if (value == null || !value.isObject()) {
                throw new IllegalArgumentException("Driver information must be a JSON object");
            }
            driverNode.set(prop, value.deepCopy());
            return this;
        }

        public OfferBuilder addDriverDict(String prop, Map<String, ?> value) {
            return addDriverDict(prop, JsonUtils.toNode(value));
        }

        public <T> OfferBuilder addSolver(String prop, T value) {
            if (solver == null) { solver = JsonUtils.newEmptyNode(); }
            setValue(solver, prop, value);
            return this;
        }

        public OfferBuilder isValidateOnly(boolean bool) {
            node.put("validate_only", bool);
            return this;
        }

        public OfferBuilder setMinCoinAmount(long amount) {
            return setMinCoinAmount(BigInteger.valueOf(amount));
        }

        public OfferBuilder setMinCoinAmount(BigInteger amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public OfferBuilder setMaxCoinAmount(long amount) {
            return setMaxCoinAmount(BigInteger.valueOf(amount));
        }

        public OfferBuilder setMaxCoinAmount(BigInteger amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public OfferBuilder setExcludedCoins(List<Coin> coins) {
            setValue(node, "excluded_coins", coins);
            return this;
        }

        public OfferBuilder setExcludedCoinIds(List<String> coinIds) {
            setValue(node, "excluded_coin_ids", coinIds);
            return this;
        }

        public OfferBuilder setReusePuzzleHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public OfferBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public OfferBuilder addFee(BigInteger fee) {
            node.put("fee", fee);
            return this;
        }

        public OfferBuilder setMaxValidHeight(long height) {
            node.put("max_height", height);
            return this;
        }

        public OfferBuilder setMaxValidTime(long unixTime) {
            return setMaxValidTime(BigInteger.valueOf(unixTime));
        }

        public OfferBuilder setMaxValidTime(BigInteger unixTime) {
            node.put("max_time", unixTime);
            return this;
        }

        public OfferBuilder setMinValidHeight(long height) {
            node.put("min_height", height);
            return this;
        }

        public OfferBuilder setMinValidTime(long unixTime) {
            return setMinValidTime(BigInteger.valueOf(unixTime));
        }

        public OfferBuilder setMinValidTime(BigInteger unixTime) {
            node.put("min_time", unixTime);
            return this;
        }

        public JsonNode build() {
            if (offerNode.isEmpty()) { throw new IllegalStateException("Must specify an offer"); }
            node.set("offer", offerNode.deepCopy());
            if (!driverNode.isEmpty()) { node.set("driver_dict", driverNode.deepCopy()); }
            if (solver != null) { node.set("solver", solver.deepCopy()); }
            return snapshot(node);
        }
    }


    public static class OfferSearchBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();

        public OfferSearchBuilder setStart(long start) {
            node.put("start", start);
            return this;
        }

        public OfferSearchBuilder setEnd(long end) {
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

        @Deprecated
        public OfferSearchBuilder setSortKey(int sortKey) {
            throw new IllegalArgumentException("Chia requires a named sort key; use setSortKey(String)");
        }

        public OfferSearchBuilder setSortKey(String sortKey) {
            if (!"CONFIRMED_AT_HEIGHT".equals(sortKey) && !"RELEVANCE".equals(sortKey)) {
                throw new IllegalArgumentException("Unknown offer sort key: " + sortKey);
            }
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
            return snapshot(node);
        }
    }


    public static class SelectCoinBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();

        public SelectCoinBuilder setWalletId(long walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public SelectCoinBuilder setAmount(long amount) {
            return setAmount(BigInteger.valueOf(amount));
        }

        public SelectCoinBuilder setAmount(BigInteger amount) {
            node.put("amount", amount);
            return this;
        }

        public SelectCoinBuilder setMinCoinAmount(long amount) {
            return setMinCoinAmount(BigInteger.valueOf(amount));
        }

        public SelectCoinBuilder setMinCoinAmount(BigInteger amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public SelectCoinBuilder setMaxCoinAmount(long amount) {
            return setMaxCoinAmount(BigInteger.valueOf(amount));
        }

        public SelectCoinBuilder setMaxCoinAmount(BigInteger amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public SelectCoinBuilder setExcludedCoinAmounts(List<? extends Number> amounts) {
            setValue(node, "excluded_coin_amounts", amounts);
            return this;
        }

        public SelectCoinBuilder setExcludedCoins(List<Coin> coins) {
            setValue(node, "excluded_coins", coins);
            return this;
        }

        public ObjectNode build() {
            if (!node.has("wallet_id") || !node.has("amount")) {
                throw new IllegalStateException("Must have both wallet id and amount");
            }
            return snapshot(node);
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
            return setMinCoinAmount(BigInteger.valueOf(amount));
        }

        public TakeOfferBuilder setMinCoinAmount(BigInteger amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public TakeOfferBuilder setMaxCoinAmount(long amount) {
            return setMaxCoinAmount(BigInteger.valueOf(amount));
        }

        public TakeOfferBuilder setMaxCoinAmount(BigInteger amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public TakeOfferBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public TakeOfferBuilder addFee(BigInteger fee) {
            node.put("fee", fee);
            return this;
        }

        public TakeOfferBuilder setReusePuzzleHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public <T> TakeOfferBuilder addSolver(String prop, T value) {
            if (solver == null) { solver = JsonUtils.newEmptyNode(); }
            setValue(solver, prop, value);
            return this;
        }

        public JsonNode build() {
            if (!node.has("offer")) {
                throw new IllegalStateException("Must include an offer");
            }
            if (solver != null) { node.set("solver", solver.deepCopy()); }
            return snapshot(node);
        }
    }


    public static class CatWalletBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public CatWalletBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public CatWalletBuilder addFee(BigInteger fee) {
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
            return setAmount(BigInteger.valueOf(amount));
        }

        public CatWalletBuilder setAmount(BigInteger amount) {
            node.put("amount", amount);
            return this;
        }

        public CatWalletBuilder setTest(boolean test) {
            node.put("test", test);
            return this;
        }

        public CatWalletBuilder setAssetId(String assetId) {
            node.put("asset_id", assetId);
            return this;
        }

        public JsonNode build() {
            String mode = node.path("mode").asText();
            if ("new".equals(mode)) {
                if (!node.path("test").asBoolean()) {
                    throw new IllegalStateException("New CAT issuance requires explicit test mode; use the CAT Admin Tool in production");
                }
                if (!node.hasNonNull("amount") || node.hasNonNull("asset_id")) {
                    throw new IllegalStateException("New CAT wallets require amount and cannot specify asset_id");
                }
            } else if ("existing".equals(mode)) {
                if (!node.hasNonNull("asset_id") || node.hasNonNull("amount")) {
                    throw new IllegalStateException("Existing CAT wallets require asset_id and cannot specify amount");
                }
            } else {
                throw new IllegalStateException("Must set new or existing CAT wallet mode");
            }
            node.put("wallet_type", "cat_wallet");
            return snapshot(node);
        }
    }


    public static class DIDWalletBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public DIDWalletBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public DIDWalletBuilder addFee(BigInteger fee) {
            node.put("fee", fee);
            return this;
        }

        public DIDWalletBuilder setName(String name) {
            node.put("wallet_name", name);
            return this;
        }

        public DIDWalletBuilder isRecovery(boolean bool) {
            return isNewWallet(!bool);
        }

        public DIDWalletBuilder setBackupData(String backupData) {
            node.put("backup_data", backupData);
            return this;
        }

        @Deprecated
        public DIDWalletBuilder setBackupDIDS(List<String> dids) {
            if (!dids.isEmpty()) {
                throw new IllegalArgumentException("Chia 2.7.5 no longer supports DID recovery lists");
            }
            return this;
        }

        public <T, U> DIDWalletBuilder setMetaData(Map<T, U> metaDict) {
            return setMetaData(JsonUtils.toNode(metaDict));
        }

        public DIDWalletBuilder setMetaData(JsonNode metaDict) {
            if (metaDict == null || !metaDict.isObject()) {
                throw new IllegalArgumentException("DID metadata must be a string-to-string object");
            }
            metaDict.fields().forEachRemaining(entry -> {
                if (!entry.getValue().isTextual()) {
                    throw new IllegalArgumentException("DID metadata values must be strings");
                }
            });
            node.set("metadata", metaDict.deepCopy());
            return this;
        }

        @Deprecated
        public DIDWalletBuilder setNumOfBackUpsNeeded(int num) {
            if (num != 0) {
                throw new IllegalArgumentException("Chia 2.7.5 no longer supports DID recovery thresholds");
            }
            return this;
        }

        public DIDWalletBuilder setAmount(long amount) {
            return setAmount(BigInteger.valueOf(amount));
        }

        public DIDWalletBuilder setAmount(BigInteger amount) {
            node.put("amount", amount);
            return this;
        }

        public DIDWalletBuilder isNewWallet(boolean bool) {
            node.put("did_type", bool ? "new" : "recovery");
            return this;
        }

        public JsonNode build() {
            String type = node.path("did_type").asText();
            if ("new".equals(type)) {
                if (!node.hasNonNull("amount") || node.hasNonNull("backup_data")) {
                    throw new IllegalStateException("New DIDs require amount and cannot specify backup_data");
                }
            } else if ("recovery".equals(type)) {
                if (!node.hasNonNull("backup_data") || node.hasNonNull("amount")
                        || (node.has("metadata") && !node.get("metadata").isEmpty())) {
                    throw new IllegalStateException("DID recovery requires backup_data without amount or metadata");
                }
            } else {
                throw new IllegalStateException("Must set new or recovery DID wallet type");
            }
            node.put("wallet_type", "did_wallet");
            return snapshot(node);
        }
    }


    public static class NFTWalletBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public NFTWalletBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public NFTWalletBuilder addFee(BigInteger fee) {
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
            return snapshot(node);
        }
    }


    public static class SignedTransactionBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<Addition> additions = new ArrayList<>();
        final List<Coin> coins = new ArrayList<>();

        public SignedTransactionBuilder setWalletId(long walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public SignedTransactionBuilder addAddition(String coinPuzzleHash, long amount) {
            return addAddition(coinPuzzleHash, BigInteger.valueOf(amount));
        }

        public SignedTransactionBuilder addAddition(String coinPuzzleHash, BigInteger amount) {
            additions.add(new Addition(coinPuzzleHash, amount));
            return this;
        }

        public SignedTransactionBuilder addAddition(String coinPuzzleHash, long amount, List<String> memos) {
            return addAddition(coinPuzzleHash, BigInteger.valueOf(amount), memos);
        }

        public SignedTransactionBuilder addAddition(String coinPuzzleHash, BigInteger amount, List<String> memos) {
            additions.add(new Addition(coinPuzzleHash, amount, memos));
            return this;
        }

        public SignedTransactionBuilder addAdditions(List<Addition> additions) {
            additions.forEach(addition -> this.additions.add(new Addition(
                    addition.puzzleHash(), addition.amount(), addition.memos())));
            return this;
        }

        public SignedTransactionBuilder setMinCoinAmount(long amount) {
            return setMinCoinAmount(BigInteger.valueOf(amount));
        }

        public SignedTransactionBuilder setMinCoinAmount(BigInteger amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public SignedTransactionBuilder setMaxCoinAmount(long amount) {
            return setMaxCoinAmount(BigInteger.valueOf(amount));
        }

        public SignedTransactionBuilder setMaxCoinAmount(BigInteger amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public SignedTransactionBuilder setExcludedCoinAmounts(List<? extends Number> amounts) {
            setValue(node, "excluded_coin_amounts", amounts);
            return this;
        }

        public SignedTransactionBuilder setExcludedCoins(List<Coin> coins) {
            setValue(node, "excluded_coins", coins);
            return this;
        }

        public SignedTransactionBuilder addCoin(String coinPuzzleHash, String parentPuzzleHash, long amount) {
            return addCoin(coinPuzzleHash, parentPuzzleHash, BigInteger.valueOf(amount));
        }

        public SignedTransactionBuilder addCoin(String coinPuzzleHash, String parentPuzzleHash, BigInteger amount) {
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
            putAnnouncements(node, "coin_announcements", coinAnnouncements);
            return this;
        }

        public SignedTransactionBuilder addPuzzleAnnouncements(List<PuzzleAnnouncement> puzzleAnnouncements) {
            putAnnouncements(node, "puzzle_announcements", puzzleAnnouncements);
            return this;
        }

        public SignedTransactionBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public SignedTransactionBuilder addFee(BigInteger fee) {
            node.put("fee", fee);
            return this;
        }

        public JsonNode build() {
            if (additions.isEmpty()) { throw new IllegalStateException("Must add at least one addition"); }
            setValue(node, "additions", additions);
            if (!coins.isEmpty()) { setValue(node, "coins", coins); }
            return snapshot(node);
        }
    }


    public static class SpendableCoinBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public SpendableCoinBuilder setWalletId(long walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public SpendableCoinBuilder setMinCoinAmount(long amount) {
            return setMinCoinAmount(BigInteger.valueOf(amount));
        }

        public SpendableCoinBuilder setMinCoinAmount(BigInteger amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public SpendableCoinBuilder setMaxCoinAmount(long amount) {
            return setMaxCoinAmount(BigInteger.valueOf(amount));
        }

        public SpendableCoinBuilder setMaxCoinAmount(BigInteger amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public SpendableCoinBuilder setExcludedCoinAmounts(List<? extends Number> amounts) {
            setValue(node, "excluded_coin_amounts", amounts);
            return this;
        }

        public SpendableCoinBuilder setExcludedCoins(List<Coin> coins) {
            setValue(node, "excluded_coins", coins);
            return this;
        }

        public SpendableCoinBuilder setExcludedCoinIds(List<String> coinIds) {
            setValue(node, "excluded_coin_ids", coinIds);
            return this;
        }

        public ObjectNode build() {
            if (!node.has("wallet_id")) {
                throw new IllegalStateException("Must set a wallet id");
            }

            return snapshot(node);
        }
    }


    public static class TransactionBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public TransactionBuilder setWalletId(long walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public TransactionBuilder setMinCoinAmount(long amount) {
            return setMinCoinAmount(BigInteger.valueOf(amount));
        }

        public TransactionBuilder setMinCoinAmount(BigInteger amount) {
            node.put("min_coin_amount", amount);
            return this;
        }

        public TransactionBuilder setAddress(String address) {
            node.put("address", address);
            return this;
        }

        public TransactionBuilder setAmount(long amount) {
            return setAmount(BigInteger.valueOf(amount));
        }

        public TransactionBuilder setAmount(BigInteger amount) {
            node.put("amount", amount);
            return this;
        }

        public TransactionBuilder setMaxCoinAmount(long amount) {
            return setMaxCoinAmount(BigInteger.valueOf(amount));
        }

        public TransactionBuilder setMaxCoinAmount(BigInteger amount) {
            node.put("max_coin_amount", amount);
            return this;
        }

        public TransactionBuilder setExcludedCoinAmounts(List<? extends Number> amounts) {
            setValue(node, "excluded_coin_amounts", amounts);
            return this;
        }

        public TransactionBuilder setExcludedCoinIds(List<?> coinIds) {
            if (coinIds.stream().allMatch(String.class::isInstance)) {
                setValue(node, "excluded_coin_ids", coinIds);
            } else if (coinIds.stream().allMatch(Coin.class::isInstance)) {
                setValue(node, "excluded_coins", coinIds);
            } else {
                throw new IllegalArgumentException("Expected coin IDs or Coin objects, not mixed values");
            }
            return this;
        }

        public TransactionBuilder setReusePuzHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public TransactionBuilder addMemos(List<String> memos) {
            setValue(node, "memos", memos);
            return this;
        }

        public TransactionBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public TransactionBuilder addFee(BigInteger fee) {
            node.put("fee", fee);
            return this;
        }

        public JsonNode build() {
            if (!node.hasNonNull("wallet_id") || !node.hasNonNull("amount") || !node.hasNonNull("address")) {
                throw new IllegalStateException("Must set wallet_id, amount and address");
            }
            return snapshot(node);
        }
    }


    public static class MultiTransactionBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<Addition> additions = new ArrayList<>();
        final List<Coin> coins = new ArrayList<>();

        public MultiTransactionBuilder setWalletId(long walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public MultiTransactionBuilder addAddition(String coinPuzzleHash, long amount) {
            return addAddition(coinPuzzleHash, BigInteger.valueOf(amount));
        }

        public MultiTransactionBuilder addAddition(String coinPuzzleHash, BigInteger amount) {
            additions.add(new Addition(coinPuzzleHash, amount));
            return this;
        }

        public MultiTransactionBuilder addAddition(String coinPuzzleHash, long amount, List<String> memos) {
            return addAddition(coinPuzzleHash, BigInteger.valueOf(amount), memos);
        }

        public MultiTransactionBuilder addAddition(String coinPuzzleHash, BigInteger amount, List<String> memos) {
            additions.add(new Addition(coinPuzzleHash, amount, memos));
            return this;
        }

        public MultiTransactionBuilder addAdditions(List<Addition> additions) {
            additions.forEach(addition -> this.additions.add(new Addition(
                    addition.puzzleHash(), addition.amount(), addition.memos())));
            return this;
        }

        public MultiTransactionBuilder addCoin(String coinPuzzleHash, String parentPuzzleHash, long amount) {
            return addCoin(coinPuzzleHash, parentPuzzleHash, BigInteger.valueOf(amount));
        }

        public MultiTransactionBuilder addCoin(String coinPuzzleHash, String parentPuzzleHash, BigInteger amount) {
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
            putAnnouncements(node, "coin_announcements", coinAnnouncements);
            return this;
        }

        public MultiTransactionBuilder addPuzzleAnnouncements(List<PuzzleAnnouncement> puzzleAnnouncements) {
            putAnnouncements(node, "puzzle_announcements", puzzleAnnouncements);
            return this;
        }

        public MultiTransactionBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public MultiTransactionBuilder addFee(BigInteger fee) {
            node.put("fee", fee);
            return this;
        }

        public JsonNode build() {
            if (!node.has("wallet_id")) { throw new IllegalStateException("Must set a wallet ID"); }
            if (additions.isEmpty()) { throw new IllegalStateException("Must add at least one addition"); }
            setValue(node, "additions", additions);
            if (!coins.isEmpty()) { setValue(node, "coins", coins); }
            return snapshot(node);
        }
    }


    public static class SpendBundleBuilder {
        final List<CoinSpend> coinSpends = new ArrayList<>();
        String aggregatedSignature;

        public SpendBundleBuilder addCoinSpend(CoinSpend coinSpend) {
            coinSpends.add(coinSpend);
            return this;
        }

        public SpendBundleBuilder addCoinSpends(List<CoinSpend> coinSpends) {
            this.coinSpends.addAll(coinSpends);
            return this;
        }

        @Deprecated
        public SpendBundleBuilder addPuzzleReveal(String puzzleReveal) {
            throw new IllegalArgumentException("Set puzzle_reveal on each CoinSpend, not on SpendBundle");
        }

        @Deprecated
        public SpendBundleBuilder addSolution(String solution) {
            throw new IllegalArgumentException("Set solution on each CoinSpend, not on SpendBundle");
        }

        public SpendBundleBuilder addAggregatedSignature(String aggSig) {
            this.aggregatedSignature = aggSig;
            return this;
        }

        public SpendBundle build() {
            if (aggregatedSignature == null) {
                throw new IllegalStateException("Must set aggregatedSignature");
            }

            return new SpendBundle(
                    aggregatedSignature,
                    List.copyOf(coinSpends),
                    null,
                    null
            );
        }
    }


    public static class BulkMintBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<MetaData> metaList = new ArrayList<>();
        final List<String> targetList = new ArrayList<>();
        final List<Coin> coinList = new ArrayList<>();

        public BulkMintBuilder setWalletId(long walletId) {
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

        public BulkMintBuilder setMintNumberStart(long start) {
            node.put("mint_number_start", start);
            return this;
        }

        public BulkMintBuilder setMintTotal(long total) {
            node.put("mint_total", total);
            return this;
        }

        @Deprecated
        public BulkMintBuilder addXchCoin(String coinId) {
            throw new IllegalArgumentException("Chia 2.7.5 requires full Coin objects; use addXchCoin(Coin)");
        }

        public BulkMintBuilder addXchCoin(Coin coin) {
            coinList.add(coin);
            return this;
        }

        public BulkMintBuilder addXchCoin(List<?> coins) {
            if (!coins.stream().allMatch(Coin.class::isInstance)) {
                throw new IllegalArgumentException("Chia 2.7.5 requires full Coin objects, not coin IDs");
            }
            coins.forEach(coin -> coinList.add((Coin) coin));
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
            setValue(node, "did_coin", coin);
            node.put("mint_from_did", true);
            return this;
        }

        public BulkMintBuilder setDidLinageParent(String linageParentHex) {
            node.put("did_lineage_parent", linageParentHex);
            return this;
        }

        public BulkMintBuilder addFee(long amont) {
            return addFee(BigInteger.valueOf(amont));
        }

        public BulkMintBuilder addFee(BigInteger amont) {
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
            setValue(node, "metadata_list", metaList);
            if (!targetList.isEmpty()) { setValue(node, "target_list", targetList); }
            if (!coinList.isEmpty()) { setValue(node, "xch_coins", coinList); }

            return snapshot(node);
        }
    }


    public static class MetaDataBuilder {
        final List<String> uris = new ArrayList<>();
        final List<String> metaUris = new ArrayList<>();
        final List<String> licenseUris = new ArrayList<>();
        String hash;
        String metaHash;
        String licenseHash;
        BigInteger editionNumber = BigInteger.ONE;
        BigInteger editionTotal = BigInteger.ONE;

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
            return setEditionNumber((long) edition, total);
        }

        public MetaDataBuilder setEditionNumber(long edition, long total) {
            return setEditionNumber(BigInteger.valueOf(edition), BigInteger.valueOf(total));
        }

        public MetaDataBuilder setEditionNumber(BigInteger edition, BigInteger total) {
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
        long walletId = -1;

        public SingleMintBuilder setWalletId(long id) {
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
            return addFee(BigInteger.valueOf(amount));
        }

        public SingleMintBuilder addFee(BigInteger amount) {
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
            if (!node.has("royalty_address")) { node.putNull("royalty_address"); }
            if (!node.has("target_address")) { node.putNull("target_address"); }
            ObjectNode result = node.deepCopy();
            result.setAll((ObjectNode) JsonUtils.getMapper().valueToTree(metaData));
            return snapshot(result);
        }
    }


    public static class SetDidBulkBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<NftCoin> nftCoins = new ArrayList<>();

        public SetDidBulkBuilder addNft(String coinId, long walletId) {
            nftCoins.add(new NftCoin(coinId, walletId));
            return this;
        }

        public SetDidBulkBuilder setDid(String did) {
            node.put("did_id", did);
            return this;
        }

        public SetDidBulkBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public SetDidBulkBuilder addFee(BigInteger fee) {
            node.put("fee", fee);
            return this;
        }

        public SetDidBulkBuilder setReusePuzHash(boolean bool) {
            node.put("reuse_puzhash", bool);
            return this;
        }

        public JsonNode build() {
            if (nftCoins.isEmpty()) { throw new IllegalStateException("Must Add NFTs"); }
            setValue(node, "nft_coin_list", nftCoins);
            return snapshot(node);
        }


    }


    public static class NftBulkTransferBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();
        final List<NftCoin> nftCoins = new ArrayList<>();

        public NftBulkTransferBuilder addNft(String coinId, long walletId) {
            nftCoins.add(new NftCoin(coinId, walletId));
            return this;
        }

        public NftBulkTransferBuilder setTargetAddress(String address) {
            node.put("target_address", address);
            return this;
        }

        public NftBulkTransferBuilder addFee(long fee) {
            return addFee(BigInteger.valueOf(fee));
        }

        public NftBulkTransferBuilder addFee(BigInteger fee) {
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
            setValue(node, "nft_coin_list", nftCoins);
            return snapshot(node);
        }

    }


    private record NftCoin(
            String nft_coin_id,
            long wallet_id
    ) { }


    public static class CoinRecordBuilder {
        final ObjectNode node = JsonUtils.newEmptyNode();

        public CoinRecordBuilder setOffset(long offset) {
            node.put("offset", offset);
            return this;
        }

        public CoinRecordBuilder setLimit(long limit) {
            node.put("limit", limit);
            return this;
        }

        public CoinRecordBuilder setWalletId(long walletId) {
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
            setValue(node, "coin_id_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", coinIds)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setPuzzleHashFilter(List<String> puzzleHashes, boolean exclude) {
            setValue(node, "puzzle_hash_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", puzzleHashes)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setParentCoinIdFilter(List<String> parentCoinIds, boolean exclude) {
            setValue(node, "parent_coin_id_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", parentCoinIds)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setAmountFilter(List<? extends Number> amounts, boolean exclude) {
            setValue(node, "amount_filter",
                    new JsonUtils.ObjectBuilder()
                            .put("values", amounts)
                            .put("mode", exclude ? 2 : 1)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setAmountRange(long start, long end) {
            return setAmountRange(BigInteger.valueOf(start), BigInteger.valueOf(end));
        }

        public CoinRecordBuilder setAmountRange(BigInteger start, BigInteger end) {
            setValue(node, "amount_range",
                    new JsonUtils.ObjectBuilder()
                            .put("start", start)
                            .put("stop", end)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setConfirmedRange(long start, long end) {
            setValue(node, "confirmed_range",
                    new JsonUtils.ObjectBuilder()
                            .put("start", start)
                            .put("stop", end)
                            .buildNode()
            );
            return this;
        }

        public CoinRecordBuilder setSpentRange(long start, long end) {
            setValue(node, "spent_range",
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
            return snapshot(node);
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