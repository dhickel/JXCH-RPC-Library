package io.mindspice.jxch.rpc.schemas.wallet.dao;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.wallet.Transaction;


public record DAOResponse(
        @JsonProperty("tx") Transaction tx,
        @JsonProperty("tx_id") String txId,
        @JsonProperty("transactions") List<Transaction> transactions
) {
    public DAOResponse {
        transactions = transactions != null ? Collections.unmodifiableList(transactions) : List.of();
    }
}