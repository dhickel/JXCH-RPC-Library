package io.mindspice.jxch.rpc.schemas.wallet.did;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record DIDAttest(
        @JsonProperty("attest_data") String attestData,
        @JsonProperty("success") boolean success,
        @JsonProperty("message_spend_bundle") String messageSpendBundle,
        @JsonProperty("info") List<String> info
) {
    public DIDAttest {
        info = info != null ? Collections.unmodifiableList(info) : List.of();
    }
}