package io.mindspice.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;


public record TransactionStatus(
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("transaction") Transaction transaction,
        @JsonProperty("success") boolean success
) {
}