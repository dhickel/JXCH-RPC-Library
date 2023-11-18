
package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.FullNode;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.schemas.custom.CatSenderInfo;
import io.mindspice.jxch.rpc.schemas.custom.InclusionCost;
import io.mindspice.jxch.rpc.schemas.fullnode.*;
import io.mindspice.jxch.rpc.schemas.object.*;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public class FullNodeAPI extends ChiaAPI {

    public FullNodeAPI(RPCClient client) {
        super(client, ChiaService.FULL_NODE);
    }

    ////////////
    // SHARED //
    ////////////

    public byte[] closeConnectionAsBytes(String nodeId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("node_id", nodeId);
            var req = new Request(FullNode.CLOSE_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> closeConnection(String nodeId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(closeConnectionAsBytes(nodeId));
            return newResponse(jsonNode, "success", Boolean.class, FullNode.CLOSE_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getConnectionsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_CONNECTIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Connection>> getConnections() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getConnectionsAsBytes());
            return newResponseList(jsonNode, "connections", TypeRefs.CONNECTION_LIST, FullNode.GET_CONNECTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getRoutesAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_ROUTES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> getRoutes() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getRoutesAsBytes());
            return newResponseList(jsonNode, "routes", TypeRefs.STRING_LIST, FullNode.GET_ROUTES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] healthzAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.HEALTHZ, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> healthz() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(healthzAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, FullNode.HEALTHZ);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] openConnectionAsBytes(String ip, int port) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("ip", ip)
                    .put("port", port)
                    .buildBytes();
            var req = new Request(FullNode.OPEN_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> openConnection(String ip, int port) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(openConnectionAsBytes(ip, port));
            return newResponse(jsonNode, "success", Boolean.class, FullNode.OPEN_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] stopNodeAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.STOP_NODE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> stopNode() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(healthzAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, FullNode.STOP_NODE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ///////////////
    // FULL NODE //
    ///////////////

    public byte[] getAdditionsAndRemovalsAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_ADDITIONS_AND_REMOVALS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<AdditionsAndRemovals> getAdditionsAndRemovals(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAdditionsAndRemovalsAsBytes(headerHash));
            return newResponse(jsonNode, AdditionsAndRemovals.class, FullNode.GET_ADDITIONS_AND_REMOVALS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Block> getBlock(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockAsBytes(headerHash));
            return newResponse(jsonNode, "block", Block.class, FullNode.GET_BLOCK);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockCountMetricsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_BLOCK_COUNT_METRICS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<BlockCountMetrics> getBlockCountMetrics() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockCountMetricsAsBytes());
            return newResponse(jsonNode, "metrics", BlockCountMetrics.class, FullNode.GET_BLOCK_COUNT_METRICS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockRecordAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK_RECORD, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<BlockRecord> getBlockRecord(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordAsBytes(headerHash));
            return newResponse(jsonNode, "block_records", BlockRecord.class, FullNode.GET_BLOCK_RECORD);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockRecordByHeightAsBytes(int height) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("height", height);
            var req = new Request(FullNode.GET_BLOCK_RECORD_BY_HEIGHT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<BlockRecord> getBlockRecordByHeight(int height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordByHeightAsBytes(height));
            return newResponse(
                    jsonNode, "block_records", BlockRecord.class, FullNode.GET_BLOCK_RECORD_BY_HEIGHT
            );
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

    public ApiResponse<List<BlockRecord>> getBlockRecords(int start, int end) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordsAsBytes(start, end));
            return newResponseList(
                    jsonNode, "block_records", TypeRefs.BLOCK_RECORD_LIST, FullNode.GET_BLOCK_RECORDS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockSpendsAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK_SPENDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinSolution>> getBlockSpends(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockSpendsAsBytes(headerHash));
            return newResponseList(
                    jsonNode, "block_spends", TypeRefs.COIN_SOLUTION_LIST, FullNode.GET_BLOCK_SPENDS
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

    public ApiResponse<List<Block>> getBlocks(int start, int end, boolean excludeHeaderHash, boolean excludeReorged)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlocksAsBytes(start, end, excludeHeaderHash, excludeReorged));
            return newResponseList(jsonNode, "blocks", TypeRefs.BLOCK_LIST, FullNode.GET_BLOCKS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getRecentSignagePointOrEOSAsByes(String hash, boolean isChallengeHash) throws RPCException {
        try {
            var data = JsonUtils.newSingleNode(isChallengeHash ? "challenge_hash" : "sp_hash", hash);
            var req = new Request(FullNode.GET_RECENT_SIGNAGE_POINT_OR_EOS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignagePointOrEOS> getRecentSignagePointOrEOS(String hash, boolean isChallengeHash)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getRecentSignagePointOrEOSAsByes(hash, isChallengeHash));
            return newResponse(jsonNode, SignagePointOrEOS.class, FullNode.GET_RECENT_SIGNAGE_POINT_OR_EOS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getUnfinishedBlockHeadersAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_UNFINISHED_BLOCK_HEADERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<BlockHeader>> getUnfinishedBlockHeaders() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getUnfinishedBlockHeadersAsBytes());
            return newResponseList(
                    jsonNode, "header", TypeRefs.BLOCK_HEADER_LIST, FullNode.GET_UNFINISHED_BLOCK_HEADERS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getCoinRecordByNameAsBytes(String name) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("name", name);
            var req = new Request(FullNode.GET_COIN_RECORD_BY_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<CoinRecord> getCoinRecordByName(String name) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCoinRecordByNameAsBytes(name));
            return newResponse(jsonNode, "coin_record", CoinRecord.class, FullNode.GET_COIN_RECORD_BY_NAME);
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

    public ApiResponse<List<CoinRecord>> getCoinRecordsByNames(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByNamesAsBytes(names, startHeight, endHeight, includeSpent)
            );
            return newResponseList(
                    jsonNode, "coin_records", TypeRefs.COIN_RECORD_LIST, FullNode.GET_COIN_RECORDS_BY_NAMES
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

    public ApiResponse<List<CoinRecord>> getCoinRecordsByHint(String hint, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByHintAsBytes(hint, startHeight, endHeight, includeSpent)
            );
            return newResponseList(
                    jsonNode, "coin_records", TypeRefs.COIN_RECORD_LIST, FullNode.GET_COIN_RECORDS_BY_HINT
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

    public ApiResponse<List<CoinRecord>> getCoinRecordsByParentIds(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByParentIdsAsBytes(names, startHeight, endHeight, includeSpent)
            );
            return newResponseList(
                    jsonNode, "coin_records", TypeRefs.COIN_RECORD_LIST, FullNode.GET_COIN_RECORDS_BY_PARENT_IDS
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

    public ApiResponse<List<CoinRecord>> getCoinRecordsByPuzzleHash(String puzzleHash, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByPuzzleHashAsBytes(puzzleHash, startHeight, endHeight, includeSpent)
            );
            return newResponseList(
                    jsonNode, "coin_records", TypeRefs.COIN_RECORD_LIST, FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASH
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

    public ApiResponse<List<CoinRecord>> getCoinRecordsByPuzzleHashes(List<String> puzzleHashes, int startHeight,
            int endHeight, boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByPuzzleHashesAsBytes(puzzleHashes, startHeight, endHeight, includeSpent)
            );
            return newResponseList(
                    jsonNode, "coin_records", TypeRefs.COIN_RECORD_LIST, FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASHES
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

    public ApiResponse<CoinSolution> getPuzzleAndSolution(String coinId, int height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPuzzleAndSolutionAsBytes(coinId, height));
            return newResponse(jsonNode, "coin_solution", CoinSolution.class, FullNode.GET_PUZZLE_AND_SOLUTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pushTxAsBytes(SpendBundle spendBundle) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("spend_bundle", spendBundle);
            var req = new Request(FullNode.PUSH_TX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> pushTx(SpendBundle spendBundle)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pushTxAsBytes(spendBundle));
            return newResponse(jsonNode, "status", String.class, FullNode.PUSH_TX);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockChainStateAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_BLOCKCHAIN_STATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<BlockChainState> getBlockChainState() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockChainStateAsBytes());
            return newResponse(
                    jsonNode, "blockchain_state", BlockChainState.class, FullNode.GET_BLOCKCHAIN_STATE
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getNetworkInfoAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_NETWORK_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Network> getNetworkInfo() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNetworkInfoAsBytes());
            if (jsonNode.get("success").asBoolean()) {
                var network = new Network(
                        jsonNode.get("network_name").asText(),
                        jsonNode.get("network_prefix").asText()
                );
                return newResponse(network, FullNode.GET_NETWORK_INFO);
            } else {
                return newFailedResponse(jsonNode, FullNode.GET_NETWORK_INFO);
            }
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

    public ApiResponse<BigInteger> getNetworkSpace(String newerBlockHash, String olderBlockHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNetworkSpaceAsBytes(newerBlockHash, olderBlockHash));
            return newResponse(jsonNode, "space", BigInteger.class, FullNode.GET_NETWORK_SPACE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getAllMempoolItemsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_ALL_MEMPOOL_ITEMS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Map<String, MempoolItem>> getAllMempoolItems() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllMempoolItemsAsBytes());
            return newResponseMap(
                    jsonNode, "mempool_items", TypeRefs.MEMPOOL_MAP, FullNode.GET_ALL_MEMPOOL_ITEMS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getAllMempoolTxIdsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_ALL_MEMPOOL_TX_IDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> getAllMempoolTxIds() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllMempoolTxIdsAsBytes());
            return newResponseList(
                    jsonNode, "tx_ids", TypeRefs.STRING_LIST, FullNode.GET_ALL_MEMPOOL_TX_IDS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getMempoolItemByTxIdAsBytes(String txId, boolean includePending) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("tx_id", txId)
                    .put("boolean includePending", includePending)
                    .buildBytes();
            var req = new Request(FullNode.GET_MEMPOOL_ITEM_BY_TX_ID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<MempoolItem> getMempoolItemByTxId(String txId, boolean includePending) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getMempoolItemByTxIdAsBytes(txId, includePending));
            return newResponse(jsonNode, "mempool_items", MempoolItem.class, FullNode.GET_MEMPOOL_ITEM_BY_TX_ID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getMempoolItemsByCoinNameAsBytes(String coinName) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("coin_name", coinName);
            var req = new Request(FullNode.GET_MEMPOOL_ITEMS_BY_COIN_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<MempoolItem> getMempoolItemsByCoinName(String coinName) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getMempoolItemsByCoinNameAsBytes(coinName));
            return newResponse(
                    jsonNode, "mempool_items", MempoolItem.class, FullNode.GET_MEMPOOL_ITEMS_BY_COIN_NAME
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    // TODO add multiple method sigs
    public byte[] getFeeEstimateForBundleAsBytes(SpendBundle spendBundle, List<Integer> targetTimes,
            int spendCount) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("spend_bundle", spendBundle)
                    .put("target_times", targetTimes)
                    .put("spend_count", spendCount)
                    .buildBytes();
            var req = new Request(FullNode.GET_FEE_ESTIMATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForBundle(SpendBundle spendBundle, List<Integer> targetTimes,
            int spendCount) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getFeeEstimateForBundleAsBytes(
                    spendBundle, targetTimes, spendCount)
            );
            return newResponse(jsonNode, FeeEstimate.class, FullNode.GET_FEE_ESTIMATE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getFeeEstimateForCostAsBytes(long cost, List<Integer> targetTimes,
            int spendCount) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("cost", cost)
                    .put("target_times", targetTimes)
                    .put("spend_count", spendCount)
                    .buildBytes();
            var req = new Request(FullNode.GET_FEE_ESTIMATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForCost(long cost, List<Integer> targetTimes, int spendCount)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getFeeEstimateForCostAsBytes(cost, targetTimes, spendCount));
            return newResponse(jsonNode, FeeEstimate.class, FullNode.GET_FEE_ESTIMATE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    // Note fee estimate can also take spend_type, but it seems to not be used atm


    /* CUSTOM */

    public byte[] getNftRecipientAddressAsBytes(String puzzleReveal, String solution) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("puzzle_reveal", puzzleReveal)
                    .put("solution", solution)
                    .buildBytes();
            var req = new Request(FullNode.GET_NFT_RECIPIENT_ADDRESS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> getNftRecipientAddress(String puzzleReveal, String solution) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNftRecipientAddressAsBytes(puzzleReveal, solution));
            return newResponse(jsonNode, "nft_recipient_address", String.class, FullNode.GET_NFT_RECIPIENT_ADDRESS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getSpendBundleInclusionCostAsBytes(SpendBundle spendBundle) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("spend_bundle", spendBundle);
            var req = new Request(FullNode.GET_SPEND_BUNDLE_INCLUSION_COST, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<InclusionCost> getSpendBundleInclusionCost(SpendBundle spendBundle) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getSpendBundleInclusionCostAsBytes(spendBundle));
            return newResponse(jsonNode, InclusionCost.class, FullNode.GET_SPEND_BUNDLE_INCLUSION_COST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getHeightAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(FullNode.GET_HEIGHT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Integer> getHeight() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getHeightAsBytes());
            return newResponse(jsonNode, "height", Integer.class, FullNode.GET_HEIGHT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getCatSenderInfoAsBytes(CoinRecord coinRecord) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("coin_record", coinRecord);
            var req = new Request(FullNode.GET_CAT_SENDER_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<CatSenderInfo> getCatSenderInfo(CoinRecord coinRecord) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCatSenderInfoAsBytes(coinRecord));
            return newResponse(jsonNode, CatSenderInfo.class, FullNode.GET_CAT_SENDER_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }
}
