package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public record SpendBundleOld(
        String aggregated_signature,
        List<CoinSpend> coin_solutions,
        String puzzle_reveal,
        String solution
) implements BlockChainObject {
    public SpendBundleOld(SpendBundle spendBundle) {
        this(
                spendBundle.aggregated_signature(),
                spendBundle.coin_spends() == null
                        ? List.of()
                        : Collections.unmodifiableList(spendBundle.coin_spends()),
                spendBundle.puzzle_reveal(),
                spendBundle.solution()
        );
    }


    @Override
    public ObjectType getObjectType() {
        return ObjectType.SPEND_BUNDLE;
    }
}
