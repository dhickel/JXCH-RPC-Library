package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.DataLayer;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.datalayer.DataLayerSchemas.*;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class DataLayerAPI extends ChiaAPI {
    public DataLayerAPI(RPCClient client) { super(client, ChiaService.DATA_LAYER); }

    private byte[] send(DataLayer endpoint, JsonNode body) throws RPCException {
        try { return client.makeRequest(new Request(endpoint, JsonUtils.writeBytes(body))); }
        catch (IOException e) { throw new RPCException("Error writing request JSON", e); }
    }

    private JsonNode responseTree(byte[] body) throws IOException {
        JsonNode node = JsonUtils.readTree(body);
        if (node == null || !node.isObject() || !node.path("success").isBoolean()) {
            throw new IOException("Expected a DataLayer response object with boolean success");
        }
        return node;
    }

    private <T> ApiResponse<T> parse(DataLayer endpoint, byte[] body, Class<T> type) throws RPCException {
        try { return newResponse(responseTree(body), type, endpoint); }
        catch (IOException e) { throw new RPCException("Error reading response JSON", e); }
    }

    private <T> ApiResponse<T> parseField(DataLayer endpoint, byte[] body, String field, Class<T> type)
            throws RPCException {
        try { return newResponse(responseTree(body), field, type, endpoint); }
        catch (IOException e) { throw new RPCException("Error reading response JSON", e); }
    }

    private ApiResponse<Boolean> success(DataLayer endpoint, byte[] body) throws RPCException {
        return parseField(endpoint, body, "success", Boolean.class);
    }

    // Omit unspecified options so the server applies its defaults. In particular, do not send
    // null for submit_on_chain/verbose/retain/urls: dict.get() would NOT apply the default.
    private ObjectNode fields(Object... pairs) {
        var builder = new JsonUtils.ObjectBuilder();
        for (int i = 0; i < pairs.length; i += 2) {
            String name = (String) pairs[i];
            Object value = pairs[i + 1];
            if (name.equals("amount") && value == null) throw new IllegalArgumentException("amount is required");
            if (value != null && (name.equals("fee") || name.equals("amount"))) value = uint64((Number) value, name);
            if (value != null && (name.equals("page") || name.equals("max_page_size"))) value = integer((Number) value, name);
            if (value != null) builder.put(name, value);
        }
        return builder.buildNode();
    }

    private static BigInteger integer(Number value, String name) {
        if (value instanceof BigInteger big) return big;
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)
            return BigInteger.valueOf(value.longValue());
        throw new IllegalArgumentException(name + " must be an integer");
    }

    private static BigInteger uint64(Number value, String name) {
        BigInteger integer = integer(value, name);
        if (integer.signum() < 0 || integer.bitLength() > 64)
            throw new IllegalArgumentException(name + " must be in uint64 range");
        return integer;
    }

    // The custom offer marshal indexes fee directly, so an unspecified fee MUST be explicit null.
    private ObjectNode offerFields(Object... pairs) {
        ObjectNode node = fields(pairs);
        if (!node.has("fee")) node.putNull("fee");
        return node;
    }

    public byte[] walletLogInAsBytes(long fingerprint) throws RPCException {
        if (fingerprint < 0 || fingerprint > 0xffff_ffffL)
            throw new IllegalArgumentException("fingerprint must be in uint32 range");
        return send(DataLayer.WALLET_LOG_IN, fields("fingerprint", fingerprint));
    }
    public ApiResponse<Boolean> walletLogIn(long fingerprint) throws RPCException {
        return success(DataLayer.WALLET_LOG_IN, walletLogInAsBytes(fingerprint));
    }

    public byte[] createDataStoreAsBytes(Number fee, Boolean verbose) throws RPCException {
        return send(DataLayer.CREATE_DATA_STORE, fields("fee", fee, "verbose", verbose));
    }
    public ApiResponse<DataStore> createDataStore(Number fee, Boolean verbose) throws RPCException {
        return parse(DataLayer.CREATE_DATA_STORE, createDataStoreAsBytes(fee, verbose), DataStore.class);
    }

    public byte[] getOwnedStoresAsBytes() throws RPCException {
        return send(DataLayer.GET_OWNED_STORES, fields());
    }
    public ApiResponse<StoreIds> getOwnedStores() throws RPCException {
        return parse(DataLayer.GET_OWNED_STORES, getOwnedStoresAsBytes(), StoreIds.class);
    }

    public byte[] batchUpdateAsBytes(String id, List<Change> changelist, Number fee, Boolean submitOnChain)
            throws RPCException {
        return send(DataLayer.BATCH_UPDATE, fields("id", id, "changelist", changelist, "fee", fee,
                "submit_on_chain", submitOnChain));
    }
    public ApiResponse<TransactionId> batchUpdate(String id, List<Change> changelist, Number fee, Boolean submitOnChain)
            throws RPCException {
        return parse(DataLayer.BATCH_UPDATE, batchUpdateAsBytes(id, changelist, fee, submitOnChain), TransactionId.class);
    }

    public byte[] multistoreBatchUpdateAsBytes(List<StoreUpdate> storeUpdates, Number fee, Boolean submitOnChain)
            throws RPCException {
        return send(DataLayer.MULTISTORE_BATCH_UPDATE, fields("store_updates", storeUpdates, "fee", fee,
                "submit_on_chain", submitOnChain));
    }
    public ApiResponse<TransactionIds> multistoreBatchUpdate(List<StoreUpdate> storeUpdates, Number fee, Boolean submitOnChain)
            throws RPCException {
        return parse(DataLayer.MULTISTORE_BATCH_UPDATE, multistoreBatchUpdateAsBytes(storeUpdates, fee, submitOnChain),
                TransactionIds.class);
    }

    public byte[] submitPendingRootAsBytes(String id, Number fee) throws RPCException {
        return send(DataLayer.SUBMIT_PENDING_ROOT, fields("id", id, "fee", fee));
    }
    public ApiResponse<TransactionId> submitPendingRoot(String id, Number fee) throws RPCException {
        return parse(DataLayer.SUBMIT_PENDING_ROOT, submitPendingRootAsBytes(id, fee), TransactionId.class);
    }

    public byte[] submitAllPendingRootsAsBytes(Number fee) throws RPCException {
        return send(DataLayer.SUBMIT_ALL_PENDING_ROOTS, fields("fee", fee));
    }
    public ApiResponse<TransactionIds> submitAllPendingRoots(Number fee) throws RPCException {
        return parse(DataLayer.SUBMIT_ALL_PENDING_ROOTS, submitAllPendingRootsAsBytes(fee), TransactionIds.class);
    }

    public byte[] getValueAsBytes(String id, String key, String rootHash) throws RPCException {
        return send(DataLayer.GET_VALUE, fields("id", id, "key", key, "root_hash", rootHash));
    }
    public ApiResponse<Value> getValue(String id, String key, String rootHash) throws RPCException {
        return parse(DataLayer.GET_VALUE, getValueAsBytes(id, key, rootHash), Value.class);
    }

    public byte[] getKeysAsBytes(String id, String rootHash, Number page, Number maxPageSize) throws RPCException {
        return send(DataLayer.GET_KEYS, fields("id", id, "root_hash", rootHash, "page", page, "max_page_size", maxPageSize));
    }
    public ApiResponse<Keys> getKeys(String id, String rootHash, Number page, Number maxPageSize) throws RPCException {
        return parse(DataLayer.GET_KEYS, getKeysAsBytes(id, rootHash, page, maxPageSize), Keys.class);
    }

    public byte[] getKeysValuesAsBytes(String id, String rootHash, Number page, Number maxPageSize) throws RPCException {
        return send(DataLayer.GET_KEYS_VALUES, fields("id", id, "root_hash", rootHash, "page", page, "max_page_size", maxPageSize));
    }
    public ApiResponse<KeysValues> getKeysValues(String id, String rootHash, Number page, Number maxPageSize)
            throws RPCException {
        return parse(DataLayer.GET_KEYS_VALUES, getKeysValuesAsBytes(id, rootHash, page, maxPageSize), KeysValues.class);
    }

    public byte[] getAncestorsAsBytes(String id, String hash) throws RPCException {
        return send(DataLayer.GET_ANCESTORS, fields("id", id, "hash", hash));
    }
    public ApiResponse<Ancestors> getAncestors(String id, String hash) throws RPCException {
        return parse(DataLayer.GET_ANCESTORS, getAncestorsAsBytes(id, hash), Ancestors.class);
    }

    public byte[] getRootAsBytes(String id) throws RPCException {
        return send(DataLayer.GET_ROOT, fields("id", id));
    }
    public ApiResponse<Root> getRoot(String id) throws RPCException {
        return parse(DataLayer.GET_ROOT, getRootAsBytes(id), Root.class);
    }

    public byte[] getLocalRootAsBytes(String id) throws RPCException {
        return send(DataLayer.GET_LOCAL_ROOT, fields("id", id));
    }
    public ApiResponse<LocalRoot> getLocalRoot(String id) throws RPCException {
        return parse(DataLayer.GET_LOCAL_ROOT, getLocalRootAsBytes(id), LocalRoot.class);
    }

    public byte[] getRootsAsBytes(List<String> ids) throws RPCException {
        return send(DataLayer.GET_ROOTS, fields("ids", ids));
    }
    public ApiResponse<Roots> getRoots(List<String> ids) throws RPCException {
        return parse(DataLayer.GET_ROOTS, getRootsAsBytes(ids), Roots.class);
    }

    public byte[] deleteKeyAsBytes(String id, String key, Number fee) throws RPCException {
        return send(DataLayer.DELETE_KEY, fields("id", id, "key", key, "fee", fee));
    }
    public ApiResponse<TransactionId> deleteKey(String id, String key, Number fee) throws RPCException {
        return parse(DataLayer.DELETE_KEY, deleteKeyAsBytes(id, key, fee), TransactionId.class);
    }

    public byte[] insertAsBytes(String id, String key, String value, Number fee) throws RPCException {
        return send(DataLayer.INSERT, fields("id", id, "key", key, "value", value, "fee", fee));
    }
    public ApiResponse<TransactionId> insert(String id, String key, String value, Number fee) throws RPCException {
        return parse(DataLayer.INSERT, insertAsBytes(id, key, value, fee), TransactionId.class);
    }

    public byte[] subscribeAsBytes(String id, List<String> urls) throws RPCException {
        return send(DataLayer.SUBSCRIBE, fields("id", id, "urls", urls));
    }
    public ApiResponse<Boolean> subscribe(String id, List<String> urls) throws RPCException {
        return success(DataLayer.SUBSCRIBE, subscribeAsBytes(id, urls));
    }

    public byte[] unsubscribeAsBytes(String id, Boolean retain) throws RPCException {
        return send(DataLayer.UNSUBSCRIBE, fields("id", id, "retain", retain));
    }
    public ApiResponse<Boolean> unsubscribe(String id, Boolean retain) throws RPCException {
        return success(DataLayer.UNSUBSCRIBE, unsubscribeAsBytes(id, retain));
    }

    public byte[] addMirrorAsBytes(String id, List<String> urls, Number amount, Number fee) throws RPCException {
        return send(DataLayer.ADD_MIRROR, fields("id", id, "urls", urls, "amount", amount, "fee", fee));
    }
    public ApiResponse<Boolean> addMirror(String id, List<String> urls, Number amount, Number fee) throws RPCException {
        return success(DataLayer.ADD_MIRROR, addMirrorAsBytes(id, urls, amount, fee));
    }

    public byte[] deleteMirrorAsBytes(String coinId, Number fee) throws RPCException {
        return send(DataLayer.DELETE_MIRROR, fields("coin_id", coinId, "fee", fee));
    }
    public ApiResponse<Boolean> deleteMirror(String coinId, Number fee) throws RPCException {
        return success(DataLayer.DELETE_MIRROR, deleteMirrorAsBytes(coinId, fee));
    }

    public byte[] getMirrorsAsBytes(String id) throws RPCException {
        return send(DataLayer.GET_MIRRORS, fields("id", id));
    }
    public ApiResponse<Mirrors> getMirrors(String id) throws RPCException {
        return parse(DataLayer.GET_MIRRORS, getMirrorsAsBytes(id), Mirrors.class);
    }

    public byte[] removeSubscriptionsAsBytes(String id, List<String> urls) throws RPCException {
        return send(DataLayer.REMOVE_SUBSCRIPTIONS, fields("id", id, "urls", urls));
    }
    public ApiResponse<Boolean> removeSubscriptions(String id, List<String> urls) throws RPCException {
        return success(DataLayer.REMOVE_SUBSCRIPTIONS, removeSubscriptionsAsBytes(id, urls));
    }

    public byte[] subscriptionsAsBytes() throws RPCException {
        return send(DataLayer.SUBSCRIPTIONS, fields());
    }
    public ApiResponse<StoreIds> subscriptions() throws RPCException {
        return parse(DataLayer.SUBSCRIPTIONS, subscriptionsAsBytes(), StoreIds.class);
    }

    public byte[] getKvDiffAsBytes(String id, String hash1, String hash2, Number page, Number maxPageSize)
            throws RPCException {
        return send(DataLayer.GET_KV_DIFF, fields("id", id, "hash_1", hash1, "hash_2", hash2,
                "page", page, "max_page_size", maxPageSize));
    }
    public ApiResponse<KvDiff> getKvDiff(String id, String hash1, String hash2, Number page, Number maxPageSize)
            throws RPCException {
        return parse(DataLayer.GET_KV_DIFF, getKvDiffAsBytes(id, hash1, hash2, page, maxPageSize), KvDiff.class);
    }

    public byte[] getRootHistoryAsBytes(String id) throws RPCException {
        return send(DataLayer.GET_ROOT_HISTORY, fields("id", id));
    }
    public ApiResponse<RootHistory> getRootHistory(String id) throws RPCException {
        return parse(DataLayer.GET_ROOT_HISTORY, getRootHistoryAsBytes(id), RootHistory.class);
    }

    public byte[] addMissingFilesAsBytes(List<String> ids, Boolean overwrite, String foldername) throws RPCException {
        return send(DataLayer.ADD_MISSING_FILES, fields("ids", ids, "overwrite", overwrite, "foldername", foldername));
    }
    public ApiResponse<Boolean> addMissingFiles(List<String> ids, Boolean overwrite, String foldername) throws RPCException {
        return success(DataLayer.ADD_MISSING_FILES, addMissingFilesAsBytes(ids, overwrite, foldername));
    }

    public byte[] makeOfferAsBytes(List<OfferStore> maker, List<OfferStore> taker, Number fee) throws RPCException {
        return send(DataLayer.MAKE_OFFER, offerFields("maker", maker, "taker", taker, "fee", fee));
    }
    public ApiResponse<Offer> makeOffer(List<OfferStore> maker, List<OfferStore> taker, Number fee) throws RPCException {
        return parseField(DataLayer.MAKE_OFFER, makeOfferAsBytes(maker, taker, fee), "offer", Offer.class);
    }

    public byte[] takeOfferAsBytes(Offer offer, Number fee) throws RPCException {
        return send(DataLayer.TAKE_OFFER, offerFields("offer", offer, "fee", fee));
    }
    public ApiResponse<TradeId> takeOffer(Offer offer, Number fee) throws RPCException {
        return parse(DataLayer.TAKE_OFFER, takeOfferAsBytes(offer, fee), TradeId.class);
    }

    public byte[] verifyOfferAsBytes(Offer offer, Number fee) throws RPCException {
        return send(DataLayer.VERIFY_OFFER, offerFields("offer", offer, "fee", fee));
    }
    public ApiResponse<OfferVerification> verifyOffer(Offer offer, Number fee) throws RPCException {
        return parse(DataLayer.VERIFY_OFFER, verifyOfferAsBytes(offer, fee), OfferVerification.class);
    }

    public byte[] cancelOfferAsBytes(String tradeId, boolean secure, Number fee) throws RPCException {
        return send(DataLayer.CANCEL_OFFER, offerFields("trade_id", tradeId, "secure", secure, "fee", fee));
    }
    public ApiResponse<Boolean> cancelOffer(String tradeId, boolean secure, Number fee) throws RPCException {
        return success(DataLayer.CANCEL_OFFER, cancelOfferAsBytes(tradeId, secure, fee));
    }

    public byte[] getSyncStatusAsBytes(String id) throws RPCException {
        return send(DataLayer.GET_SYNC_STATUS, fields("id", id));
    }
    public ApiResponse<SyncStatus> getSyncStatus(String id) throws RPCException {
        return parseField(DataLayer.GET_SYNC_STATUS, getSyncStatusAsBytes(id), "sync_status", SyncStatus.class);
    }

    public byte[] checkPluginsAsBytes() throws RPCException {
        return send(DataLayer.CHECK_PLUGINS, fields());
    }
    public ApiResponse<PluginStatus> checkPlugins() throws RPCException {
        return parseField(DataLayer.CHECK_PLUGINS, checkPluginsAsBytes(), "plugin_status", PluginStatus.class);
    }

    public byte[] clearPendingRootsAsBytes(String storeId) throws RPCException {
        return send(DataLayer.CLEAR_PENDING_ROOTS, fields("store_id", storeId));
    }
    public ApiResponse<PendingRoot> clearPendingRoots(String storeId) throws RPCException {
        return parseField(DataLayer.CLEAR_PENDING_ROOTS, clearPendingRootsAsBytes(storeId), "root", PendingRoot.class);
    }

    public byte[] getProofAsBytes(String storeId, List<String> keys) throws RPCException {
        return send(DataLayer.GET_PROOF, fields("store_id", storeId, "keys", keys));
    }
    public ApiResponse<DLProof> getProof(String storeId, List<String> keys) throws RPCException {
        return parseField(DataLayer.GET_PROOF, getProofAsBytes(storeId, keys), "proof", DLProof.class);
    }

    public byte[] verifyProofAsBytes(DLProof proof) throws RPCException {
        // DLProof is the request body itself; it is not nested under a "proof" key.
        return send(DataLayer.VERIFY_PROOF, JsonUtils.toNode(proof));
    }
    public ApiResponse<ProofVerification> verifyProof(DLProof proof) throws RPCException {
        return parse(DataLayer.VERIFY_PROOF, verifyProofAsBytes(proof), ProofVerification.class);
    }
}
