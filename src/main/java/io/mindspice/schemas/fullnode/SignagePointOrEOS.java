package io.mindspice.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.SignagePoint;
import io.mindspice.schemas.object.SubSlot;


public record SignagePointOrEOS(
        @JsonProperty("time_received") long timeReceived,
        @JsonProperty("reverted") boolean reverted,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("signage_point") SignagePoint signagePoint,
        @JsonProperty("eos") SubSlot eos
) { }
