package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.Collections;
import java.util.List;


public record BlockRecord(
        String challenge_block_info_hash,
        Output challenge_vdf_output,
        int deficit,
        String farmer_puzzle_hash,
        long fees,
        List<String> finished_challenge_slot_hashes,
        List<String> finished_infused_challenge_slot_hashes,
        List<String> finished_reward_slot_hashes,
        String header_hash,
        int height,
        Output infused_challenge_vdf_output,
        boolean overflow,
        String pool_puzzle_hash,
        String prev_hash,
        String prev_transaction_block_hash,
        int prev_transaction_block_height,
        long required_iters,
        List<Coin> reward_claims_incorporated,
        String reward_infusion_new_challenge,
        int signage_point_index,
        boolean sub_epoch_summary_included,
        long sub_slot_iters,
        int timestamp,
        long total_iters,
        long weight
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.BLOCK_RECORD;
    }


    public BlockRecord {
        finished_challenge_slot_hashes = finished_challenge_slot_hashes() == null
                ? List.of()
                : Collections.unmodifiableList(finished_challenge_slot_hashes);

        finished_infused_challenge_slot_hashes = finished_infused_challenge_slot_hashes == null
                ? List.of() :
                Collections.unmodifiableList(finished_infused_challenge_slot_hashes);

        finished_reward_slot_hashes = finished_reward_slot_hashes == null
                ? List.of()
                : Collections.unmodifiableList(finished_reward_slot_hashes);

        reward_claims_incorporated = reward_claims_incorporated == null
                ? List.of()
                : Collections.unmodifiableList(reward_claims_incorporated);
    }
}









