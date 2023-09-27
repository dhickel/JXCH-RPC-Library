package io.mindspice.mindlib.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class JsonUtils {
    private static ObjectMapper mapper;
    private static Map<Class<?>, ObjectReader> readerCache;
    private static Map<Class<?>, ObjectWriter> writerCache;
    private static Map<TypeReference<?>, ObjectReader> typeRefCache;
    private static final ObjectNode emptyNode;

    static {
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        readerCache = new ConcurrentHashMap<>(20);
        writerCache = new ConcurrentHashMap<>(20);
        typeRefCache = new ConcurrentHashMap<>(20);
        emptyNode = mapper.createObjectNode();
    }

    public static void setFailOnUnknownProperties(boolean isFailOn) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, isFailOn);
    }

    public static void setSerializationIncludeNull(boolean includeNull) {
        mapper.setSerializationInclusion(includeNull ? JsonInclude.Include.ALWAYS : JsonInclude.Include.NON_NULL);
    }

    private JsonUtils() { }

    private static ObjectReader readerFor(Class<?> objClass, boolean isArray) {
        if (isArray) {
            return readerCache.computeIfAbsent(objClass, mapper::readerForArrayOf);
        } else {
            return readerCache.computeIfAbsent(objClass, mapper::readerFor);
        }
    }

    public void setMapperOverride(ObjectMapper mapper) {
        JsonUtils.mapper = mapper;
        JsonUtils.readerCache = new ConcurrentHashMap<>(20);
        JsonUtils.writerCache = new ConcurrentHashMap<>(20);
        JsonUtils.typeRefCache = new ConcurrentHashMap<>(20);
    }

    private static ObjectWriter writerFor(Class<?> objClass) {
        return writerCache.computeIfAbsent(objClass, mapper::writerFor);
    }

    private static <T> ObjectReader writerForRef(TypeReference<T> typeRef) {
        return typeRefCache.computeIfAbsent(typeRef, mapper::readerFor);
    }

    public static <T> T readJson(String json, Class<?> objClass) throws JsonProcessingException {
        if (json == null) { return null; }
        return readerFor(objClass, false).readValue(json);
    }

    public static <T> T readJson(JsonNode json, Class<?> objClass) throws IOException {
        if (json == null) { return null; }

        return readerFor(objClass, false).readValue(json);
    }

    public static <T> T readJson(byte[] json, Class<?> objClass) throws IOException {
        if (json == null) { return null; }
        return readerFor(objClass, false).readValue(json);
    }

    public static byte[] writeBytes(JsonNode json) throws JsonProcessingException {
        return mapper.writeValueAsBytes(json);
    }

    public static byte[] writeBytes(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsBytes(obj);
    }

    public static String writeString(JsonNode json) throws JsonProcessingException {
        return mapper.writeValueAsString(json);
    }

    public static String writeString(byte[] json) throws JsonProcessingException {
        return mapper.writeValueAsString(json);
    }

    public static <T> String writeString(T obj) throws JsonProcessingException {
        return writerFor(obj.getClass()).writeValueAsString(obj);
    }

    public static <T> String writePretty(T obj) throws JsonProcessingException {
        return writerFor(obj.getClass()).withDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static JsonNode readTree(String json) throws JsonProcessingException {
        if (json == null) { return newEmptyNode(); }
        return mapper.readTree(json);
    }

    public static JsonNode readTree(byte[] json) throws IOException {
        if (json == null) { return newEmptyNode(); }
        return mapper.readTree(json);
    }

    public static JsonNode readTree(JsonNode json) throws IOException {
        if (json == null) { return newEmptyNode(); }
        return mapper.readTree(json.traverse());
    }

    public static <T> T readValue(String json, Class<T> objClass) throws JsonProcessingException {
        if (json == null) { return null; }
        return mapper.readValue(json, objClass);
    }

    public static <T> T readValue(byte[] json, Class<T> objClass) throws IOException {
        if (json == null) { return null; }
        return mapper.readValue(json, objClass);
    }

    public static <T> T readValue(JsonParser json, Class<T> objClass) throws IOException {
        if (json == null) { return null; }
        return mapper.readValue(json, objClass);
    }

    public static <T> T readValue(InputStream json, Class<T> objClass) throws IOException {
        if (json == null) { return null; }
        return mapper.readValue(json, objClass);
    }

    public static <T> JsonNode toNode(T obj) {
        return mapper.valueToTree(obj);
    }

    public static <T> ObjectNode newSingleNode(String field, T value) {
        ObjectNode node = mapper.createObjectNode();
        return (node.putPOJO(field, value));
    }

    public static <T> byte[] newSingleNodeAsBytes(String field, T value) throws JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        return (mapper.writeValueAsBytes(node.putPOJO(field, value)));
    }

    public static <T> String newSingleNodeAsString(String field, T value) throws JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        return (mapper.writeValueAsString(node.putPOJO(field, value)));
    }

    public static String newEmptyNodeAsString() throws JsonProcessingException {
        return mapper.writeValueAsString(emptyNode);
    }

    public static byte[] newEmptyNodeAsBytes() throws JsonProcessingException {
        return mapper.writeValueAsBytes(emptyNode);
    }

    public static ObjectNode newEmptyNode() {
        return mapper.createObjectNode();
    }

    public static <T> T readJson(JsonParser json, TypeReference<T> typeRef) throws IOException {
        return writerForRef(typeRef).readValue(json, typeRef);
    }

    public static JsonNode merge(JsonNode node1, JsonNode node2) {
        ObjectNode rootNode = (ObjectNode) node1;
        node2.fields().forEachRemaining(entry -> {
            rootNode.set(entry.getKey(), entry.getValue());
        });
        return rootNode;
    }

    public static JsonNode successMsg(ObjectNode jsonNode) {
        jsonNode.put("error", false);
        jsonNode.put("success", true);
        jsonNode.put("error_msg", "");
        return jsonNode;
    }

    public static JsonNode failMsg() {
        return new ObjectBuilder()
                .put("error", false)
                .put("error_msg", "")
                .put("success", false)
                .buildNode();
    }

    public static JsonNode errorMsg(String error) {
        return new ObjectBuilder()
                .put("error", true)
                .put("error_msg", error)
                .put("success", false)
                .buildNode();
    }

    // Fluent builder for easier json construction
    public static class ObjectBuilder {
        private final ObjectNode node = mapper.createObjectNode();

        public <T> ObjectBuilder put(String field, T value) {
            node.putPOJO(field, value);
            return this;
        }

        public ObjectNode buildNode() {
            return node;
        }

        public String buildString() throws JsonProcessingException {
            return mapper.writeValueAsString(node);
        }

        public byte[] buildBytes() throws JsonProcessingException {
            return mapper.writeValueAsBytes(node);
        }
    }


}