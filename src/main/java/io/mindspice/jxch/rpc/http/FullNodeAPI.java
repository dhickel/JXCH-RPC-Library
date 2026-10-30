package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.FullNode;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.schemas.fullnode.*;
import io.mindspice.jxch.rpc.schemas.object.*;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.schemas.fullnode.Network;
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
                    .put("host", ip)
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
            var jsonNode = JsonUtils.readTree(stopNodeAsBytes());
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
            return newResponse(jsonNode, "block_record", BlockRecord.class, FullNode.GET_BLOCK_RECORD);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockRecordByHeightAsBytes(int height) throws RPCException {
        return getBlockRecordByHeightAsBytes((long) height);
    }

    public byte[] getBlockRecordByHeightAsBytes(long height) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("height", height);
            var req = new Request(FullNode.GET_BLOCK_RECORD_BY_HEIGHT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<BlockRecord> getBlockRecordByHeight(int height) throws RPCException {
        return getBlockRecordByHeight((long) height);
    }

    public ApiResponse<BlockRecord> getBlockRecordByHeight(long height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordByHeightAsBytes(height));
            return newResponse(
                    jsonNode, "block_record", BlockRecord.class, FullNode.GET_BLOCK_RECORD_BY_HEIGHT
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlockRecordsAsBytes(int start, int end) throws RPCException {
        return getBlockRecordsAsBytes((long) start, (long) end);
    }

    public byte[] getBlockRecordsAsBytes(long start, long end) throws RPCException {
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
        return getBlockRecords((long) start, (long) end);
    }

    public ApiResponse<List<BlockRecord>> getBlockRecords(long start, long end) throws RPCException {
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

    public byte[] getBlockSpendsWithConditionsAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK_SPENDS_WITH_CONDITIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<BlockSpendWithConditions>> getBlockSpendsWithConditions(String headerHash)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockSpendsWithConditionsAsBytes(headerHash));
            return newResponseList(jsonNode, "block_spends_with_conditions",
                    new TypeReference<List<BlockSpendWithConditions>>() {}, FullNode.GET_BLOCK_SPENDS_WITH_CONDITIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getAggsigAdditionalDataAsBytes() throws RPCException {
        try {
            var req = new Request(FullNode.GET_AGGSIG_ADDITIONAL_DATA, JsonUtils.newEmptyNodeAsBytes());
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> getAggsigAdditionalData() throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(getAggsigAdditionalDataAsBytes()), "additional_data",
                    String.class, FullNode.GET_AGGSIG_ADDITIONAL_DATA);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getConstantsAsBytes() throws RPCException {
        try {
            var req = new Request(FullNode.GET_CONSTANTS, JsonUtils.newEmptyNodeAsBytes());
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> getConstants() throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(getConstantsAsBytes()), "constants", JsonNode.class,
                    FullNode.GET_CONSTANTS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] createBlockGeneratorAsBytes() throws RPCException {
        try {
            var req = new Request(FullNode.CREATE_BLOCK_GENERATOR, JsonUtils.newEmptyNodeAsBytes());
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<BlockGenerator> createBlockGenerator() throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(createBlockGeneratorAsBytes()), BlockGenerator.class,
                    FullNode.CREATE_BLOCK_GENERATOR);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getBlocksAsBytes(int start, int end, boolean excludeHeaderHash, boolean excludeReorged)
            throws RPCException {
        return getBlocksAsBytes((long) start, (long) end, excludeHeaderHash, excludeReorged);
    }

    public byte[] getBlocksAsBytes(long start, long end, boolean excludeHeaderHash, boolean excludeReorged)
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
        return getBlocks((long) start, (long) end, excludeHeaderHash, excludeReorged);
    }

    public ApiResponse<List<Block>> getBlocks(long start, long end, boolean excludeHeaderHash, boolean excludeReorged)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlocksAsBytes(start, end, excludeHeaderHash, excludeReorged));
            return newResponseList(jsonNode, "blocks", TypeRefs.BLOCK_LIST, FullNode.GET_BLOCKS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated
    public byte[] getRecentSignagePointOrEOSAsByes(String hash, boolean isChallengeHash) throws RPCException {
        return getRecentSignagePointOrEOSAsBytes(hash, isChallengeHash);
    }

    public byte[] getRecentSignagePointOrEOSAsBytes(String hash, boolean isChallengeHash) throws RPCException {
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
            var jsonNode = JsonUtils.readTree(getRecentSignagePointOrEOSAsBytes(hash, isChallengeHash));
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
                    jsonNode, "headers", TypeRefs.BLOCK_HEADER_LIST, FullNode.GET_UNFINISHED_BLOCK_HEADERS
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
        return getCoinRecordsByNamesAsBytes(names, (long) startHeight, (long) endHeight, includeSpent);
    }

    public byte[] getCoinRecordsByNamesAsBytes(List<String> names, long startHeight, long endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("names", names)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spent_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_NAMES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByNames(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        return getCoinRecordsByNames(names, (long) startHeight, (long) endHeight, includeSpent);
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByNames(List<String> names, long startHeight, long endHeight,
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
        return getCoinRecordsByHintAsBytes(hint, (long) startHeight, (long) endHeight, includeSpent);
    }

    public byte[] getCoinRecordsByHintAsBytes(String hint, long startHeight, long endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("hint", hint)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spent_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_HINT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByHint(String hint, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        return getCoinRecordsByHint(hint, (long) startHeight, (long) endHeight, includeSpent);
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByHint(String hint, long startHeight, long endHeight,
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
        return getCoinRecordsByParentIdsAsBytes(parentIds, (long) startHeight, (long) endHeight, includeSpent);
    }

    public byte[] getCoinRecordsByParentIdsAsBytes(List<String> parentIds, long startHeight, long endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("parent_ids", parentIds)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spent_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_PARENT_IDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByParentIds(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        return getCoinRecordsByParentIds(names, (long) startHeight, (long) endHeight, includeSpent);
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByParentIds(List<String> names, long startHeight, long endHeight,
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
        return getCoinRecordsByPuzzleHashAsBytes(puzzleHash, (long) startHeight, (long) endHeight, includeSpent);
    }

    public byte[] getCoinRecordsByPuzzleHashAsBytes(String puzzleHash, long startHeight, long endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("puzzle_hash", puzzleHash)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spent_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASH, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByPuzzleHash(String puzzleHash, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        return getCoinRecordsByPuzzleHash(puzzleHash, (long) startHeight, (long) endHeight, includeSpent);
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByPuzzleHash(String puzzleHash, long startHeight, long endHeight,
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
        return getCoinRecordsByPuzzleHashesAsBytes(puzzleHashes, (long) startHeight, (long) endHeight, includeSpent);
    }

    public byte[] getCoinRecordsByPuzzleHashesAsBytes(List<String> puzzleHashes, long startHeight, long endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("puzzle_hashes", puzzleHashes)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spent_coins", includeSpent)
                    .buildBytes();
            var req = new Request(FullNode.GET_COIN_RECORDS_BY_PUZZLE_HASHES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByPuzzleHashes(List<String> puzzleHashes, int startHeight,
            int endHeight, boolean includeSpent) throws RPCException {
        return getCoinRecordsByPuzzleHashes(puzzleHashes, (long) startHeight, (long) endHeight, includeSpent);
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByPuzzleHashes(List<String> puzzleHashes, long startHeight,
            long endHeight, boolean includeSpent) throws RPCException {
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
        return getPuzzleAndSolutionAsBytes(coinId, (long) height);
    }

    public byte[] getPuzzleAndSolutionAsBytes(String coinId, long height) throws RPCException {
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
        return getPuzzleAndSolution(coinId, (long) height);
    }

    public ApiResponse<CoinSolution> getPuzzleAndSolution(String coinId, long height) throws RPCException {
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
            return newResponse(jsonNode, Network.class, FullNode.GET_NETWORK_INFO);
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
                    .put("include_pending", includePending)
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
            return newResponse(jsonNode, "mempool_item", MempoolItem.class, FullNode.GET_MEMPOOL_ITEM_BY_TX_ID);
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

    public ApiResponse<List<MempoolItem>> getMempoolItemsByCoinName(String coinName) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getMempoolItemsByCoinNameAsBytes(coinName));
            return newResponseList(
                    jsonNode, "mempool_items", new TypeReference<List<MempoolItem>>() {},
                    FullNode.GET_MEMPOOL_ITEMS_BY_COIN_NAME
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getFeeEstimateForSpendTypeAsBytes(String spendType, List<? extends Number> targetTimes, int spendCount)
            throws RPCException {
        return getFeeEstimateForSpendTypeAsBytes(spendType, targetTimes, (long) spendCount);
    }

    public byte[] getFeeEstimateForSpendTypeAsBytes(String spendType, List<? extends Number> targetTimes, long spendCount)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("spend_type", spendType)
                    .put("target_times", feeTargetTimes(targetTimes))
                    .put("spend_count", spendCount)
                    .buildBytes();
            return client.makeRequest(new Request(FullNode.GET_FEE_ESTIMATE, data));
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForSpendType(String spendType, List<? extends Number> targetTimes,
            int spendCount) throws RPCException {
        return getFeeEstimateForSpendType(spendType, targetTimes, (long) spendCount);
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForSpendType(String spendType, List<? extends Number> targetTimes,
            long spendCount) throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(getFeeEstimateForSpendTypeAsBytes(spendType, targetTimes, spendCount)),
                    FeeEstimate.class, FullNode.GET_FEE_ESTIMATE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getFeeEstimateForBundleAsBytes(SpendBundle spendBundle, List<? extends Number> targetTimes,
            int spendCount) throws RPCException {
        return getFeeEstimateForBundleAsBytes(spendBundle, targetTimes, (long) spendCount);
    }

    public byte[] getFeeEstimateForBundleAsBytes(SpendBundle spendBundle, List<? extends Number> targetTimes,
            long spendCount) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("spend_bundle", spendBundle)
                    .put("target_times", feeTargetTimes(targetTimes))
                    .put("spend_count", spendCount)
                    .buildBytes();
            var req = new Request(FullNode.GET_FEE_ESTIMATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForBundle(SpendBundle spendBundle, List<? extends Number> targetTimes,
            int spendCount) throws RPCException {
        return getFeeEstimateForBundle(spendBundle, targetTimes, (long) spendCount);
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForBundle(SpendBundle spendBundle, List<? extends Number> targetTimes,
            long spendCount) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getFeeEstimateForBundleAsBytes(
                    spendBundle, targetTimes, spendCount)
            );
            return newResponse(jsonNode, FeeEstimate.class, FullNode.GET_FEE_ESTIMATE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getFeeEstimateForCostAsBytes(long cost, List<? extends Number> targetTimes,
            int spendCount) throws RPCException {
        return getFeeEstimateForCostAsBytes(BigInteger.valueOf(cost), targetTimes, (long) spendCount);
    }

    public byte[] getFeeEstimateForCostAsBytes(long cost, List<? extends Number> targetTimes,
            long spendCount) throws RPCException {
        return getFeeEstimateForCostAsBytes(BigInteger.valueOf(cost), targetTimes, spendCount);
    }

    public byte[] getFeeEstimateForCostAsBytes(BigInteger cost, List<? extends Number> targetTimes,
            int spendCount) throws RPCException {
        return getFeeEstimateForCostAsBytes(cost, targetTimes, (long) spendCount);
    }

    public byte[] getFeeEstimateForCostAsBytes(BigInteger cost, List<? extends Number> targetTimes,
            long spendCount) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("cost", cost)
                    .put("target_times", feeTargetTimes(targetTimes))
                    .put("spend_count", spendCount)
                    .buildBytes();
            var req = new Request(FullNode.GET_FEE_ESTIMATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForCost(long cost, List<? extends Number> targetTimes, int spendCount)
            throws RPCException {
        return getFeeEstimateForCost(BigInteger.valueOf(cost), targetTimes, (long) spendCount);
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForCost(long cost, List<? extends Number> targetTimes, long spendCount)
            throws RPCException {
        return getFeeEstimateForCost(BigInteger.valueOf(cost), targetTimes, spendCount);
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForCost(BigInteger cost, List<? extends Number> targetTimes, int spendCount)
            throws RPCException {
        return getFeeEstimateForCost(cost, targetTimes, (long) spendCount);
    }

    public ApiResponse<FeeEstimate> getFeeEstimateForCost(BigInteger cost, List<? extends Number> targetTimes, long spendCount)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getFeeEstimateForCostAsBytes(cost, targetTimes, spendCount));
            return newResponse(jsonNode, FeeEstimate.class, FullNode.GET_FEE_ESTIMATE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    private static List<BigInteger> feeTargetTimes(List<? extends Number> targetTimes) {
        if (targetTimes == null) {
            throw new IllegalArgumentException("target_times must be an array of non-negative integers");
        }
        return targetTimes.stream().map(time -> {
            BigInteger integer;
            if (time instanceof BigInteger value) {
                integer = value;
            } else if (time instanceof Byte || time instanceof Short || time instanceof Integer || time instanceof Long) {
                integer = BigInteger.valueOf(time.longValue());
            } else {
                throw new IllegalArgumentException("target_times members must be Byte, Short, Integer, Long, or BigInteger");
            }
            if (integer.signum() < 0) {
                throw new IllegalArgumentException("target_times members must be non-negative");
            }
            return integer;
        }).toList();
    }

}


