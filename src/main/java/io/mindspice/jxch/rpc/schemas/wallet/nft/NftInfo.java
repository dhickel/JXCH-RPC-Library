package io.mindspice.jxch.rpc.schemas.wallet.nft;

import java.util.Collections;
import java.util.List;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;


public record NftInfo(
        @JsonProperty("nft_coin_id") String nftCoinId,
        @JsonProperty("owner_did") String ownerDid,
        @JsonProperty("supports_did") boolean supportsDid,
        @JsonProperty("off_chain_metadata") String offChainMetadata,
        @JsonProperty("launcher_id") String launcherId,
        @JsonProperty("edition_number") BigInteger editionNumber,
        @JsonProperty("royalty_percentage") Integer royaltyPercentage,
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
        @JsonProperty("edition_total") BigInteger editionTotal,
        @JsonProperty("mint_height") long mintHeight,
        @JsonProperty("nft_id") String nftId,
        @JsonProperty("nft_coin_confirmation_height") long nftCoinConfirmationHeight
) {
    public NftInfo(String nftCoinId, String ownerDid, boolean supportsDid, String offChainMetadata,
            String launcherId, long editionNumber, Integer royaltyPercentage, List<String> dataUris,
            String p2Address, String dataHash, String licenseHash, String royaltyPuzzleHash,
            String updaterPuzhash, boolean pendingTransaction, String metadataHash, String minterDid,
            String chainInfo, String launcherPuzhash, List<String> metadataUris, List<String> licenseUris,
            long editionTotal, int mintHeight) {
        this(nftCoinId, ownerDid, supportsDid, offChainMetadata, launcherId, BigInteger.valueOf(editionNumber),
                royaltyPercentage, dataUris, p2Address, dataHash, licenseHash, royaltyPuzzleHash,
                updaterPuzhash, pendingTransaction, metadataHash, minterDid, chainInfo, launcherPuzhash,
                metadataUris, licenseUris, BigInteger.valueOf(editionTotal), mintHeight, null, 0);
    }

    public NftInfo {
        dataUris =  dataUris != null ? Collections.unmodifiableList(dataUris) : List.of();
        metadataUris =  metadataUris != null ? Collections.unmodifiableList(metadataUris) : List.of();
        licenseUris =  licenseUris != null ? Collections.unmodifiableList(licenseUris) : List.of();
    }
}