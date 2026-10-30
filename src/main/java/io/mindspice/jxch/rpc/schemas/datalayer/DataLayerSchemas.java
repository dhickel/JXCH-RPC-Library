package io.mindspice.jxch.rpc.schemas.datalayer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public final class DataLayerSchemas {
    private DataLayerSchemas() { }

    private static <T> List<T> copy(List<T> value) {
        return value == null ? null : java.util.Collections.unmodifiableList(new java.util.ArrayList<>(value));
    }

    private static <K, V> Map<K, V> copyMap(Map<K, V> value) {
        return value == null ? null : java.util.Collections.unmodifiableMap(new java.util.LinkedHashMap<>(value));
    }

    // Hex strings retain the wire representation: marshal uses bare hex, streamable uses 0x.
    // txs are wallet TransactionRecords (including CLVM spend bundles), not DataLayer records.
    // Preserve that cross-service payload losslessly without owning the wallet/CLVM model here.
    public record DataStore(String id, List<JsonNode> txs) { public DataStore { txs = copy(txs); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record StoreIds(List<String> storeIds) { public StoreIds { storeIds = copy(storeIds); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TransactionId(String txId) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TransactionIds(List<String> txId) { public TransactionIds { txId = copy(txId); } }

    public record Value(String value) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Keys(List<String> keys,
                       @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalPages,
                       @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalBytes,
                       String rootHash) { public Keys { keys = copy(keys); } }

    // atom is an upstream compatibility field whose only valid value is null.
    public record TerminalNode(String hash, String key, String value, Void atom) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record KeysValues(List<TerminalNode> keysValues,
                             @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalPages,
                             @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalBytes,
                             String rootHash) { public KeysValues { keysValues = copy(keysValues); } }

    // get_ancestors constructs InternalNodes without populated left/right children.
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record InternalNode(String hash, String leftHash, String rightHash) { }

    public record Ancestors(List<InternalNode> ancestors) { public Ancestors { ancestors = copy(ancestors); } }

    public record Root(String hash, boolean confirmed,
                       @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger timestamp) { }

    public record LocalRoot(String hash) { }

    public record StoreRoot(String id, String hash, boolean confirmed,
                            @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger timestamp) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Roots(List<StoreRoot> rootHashes) { public Roots { rootHashes = copy(rootHashes); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Mirror(String coinId, String launcherId,
                         @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger amount, List<String> urls,
                         boolean ours, @JsonDeserialize(using = ExactIntegerDeserializer.class) Long confirmedAtHeight) {
        public Mirror { urls = copy(urls); }
    }

    public record Mirrors(List<Mirror> mirrors) { public Mirrors { mirrors = copy(mirrors); } }

    public record Diff(String type, String key, String value) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record KvDiff(List<Diff> diff,
                         @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalPages,
                         @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalBytes) {
        public KvDiff { diff = copy(diff); }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RootHistoryEntry(String rootHash, boolean confirmed,
                                   @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger timestamp) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RootHistory(List<RootHistoryEntry> rootHistory) { public RootHistory { rootHistory = copy(rootHistory); } }

    // process_change uses numeric Side (0=left, 1=right), not the offer layer's string Side.
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Change(String action, String key, String value, String referenceNodeHash,
                         @JsonDeserialize(using = ExactIntegerDeserializer.class) Integer side) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record StoreUpdate(String storeId, List<Change> changelist) { public StoreUpdate { changelist = copy(changelist); } }

    public record KeyValue(String key, String value) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record OfferStore(String storeId, List<KeyValue> inclusions) { public OfferStore { inclusions = copy(inclusions); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record OfferLayer(String otherHashSide, String otherHash, String combinedHash) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record OfferProof(String key, String value, String nodeHash, List<OfferLayer> layers) { public OfferProof { layers = copy(layers); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record StoreProofs(String storeId, List<OfferProof> proofs) { public StoreProofs { proofs = copy(proofs); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Offer(String tradeId, String offer, List<OfferStore> taker, List<StoreProofs> maker) { public Offer { taker = copy(taker); maker = copy(maker); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TradeId(String tradeId) { }

    public record OfferVerification(boolean valid, String error,
                                    @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger fee) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SyncStatus(String rootHash,
                             @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger generation,
                             String targetRootHash,
                             @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger targetGeneration) { }

    // get_plugin_info wraps an HTTP status and arbitrary /plugin_info JSON, or a transport error.
    // name/version/instance describe the service's fallback when no result exists for a URL.
    // Only response is plugin-defined; keep that independently versioned payload lossless.
    public record PluginInfo(@JsonDeserialize(using = ExactIntegerDeserializer.class) Integer status,
                             JsonNode response, String error, String name, String version, String instance) { }

    public record PluginStatus(Map<String, PluginInfo> uploaders, Map<String, PluginInfo> downloaders) { public PluginStatus { uploaders = copyMap(uploaders); downloaders = copyMap(downloaders); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PendingRoot(String treeId, String nodeHash,
                              @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger generation,
                              @JsonDeserialize(using = ExactIntegerDeserializer.class) int status) { }

    // Unlike offer proofs, streamable proof layers use numeric Side and hashed key/value fields.
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ProofLayer(@JsonDeserialize(using = ExactIntegerDeserializer.class) int otherHashSide,
                             String otherHash, String combinedHash) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record HashOnlyProof(String keyClvmHash, String valueClvmHash, String nodeHash, List<ProofLayer> layers) { public HashOnlyProof { layers = copy(layers); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record StoreProofsHashes(String storeId, List<HashOnlyProof> proofs) { public StoreProofsHashes { proofs = copy(proofs); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record DLProof(StoreProofsHashes storeProofs, String coinId, String innerPuzzleHash) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record KeyValueHashes(String keyClvmHash, String valueClvmHash) { }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ProofResultInclusions(String storeId, List<KeyValueHashes> inclusions) { public ProofResultInclusions { inclusions = copy(inclusions); } }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ProofVerification(ProofResultInclusions verifiedClvmHashes, boolean currentRoot) { }
}
