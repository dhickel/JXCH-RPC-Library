package io.mindspice.util;

import com.fasterxml.jackson.databind.JsonNode;


public class Utils {

    public static boolean checkRpcSuccess(JsonNode node) throws RPCRequestError {
        try {
            if(node.get("success").asBoolean()) {
                return true;
            } else {
                throw new RPCRequestError(node.get("error").asText());
            }
        } catch (RPCException e) {
            throw e;
        }
    }
}
