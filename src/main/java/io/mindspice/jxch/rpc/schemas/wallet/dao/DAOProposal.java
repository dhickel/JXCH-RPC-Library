package io.mindspice.jxch.rpc.schemas.wallet.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;


public record DAOProposal(
        @JsonProperty("amount_voted") long amountVoted,
        @JsonProperty("current_coin") Coin currentCoin,
        @JsonProperty("current_innerpuz") String currentInnerpuz,
        @JsonProperty("proposal_id") String proposalId,
        @JsonProperty("inner_puzzle") String innerPuzzle,
        @JsonProperty("closed") boolean closed,
        @JsonProperty("yes_votes") long yesVotes,
        @JsonProperty("timer_coin") Coin timerCoin,
        @JsonProperty("passed") boolean passed,
        @JsonProperty("singleton_block_height") long singletonBlockHeight
) {
}