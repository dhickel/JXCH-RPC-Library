package io.mindspice.jxch.rpc.schemas.wallet.nft;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;
import io.mindspice.jxch.rpc.schemas.wallet.Transaction;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


public record NftBundle(
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("nft_id_list") List<String> nftIdList,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("transactions") List<Transaction> transactions,
        @JsonProperty("unsigned_transactions") List<JsonNode> unsignedTransactions
) {
    public NftBundle {
        nftIdList = nftIdList != null ? List.copyOf(nftIdList) : List.of();
        transactions = transactions != null ? List.copyOf(transactions) : List.of();
        unsignedTransactions = unsignedTransactions != null ? List.copyOf(unsignedTransactions) : List.of();
    }

    public NftBundle(SpendBundle spendBundle, List<String> nftIdList, boolean success, String error) {
        this(spendBundle, nftIdList, success, error, List.of(), List.of());
    }
}