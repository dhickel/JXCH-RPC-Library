package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;
import io.mindspice.jxch.rpc.enums.endpoints.Farmer;
import io.mindspice.mindlib.util.JsonUtils;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.schemas.farmer.*;
import io.mindspice.jxch.rpc.util.RPCException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FarmerAPI extends SharedAPI {

    public FarmerAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }

    // Shared method for reading plot pages
    private ApiResponse<List<String>> processPlotPages(String nodeId, List<String> filter, int pageSize,
            boolean reverse, Endpoint endpoint) throws RPCException {
        try {
            var responseBytes = makePlotRequest(nodeId, filter, 0, pageSize, reverse, endpoint);
            var jsonNode = JsonUtils.readTree(responseBytes);
            if (!jsonNode.get("success").asBoolean()) { return newFailedResponse(jsonNode, endpoint); }

            List<String> plotList = new ArrayList<>(
                    JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.STRING_LIST)
            );

            for (int i = jsonNode.get("page").asInt(); i < jsonNode.get("page_count").asInt(); ++i) {
                var nextBytes = makePlotRequest(nodeId, filter, i, pageSize, reverse, endpoint);
                var nextNode = JsonUtils.readTree(nextBytes);
                plotList.addAll(JsonUtils.readJson(nextNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            }
            return newResponseList(jsonNode, plotList, endpoint);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    // Shared method for making plot request
    private byte[] makePlotRequest(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse, Endpoint endpoint) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("node_id", nodeId)
                    .put("page", page)
                    .put("page_size", pageSize)
                    .put("filter", filter)
                    .put("reverse", reverse)
                    .buildBytes();
            var req = new Request(endpoint, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public byte[] getHarvesterPlotsDuplicatesAsBytes(String nodeId, List<String> filter, int page,
            int pageSize, boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_DUPLICATES);
    }

    public ApiResponse<List<String>> getHarvesterPlotsDuplicates(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        return processPlotPages(nodeId, filter, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_DUPLICATES);
    }

    public ApiResponse<List<String>> getHarvesterPlotsDuplicates(String nodeId)
            throws RPCException {
        return getHarvesterPlotsDuplicates(nodeId, List.of(), 500, false);
    }

    public byte[] getHarvesterPlotsInvalidAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_INVALID);
    }

    public ApiResponse<List<String>> getHarvesterPlotsInvalid(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        return processPlotPages(nodeId, filter, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_INVALID);
    }

    public ApiResponse<List<String>> getHarvesterPlotsInvalid(String nodeId)
            throws RPCException {
        return getHarvesterPlotsDuplicates(nodeId, List.of(), 500, false);
    }

    public byte[] getHarvesterPlotsKeysMissingAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_KEYS_MISSING);
    }

    public ApiResponse<List<String>> getHarvesterPlotsKeysMissing(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        return processPlotPages(nodeId, filter, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_KEYS_MISSING);
    }

    public ApiResponse<List<String>> getHarvesterPlotsKeysMissing(String nodeId)
            throws RPCException {
        return getHarvesterPlotsKeysMissing(nodeId, List.of(), 500, false);
    }

    public byte[] getHarvesterPlotsValidAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_VALID);
    }

    public ApiResponse<List<Plot>> getHarvesterPlotsValid(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        try {
            Endpoint endpoint = Farmer.GET_HARVESTER_PLOTS_VALID;
            var responseBytes = makePlotRequest(nodeId, filter, 0, pageSize, reverse, endpoint);
            var jsonNode = JsonUtils.readTree(responseBytes);
            if (!jsonNode.get("success").asBoolean()) { return newFailedResponse(jsonNode, endpoint); }

            List<Plot> plotList = new ArrayList<>(
                    JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.PLOT_LIST)
            );

            for (int i = jsonNode.get("page").asInt(); i < jsonNode.get("page_count").asInt(); ++i) {
                var nextBytes = makePlotRequest(nodeId, filter, i, pageSize, reverse, endpoint);
                var nextNode = JsonUtils.readTree(nextBytes);
                plotList.addAll(JsonUtils.readJson(nextNode.get("plots").traverse(), TypeRefs.PLOT_LIST));
            }
            return newResponseList(jsonNode, plotList, endpoint);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<List<Plot>> getHarvesterPlotsValid(String nodeId)
            throws RPCException {
        return getHarvesterPlotsValid(nodeId, List.of(), 500, false);
    }

    public byte[] getHarvestersAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.GET_HARVESTERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<HarvesterDetails>> getHarvesters() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getHarvestersAsBytes());
            return newResponseList(
                    jsonNode, "harvesters", TypeRefs.HARVESTER_DETAILS_LIST, Farmer.GET_HARVESTERS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getHarvestersSummaryAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.GET_HARVESTERS_SUMMARY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<HarvesterSummary>> getHarvestersSummary() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getHarvestersSummaryAsBytes());
            return newResponseList(
                    jsonNode, "harvesters", TypeRefs.HARVESTER_SUMMARY_LIST, Farmer.GET_HARVESTERS_SUMMARY
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getPoolLoginLinkAsBytes(String launcherId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("launcher_id", launcherId);
            var req = new Request(Farmer.GET_POOL_LOGIN_LINK, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> getPoolLoginLink(String launcherId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPoolLoginLinkAsBytes(launcherId));
            return newResponse(jsonNode, "login_link", String.class, Farmer.GET_POOL_LOGIN_LINK);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getPoolStateAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.GET_POOL_STATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<PoolState>> getPoolState() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPoolStateAsBytes());
            return newResponseList(
                    jsonNode, "pool_state", TypeRefs.POOL_STATE_LIST, Farmer.GET_POOL_STATE
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getRewardTargetsAsBytes(boolean searchForPrivateKey, int derivationDepth)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("search_for_private_key", searchForPrivateKey)
                    .put("max_ph_to_search", derivationDepth)
                    .buildBytes();
            var req = new Request(Farmer.GET_REWARD_TARGETS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<RewardTarget> getRewardTargets(boolean searchForPrivateKey, int derivationDepth)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getRewardTargetsAsBytes(searchForPrivateKey, derivationDepth));
            return newResponse(jsonNode, RewardTarget.class, Farmer.GET_REWARD_TARGETS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] setRewardTargetsAsBytes(String farmerTarget, String poolTarget) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("farmer_target", farmerTarget)
                    .put("pool_target", poolTarget)
                    .buildBytes();
            var req = new Request(Farmer.SET_REWARD_TARGETS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<RewardTarget> setRewardTargets(String farmerTarget, String poolTarget)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(setRewardTargetsAsBytes(farmerTarget, poolTarget));
            return newResponse(jsonNode, RewardTarget.class, Farmer.SET_REWARD_TARGETS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getSignagePointAsBytes(String spHash) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("sp_hash", spHash);
            var req = new Request(Farmer.GET_SIGNAGE_POINT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignagePointBundle> getSignagePoint(String spHash)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getSignagePointAsBytes(spHash));
            return newResponse(jsonNode, SignagePointBundle.class, Farmer.GET_SIGNAGE_POINT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getSignagePointsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.GET_SIGNAGE_POINTS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<SignagePointBundle>> getSignagePoints() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getSignagePointsAsBytes());
            return newResponseList(
                    jsonNode, "signage_points", TypeRefs.SIGNAGE_POINT_BUNDLE_LIST, Farmer.GET_SIGNAGE_POINTS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }
}
