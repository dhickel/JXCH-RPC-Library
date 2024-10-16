package io.mindspice.jxch.rpc.schemas.wallet.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DAOProposalState(
	@JsonProperty("blocks_needed") long blocksNeeded,
	@JsonProperty("yes_votes_needed") long yesVotesNeeded,
	@JsonProperty("closable") boolean closable,
	@JsonProperty("closed") boolean closed,
	@JsonProperty("passed") boolean passed,
	@JsonProperty("total_votes_needed") long totalVotesNeeded
) { }