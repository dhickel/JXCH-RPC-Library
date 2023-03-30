package io.mindspice.schemas.components;

import java.math.BigInteger;


public record RewardChainBlock(
        VDF challenge_chain_ip_vdf,
        String challenge_chain_sp_signature,
        VDF challenge_chain_sp_vdf,
        long height,
        Object infused_challenge_chain_ip_vdf,
        boolean is_transaction_block,
        String pos_ss_cc_challenge_hash,
        ProofOfSpace proof_of_space,
        VDF reward_chain_ip_vdf,
        String reward_chain_sp_signature,
        VDF reward_chain_sp_vdf,
        int signage_point_index,
        BigInteger total_iters,
        long weight
) { }
