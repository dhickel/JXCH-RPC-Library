package io.mindspice.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Farmer;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.TypeRefs;
import io.mindspice.schemas.farmer.*;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;
import io.mindspice.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FarmerAPI extends SharedAPI {


    public FarmerAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }


    public byte[] getHarvesterPlotsDuplicatesAsBytes(String nodeId, List<String> filter, int page,
            int pageSize, boolean reverse) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("node_id", nodeId)
                    .put("page", page)
                    .put("page_size", pageSize)
                    .put("filter", filter)
                    .put("reverse", reverse)
                    .buildBytes();
            var req = new Request(Farmer.GET_HARVESTER_PLOTS_DUPLICATES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<String> getHarvesterPlotsDuplicates(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getHarvesterPlotsDuplicatesAsBytes(nodeId, filter, 0, pageSize, reverse)
            );
            Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_DUPLICATES, jsonNode);

            var dupeList = new ArrayList<String>(JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            for (int i = jsonNode.get("page").asInt(); i < jsonNode.get("page_count").asInt(); ++i) {
                var nextNode = JsonUtils.readTree(
                        getHarvesterPlotsDuplicatesAsBytes(nodeId, filter, i, pageSize, reverse)
                );
                Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_DUPLICATES, nextNode);
                dupeList.addAll(JsonUtils.readJson(nextNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            }

            return Collections.unmodifiableList(dupeList);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public List<String> getHarvesterPlotsDuplicates(String nodeId)
            throws RPCException {
        return getHarvesterPlotsDuplicates(nodeId, List.of(), 500, false);
    }


    public byte[] getHarvesterPlotsInvalidAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("node_id", nodeId)
                    .put("page", page)
                    .put("page_size", pageSize)
                    .put("filter", filter)
                    .put("reverse", reverse)
                    .buildBytes();
            var req = new Request(Farmer.GET_HARVESTER_PLOTS_INVALID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<String> getHarvesterPlotsInvalid(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getHarvesterPlotsInvalidAsBytes(nodeId, filter, 0, pageSize, reverse)
            );
            Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_INVALID, jsonNode);

            var dupeList = new ArrayList<String>(JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            for (int i = jsonNode.get("page").asInt(); i < jsonNode.get("page_count").asInt(); ++i) {
                var nextNode = JsonUtils.readTree(
                        getHarvesterPlotsDuplicatesAsBytes(nodeId, filter, i, pageSize, reverse)
                );
                Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_INVALID, nextNode);
                dupeList.addAll(JsonUtils.readJson(nextNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            }

            return Collections.unmodifiableList(dupeList);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public List<String> getHarvesterPlotsInvalid(String nodeId)
            throws RPCException {
        return getHarvesterPlotsDuplicates(nodeId, List.of(), 500, false);
    }


    public byte[] getHarvesterPlotsKeysMissingAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("node_id", nodeId)
                    .put("page", page)
                    .put("page_size", pageSize)
                    .put("filter", filter)
                    .put("reverse", reverse)
                    .buildBytes();
            var req = new Request(Farmer.GET_HARVESTER_PLOTS_KEYS_MISSING, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<String> getHarvesterPlotsKeysMissing(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getHarvesterPlotsKeysMissingAsBytes(nodeId, filter, 0, pageSize, reverse)
            );
            Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_KEYS_MISSING, jsonNode);

            var dupeList = new ArrayList<String>(JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            for (int i = jsonNode.get("page").asInt(); i < jsonNode.get("page_count").asInt(); ++i) {
                var nextNode = JsonUtils.readTree(
                        getHarvesterPlotsDuplicatesAsBytes(nodeId, filter, i, pageSize, reverse)
                );
                Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_KEYS_MISSING, nextNode);
                dupeList.addAll(JsonUtils.readJson(nextNode.get("plots").traverse(), TypeRefs.STRING_LIST));
            }

            return Collections.unmodifiableList(dupeList);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public List<String> getHarvesterPlotsKeysMissing(String nodeId)
            throws RPCException {
        return getHarvesterPlotsKeysMissing(nodeId, List.of(), 500, false);
    }


    public byte[] getHarvesterPlotsValidAsBytes(String nodeId, List<String> filter, int page, int pageSize,
            boolean reverse) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("node_id", nodeId)
                    .put("page", page)
                    .put("page_size", pageSize)
                    .put("filter", filter)
                    .put("reverse", reverse)
                    .buildBytes();
            var req = new Request(Farmer.GET_HARVESTER_PLOTS_VALID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<Plot> getHarvesterPlotsValid(String nodeId, List<String> filter, int pageSize,
            boolean reverse) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getHarvesterPlotsValidAsBytes(nodeId, filter, 0, pageSize, reverse)
            );
            Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_VALID, jsonNode);

            var dupeList = new ArrayList<Plot>(JsonUtils.readJson(jsonNode.get("plots").traverse(), TypeRefs.PLOT_LIST));
            for (int i = jsonNode.get("page").asInt(); i < jsonNode.get("page_count").asInt(); ++i) {
                var nextNode = JsonUtils.readTree(
                        getHarvesterPlotsDuplicatesAsBytes(nodeId, filter, i, pageSize, reverse)
                );
                Utils.checkRpcSuccess(address, Farmer.GET_HARVESTER_PLOTS_VALID, nextNode);
                dupeList.addAll(JsonUtils.readJson(nextNode.get("plots").traverse(), TypeRefs.PLOT_LIST));
            }

            return Collections.unmodifiableList(dupeList);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public List<Plot> getHarvesterPlotsValid(String nodeId)
            throws RPCException {
        return getHarvesterPlotsValid(nodeId, List.of(), 500, false);
    }


    public byte[] getHarvestersAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(Farmer.GET_HARVESTERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<HarvesterDetails> getHarvesters() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getHarvestersAsBytes());
            Utils.checkRpcSuccess(address, Farmer.GET_HARVESTERS, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("harvesters").traverse(), TypeRefs.HARVESTER_DETAILS_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getHarvestersSummaryAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(Farmer.GET_HARVESTERS_SUMMARY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<HarvesterSummary> getHarvestersSummary() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getHarvestersSummaryAsBytes());
            Utils.checkRpcSuccess(address, Farmer.GET_HARVESTERS_SUMMARY, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("harvesters").traverse(), TypeRefs.HARVESTER_SUMMARY_LIST)
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getPoolLoginLinkAsBytes(String launcherId) throws RPCException {
        try {
            var data = JsonUtils.singleNodeAsBytes("launcher_id", launcherId);
            var req = new Request(Farmer.GET_POOL_LOGIN_LINK, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public String getPoolLoginLink(String launcherId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPoolLoginLinkAsBytes(launcherId));
            Utils.checkRpcSuccess(address, Farmer.GET_POOL_LOGIN_LINK, jsonNode);
            return jsonNode.get("login_link").asText();
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getPoolStateAsBytes() throws RPCException {
        try {
            var data = JsonUtils.emptyNodeAsBytes();
            var req = new Request(Farmer.GET_POOL_STATE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }


    public List<PoolState> getPoolState() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPoolStateAsBytes());
            System.out.println(JsonUtils.writeString(jsonNode));
            Utils.checkRpcSuccess(address, Farmer.GET_POOL_STATE, jsonNode);
            return Collections.unmodifiableList(
                    JsonUtils.readJson(jsonNode.get("pool_state").traverse(), TypeRefs.POOL_STATE_LIST)
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
            var jsonNode = JsonUtils.readTree(
                    getRewardTargetsAsBytes(searchForPrivateKey, derivationDepth)
            );
            //return new ApiResponse<>(JsonUtils.readJson(jsonNode, RewardTarget.class), "", jsonNode.get("success").asBoolean(), "");
            return newResponse(jsonNode, RewardTarget.class, Farmer.SET_REWARD_TARGETS);
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


    public RewardTarget setRewardTargets(String farmerTarget, String poolTarget)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(setRewardTargetsAsBytes(farmerTarget, poolTarget));
            Utils.checkRpcSuccess(address, Farmer.SET_REWARD_TARGETS, jsonNode);
            return JsonUtils.readJson(jsonNode, RewardTarget.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] getSignagePointAsBytes(String farmerTarget, String poolTarget) throws RPCException {
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


    public RewardTarget getSignagePoint(String farmerTarget, String poolTarget)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getSignagePointAsBytes(farmerTarget, poolTarget)
            );
            System.out.println(JsonUtils.writeString(jsonNode));
            Utils.checkRpcSuccess(address, Farmer.SET_REWARD_TARGETS, jsonNode);
            return JsonUtils.readJson(jsonNode, RewardTarget.class);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


}
