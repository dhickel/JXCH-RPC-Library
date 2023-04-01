package io.mindspice.http.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.http.RPCClient;
import io.mindspice.schemas.Objects.*;
import io.mindspice.schemas.TypeRefs;
import io.mindspice.schemas.fullnode.*;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;
import io.mindspice.util.Utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FullNodeAPI {

    private final RPCClient client;
    private final TypeReference<List<BlockRecord>> blockref = new TypeReference<>() { };


    public FullNodeAPI(RPCClient client) { this.client = client; }


    public byte[] getAdditionsAndRemovalsAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_ADDITIONS_AND_REMOVALS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public AdditionsAndRemovals getAdditionsAndRemovals(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAdditionsAndRemovalsAsBytes(headerHash));
            Utils.checkRpcSuccess(FullNode.GET_ADDITIONS_AND_REMOVALS, jsonNode);
            return JsonUtils.readJson(jsonNode, AdditionsAndRemovals.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public Block getBlock(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockAsBytes(headerHash));
            Utils.checkRpcSuccess(FullNode.GET_BLOCK, jsonNode);
            return JsonUtils.readJson(jsonNode.get("block"), Block.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockCountMetricsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(FullNode.GET_BLOCK_COUNT_METRICS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BlockCountMetrics getBlockCountMetrics() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockCountMetricsAsBytes());
            Utils.checkRpcSuccess(FullNode.GET_BLOCK_COUNT_METRICS, jsonNode);
            return JsonUtils.readJson(jsonNode.get("metrics"), BlockCountMetrics.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockRecordAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK_RECORD, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BlockRecord getBlockRecord(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordAsBytes(headerHash));
            Utils.checkRpcSuccess(FullNode.GET_BLOCK_RECORD, jsonNode);
            return JsonUtils.readJson(jsonNode.get("block_record"), BlockRecord.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockRecordByHeightAsBytes(int height) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("height", height);
            var req = new Request(FullNode.GET_BLOCK_RECORD_BY_HEIGHT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BlockRecord getBlockRecordByHeight(int height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordByHeightAsBytes(height));
            Utils.checkRpcSuccess(FullNode.GET_BLOCK_RECORD_BY_HEIGHT, jsonNode);
            return JsonUtils.readJson(jsonNode.get("block_record"), BlockRecord.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockRecordsAsBytes(int start, int end) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("start", start)
                    .put("end", end)
                    .buildBytes();
            var req = new Request(FullNode.GET_BLOCK_RECORDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<BlockRecord> getBlockRecords(int start, int end) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordsAsBytes(start, end));
            Utils.checkRpcSuccess(FullNode.GET_BLOCK_RECORDS, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("block_records").traverse(), TypeRefs.BLOCK_RECORD_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockSpendsAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK_SPENDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<CoinSolution> getBlockSpends(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockSpendsAsBytes(headerHash));
            Utils.checkRpcSuccess(FullNode.GET_BLOCK_SPENDS, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("block_spends").traverse(), TypeRefs.COIN_SOLUTION_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlocksAsBytes(int start, int end, boolean excludeHeaderHash, boolean excludeReorged)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("start", start)
                    .put("end", end)
                    .put("exclude_header_hash", excludeHeaderHash)
                    .put("exclude_reorged", excludeReorged)
                    .buildBytes();
            var req = new Request(FullNode.GET_BLOCKS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<Block> getBlocks(int start, int end, boolean excludeHeaderHash, boolean excludeReorged)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getBlocksAsBytes(start, end, excludeHeaderHash, excludeReorged)
            );
            Utils.checkRpcSuccess(FullNode.GET_BLOCKS, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("blocks").traverse(), TypeRefs.BLOCK_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getRecentSignagePointOrEOSAsByes(String hash, boolean isChallengeHash) throws RPCException {
        try {
            var data = JsonUtils.singleNode(isChallengeHash ? "challenge_hash" : "sp_hash", hash);
            var req = new Request(FullNode.GET_RECENT_SIGNAGE_POINT_OR_EOS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public SignagePointOrEOS getRecentSignagePointOrEOS(String hash, boolean isChallengeHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getRecentSignagePointOrEOSAsByes(hash, isChallengeHash));
            Utils.checkRpcSuccess(FullNode.GET_RECENT_SIGNAGE_POINT_OR_EOS, jsonNode);
            return JsonUtils.readJson(jsonNode, SignagePointOrEOS.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getUnfinishedBlockHeadersAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(FullNode.GET_UNFINISHED_BLOCK_HEADERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<BlockHeader> getUnfinishedBlockHeaders() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getUnfinishedBlockHeadersAsBytes());
            Utils.checkRpcSuccess(FullNode.GET_UNFINISHED_BLOCK_HEADERS, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("headers").traverse(), TypeRefs.BLOCK_HEADER_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordByNameAsBytes(String name) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("name", name);
            var req = new Request(FullNode.GET_COIN_RECORD_BY_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public CoinRecord getCoinRecordByName(String name) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCoinRecordByNameAsBytes(name));
            Utils.checkRpcSuccess(FullNode.GET_COIN_RECORD_BY_NAME, jsonNode);
            return JsonUtils.readJson(jsonNode.get("coin_record"), CoinRecord.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordsByNamesAsBytes(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("names", names)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spend_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_NAMES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<CoinRecord> getCoinRecordsByNames(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByNamesAsBytes(names, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(FullNode.GET_COIN_RECORDS_BY_NAMES, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("coin_records").traverse(), TypeRefs.COIN_RECORD_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordsByHintAsBytes(String hint, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("hint", hint)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spend_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_HINT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<CoinRecord> getCoinRecordsByHint(String hint, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByHintAsBytes(hint, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(FullNode.GET_COIN_RECORDS_BY_HINT, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("coin_records").traverse(), TypeRefs.COIN_RECORD_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordsByParentIdsAsBytes(List<String> parentIds, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("parent_ids", parentIds)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spend_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_PARENT_IDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<CoinRecord> getCoinRecordsByParentIds(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByParentIdsAsBytes(names, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(FullNode.GET_COIN_RECORDS_BY_PARENT_IDS, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("coin_records").traverse(), TypeRefs.COIN_RECORD_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordsByPuzzleHashAsBytes(String puzzleHash, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("puzzle_hash", puzzleHash)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spend_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASH, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<CoinRecord> getCoinRecordsByPuzzleHash(String puzzleHash, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByPuzzleHashAsBytes(puzzleHash, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASH, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("coin_records").traverse(), TypeRefs.COIN_RECORD_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordsByPuzzleHashesAsBytes(List<String> puzzleHashes, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("puzzle_hashes", puzzleHashes)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spend_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASHES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<CoinRecord> getCoinRecordsByPuzzleHashes(List<String> puzzleHashes, int startHeight,
            int endHeight, boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByPuzzleHashesAsBytes(puzzleHashes, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASHES, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("coin_records").traverse(), TypeRefs.COIN_RECORD_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getPuzzleAndSolutionAsBytes(String coinId, int height) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("coin_id", coinId)
                    .put("height", height)

                    .buildBytes();
            var req = new Request(FullNode.GET_PUZZLE_AND_SOLUTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public CoinSolution getPuzzleAndSolutionAs(String coinId, int height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPuzzleAndSolutionAsBytes(coinId, height));
            Utils.checkRpcSuccess(FullNode.GET_PUZZLE_AND_SOLUTION, jsonNode);
            return JsonUtils.readJson(jsonNode.get("coin_solution"), CoinSolution.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] pushTxAsBytes(SpendBundle spendBundle) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("spend_bundle", spendBundle);
            var req = new Request(FullNode.PUSH_TX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BasicResponse pushTx(SpendBundle spendBundle, boolean suppressSuccessException)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pushTxAsBytes(spendBundle));
            if (!suppressSuccessException) {
                Utils.checkRpcSuccess(FullNode.PUSH_TX, jsonNode);
            }
            return JsonUtils.readJson(jsonNode, BasicResponse.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockChainStateAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(FullNode.GET_BLOCKCHAIN_STATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BlockChainState getBlockChainState() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockChainStateAsBytes());
            Utils.checkRpcSuccess(FullNode.GET_BLOCKCHAIN_STATE, jsonNode);
            return JsonUtils.readJson(jsonNode.get("blockchain_state"), BlockChainState.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getNetworkInfoAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(FullNode.GET_NETWORK_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public String[] getNetworkInfo() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNetworkInfoAsBytes());
            Utils.checkRpcSuccess(FullNode.GET_NETWORK_INFO, jsonNode);
            return new String[]{
                    jsonNode.asText("network_name"),
                    jsonNode.asText("network_prefix")
            };
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getNetworkSpaceAsBytes(String newerBlockHash, String olderBlockHash)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("newer_block_header_hash", newerBlockHash)
                    .put("older_block_header_hash", olderBlockHash)
                    .buildBytes();
            var req = new Request(FullNode.GET_NETWORK_SPACE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BigInteger getNetworkSpace(String newerBlockHash, String olderBlockHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNetworkSpaceAsBytes(newerBlockHash, olderBlockHash));
            Utils.checkRpcSuccess(FullNode.GET_NETWORK_SPACE, jsonNode);
            return (jsonNode.get("space").bigIntegerValue());
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getAllMempoolItemsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(FullNode.GET_ALL_MEMPOOL_ITEMS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public Map<String, MempoolItem> getAllMempoolItems() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllMempoolItemsAsBytes());
            Utils.checkRpcSuccess(FullNode.GET_ALL_MEMPOOL_ITEMS, jsonNode);
            return Collections.unmodifiableMap(
                    JsonUtils.readJson(jsonNode.get("mempool_items").traverse(), TypeRefs.MEMPOOL_HASHMAP)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getAllMempoolTxIdsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(FullNode.GET_ALL_MEMPOOL_TX_IDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<String> getAllMempoolTxIds() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllMempoolTxIdsAsBytes());
            Utils.checkRpcSuccess(FullNode.GET_ALL_MEMPOOL_TX_IDS, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("tx_ids").traverse(), TypeRefs.STRING_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getMempoolItemByTxIdAsBytes(String txId) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("tx_id", txId);
            var req = new Request(FullNode.GET_MEMPOOL_ITEM_BY_TX_ID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public MempoolItem getMempoolItemByTxId(String txId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getMempoolItemByTxIdAsBytes(txId));
            Utils.checkRpcSuccess(FullNode.GET_MEMPOOL_ITEM_BY_TX_ID, jsonNode);
            return JsonUtils.readJson(jsonNode.get("mempool_item"), MempoolItem.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

 // TODO add multiple method sigs
    public byte[] getFeeEstimateAsBytes(SpendBundle spendBundle, long cost, List<Integer> targetTimes,
            int spendType, int spendCount) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("spend_bundle", spendBundle)
 //                   .put("cost", cost)
                    .put("target_times", targetTimes)
//                    .put("spend_type",spendType)
//                    .put("spend_count", spendCount)
                    .buildBytes();
            var req = new Request(FullNode.GET_FEE_ESTIMATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public FeeEstimate getFeeEstimate(SpendBundle spendBundle, long cost, List<Integer> targetTimes,
            int spendType, int spendCount) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getFeeEstimateAsBytes(
                    spendBundle, cost, targetTimes, spendType, spendCount)
            );
            Utils.checkRpcSuccess(FullNode.GET_FEE_ESTIMATE, jsonNode);
            return JsonUtils.readJson(jsonNode, FeeEstimate.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


}


