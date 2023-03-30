package io.mindspice.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.schemas.fullnode.Block;
import io.mindspice.schemas.fullnode.BlockRecords;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class JsonUtils {
    private static ObjectMapper mapper;
    private static final Map<Class<?>, ObjectReader> readerCache = new ConcurrentHashMap<>(10);
    private static final Map<Class<?>, ObjectWriter> writerCache = new ConcurrentHashMap<>(10);
    private static final Map<TypeReference, ObjectReader> typeRefCache = new ConcurrentHashMap<>(10);
    private static final ObjectNode emptyNode;

    static {
        mapper = new ObjectMapper();//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        emptyNode = mapper.createObjectNode();

    }


    private JsonUtils() { }


    private static ObjectReader readerFor(Class<?> objClass, boolean isArray) {
        if (isArray) {
            return readerCache.computeIfAbsent(objClass, mapper::readerForArrayOf);
        } else {
            return readerCache.computeIfAbsent(objClass, mapper::readerFor);
        }
    }


    private static ObjectWriter writerFor(Class<?> objClass) {
        return writerCache.computeIfAbsent(objClass, mapper::writerFor);
    }


    private static <T>  ObjectReader writerForRef(TypeReference<T> typeRef) {
        return typeRefCache.computeIfAbsent(typeRef, mapper::readerFor);
    }


    public static <T> T readJson(String json, Class<?> objClass) throws JsonProcessingException {
        return readerFor(objClass, false).readValue(json);
    }


    public static <T> T readJson(JsonNode json, Class<?> objClass) throws IOException {
        return readerFor(objClass, false).readValue(json);
    }


    public static <T> T readJson(byte[] json, Class<?> objClass) throws IOException {
        return readerFor(objClass, false).readValue(json);
    }


    public static <T> T readJsonArray(String json, Class<?> objClass) throws JsonProcessingException {
        return readerFor(objClass, true).readValue(json);
    }


    public static <T> T readJsonArray(JsonNode json, Class<T> objClass) throws IOException {
        return readerFor(objClass, true).readValue(json);
    }


    public static <T> T readJsonArray(byte[] json, Class<T> objClass) throws IOException {
        return readerFor(objClass, true).readValue(json);
    }


    public static byte[] writeBytes(JsonNode json) throws JsonProcessingException {
        return mapper.writeValueAsBytes(json);
    }


    public static byte[] writeBytes(Class<?> json) throws JsonProcessingException {
        return writerFor(json).writeValueAsBytes(json);
    }


    public static String writeString(JsonNode json) throws JsonProcessingException {
        return mapper.writeValueAsString(json);
    }


    public static String writeString(byte[] json) throws JsonProcessingException {
        return mapper.writeValueAsString(json);
    }


    public static String writeString(Class<?> json) throws JsonProcessingException {
        return writerFor(json).writeValueAsString(json);
    }


    public static ObjectMapper getMapper() {
        return mapper;
    }


    public static JsonNode readTree(String json) throws JsonProcessingException {
        return mapper.readTree(json);
    }



    public static JsonNode readTree(byte[] json) throws IOException {
        return mapper.readTree(json);
    }


    public static <T> ObjectNode singleNode(String field, T value) {
        ObjectNode node = mapper.createObjectNode();
        return (node.putPOJO(field, value));
    }


    public static ObjectNode emptyNode() {
        return emptyNode;
    }


    public static <T> T readJson(JsonParser json, TypeReference<T> typeRef) throws IOException {
        return writerForRef(typeRef).readValue(json, typeRef);
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
