package io.mindspice.jxch.rpc.schemas.wallet.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;


public record DAOInfo(
        @JsonProperty("current_height") long currentHeight,
        @JsonProperty("assets") List<String> assets,
        @JsonProperty("proposals_list") List<DAOProposal> proposalsList,
        @JsonProperty("cat_wallet_id") int catWalletId,
        @JsonProperty("dao_cat_wallet_id") int daoCatWalletId,
        @JsonProperty("current_treasury_coin") Coin currentTreasuryCoin,
        @JsonProperty("singleton_block_height") long singletonBlockHeight,
        @JsonProperty("filter_below_vote_amount") long filterBelowVoteAmount,
        @JsonProperty("treasury_id") String treasuryId,
        @JsonProperty("parent_info") List<Map<String, Coin>> parentInfo
) {
    public DAOInfo {
        assets = assets != null ? Collections.unmodifiableList(assets) : List.of();
        proposalsList = proposalsList != null ? Collections.unmodifiableList(proposalsList) : List.of();

        if (parentInfo != null) {
            parentInfo = parentInfo.stream().map(Collections::unmodifiableMap).toList();
        } else { parentInfo = List.of(); }
    }
}