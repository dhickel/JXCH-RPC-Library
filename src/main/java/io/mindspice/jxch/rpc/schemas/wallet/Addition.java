package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.math.BigInteger;
import java.util.Objects;
import java.util.StringJoiner;


public class Addition {
    @JsonProperty("puzzle_hash")
    private String puzzleHash;
    @JsonProperty("amount")
    private BigInteger amount;
    @JsonProperty("memos")
    private List<String> memos;


    public Addition(String puzzleHash, long amount) {
        this(puzzleHash, BigInteger.valueOf(amount), null);
    }

    public Addition(String puzzleHash, BigInteger amount) {
        this(puzzleHash, amount, null);
    }

    public Addition(String puzzleHash, long amount, List<String> memos) {
        this(puzzleHash, BigInteger.valueOf(amount), memos);
    }

    public Addition(String puzzleHash, BigInteger amount, List<String> memos) {
        this.puzzleHash = puzzleHash;
        this.amount = amount;
        this.memos = memos == null ? null : List.copyOf(memos);
    }

    public Addition() {
    }

    public String puzzleHash() {
        return puzzleHash;
    }

    public BigInteger amount() {
        return amount;
    }

    public List<String> memos() {
        return memos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Addition addition = (Addition) o;
        return Objects.equals(amount, addition.amount) && Objects.equals(puzzleHash, addition.puzzleHash)
                && Objects.equals(memos, addition.memos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(puzzleHash, amount, memos);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Addition.class.getSimpleName() + "[", "]")
                .add("puzzleHash='" + puzzleHash + "'")
                .add("amount=" + amount)
                .add("memos=" + memos)
                .toString();
    }
}
