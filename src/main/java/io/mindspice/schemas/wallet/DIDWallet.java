package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;


public class DIDWallet extends Wallet {
    @JsonProperty("my_did")
    private final String myDID;


    public DIDWallet(int type, int walletId, boolean success, String myDID) {
        super(type, walletId, success);
        this.myDID = myDID;
    }


    public String myDID() {
        return myDID;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DIDWallet didWallet = (DIDWallet) o;
        return Objects.equals(myDID, didWallet.myDID);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), myDID);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", DIDWallet.class.getSimpleName() + "[", "]")
                .add("myDID='" + myDID + "'")
                .toString();
    }
}
