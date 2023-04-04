package io.mindspice.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.schemas.object.Coin;

import java.util.List;


public class ChiaUtils {

    public static class CatSpendBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();

        public CatSpendBuilder setWalletId(int walletId) {
            node.put("wallet_id", walletId);
            return this;
        }

        public CatSpendBuilder setAdditions(List<Coin> additions) {
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

        public CatSpendBuilder setFee(long fee) {
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

        public ObjectNode build() {
            if (node.has("additions") && node.has("amount")) {
                throw new IllegalStateException("Must have either additions or amount, not both");
            }
            if (!node.has("additions") && !node.has("amount")) {
                throw new IllegalStateException("Must have either additions or an amount");
            }
            if (!node.has("inner_address")) {
                throw new IllegalStateException("Must have an inner address");
            }
            return node;
        }
    }


    public static class OfferBuilder {
        private final ObjectNode node = JsonUtils.newEmptyNode();
        private final ObjectNode offerNode = JsonUtils.newEmptyNode();
        private final ObjectNode driverNode = JsonUtils.newEmptyNode();
        private ObjectNode solver;

        public OfferBuilder putAsset(String asset, int value) {
            offerNode.put(asset, value);
            return this;
        }

        public OfferBuilder putDriverDict(String prop, String value) {
            driverNode.put(prop, value);
            return this;
        }

        public <T> OfferBuilder putSolver(String prop, T value) {
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

        public OfferBuilder setFee(long fee) {
            node.put("fee", fee);
            return this;
        }

        public ObjectNode build() {
            if (offerNode.isEmpty() || driverNode.isEmpty()) {
                throw new IllegalStateException("Must have both offered assets and driver dict entries");
            }
            node.putPOJO("offer", offerNode);
            node.putPOJO("driver_dict", driverNode);
            if (solver != null) { node.putPOJO("solver", solver); }
            return node;
        }

        public static class OfferSearchBuilder {
            private final ObjectNode node = JsonUtils.newEmptyNode();

            public OfferSearchBuilder setStart(int start){
                node.put("start", start);
                return this;
            }

            public OfferSearchBuilder setEnd(int end){
                node.put("end", end);
                return this;
            }

            public OfferSearchBuilder excludeMadeOffers(boolean bool){
                node.put("exclude_my_offers", bool);
                return this;
            }

            public OfferSearchBuilder excludeTakenOffers(boolean bool){
                node.put("exclude_taken_offers", bool);
                return this;
            }

            public OfferSearchBuilder includeCompletedOffers(boolean bool){
                node.put("include_completed", bool);
                return this;
            }

            public OfferSearchBuilder setSortKey(int sortKey){
                node.put("sort_key", sortKey);
                return this;
            }

            public OfferSearchBuilder reverse(boolean bool){
                node.put("reverse", bool);
                return this;
            }

            public OfferSearchBuilder displayFileContents(boolean bool){
                node.put("file_contents", bool);
                return this;
            }

            public ObjectNode build(){
                return node;
            }






        }
    }


}
