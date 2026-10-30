package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;


public record TransactionStatus(
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("transaction") Transaction transaction,
        @JsonProperty("success") boolean success,
        @JsonProperty("transactions") List<Transaction> transactions,
        @JsonProperty("unsigned_transactions") List<JsonNode> unsignedTransactions,
        @JsonProperty("error") String error
) {
    public TransactionStatus {
        transactions = transactions != null ? List.copyOf(transactions) : List.of();
        unsignedTransactions = unsignedTransactions != null ? List.copyOf(unsignedTransactions) : List.of();
    }

    public TransactionStatus(String transactionId, Transaction transaction, boolean success) {
        this(transactionId, transaction, success, List.of(), List.of(), null);
    }
}