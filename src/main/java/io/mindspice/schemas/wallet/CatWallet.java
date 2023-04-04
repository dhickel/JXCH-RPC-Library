package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;


public class CatWallet extends Wallet {
    @JsonProperty("asset_id")
    private final String assetId;

    public CatWallet(int type, int walletId, boolean success, String assetId) {
        super(type, walletId, success);
        this.assetId = assetId;
    }


    public String assetId() {
        return assetId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CatWallet catWallet = (CatWallet) o;
        return Objects.equals(assetId, catWallet.assetId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), assetId);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", CatWallet.class.getSimpleName() + "[", "]")
                .add("assetId='" + assetId + "'")
                .toString();
    }
}

