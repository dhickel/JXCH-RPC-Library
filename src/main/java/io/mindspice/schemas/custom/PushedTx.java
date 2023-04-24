package io.mindspice.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PushedTx(
        @JsonProperty("status") String status,
        @JsonProperty("spend_bundle_hash") String spendBundleHash
) {
}
