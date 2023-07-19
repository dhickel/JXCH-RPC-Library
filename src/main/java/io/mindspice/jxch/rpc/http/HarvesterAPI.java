package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Harvester;
import io.mindspice.mindlib.util.JsonUtils;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.harvester.HarvesterPlots;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.util.RPCException;

import java.io.IOException;
import java.util.List;


public class HarvesterAPI extends SharedAPI {

    public HarvesterAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }

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
}
