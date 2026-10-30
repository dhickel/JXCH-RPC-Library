package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;
import io.mindspice.jxch.rpc.enums.endpoints.Common;
import io.mindspice.jxch.rpc.schemas.fullnode.Network;
import io.mindspice.jxch.rpc.schemas.shared.LogLevel;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.util.JsonUtils;
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

    private record CommonEndpoint(ChiaService service, Common endpoint) implements Endpoint {
        @Override public ChiaService getService() { return service; }
        @Override public String getPath() { return endpoint.getPath(); }
        @Override public String toString() { return endpoint.toString(); }
    }

    private Endpoint bound(Common endpoint) {
        return new CommonEndpoint(service, endpoint);
    }

    private byte[] commonRequest(Common endpoint, JsonUtils.ObjectBuilder payload) throws RPCException {
        try {
            return client.makeRequest(new Request(service, endpoint, payload.buildBytes()));
        } catch (IOException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    private JsonNode commonResponseTree(byte[] bytes) throws IOException {
        var json = JsonUtils.readTree(bytes);
        if (json == null || !json.isObject() || !json.path("success").isBoolean()) {
            throw new IOException("Expected an RPC response object with boolean success");
        }
        return json;
    }

    private <T> ApiResponse<T> commonResponse(byte[] bytes, String field, Class<T> type, Common endpoint)
            throws RPCException {
        try {
            var json = commonResponseTree(bytes);
            if (!json.path("success").asBoolean() && !json.hasNonNull("error") && json.hasNonNull("errors")) {
                return new ApiResponse<>(Optional.empty(), false, json.get("errors").toString(), nodeAddress,
                        serviceAddress + endpoint.getPath(), bound(endpoint));
            }
            return field == null ? newResponse(json, type, bound(endpoint)) : newResponse(json, field, type, bound(endpoint));
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] closeConnectionAsBytes(String nodeId) throws RPCException {
        return commonRequest(Common.CLOSE_CONNECTION, new JsonUtils.ObjectBuilder().put("node_id", nodeId));
    }

    public ApiResponse<Boolean> closeConnection(String nodeId) throws RPCException {
        return commonResponse(closeConnectionAsBytes(nodeId), "success", Boolean.class, Common.CLOSE_CONNECTION);
    }

    public byte[] getConnectionsAsBytes() throws RPCException {
        return commonRequest(Common.GET_CONNECTIONS, new JsonUtils.ObjectBuilder());
    }

    public byte[] getConnectionsAsBytes(int nodeType) throws RPCException {
        if (nodeType < 1 || nodeType > 8) throw new IllegalArgumentException("nodeType must be a Chia NodeType (1..8)");
        return commonRequest(Common.GET_CONNECTIONS, new JsonUtils.ObjectBuilder().put("node_type", nodeType));
    }

    public ApiResponse<List<Connection>> getConnections() throws RPCException {
        return connectionsResponse(getConnectionsAsBytes());
    }

    public ApiResponse<List<Connection>> getConnections(int nodeType) throws RPCException {
        return connectionsResponse(getConnectionsAsBytes(nodeType));
    }

    private ApiResponse<List<Connection>> connectionsResponse(byte[] bytes) throws RPCException {
        try {
            return newResponseList(commonResponseTree(bytes), "connections",
                    new TypeReference<List<Connection>>() {}, bound(Common.GET_CONNECTIONS));
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getRoutesAsBytes() throws RPCException {
        return commonRequest(Common.GET_ROUTES, new JsonUtils.ObjectBuilder());
    }

    public ApiResponse<List<String>> getRoutes() throws RPCException {
        try {
            return newResponseList(commonResponseTree(getRoutesAsBytes()), "routes",
                    new TypeReference<List<String>>() {}, bound(Common.GET_ROUTES));
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] healthzAsBytes() throws RPCException {
        return commonRequest(Common.HEALTHZ, new JsonUtils.ObjectBuilder());
    }

    public ApiResponse<Boolean> healthz() throws RPCException {
        return commonResponse(healthzAsBytes(), "success", Boolean.class, Common.HEALTHZ);
    }

    public byte[] openConnectionAsBytes(String host, int port) throws RPCException {
        if (port < 0 || port > 65535) throw new IllegalArgumentException("port must fit uint16");
        return commonRequest(Common.OPEN_CONNECTION, new JsonUtils.ObjectBuilder().put("host", host).put("port", port));
    }

    public ApiResponse<Boolean> openConnection(String host, int port) throws RPCException {
        return commonResponse(openConnectionAsBytes(host, port), "success", Boolean.class, Common.OPEN_CONNECTION);
    }

    public byte[] stopNodeAsBytes() throws RPCException {
        return commonRequest(Common.STOP_NODE, new JsonUtils.ObjectBuilder());
    }

    public ApiResponse<Boolean> stopNode() throws RPCException {
        return commonResponse(stopNodeAsBytes(), "success", Boolean.class, Common.STOP_NODE);
    }

    public byte[] getNetworkInfoAsBytes() throws RPCException {
        return commonRequest(Common.GET_NETWORK_INFO, new JsonUtils.ObjectBuilder());
    }

    public ApiResponse<Network> getNetworkInfo() throws RPCException {
        return commonResponse(getNetworkInfoAsBytes(), null, Network.class, Common.GET_NETWORK_INFO);
    }

    public byte[] getVersionAsBytes() throws RPCException {
        return commonRequest(Common.GET_VERSION, new JsonUtils.ObjectBuilder());
    }

    public ApiResponse<String> getVersion() throws RPCException {
        return commonResponse(getVersionAsBytes(), "version", String.class, Common.GET_VERSION);
    }

    public byte[] getLogLevelAsBytes() throws RPCException {
        return commonRequest(Common.GET_LOG_LEVEL, new JsonUtils.ObjectBuilder());
    }

    public ApiResponse<LogLevel> getLogLevel() throws RPCException {
        return commonResponse(getLogLevelAsBytes(), null, LogLevel.class, Common.GET_LOG_LEVEL);
    }

    public byte[] setLogLevelAsBytes(String level) throws RPCException {
        return commonRequest(Common.SET_LOG_LEVEL, new JsonUtils.ObjectBuilder().put("level", level));
    }

    public ApiResponse<LogLevel> setLogLevel(String level) throws RPCException {
        return commonResponse(setLogLevelAsBytes(level), null, LogLevel.class, Common.SET_LOG_LEVEL);
    }

    public byte[] setLogLevelAsBytes(Object level) throws RPCException {
        if (level == null || level instanceof String) return setLogLevelAsBytes((String) level);
        if (!(level instanceof Number number)) throw new IllegalArgumentException("level must be a string or integer");
        return commonRequest(Common.SET_LOG_LEVEL,
                new JsonUtils.ObjectBuilder().put("level", ExactIntegerDeserializer.requireIntegral(number)));
    }

    public ApiResponse<LogLevel> setLogLevel(Object level) throws RPCException {
        return commonResponse(setLogLevelAsBytes(level), null, LogLevel.class, Common.SET_LOG_LEVEL);
    }

    public byte[] resetLogLevelAsBytes() throws RPCException {
        return commonRequest(Common.RESET_LOG_LEVEL, new JsonUtils.ObjectBuilder());
    }

    public ApiResponse<LogLevel> resetLogLevel() throws RPCException {
        return commonResponse(resetLogLevelAsBytes(), null, LogLevel.class, Common.RESET_LOG_LEVEL);
    }


}
