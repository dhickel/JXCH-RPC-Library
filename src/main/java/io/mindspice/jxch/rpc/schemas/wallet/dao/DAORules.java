package io.mindspice.jxch.rpc.schemas.wallet.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DAORules(
    @JsonProperty("soft_close_length") long softCloseLength,
    @JsonProperty("attendance_required") long attendanceRequired,
    @JsonProperty("self_destruct_length") long selfDestructLength,
    @JsonProperty("pass_percentage") long passPercentage,
    @JsonProperty("proposal_minimum_amount") long proposalMinimumAmount,
    @JsonProperty("proposal_timelock") long proposalTimelock,
    @JsonProperty("oracle_spend_delay") long oracleSpendDelay
) {
}