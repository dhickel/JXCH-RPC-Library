package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.wallet.offers.OfferSummary;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public final class WalletRpcResponses {
    private WalletRpcResponses() {}

    public record SingletonDependencies(@JsonProperty("launcher_id") String launcherId,
            @JsonProperty("values_to_prove") List<String> valuesToProve) {}
    public record SingletonSummary(@JsonProperty("launcher_id") String launcherId,
            @JsonProperty("new_root") String newRoot, List<SingletonDependencies> dependencies) {}
    public record DataLayerOfferSummary(List<SingletonSummary> offered) {}
    public record OfferSummaryResult(String id, OfferSummary assetSummary, DataLayerOfferSummary dataLayerSummary) {}
    public record PuzzleAndSolution(@JsonProperty("puzzle_reveal") String puzzleReveal, String solution) {}
    public record HeightInfo(long height, @JsonProperty("latest_timestamp") BigInteger latestTimestamp,
            @JsonProperty("is_transaction_block") Boolean isTransactionBlock,
            @JsonProperty("prev_transaction_block_height") Long prevTransactionBlockHeight) {}
    public record WalletIdentifier(long id, int type) {}
    public record WalletCoinRecord(@JsonProperty("parent_coin_info") String parentCoinInfo,
            @JsonProperty("puzzle_hash") String puzzleHash, BigInteger amount, String id, int type,
            @JsonProperty("wallet_identifier") WalletIdentifier walletIdentifier,
            @JsonProperty("confirmed_height") long confirmedHeight,
            @JsonProperty("spent_height") long spentHeight, boolean coinbase, JsonNode metadata) {}
    public record WalletCoinRecords(@JsonProperty("coin_records") List<WalletCoinRecord> coinRecords,
            @JsonProperty("total_count") Long totalCount) {}
    public record PublicKeys(@JsonProperty("keyring_is_locked") boolean keyringIsLocked,
            @JsonProperty("public_key_fingerprints") List<Long> publicKeyFingerprints) {}
    public record LineageProof(@JsonProperty("parent_name") String parentName,
            @JsonProperty("inner_puzzle_hash") String innerPuzzleHash, BigInteger amount,
            @JsonProperty("parent_proof_hash") String parentProofHash) {}
    public record Singleton(@JsonProperty("coin_id") String coinId, @JsonProperty("launcher_id") String launcherId,
            String root, @JsonProperty("inner_puzzle_hash") String innerPuzzleHash, boolean confirmed,
            @JsonProperty("confirmed_at_height") long confirmedAtHeight,
            @JsonProperty("lineage_proof") LineageProof lineageProof, long generation, BigInteger timestamp) {}
    public record SingletonHistory(List<Singleton> history, long count) {}
    public record OwnedSingletons(List<Singleton> singletons, long count) {}
    public record Mirror(@JsonProperty("coin_id") String coinId, @JsonProperty("launcher_id") String launcherId,
            BigInteger amount, List<String> urls, boolean ours,
            @JsonProperty("confirmed_at_height") Long confirmedAtHeight) {}
    public record KeyValueHashes(@JsonProperty("key_clvm_hash") String keyClvmHash,
            @JsonProperty("value_clvm_hash") String valueClvmHash) {}
    public record ProofInclusions(@JsonProperty("store_id") String storeId, List<KeyValueHashes> inclusions) {}
    public record VerifiedProof(@JsonProperty("verified_clvm_hashes") ProofInclusions verifiedClvmHashes,
            @JsonProperty("current_root") boolean currentRoot) {}
    public record VerifiedCredential(Coin coin,
            @JsonProperty("singleton_lineage_proof") LineageProof singletonLineageProof,
            @JsonProperty("eml_lineage_proof") LineageProof emlLineageProof,
            @JsonProperty("launcher_id") String launcherId, @JsonProperty("inner_puzzle_hash") String innerPuzzleHash,
            @JsonProperty("proof_provider") String proofProvider, @JsonProperty("proof_hash") String proofHash) {}
    public record VcRecord(VerifiedCredential vc, @JsonProperty("confirmed_at_height") long confirmedAtHeight,
            @JsonProperty("coin_id") String coinId) {}
    public record VcList(@JsonProperty("vc_records") List<VcRecord> vcRecords,
            Map<String, Map<String, String>> proofs) {}
    public record SignerCoin(@JsonProperty("parent_coin_id") String parentCoinId,
            @JsonProperty("puzzle_hash") String puzzleHash, BigInteger amount) {}
    public record Spend(SignerCoin coin, String puzzle, String solution) {}
    public record TransactionInfo(List<Spend> spends) {}
    public record SigningTarget(String fingerprint, String message, String hook) {}
    public record SumHint(List<String> fingerprints, @JsonProperty("synthetic_offset") String syntheticOffset,
            @JsonProperty("final_pubkey") String finalPubkey) {}
    public record PathHint(@JsonProperty("root_fingerprint") String rootFingerprint, List<BigInteger> path) {}
    public record KeyHints(@JsonProperty("sum_hints") List<SumHint> sumHints,
            @JsonProperty("path_hints") List<PathHint> pathHints) {}
    public record SigningInstructions(@JsonProperty("key_hints") KeyHints keyHints, List<SigningTarget> targets) {}
    public record UnsignedTransaction(@JsonProperty("transaction_info") TransactionInfo transactionInfo,
            @JsonProperty("signing_instructions") SigningInstructions signingInstructions) {}
    public record SigningResponse(String signature, String hook) {}
    public record Signature(String type, String signature) {}
    public record SignerTransaction(@JsonProperty("transaction_info") TransactionInfo transactionInfo,
            List<Signature> signatures) {}
    public record CreateWalletResult(String type, @JsonProperty("wallet_id") Long walletId,
            @JsonProperty("asset_id") String assetId, @JsonProperty("my_did") String myDid,
            @JsonProperty("coin_name") String coinName, @JsonProperty("coin_list") List<JsonNode> coinList,
            String newpuzhash, String pubkey, @JsonProperty("backup_dids") List<String> backupDids,
            @JsonProperty("num_verifications_required") BigInteger numVerificationsRequired,
            @JsonProperty("total_fee") BigInteger totalFee, Transaction transaction,
            @JsonProperty("launcher_id") String launcherId,
            @JsonProperty("p2_singleton_puzzle_hash") String p2SingletonPuzzleHash,
            @JsonProperty("unsigned_transactions") List<UnsignedTransaction> unsignedTransactions,
            List<Transaction> transactions) {}
    public record TransactionResult(@JsonProperty("unsigned_transactions") List<UnsignedTransaction> unsignedTransactions,
            List<Transaction> transactions) {}
    public record CreateDataLayerResult(@JsonProperty("launcher_id") String launcherId,
            @JsonProperty("unsigned_transactions") List<UnsignedTransaction> unsignedTransactions,
            List<Transaction> transactions) {}
    public record UpdateDataLayerResult(@JsonProperty("tx_record") Transaction txRecord,
            @JsonProperty("unsigned_transactions") List<UnsignedTransaction> unsignedTransactions,
            List<Transaction> transactions) {}
    public record VcMintResult(@JsonProperty("vc_record") VcRecord vcRecord,
            @JsonProperty("unsigned_transactions") List<UnsignedTransaction> unsignedTransactions,
            List<Transaction> transactions) {}
}
