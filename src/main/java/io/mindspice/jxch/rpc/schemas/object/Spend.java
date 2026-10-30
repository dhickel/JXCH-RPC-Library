package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.math.BigInteger;
import java.util.List;

public record Spend(
        @JsonProperty("agg_sig_me") List<List<String>> aggSigMe,
        @JsonProperty("before_height_relative") Long beforeHeightRelative,
        @JsonProperty("before_seconds_relative") BigInteger beforeSecondsRelative,
        @JsonProperty("birth_height") Long birthHeight,
        @JsonProperty("birth_seconds") BigInteger birthSeconds,
        @JsonProperty("coin_id") String coinId,
        @JsonProperty("create_coin") List<JsonNode> createCoin,
        @JsonProperty("flags") long flags,
        @JsonProperty("height_relative") Long heightRelative,
        @JsonProperty("puzzle_hash") String puzzleHash,
        @JsonProperty("seconds_relative") BigInteger secondsRelative,
        @JsonProperty("parent_id") String parentId,
        @JsonProperty("coin_amount") BigInteger coinAmount,
        @JsonProperty("agg_sig_parent") List<List<String>> aggSigParent,
        @JsonProperty("agg_sig_puzzle") List<List<String>> aggSigPuzzle,
        @JsonProperty("agg_sig_amount") List<List<String>> aggSigAmount,
        @JsonProperty("agg_sig_puzzle_amount") List<List<String>> aggSigPuzzleAmount,
        @JsonProperty("agg_sig_parent_amount") List<List<String>> aggSigParentAmount,
        @JsonProperty("agg_sig_parent_puzzle") List<List<String>> aggSigParentPuzzle,
        @JsonProperty("execution_cost") BigInteger executionCost,
        @JsonProperty("condition_cost") BigInteger conditionCost,
        @JsonProperty("atom_count") BigInteger atomCount,
        @JsonProperty("pair_count") BigInteger pairCount,
        @JsonProperty("fingerprint") String fingerprint
) {
    public Spend {
        aggSigMe = signatures(aggSigMe);
        aggSigParent = signatures(aggSigParent);
        aggSigPuzzle = signatures(aggSigPuzzle);
        aggSigAmount = signatures(aggSigAmount);
        aggSigPuzzleAmount = signatures(aggSigPuzzleAmount);
        aggSigParentAmount = signatures(aggSigParentAmount);
        aggSigParentPuzzle = signatures(aggSigParentPuzzle);
        createCoin = createCoin == null ? List.of() : List.copyOf(createCoin);
    }

    @Deprecated
    public Spend(List<List<String>> aggSigMe, int beforeHeightRelative, int beforeSecondsRelative,
                 int birthHeight, int birthSeconds, String coinId, List<List<String>> createCoin,
                 int flags, int heightRelative, String puzzleHash, long secondsRelative) {
        this(aggSigMe, (long) beforeHeightRelative, BigInteger.valueOf(beforeSecondsRelative), (long) birthHeight,
                BigInteger.valueOf(birthSeconds), coinId,
                createCoin == null ? List.of() : createCoin.stream().map(Spend::legacyCreateCoin).toList(),
                flags, (long) heightRelative, puzzleHash, BigInteger.valueOf(secondsRelative), null, null,
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), null, null, null, null, null);
    }

    private static List<List<String>> signatures(List<List<String>> pairs) {
        return pairs == null ? List.of() : pairs.stream().map(List::copyOf).toList();
    }

    private static JsonNode legacyCreateCoin(List<String> tuple) {
        if (tuple.size() != 3) {
            throw new IllegalArgumentException("create_coin requires [puzzle_hash, amount, hint] tuples");
        }
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        result.add(tuple.get(0));
        result.add(new BigInteger(tuple.get(1)));
        result.add(tuple.get(2));
        return result;
    }
}
