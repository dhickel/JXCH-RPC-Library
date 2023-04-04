package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;


public class Wallet {
    @JsonProperty("type")
    private final int type;
    @JsonProperty("wallet_id")
    private final int walletId;
    @JsonProperty("success")
    private final boolean success;


    public Wallet(int type, int walletId, boolean success) {
        this.type = type;
        this.walletId = walletId;
        this.success = success;
    }


    public int type() {
        return type;
    }


    public int walletId() {
        return walletId;
    }


    public boolean success() {
        return success;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return type == wallet.type && walletId == wallet.walletId && success == wallet.success;
    }


    @Override
    public int hashCode() {
        return Objects.hash(type, walletId, success);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Wallet.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("walletId=" + walletId)
                .add("success=" + success)
                .toString();
    }
}

