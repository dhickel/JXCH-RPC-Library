package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public record Spend(
        List<List<String>> agg_sig_me,
        int before_height_relative,
        int before_seconds_relative,
        int birth_height,
        int birth_seconds,
        String coin_id,
        List<List<String>> create_coin,
        int flags,
        int height_relative,
        String puzzle_hash,
        long seconds_relative
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.SPEND;
    }


    public Spend {
        agg_sig_me = agg_sig_me == null ? List.of() : Collections.unmodifiableList(agg_sig_me);

        List<List<String>> ccList = new ArrayList<>();
        for (var cc : create_coin) {
            ccList.add(Collections.unmodifiableList(cc));
        }
        create_coin = Collections.unmodifiableList(ccList);
    }
}
