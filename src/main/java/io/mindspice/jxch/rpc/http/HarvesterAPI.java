package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Harvester;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.harvester.HarvesterConfig;
import io.mindspice.jxch.rpc.schemas.harvester.HarvesterPlots;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;

import java.io.IOException;
import java.util.List;


public class HarvesterAPI extends ChiaAPI {

    public HarvesterAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }
    
    ////////////
    // SHARED //
    ////////////

    public byte[] closeConnectionAsBytes(String nodeId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("node_id", nodeId);
            var req = new Request(Harvester.CLOSE_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> closeConnection(String nodeId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(closeConnectionAsBytes(nodeId));
            return newResponse(jsonNode, "success", Boolean.class, Harvester.CLOSE_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getConnectionsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.GET_CONNECTIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Connection>> getConnections() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getConnectionsAsBytes());
            return newResponseList(jsonNode, "connections", TypeRefs.CONNECTION_LIST, Harvester.GET_CONNECTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getRoutesAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.GET_ROUTES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> getRoutes() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getRoutesAsBytes());
            return newResponseList(jsonNode, "routes", TypeRefs.STRING_LIST, Harvester.GET_ROUTES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] healthzAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.HEALTHZ, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> healthz() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(healthzAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Harvester.HEALTHZ);
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
            var req = new Request(Harvester.OPEN_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> openConnection(String ip, int port) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(openConnectionAsBytes(ip, port));
            return newResponse(jsonNode, "success", Boolean.class, Harvester.OPEN_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] stopNodeAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.STOP_NODE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> stopNode() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(healthzAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Harvester.STOP_NODE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }
    
    
    ///////////////
    // HARVESTER //
    ///////////////
    public byte[] getPlotsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.GET_PLOTS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<HarvesterPlots> getPlots() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPlotsAsBytes());
            return newResponse(jsonNode, HarvesterPlots.class, Harvester.GET_PLOTS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] refreshPlotsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.GET_PLOTS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> refreshPlots() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(refreshPlotsAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Harvester.REFRESH_PLOTS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] deletePlotAsBytes(String filename) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("filename", filename);
            var req = new Request(Harvester.DELETE_PLOT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deletePlot(String filename) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deletePlotAsBytes(filename));
            return newResponse(jsonNode, "success", Boolean.class, Harvester.DELETE_PLOT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] addPlotDirectoryAsBytes(String directoryName) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("dirname", directoryName);
            var req = new Request(Harvester.ADD_PLOT_DIRECTORY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> addPlotDirectory(String directoryName) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(addPlotDirectoryAsBytes(directoryName));
            return newResponse(jsonNode, "success", Boolean.class, Harvester.ADD_PLOT_DIRECTORY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] removePlotDirectoryAsBytes(String directoryName) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("dirname", directoryName);
            var req = new Request(Harvester.REMOVE_PLOT_DIRECTORY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> removePlotDirectory(String directoryName) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(removePlotDirectoryAsBytes(directoryName));
            return newResponse(jsonNode, "success", Boolean.class, Harvester.REMOVE_PLOT_DIRECTORY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getPlotDirectoriesAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.GET_PLOT_DIRECTORIES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> getPlotDirectories() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPlotDirectoriesAsBytes());
            return newResponseList(jsonNode, "directories", TypeRefs.STRING_LIST, Harvester.GET_PLOT_DIRECTORIES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getHarvesterConfigAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Harvester.GET_HARVESTER_CONFIG, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<HarvesterConfig> getHarvesterConfig() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPlotDirectoriesAsBytes());
            return newResponse(jsonNode, HarvesterConfig.class, Harvester.GET_HARVESTER_CONFIG);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }


    public byte[] updateHarvesterConfigAsBytes(JsonNode harvesterConfigBuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(harvesterConfigBuilder);
            var req = new Request(Harvester.UPDATE_HARVESTER_CONFIG, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> updateHarvesterConfig(JsonNode harvesterConfigBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(updateHarvesterConfigAsBytes(harvesterConfigBuilder));
            return newResponse(jsonNode, "success", Boolean.class, Harvester.UPDATE_HARVESTER_CONFIG);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }
}
