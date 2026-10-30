package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;
import io.mindspice.jxch.rpc.enums.endpoints.Farmer;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.schemas.farmer.*;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class FarmerAPI extends ChiaAPI {

    public FarmerAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }

    ////////////
    // SHARED //
    ////////////

    public byte[] closeConnectionAsBytes(String nodeId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("node_id", nodeId);
            var req = new Request(Farmer.CLOSE_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> closeConnection(String nodeId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(closeConnectionAsBytes(nodeId));
            return newResponse(jsonNode, "success", Boolean.class, Farmer.CLOSE_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getConnectionsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.GET_CONNECTIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Connection>> getConnections() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getConnectionsAsBytes());
            return newResponseList(jsonNode, "connections", TypeRefs.CONNECTION_LIST, Farmer.GET_CONNECTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getRoutesAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.GET_ROUTES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> getRoutes() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getRoutesAsBytes());
            return newResponseList(jsonNode, "routes", TypeRefs.STRING_LIST, Farmer.GET_ROUTES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] healthzAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.HEALTHZ, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> healthz() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(healthzAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Farmer.HEALTHZ);
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
            var req = new Request(Farmer.OPEN_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> openConnection(String ip, int port) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(openConnectionAsBytes(ip, port));
            return newResponse(jsonNode, "success", Boolean.class, Farmer.OPEN_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] stopNodeAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Farmer.STOP_NODE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> stopNode() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(stopNodeAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Farmer.STOP_NODE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    private static long uint32(long value, String name) {
        if (value < 0 || value > 0xffff_ffffL)
            throw new IllegalArgumentException(name + " must be in uint32 range");
        return value;
    }

    private static long pageNumber(JsonNode response, String name) throws IOException {
        JsonNode value = response.get(name);
        if (value == null || !value.isIntegralNumber() || !value.canConvertToLong() || value.longValue() < 0
                || (name.equals("page") && value.longValue() > 0xffff_ffffL))
            throw new IOException("Invalid pagination integer " + name);
        return value.longValue();
    }

    // Farmer method for reading plot pages
    private ApiResponse<List<String>> processPlotPages(String nodeId, List<String> filter, long pageSize,
            boolean reverse, Endpoint endpoint) throws RPCException {
        try {
            var responseBytes = makePlotRequest(nodeId, filter, 0, pageSize, reverse, endpoint);
            var jsonNode = JsonUtils.readTree(responseBytes);
            if (!jsonNode.get("success").asBoolean()) { return newFailedResponse(jsonNode, endpoint); }

            List<String> plotList = new ArrayList<>(
                    JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.STRING_LIST)
            );

            for (long i = pageNumber(jsonNode, "page") + 1; i < pageNumber(jsonNode, "page_count"); ++i) {
                var nextBytes = makePlotRequest(nodeId, filter, i, pageSize, reverse, endpoint);
                var nextNode = JsonUtils.readTree(nextBytes);
                if (!nextNode.get("success").asBoolean()) { return newFailedResponse(nextNode, endpoint); }
                plotList.addAll(JsonUtils.readJson(nextNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            }
            return newResponseList(jsonNode, plotList, endpoint);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    // Farmer method for making plot request
    private byte[] makePlotRequest(String nodeId, List<String> filter, long page, long pageSize,
            boolean reverse, Endpoint endpoint) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("node_id", nodeId)
                    .put("page", uint32(page, "page"))
                    .put("page_size", uint32(pageSize, "page_size"))
                    .put("filter", endpoint == Farmer.GET_HARVESTER_PLOTS_VALID
                            ? filter.stream().map(value -> new PlotFilter("filename", value)).toList() : filter)
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
        return getHarvesterPlotsDuplicatesAsBytes(nodeId, filter, (long) page, pageSize, reverse);
    }

    public byte[] getHarvesterPlotsDuplicatesAsBytes(String nodeId, List<String> filter, long page,
            long pageSize, boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_DUPLICATES);
    }

    public ApiResponse<List<String>> getHarvesterPlotsDuplicates(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsDuplicates(nodeId, filter, (long) pageSize, reverse);
    }

    public ApiResponse<List<String>> getHarvesterPlotsDuplicates(String nodeId, List<String> filter, long pageSize,
            boolean reverse) throws RPCException {
        return processPlotPages(nodeId, filter, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_DUPLICATES);
    }

    public ApiResponse<List<String>> getHarvesterPlotsDuplicates(String nodeId)
            throws RPCException {
        return getHarvesterPlotsDuplicates(nodeId, List.of(), 500, false);
    }

    public byte[] getHarvesterPlotsInvalidAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsInvalidAsBytes(nodeId, filter, (long) page, pageSize, reverse);
    }

    public byte[] getHarvesterPlotsInvalidAsBytes(String nodeId, List<String> filter, long page, long pageSize,
            boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_INVALID);
    }

    public ApiResponse<List<String>> getHarvesterPlotsInvalid(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsInvalid(nodeId, filter, (long) pageSize, reverse);
    }

    public ApiResponse<List<String>> getHarvesterPlotsInvalid(String nodeId, List<String> filter, long pageSize,
            boolean reverse) throws RPCException {
        return processPlotPages(nodeId, filter, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_INVALID);
    }

    public ApiResponse<List<String>> getHarvesterPlotsInvalid(String nodeId)
            throws RPCException {
        return getHarvesterPlotsInvalid(nodeId, List.of(), 500, false);
    }

    public byte[] getHarvesterPlotsKeysMissingAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsKeysMissingAsBytes(nodeId, filter, (long) page, pageSize, reverse);
    }

    public byte[] getHarvesterPlotsKeysMissingAsBytes(String nodeId, List<String> filter, long page, long pageSize,
            boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_KEYS_MISSING);
    }

    public ApiResponse<List<String>> getHarvesterPlotsKeysMissing(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsKeysMissing(nodeId, filter, (long) pageSize, reverse);
    }

    public ApiResponse<List<String>> getHarvesterPlotsKeysMissing(String nodeId, List<String> filter, long pageSize,
            boolean reverse) throws RPCException {
        return processPlotPages(nodeId, filter, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_KEYS_MISSING);
    }

    public ApiResponse<List<String>> getHarvesterPlotsKeysMissing(String nodeId)
            throws RPCException {
        return getHarvesterPlotsKeysMissing(nodeId, List.of(), 500, false);
    }

    public byte[] getHarvesterPlotsValidAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsValidAsBytes(nodeId, filter, (long) page, pageSize, reverse);
    }

    public byte[] getHarvesterPlotsValidAsBytes(String nodeId, List<String> filter, long page, long pageSize,
            boolean reverse) throws RPCException {
        return makePlotRequest(nodeId, filter, page, pageSize, reverse, Farmer.GET_HARVESTER_PLOTS_VALID);
    }

    public ApiResponse<List<Plot>> getHarvesterPlotsValid(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsValid(nodeId, filter, (long) pageSize, reverse);
    }

    public ApiResponse<List<Plot>> getHarvesterPlotsValid(String nodeId, List<String> filter, long pageSize,
            boolean reverse) throws RPCException {
        return getHarvesterPlotsValid(nodeId, filter.stream().map(value -> new PlotFilter("filename", value)).toList(),
                "filename", pageSize, reverse);
    }

    public byte[] getHarvesterPlotsValidAsBytes(String nodeId, List<PlotFilter> filter, String sortKey,
            int page, int pageSize, boolean reverse) throws RPCException {
        return getHarvesterPlotsValidAsBytes(nodeId, filter, sortKey, (long) page, pageSize, reverse);
    }

    public byte[] getHarvesterPlotsValidAsBytes(String nodeId, List<PlotFilter> filter, String sortKey,
            long page, long pageSize, boolean reverse) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder().put("node_id", nodeId).put("filter", filter)
                    .put("sort_key", sortKey).put("page", uint32(page, "page")).put("page_size", uint32(pageSize, "page_size"))
                    .put("reverse", reverse).buildBytes();
            return client.makeRequest(new Request(Farmer.GET_HARVESTER_PLOTS_VALID, data));
        } catch (IOException e) { throw new RPCException("Error writing request JSON", e); }
    }

    public ApiResponse<List<Plot>> getHarvesterPlotsValid(String nodeId, List<PlotFilter> filter, String sortKey,
            int pageSize, boolean reverse) throws RPCException {
        return getHarvesterPlotsValid(nodeId, filter, sortKey, (long) pageSize, reverse);
    }

    public ApiResponse<List<Plot>> getHarvesterPlotsValid(String nodeId, List<PlotFilter> filter, String sortKey,
            long pageSize, boolean reverse) throws RPCException {
        try {
            Endpoint endpoint = Farmer.GET_HARVESTER_PLOTS_VALID;
            var responseBytes = getHarvesterPlotsValidAsBytes(nodeId, filter, sortKey, 0, pageSize, reverse);
            var jsonNode = JsonUtils.readTree(responseBytes);
            if (!jsonNode.get("success").asBoolean()) { return newFailedResponse(jsonNode, endpoint); }

            List<Plot> plotList = new ArrayList<>(
                    JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.PLOT_LIST)
            );

            for (long i = pageNumber(jsonNode, "page") + 1; i < pageNumber(jsonNode, "page_count"); ++i) {
                var nextBytes = getHarvesterPlotsValidAsBytes(nodeId, filter, sortKey, i, pageSize, reverse);
                var nextNode = JsonUtils.readTree(nextBytes);
                if (!nextNode.get("success").asBoolean()) { return newFailedResponse(nextNode, endpoint); }
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

    public byte[] setPayoutInstructionsAsBytes(String launcherId, String payoutInstructions) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder().put("launcher_id", launcherId)
                    .put("payout_instructions", payoutInstructions).buildBytes();
            return client.makeRequest(new Request(Farmer.SET_PAYOUT_INSTRUCTIONS, data));
        } catch (IOException e) { throw new RPCException("Error writing request JSON", e); }
    }

    public ApiResponse<Boolean> setPayoutInstructions(String launcherId, String payoutInstructions) throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(setPayoutInstructionsAsBytes(launcherId, payoutInstructions)),
                    "success", Boolean.class, Farmer.SET_PAYOUT_INSTRUCTIONS);
        } catch (IOException e) { throw new RPCException("Error reading response JSON", e); }
    }

    public byte[] connectToSolverAsBytes(String host, int port) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder().put("host", host).put("port", port).buildBytes();
            return client.makeRequest(new Request(Farmer.CONNECT_TO_SOLVER, data));
        } catch (IOException e) { throw new RPCException("Error writing request JSON", e); }
    }

    public ApiResponse<Boolean> connectToSolver(String host, int port) throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(connectToSolverAsBytes(host, port)),
                    "success", Boolean.class, Farmer.CONNECT_TO_SOLVER);
        } catch (IOException e) { throw new RPCException("Error reading response JSON", e); }
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
        return getRewardTargetsAsBytes(searchForPrivateKey, (Number) derivationDepth);
    }

    public byte[] getRewardTargetsAsBytes(boolean searchForPrivateKey, Number derivationDepth)
            throws RPCException {
        BigInteger depth;
        if (derivationDepth instanceof BigInteger big) depth = big;
        else if (derivationDepth instanceof Byte || derivationDepth instanceof Short
                || derivationDepth instanceof Integer || derivationDepth instanceof Long)
            depth = BigInteger.valueOf(derivationDepth.longValue());
        else throw new IllegalArgumentException("max_ph_to_search must be an integer");
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("search_for_private_key", searchForPrivateKey)
                    .put("max_ph_to_search", depth)
                    .buildBytes();
            var req = new Request(Farmer.GET_REWARD_TARGETS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<RewardTarget> getRewardTargets(boolean searchForPrivateKey, int derivationDepth)
            throws RPCException {
        return getRewardTargets(searchForPrivateKey, (Number) derivationDepth);
    }

    public ApiResponse<RewardTarget> getRewardTargets(boolean searchForPrivateKey, Number derivationDepth)
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
            var payload = new JsonUtils.ObjectBuilder();
            if (farmerTarget != null) { payload.put("farmer_target", farmerTarget); }
            if (poolTarget != null) { payload.put("pool_target", poolTarget); }
            var data = payload.buildBytes();
            var req = new Request(Farmer.SET_REWARD_TARGETS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> setRewardTargets(String farmerTarget, String poolTarget)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(setRewardTargetsAsBytes(farmerTarget, poolTarget));
            return newResponse(jsonNode, "success", Boolean.class, Farmer.SET_REWARD_TARGETS);
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
