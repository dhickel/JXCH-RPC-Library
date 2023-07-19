package io.mindspice.jxch.rpc.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PushedTx(
        @JsonProperty("status") String status,
        @JsonProperty("spend_bundle_name") String spendBundleName,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error
) {
}