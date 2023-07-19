package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;


public record TransactionStatus(
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("transaction") Transaction transaction,
        @JsonProperty("success") boolean success
) {
}