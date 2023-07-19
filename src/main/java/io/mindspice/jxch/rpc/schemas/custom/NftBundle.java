package io.mindspice.jxch.rpc.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;

import java.util.List;


public record NftBundle(
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("nft_id_list") List<String> nftIdList,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error
) { }
