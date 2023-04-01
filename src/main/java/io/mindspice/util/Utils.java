package io.mindspice.util;

import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.enums.endpoints.Endpoint;


public class Utils {

    public static boolean checkRpcSuccess(Endpoint endpoint, JsonNode node) throws RPCRequestError {
        try {
            if(node.get("success").asBoolean()) {
                return true;
            } else {
                throw new RPCRequestError("Endpoint:" + endpoint.getPath() + " Error:" +node.get("error").asText());
            }
        } catch (RPCException e) {
            throw e;
        }
    }
}
