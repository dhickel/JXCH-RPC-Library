package io.mindspice.schemas.components;

import java.util.List;


public record TransactionInfo(
        String generator_root,
        String generator_refs_root,
        String aggregated_signature,
        String fees,
        String cost,
        List<Coin> reward_claims_incorporated
) { }

