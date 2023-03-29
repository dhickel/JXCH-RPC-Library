package io.mindspice.http.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.http.RPCClient;
import io.mindspice.schemas.fullnode.AdditionsAndRemovals;
import io.mindspice.schemas.fullnode.Block;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;

import java.io.IOException;


public class FullNodeAPI {

    private final RPCClient client;


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
            return JsonUtils.readJson(getAdditionsAndRemovalsAsBytes(headerHash), AdditionsAndRemovals.class);
        } catch (IOException e) {
            throw new RPCException("Error reading request JSON", e);
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
            return JsonUtils.readJson(jsonNode.get("block"), Block.class);
        } catch (IOException e) {
            throw new RPCException("Error reading request JSON", e);
        }
    }


}
