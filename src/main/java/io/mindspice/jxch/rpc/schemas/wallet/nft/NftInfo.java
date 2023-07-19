package io.mindspice.jxch.rpc.schemas.wallet.nft;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record NftInfo(
        @JsonProperty("nft_coin_id") String nftCoinId,
        @JsonProperty("owner_did") String ownerDid,
        @JsonProperty("supports_did") boolean supportsDid,
        @JsonProperty("off_chain_metadata") String offChainMetadata,
        @JsonProperty("launcher_id") String launcherId,
        @JsonProperty("edition_number") int editionNumber,
        @JsonProperty("royalty_percentage") int royaltyPercentage,
        @JsonProperty("data_uris") List<String> dataUris,
        @JsonProperty("p2_address") String p2Address,
        @JsonProperty("data_hash") String dataHash,
        @JsonProperty("license_hash") String licenseHash,
        @JsonProperty("royalty_puzzle_hash") String royaltyPuzzleHash,
        @JsonProperty("updater_puzhash") String updaterPuzhash,
        @JsonProperty("pending_transaction") boolean pendingTransaction,
        @JsonProperty("metadata_hash") String metadataHash,
        @JsonProperty("minter_did") String minterDid,
        @JsonProperty("chain_info") String chainInfo,
        @JsonProperty("launcher_puzhash") String launcherPuzhash,
        @JsonProperty("metadata_uris") List<String> metadataUris,
        @JsonProperty("license_uris") List<String> licenseUris,
        @JsonProperty("edition_total") int editionTotal,
        @JsonProperty("mint_height") int mintHeight
) {
    public NftInfo {
        dataUris =  dataUris != null ? Collections.unmodifiableList(dataUris) : List.of();
        metadataUris =  metadataUris != null ? Collections.unmodifiableList(metadataUris) : List.of();
        licenseUris =  licenseUris != null ? Collections.unmodifiableList(licenseUris) : List.of();
    }
}