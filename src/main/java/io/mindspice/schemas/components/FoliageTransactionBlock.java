package io.mindspice.schemas.components;

public record FoliageTransactionBlock(
        String prev_transaction_block_hash,
        long timestamp,
        String filter_hash,
        String additions_root,
        String removals_root,
        String transactions_info_hash
) {}
