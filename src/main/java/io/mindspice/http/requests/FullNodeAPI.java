package io.mindspice.http.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.http.RPCClient;
import io.mindspice.schemas.TypeRefs;
import io.mindspice.schemas.components.CoinRecord;
import io.mindspice.schemas.fullnode.*;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;
import io.mindspice.util.Utils;

import java.io.IOException;
import java.util.List;


public class FullNodeAPI {

    private final RPCClient client;
    private final TypeReference<List<Block>> blockref = new TypeReference<>() { };


    public FullNodeAPI(RPCClient client) { this.client = client; }


    public byte[] getAdditionsAndRemovalsAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNode("header_hash", headerHash);
            var req = new Request(FullNode.GET_ADDITIONS_AND_REMOVALS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public AdditionsAndRemovals getAdditionsAndRemovals(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAdditionsAndRemovalsAsBytes(headerHash));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, AdditionsAndRemovals.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNode("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public Block getBlock(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockAsBytes(headerHash));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode.get("block"), Block.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockCountMetricsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNode();
            var req = new Request(FullNode.GET_BLOCK_COUNT_METRICS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public Metrics getBlockCountMetrics() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockCountMetricsAsBytes());
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode.get("metrics"), Metrics.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockRecordAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNode("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK_RECORD, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BlockRecord getBlockRecord(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordAsBytes(headerHash));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode.get("block_record"), BlockRecord.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockRecordByHeightAsBytes(int height) throws RPCException {
        try {
            var data = JsonUtils.singleNode("height", height);
            var req = new Request(FullNode.GET_BLOCK_RECORD_BY_HEIGHT, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BlockRecord getBlockRecordByHeight(int height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordByHeightAsBytes(height));
            Utils.checkRpcSuccess(jsonNode);
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


    public List<Block> getBlockRecords(int start, int end) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockRecordsAsBytes(start, end));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode.get("block_records").traverse(), TypeRefs.BLOCK_LIST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getBlockSpendsAsBytes(String headerHash) throws RPCException {
        try {
            var data = JsonUtils.singleNode("header_hash", headerHash);
            var req = new Request(FullNode.GET_BLOCK_SPENDS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public BlockSpends getBlockSpends(String headerHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlockSpendsAsBytes(headerHash));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, BlockSpends.class);
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


    public Blocks getBlocks(int start, int end, boolean excludeHeaderHash, boolean excludeReorged)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getBlocksAsBytes(start, end, excludeHeaderHash, excludeReorged));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, Blocks.class);
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
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, SignagePointOrEOS.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getUnfinishedBlockHeadersAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNode();
            var req = new Request(FullNode.GET_UNFINISHED_BLOCK_HEADERS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public UnfinishedBlockHeaders getUnfinishedBlockHeaders() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getUnfinishedBlockHeadersAsBytes());
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, UnfinishedBlockHeaders.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordByNameAsBytes(String name) throws RPCException {
        try {
            var data = JsonUtils.singleNode("name", name);
            var req = new Request(FullNode.GET_COIN_RECORD_BY_NAME, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public CoinRecord getCoinRecordByName(String name) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCoinRecordByNameAsBytes(name));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode.get("coin_record"), CoinRecord.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordsByNamesAsBytes(List<String> names, int startHeight, int endHeight, boolean includeSpent)
            throws RPCException {
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


    public CoinRecords getCoinRecordsByNames(List<String> names, int startHeight, int endHeight, boolean includeSpent)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByNamesAsBytes(names, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, CoinRecords.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getCoinRecordsByHintAsBytes(String hint, int startHeight, int endHeight, boolean includeSpent)
            throws RPCException {
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


    public CoinRecords getCoinRecordsByHint(String hint, int startHeight, int endHeight, boolean includeSpent)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByHintAsBytes(hint, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, CoinRecords.class);
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


    public CoinRecords getCoinRecordsByParentIds(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByParentIdsAsBytes(names, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, CoinRecords.class);
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


    public CoinRecords getCoinRecordsByPuzzleHash(String puzzleHash, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByPuzzleHashAsBytes(puzzleHash, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, CoinRecords.class);
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


    public CoinRecords getCoinRecordsByPuzzleHashes(List<String> puzzleHashes, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByPuzzleHashesAsBytes(puzzleHashes, startHeight, endHeight, includeSpent)
            );
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, CoinRecords.class);
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


    public CoinRecords getPuzzleAndSolutionAs(String coinId, int height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPuzzleAndSolutionAsBytes(coinId, height));
            Utils.checkRpcSuccess(jsonNode);
            return JsonUtils.readJson(jsonNode, CoinRecords.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


}
