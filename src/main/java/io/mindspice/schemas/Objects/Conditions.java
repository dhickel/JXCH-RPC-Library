package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.Collections;
import java.util.List;


public record Conditions(
        long addition_amount,
        List<String> agg_sig_unsafe,//FIXME not sure what this is, returns []
        long before_height_absolute,
        long before_seconds_absolute,
        long cost,
        long height_absolute,
        long removal_amount,
        long reserve_fee,
        long seconds_absolute,
        List<Spend> spends

) implements BlockChainObject {

    @Override
    public ObjectType getObjectType() {
        return ObjectType.CONDITIONS;
    }


    public Conditions {
        agg_sig_unsafe = agg_sig_unsafe == null ? List.of() : Collections.unmodifiableList(agg_sig_unsafe);
        spends = spends == null ? List.of() : Collections.unmodifiableList(spends);
    }
}
