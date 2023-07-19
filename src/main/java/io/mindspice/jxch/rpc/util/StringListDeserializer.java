package io.mindspice.jxch.rpc.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StringListDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws  IOException {
        List<String> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.isArray()) {
            for (JsonNode element : node) {
                if (element.isArray()) {
                    for (JsonNode innerElement : element) { result.add(innerElement.asText()); }
                } else {
                    result.add(element.asText());
                }
            }
        }

        return result;
    }
}