package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record BasicResponse(
        String status,
        boolean success,
        String error
) implements BlockChainObject {
    public BasicResponse(String status, boolean success, String error) {
        this.status = status == null ? "" : status;
        this.success = success;
        this.error = error;
    }


    @Override
    public ObjectType getObjectType() {
        return ObjectType.BASIC_RESPONSE;
    }
}
