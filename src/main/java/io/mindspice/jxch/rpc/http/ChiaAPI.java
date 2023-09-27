package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;
import io.mindspice.mindlib.util.JsonUtils;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.util.RPCException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class ChiaAPI {
    protected final RPCClient client;
    protected final String serviceAddress;
    protected final String nodeAddress;
    protected final ChiaService service;

    protected ChiaAPI(RPCClient client, ChiaService chiaService) {
        this.client = client;
        this.serviceAddress = client.getAddressFor(chiaService);
        this.nodeAddress = client.getAddress();
        service = chiaService;
    }

    protected <T> ApiResponse<T> newResponse(JsonNode jsonNode, String dataField, Class<T> type,
            Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<T> data = success
                ? Optional.ofNullable(JsonUtils.readJson(jsonNode.get(dataField), type))
                : Optional.empty();

        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected ApiResponse<JsonNode> newResponseNode(JsonNode jsonNode, String dataField,
            Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<JsonNode> data = success
                ? Optional.ofNullable(jsonNode.get(dataField))
                : Optional.empty();

        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected ApiResponse<JsonNode> newResponseNode(JsonNode jsonNode, Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<JsonNode> data = success
                ? Optional.of(jsonNode)
                : Optional.empty();

        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T> ApiResponse<T> newResponse(JsonNode jsonNode, Class<T> type,
            Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<T> data = success
                ? Optional.ofNullable(JsonUtils.readJson(jsonNode, type))
                : Optional.empty();

        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T> ApiResponse<List<T>> newResponseList(JsonNode jsonNode, String dataField,
            TypeReference<List<T>> typeRef, Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<List<T>> data;
        if (success) {
            var list = JsonUtils.readJson(jsonNode.get(dataField).traverse(), typeRef);
            data = Optional.of(Collections.unmodifiableList(list));
        } else {
            data = Optional.empty();
        }
        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T> ApiResponse<List<T>> newResponseList(JsonNode jsonNode,
            TypeReference<List<T>> typeRef, Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<List<T>> data;
        if (success) {
            var list = JsonUtils.readJson(jsonNode.traverse(), typeRef);
            data = Optional.of(Collections.unmodifiableList(list));
        } else {
            data = Optional.empty();
        }
        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T> ApiResponse<List<T>> newResponseList(JsonNode jsonNode, List<T> list,
            Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<List<T>> data = Optional.of(Collections.unmodifiableList(list));
        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T, U> ApiResponse<Map<T, U>> newResponseMap(JsonNode jsonNode, String dataField,
            TypeReference<Map<T, U>> typeRef, Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<Map<T, U>> data;
        if (success) {
            var map = JsonUtils.readJson(jsonNode.get(dataField).traverse(), typeRef);
            data = Optional.of(Collections.unmodifiableMap(map));
        } else {
            data = Optional.empty();
        }
        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T, U> ApiResponse<Map<T, U>> newResponseMap(JsonNode jsonNode,
            TypeReference<Map<T, U>> typeRef, Endpoint endpoint) throws IOException {

        var success = jsonNode.get("success").asBoolean();
        Optional<Map<T, U>> data;
        if (success) {
            var map = JsonUtils.readJson(jsonNode.traverse(), typeRef);
            data = Optional.of(Collections.unmodifiableMap(map));
        } else {
            data = Optional.empty();
        }
        return new ApiResponse<>(
                data,
                success,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T> ApiResponse<T> newResponse(T object, Endpoint endpoint) throws IOException {
        return new ApiResponse<>(
                Optional.of(object),
                true,
                "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    protected <T> ApiResponse<T> newFailedResponse(JsonNode jsonNode, Endpoint endpoint) {
        return new ApiResponse<>(
                Optional.empty(),
                false,
                jsonNode.hasNonNull("error") ? jsonNode.get("error").asText() : "",
                nodeAddress,
                serviceAddress + endpoint.getPath(),
                endpoint
        );
    }

    abstract public byte[] closeConnectionAsBytes(String nodeId) throws RPCException;

    abstract public ApiResponse<Boolean> closeConnection(String nodeId) throws RPCException;

    abstract public byte[] getConnectionsAsBytes() throws RPCException;

    abstract public ApiResponse<List<Connection>> getConnections() throws RPCException;

    abstract public byte[] getRoutesAsBytes() throws RPCException;

    abstract public ApiResponse<List<String>> getRoutes() throws RPCException;

    abstract public byte[] healthzAsBytes() throws RPCException;

    abstract public ApiResponse<Boolean> healthz() throws RPCException;

    abstract public byte[] openConnectionAsBytes(String ip, int port) throws RPCException;

    abstract public ApiResponse<Boolean> openConnection(String ip, int port) throws RPCException;

    abstract public byte[] stopNodeAsBytes() throws RPCException;

    abstract public ApiResponse<Boolean> stopNode() throws RPCException;


}
