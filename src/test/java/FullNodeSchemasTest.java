import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.jxch.rpc.schemas.fullnode.*;
import io.mindspice.jxch.rpc.schemas.object.*;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FullNodeSchemasTest {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
    private static final BigInteger U64 = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
    private static final BigInteger U128 = BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE);
    private static final long U32 = 4_294_967_295L;

    // Field sets and tuple shapes verified against Chia 2.7.5 and chia_rs 0.48.0 to_json_dict().
    private static final String COIN = """
            {"parent_coin_info":"0x01","puzzle_hash":"0x02","amount":18446744073709551615}
            """;
    private static final String COIN_SPEND = """
            {"coin":%s,"puzzle_reveal":"0x80","solution":"0xff8080"}
            """.formatted(COIN);
    private static final String COIN_RECORD = """
            {"coin":%s,"confirmed_block_index":4294967295,"spent_block_index":4294967295,
             "coinbase":false,"timestamp":18446744073709551615,"spent":true}
            """.formatted(COIN);
    private static final String OUTPUT = "{\"data\":\"0x03\"}";
    private static final String VDF_JSON = """
            {"challenge":"0x04","number_of_iterations":18446744073709551615,"output":%s}
            """.formatted(OUTPUT);
    private static final String PROOF = "{\"witness_type\":255,\"witness\":\"0x05\",\"normalized_to_identity\":true}";
    private static final String POS = """
            {"challenge":"0x06","pool_public_key":null,"pool_contract_puzzle_hash":"0x07",
             "plot_public_key":"0x08","version":1,"plot_index":65535,"meta_group":255,
             "strength":255,"size":32,"proof":"0x09"}
            """;
    private static final String SUMMARY = """
            {"prev_subepoch_summary_hash":"0x10","reward_chain_hash":"0x11","num_blocks_overflow":255,
             "new_difficulty":18446744073709551615,"new_sub_slot_iters":18446744073709551615,
             "challenge_merkle_root":"0x12"}
            """;
    private static final String CHALLENGE_CHAIN = """
            {"challenge_chain_end_of_slot_vdf":%s,"infused_challenge_chain_sub_slot_hash":null,
             "subepoch_summary_hash":"0x13","new_sub_slot_iters":18446744073709551615,
             "new_difficulty":18446744073709551615}
            """.formatted(VDF_JSON);
    private static final String INFUSED_CHAIN = "{\"infused_challenge_chain_end_of_slot_vdf\":" + VDF_JSON + "}";
    private static final String REWARD_CHAIN = """
            {"end_of_slot_vdf":%s,"challenge_chain_sub_slot_hash":"0x14",
             "infused_challenge_chain_sub_slot_hash":null,"deficit":255}
            """.formatted(VDF_JSON);
    private static final String SUB_SLOT_PROOFS = """
            {"challenge_chain_slot_proof":%s,"infused_challenge_chain_slot_proof":null,"reward_chain_slot_proof":%s}
            """.formatted(PROOF, PROOF);
    private static final String SUB_SLOT = """
            {"challenge_chain":%s,"infused_challenge_chain":%s,"reward_chain":%s,"proofs":%s}
            """.formatted(CHALLENGE_CHAIN, INFUSED_CHAIN, REWARD_CHAIN, SUB_SLOT_PROOFS);
    private static final String POOL_TARGET = "{\"puzzle_hash\":\"0x15\",\"max_height\":4294967295}";
    private static final String FOLIAGE_DATA = """
            {"unfinished_reward_block_hash":"0x16","pool_target":%s,"pool_signature":null,
             "farmer_reward_puzzle_hash":"0x17","extension_data":"0x18"}
            """.formatted(POOL_TARGET);
    private static final String FOLIAGE = """
            {"prev_block_hash":"0x19","reward_block_hash":"0x20","foliage_block_data":%s,
             "foliage_block_data_signature":"0x21","foliage_transaction_block_hash":null,
             "foliage_transaction_block_signature":null}
            """.formatted(FOLIAGE_DATA);
    private static final String FOLIAGE_TX = """
            {"prev_transaction_block_hash":"0x22","timestamp":18446744073709551615,
             "filter_hash":"0x23","additions_root":"0x24","removals_root":"0x25","transactions_info_hash":"0x26"}
            """;
    private static final String TX_INFO = """
            {"generator_root":"0x27","generator_refs_root":"0x28","aggregated_signature":"0x29",
             "fees":18446744073709551615,"cost":18446744073709551615,"reward_claims_incorporated":[%s]}
            """.formatted(COIN);
    private static final String REWARD_BLOCK = """
            {"weight":340282366920938463463374607431768211455,"height":4294967295,
             "total_iters":340282366920938463463374607431768211455,"signage_point_index":255,
             "pos_ss_cc_challenge_hash":"0x30","proof_of_space":%s,"challenge_chain_sp_vdf":null,
             "challenge_chain_sp_signature":"0x31","challenge_chain_ip_vdf":%s,"reward_chain_sp_vdf":null,
             "reward_chain_sp_signature":"0x32","reward_chain_ip_vdf":%s,
             "infused_challenge_chain_ip_vdf":%s,"header_mmr_root":"0x33","is_transaction_block":true}
            """.formatted(POS, VDF_JSON, VDF_JSON, VDF_JSON);
    private static final String UNFINISHED_REWARD_BLOCK = """
            {"total_iters":340282366920938463463374607431768211455,"signage_point_index":255,
             "pos_ss_cc_challenge_hash":"0x34","proof_of_space":%s,"challenge_chain_sp_vdf":null,
             "challenge_chain_sp_signature":"0x35","reward_chain_sp_vdf":null,"reward_chain_sp_signature":"0x36"}
            """.formatted(POS);
    private static final String BLOCK = """
            {"header_hash":"0x37","finished_sub_slots":[%s],"reward_chain_block":%s,
             "challenge_chain_sp_proof":null,"challenge_chain_ip_proof":%s,"reward_chain_sp_proof":null,
             "reward_chain_ip_proof":%s,"infused_challenge_chain_ip_proof":null,"foliage":%s,
             "foliage_transaction_block":%s,"transactions_info":%s,"transactions_generator":"0x80",
             "transactions_generator_ref_list":[0,4294967295],"transactions_generator_buffer":"0xff0180","version":1}
            """.formatted(SUB_SLOT, REWARD_BLOCK, PROOF, PROOF, FOLIAGE, FOLIAGE_TX, TX_INFO);
    private static final String HEADER = """
            {"finished_sub_slots":[%s],"reward_chain_block":%s,"challenge_chain_sp_proof":null,
             "reward_chain_sp_proof":null,"foliage":%s,"foliage_transaction_block":null,"transactions_filter":""}
            """.formatted(SUB_SLOT, UNFINISHED_REWARD_BLOCK, FOLIAGE);
    private static final String BLOCK_RECORD = """
            {"header_hash":"0x38","prev_hash":"0x39","height":4294967295,
             "weight":340282366920938463463374607431768211455,"total_iters":340282366920938463463374607431768211455,
             "signage_point_index":255,"challenge_vdf_output":%s,"infused_challenge_vdf_output":null,
             "reward_infusion_new_challenge":"0x40","challenge_block_info_hash":"0x41",
             "sub_slot_iters":18446744073709551615,"pool_puzzle_hash":"0x42","farmer_puzzle_hash":"0x43",
             "required_iters":18446744073709551615,"deficit":255,"overflow":true,
             "prev_transaction_block_height":4294967295,"timestamp":null,"prev_transaction_block_hash":null,
             "fees":null,"reward_claims_incorporated":null,"finished_challenge_slot_hashes":null,
             "finished_infused_challenge_slot_hashes":null,"finished_reward_slot_hashes":null,
             "sub_epoch_summary_included":%s}
            """.formatted(OUTPUT, SUMMARY);
    private static final String SPEND = """
            {"coin_id":"0x44","parent_id":"0x45","puzzle_hash":"0x46","coin_amount":18446744073709551615,
             "height_relative":null,"seconds_relative":null,"before_height_relative":4294967295,
             "before_seconds_relative":18446744073709551615,"birth_height":null,"birth_seconds":null,
             "create_coin":[["0x47",18446744073709551615,null],["0x48",0,"0x6869"]],
             "agg_sig_me":[["0x49","0x50"]],"agg_sig_parent":[["0x51","0x52"]],
             "agg_sig_puzzle":[["0x53","0x54"]],"agg_sig_amount":[["0x55","0x56"]],
             "agg_sig_puzzle_amount":[["0x57","0x58"]],"agg_sig_parent_amount":[["0x59","0x60"]],
             "agg_sig_parent_puzzle":[["0x61","0x62"]],"flags":4294967295,
             "execution_cost":18446744073709551615,"condition_cost":18446744073709551615,
             "atom_count":18446744073709551615,"pair_count":18446744073709551615,"fingerprint":"0x63"}
            """;
    private static final String CONDITIONS = """
            {"spends":[%s],"reserve_fee":18446744073709551615,"height_absolute":4294967295,
             "seconds_absolute":18446744073709551615,"before_height_absolute":null,"before_seconds_absolute":null,
             "agg_sig_unsafe":[["0x64","0x65"]],"cost":18446744073709551615,
             "removal_amount":340282366920938463463374607431768211455,
             "addition_amount":340282366920938463463374607431768211455,"validated_signature":true,
             "execution_cost":18446744073709551615,"condition_cost":18446744073709551615,
             "num_atoms":4294967295,"num_pairs":4294967295,"heap_size":4294967295}
            """.formatted(SPEND);
    private static final String NPC_RESULT = "{\"Error\":null,\"conds\":" + CONDITIONS + "}";
    private static final String BUNDLE = "{\"coin_spends\":[" + COIN_SPEND + "],\"aggregated_signature\":\"0x66\"}";
    private static final String MEMPOOL = """
            {"spend_bundle":%s,"fee":18446744073709551615,"npc_result":%s,"cost":18446744073709551615,
             "spend_bundle_name":"0x67","additions":[%s],"removals":[%s]}
            """.formatted(BUNDLE, NPC_RESULT, COIN, COIN);
    private static final String SIGNAGE = """
            {"cc_vdf":%s,"cc_proof":%s,"rc_vdf":%s,"rc_proof":%s}
            """.formatted(VDF_JSON, PROOF, VDF_JSON, PROOF);
    private static final String SYNC = """
            {"sync_mode":false,"synced":true,"sync_tip_height":4294967295,"sync_progress_height":4294967295}
            """;
    private static final String STATE = """
            {"node_id":"0x68","peak":%s,"genesis_challenge_initialized":true,"sync":%s,
             "difficulty":18446744073709551615,"sub_slot_iters":18446744073709551615,
             "space":340282366920938463463374607431768211455,"average_block_time":18.75,
             "mempool_size":4294967295,"mempool_cost":18446744073709551615,
             "mempool_fees":18446744073709551615,"mempool_min_fees":{"cost_5000000":0.125},
             "mempool_max_total_cost":18446744073709551615,"block_max_cost":18446744073709551615}
            """.formatted(BLOCK_RECORD, SYNC);
    private static final String FEE = """
            {"estimates":[0,18446744073709551615],"target_times":[0,4294967295],"current_fee_rate":0.125,
             "mempool_size":18446744073709551615,"mempool_fees":18446744073709551615,
             "num_spends":4294967295,"mempool_max_size":18446744073709551615,"full_node_synced":true,
             "peak_height":4294967295,"last_peak_timestamp":18446744073709551615,
             "node_time_utc":18446744073709551615,"last_block_cost":18446744073709551615,
             "fees_last_block":18446744073709551615,"fee_rate_last_block":0.375,
             "last_tx_block_height":4294967295,"success":true,"error":null}
            """;
    private static final String GENERATOR = """
            {"generator":"0xff0180","refs":[0,4294967295],"additions":[%s],"removals":[%s],
             "sig":"0x69","cost":18446744073709551615}
            """.formatted(COIN, COIN);
    private static final String SPEND_WITH_CONDITIONS = """
            {"coin_spend":%s,"conditions":[[51,["0x02","0x00"]],[66,["0x24","0x68656c6c6f","0x01"]]]}
            """.formatted(COIN_SPEND);

    @TestFactory
    Stream<DynamicTest> everyStableSchemaRetainsItsWireFieldsAndTypes() {
        Map<Class<?>, String> fixtures = Map.ofEntries(
                Map.entry(Coin.class, COIN), Map.entry(CoinRecord.class, COIN_RECORD),
                Map.entry(CoinSpend.class, COIN_SPEND), Map.entry(CoinSolution.class, COIN_SPEND),
                Map.entry(BlockSpend.class, COIN_SPEND), Map.entry(Output.class, OUTPUT),
                Map.entry(VDF.class, VDF_JSON), Map.entry(Proof.class, PROOF), Map.entry(ProofOfSpace.class, POS),
                Map.entry(SubEpochSummary.class, SUMMARY), Map.entry(ChallengeChain.class, CHALLENGE_CHAIN),
                Map.entry(InfusedChallengeChain.class, INFUSED_CHAIN), Map.entry(RewardChain.class, REWARD_CHAIN),
                Map.entry(SubSlotProofs.class, SUB_SLOT_PROOFS), Map.entry(SubSlot.class, SUB_SLOT),
                Map.entry(PoolTarget.class, POOL_TARGET), Map.entry(FoliageBlockData.class, FOLIAGE_DATA),
                Map.entry(Foliage.class, FOLIAGE), Map.entry(FoliageTransactionBlock.class, FOLIAGE_TX),
                Map.entry(TransactionInfo.class, TX_INFO), Map.entry(RewardChainBlock.class, REWARD_BLOCK),
                Map.entry(RewardChainBlockUnfinished.class, UNFINISHED_REWARD_BLOCK), Map.entry(Block.class, BLOCK),
                Map.entry(BlockHeader.class, HEADER), Map.entry(BlockRecord.class, BLOCK_RECORD),
                Map.entry(BlockChainState.Peak.class, BLOCK_RECORD), Map.entry(Spend.class, SPEND),
                Map.entry(Conditions.class, CONDITIONS), Map.entry(NpcResult.class, NPC_RESULT),
                Map.entry(SpendBundle.class, BUNDLE), Map.entry(MempoolItem.class, MEMPOOL),
                Map.entry(SignagePoint.class, SIGNAGE), Map.entry(BlockChainState.class, STATE),
                Map.entry(BlockChainState.Sync.class, SYNC),
                Map.entry(BlockChainState.MemPoolFees.class, "{\"cost_5000000\":0.125}"),
                Map.entry(FeeEstimate.class, FEE), Map.entry(BlockGenerator.class, GENERATOR),
                Map.entry(BlockSpendWithConditions.class, SPEND_WITH_CONDITIONS),
                Map.entry(Network.class, "{\"network_name\":\"mainnet\",\"network_prefix\":\"xch\",\"genesis_challenge\":\"0x01\"}"),
                Map.entry(BlockCountMetrics.class, "{\"compact_blocks\":4294967295,\"uncompact_blocks\":4294967295,\"hint_count\":4294967295}"),
                Map.entry(AdditionsAndRemovals.class, "{\"additions\":[" + COIN_RECORD + "],\"removals\":[],\"success\":true,\"error\":null}"),
                Map.entry(SignagePointOrEOS.class, "{\"time_received\":1700000000.125,\"reverted\":false,\"success\":true,\"error\":null,\"signage_point\":" + SIGNAGE + ",\"eos\":null}")
        );
        return fixtures.entrySet().stream().map(e -> DynamicTest.dynamicTest(e.getKey().getSimpleName(), () -> {
            Object parsed = MAPPER.readValue(e.getValue(), e.getKey());
            assertEquals(MAPPER.readTree(e.getValue()), MAPPER.readTree(MAPPER.writeValueAsBytes(parsed)));
        }));
    }

    @Test
    void networkAndBlockPrecisionIsNotLimitedBySignedJavaIntegers() throws Exception {
        BlockChainState state = MAPPER.readValue(STATE, BlockChainState.class);
        assertEquals(U128, state.space());
        assertEquals(U64, state.difficulty());
        assertEquals(U128, state.peak().totalIters());
        assertEquals(U128, state.peak().weight());
        assertEquals(U32, state.peak().height());
        assertEquals(U64, state.peak().requiredIters());
        assertEquals(U64, state.peak().subEpochSummaryIncluded().newDifficulty());
        assertEquals(0.125, state.mempoolMinFees().cost_5000000());
        assertEquals(18.75, state.averageBlockTime());
        Block block = MAPPER.readValue(BLOCK, Block.class);
        assertEquals(U128, block.rewardChainBlock().weight());
        assertEquals(U64, block.rewardChainBlock().infusedChallengeChainIpVdf().numberOfIterations());
        assertEquals(List.of(0L, U32), block.transactionsGeneratorRefList());
        assertEquals(U64, block.transactionsInfo().fees());
        assertEquals(U64, block.transactionsInfo().rewardClaimsIncorporated().get(0).amount());
    }

    @Test
    void optionalBlockRecordFieldsRemainNullRatherThanFalseZeroOrEmpty() throws Exception {
        ObjectNode json = (ObjectNode) MAPPER.readTree(BLOCK_RECORD);
        json.putNull("sub_epoch_summary_included");
        BlockRecord record = MAPPER.treeToValue(json, BlockRecord.class);
        BlockChainState.Peak peak = MAPPER.treeToValue(json, BlockChainState.Peak.class);
        assertNull(record.subEpochSummaryIncluded());
        assertNull(record.timestamp());
        assertNull(record.fees());
        assertNull(record.rewardClaimsIncorporated());
        assertNull(record.finishedChallengeSlotHashes());
        assertNull(record.finishedInfusedChallengeSlotHashes());
        assertNull(record.finishedRewardSlotHashes());
        assertNull(peak.timestamp());
        assertNull(peak.fees());
        assertNull(peak.rewardClaimsIncorporated());
        assertNull(peak.subEpochSummaryIncluded());
        json.put("timestamp", U64).put("fees", U64);
        json.putArray("reward_claims_incorporated");
        json.putArray("finished_challenge_slot_hashes");
        record = MAPPER.treeToValue(json, BlockRecord.class);
        assertEquals(U64, record.timestamp());
        assertEquals(U64, record.fees());
        assertEquals(List.of(), record.rewardClaimsIncorporated());
        assertEquals(List.of(), record.finishedChallengeSlotHashes());
    }

    @Test
    void uninitializedNodePreservesNullableAveragePeakAndAbsentMempoolFees() throws Exception {
        ObjectNode json = (ObjectNode) MAPPER.readTree(STATE);
        json.putNull("peak").putNull("average_block_time").remove("mempool_fees");
        BlockChainState state = MAPPER.treeToValue(json, BlockChainState.class);
        assertNull(state.peak());
        assertNull(state.averageBlockTime());
        assertNull(state.mempoolFees());
    }

    @Test
    void mempoolConditionsIncludeAllSignaturesTypedCreateCoinAndNullableDeadlines() throws Exception {
        MempoolItem item = MAPPER.readValue(MEMPOOL, MempoolItem.class);
        assertNull(item.heightAddedToMempool());
        assertNull(item.npcResult().error());
        Conditions conditions = item.npcResult().conds();
        assertEquals(U128, conditions.additionAmount());
        assertEquals(U128, conditions.removalAmount());
        assertEquals(List.of(List.of("0x64", "0x65")), conditions.aggSigUnsafe());
        assertNull(conditions.beforeHeightAbsolute());
        assertNull(conditions.beforeSecondsAbsolute());
        assertTrue(conditions.validatedSignature());
        assertEquals(U32, conditions.numAtoms());
        Spend spend = conditions.spends().get(0);
        assertEquals(U64, spend.coinAmount());
        assertEquals("0x45", spend.parentId());
        assertNull(spend.heightRelative());
        assertNull(spend.secondsRelative());
        assertNull(spend.birthHeight());
        assertNull(spend.birthSeconds());
        assertEquals(U32, spend.beforeHeightRelative());
        assertEquals(U64, spend.beforeSecondsRelative());
        assertTrue(spend.createCoin().get(0).get(1).isIntegralNumber());
        assertEquals(U64, spend.createCoin().get(0).get(1).bigIntegerValue());
        assertTrue(spend.createCoin().get(0).get(2).isNull());
        assertEquals("0x6869", spend.createCoin().get(1).get(2).textValue());
        for (var signatures : List.of(spend.aggSigMe(), spend.aggSigParent(), spend.aggSigPuzzle(),
                spend.aggSigAmount(), spend.aggSigPuzzleAmount(), spend.aggSigParentAmount(), spend.aggSigParentPuzzle())) {
            assertEquals(2, signatures.get(0).size());
        }
        assertEquals(U64, spend.atomCount());
        assertEquals(U64, spend.pairCount());
        assertFalse(MAPPER.valueToTree(item).has("height_added_to_mempool"));
        assertFalse(MAPPER.valueToTree(item.npcResult()).has("cost"));
    }

    @Test
    void zeroConditionBoundsAreDistinctFromNull() throws Exception {
        ObjectNode spendJson = (ObjectNode) MAPPER.readTree(SPEND);
        for (String field : List.of("height_relative", "seconds_relative", "before_height_relative",
                "before_seconds_relative", "birth_height", "birth_seconds")) {
            spendJson.put(field, 0);
        }
        Spend spend = MAPPER.treeToValue(spendJson, Spend.class);
        assertEquals(0L, spend.heightRelative());
        assertEquals(BigInteger.ZERO, spend.secondsRelative());
        assertEquals(0L, spend.beforeHeightRelative());
        assertEquals(BigInteger.ZERO, spend.beforeSecondsRelative());
        assertEquals(0L, spend.birthHeight());
        assertEquals(BigInteger.ZERO, spend.birthSeconds());
        ObjectNode conditionsJson = (ObjectNode) MAPPER.readTree(CONDITIONS);
        conditionsJson.put("before_height_absolute", 0).put("before_seconds_absolute", 0);
        Conditions conditions = MAPPER.treeToValue(conditionsJson, Conditions.class);
        assertEquals(0L, conditions.beforeHeightAbsolute());
        assertEquals(BigInteger.ZERO, conditions.beforeSecondsAbsolute());
    }

    @Test
    void feeEstimatesAreUint64ButRatesRemainFractional() throws Exception {
        FeeEstimate fee = MAPPER.readValue(FEE, FeeEstimate.class);
        assertEquals(List.of(BigInteger.ZERO, U64), fee.estimates());
        assertEquals(List.of(BigInteger.ZERO, BigInteger.valueOf(U32)), fee.targetTimes());
        assertEquals(0.125, fee.currentFeeRate());
        assertEquals(0.375, fee.feeRateLastBlock());
        assertEquals(U64, fee.lastPeakTimestamp());
        assertEquals(U64, fee.nodeTimeUtc());
        assertEquals(U32, fee.lastTxBlockHeight());
    }

    @Test
    void puzzleSolutionAndConditionResponseAreNotSpendBundles() throws Exception {
        CoinSolution solution = MAPPER.treeToValue(MAPPER.readTree("{\"coin_solution\":" + COIN_SPEND + "}")
                .get("coin_solution"), CoinSolution.class);
        assertEquals(U64, solution.coin().amount());
        assertEquals("0xff8080", solution.solution());
        BlockSpendWithConditions spend = MAPPER.readValue(SPEND_WITH_CONDITIONS, BlockSpendWithConditions.class);
        assertEquals(solution, spend.coinSpend());
        assertEquals(66, spend.conditions().get(1).get(0).intValue());
        assertEquals("0x68656c6c6f", spend.conditions().get(1).get(1).get(1).textValue());
        assertTrue(spend.conditions().get(0).isArray());
        assertEquals(2, spend.conditions().get(0).size());
        assertEquals(List.of(), MAPPER.readValue("{\"coin_spend\":null,\"conditions\":[]}",
                BlockSpendWithConditions.class).conditions());
    }

    @Test
    void generatorUsesHeightRefsAndHandlesEmptyOrNullablePrograms() throws Exception {
        BlockGenerator generator = MAPPER.readValue(GENERATOR, BlockGenerator.class);
        assertEquals(List.of(0L, U32), generator.refs());
        assertEquals(U64, generator.cost());
        assertEquals(U64, generator.additions().get(0).amount());
        for (String program : List.of("null", "\"0x80\"")) {
            BlockGenerator empty = MAPPER.readValue("{\"generator\":" + program
                    + ",\"refs\":[],\"additions\":[],\"removals\":[],\"sig\":\"0xc0\",\"cost\":0}", BlockGenerator.class);
            assertEquals(List.of(), empty.refs());
            assertEquals(List.of(), empty.additions());
            assertEquals(List.of(), empty.removals());
            assertEquals(BigInteger.ZERO, empty.cost());
        }
    }

    @Test
    void nonTransactionBlocksAndUnfinishedHeadersHaveCorrectTypesAndNulls() throws Exception {
        ObjectNode json = (ObjectNode) MAPPER.readTree(BLOCK);
        for (String field : List.of("foliage_transaction_block", "transactions_info", "transactions_generator",
                "transactions_generator_buffer", "infused_challenge_chain_ip_proof")) {
            json.putNull(field);
        }
        ((ObjectNode) json.get("reward_chain_block")).putNull("header_mmr_root")
                .putNull("infused_challenge_chain_ip_vdf").put("is_transaction_block", false);
        json.putArray("transactions_generator_ref_list");
        Block block = MAPPER.treeToValue(json, Block.class);
        assertNull(block.transactionsGenerator());
        assertNull(block.transactionsInfo());
        assertNull(block.foliageTransactionBlock());
        assertNull(block.rewardChainBlock().infusedChallengeChainIpVdf());
        assertNull(block.rewardChainBlock().headerMmrRoot());
        BlockHeader header = MAPPER.readValue(HEADER, BlockHeader.class);
        assertInstanceOf(RewardChainBlockUnfinished.class, header.rewardChainBlock());
        assertEquals(U128, header.rewardChainBlock().totalIters());
        assertFalse(MAPPER.valueToTree(header.rewardChainBlock()).has("height"));
        assertFalse(MAPPER.valueToTree(header.rewardChainBlock()).has("weight"));
        assertNull(header.foliageTransactionBlock());
        assertNull(header.challengeChainSpProof());
    }

    @Test
    void endOfSlotUsesProofsWireKeyAndFractionalReceiptTime() throws Exception {
        SignagePointOrEOS eos = MAPPER.readValue("{\"eos\":" + SUB_SLOT
                + ",\"time_received\":1700000000.125,\"reverted\":true}", SignagePointOrEOS.class);
        assertEquals(1_700_000_000.125, eos.timeReceived());
        assertNull(eos.signagePoint());
        assertNotNull(eos.eos().subSlotProofs().challengeChainSlotProof());
        assertNotNull(MAPPER.valueToTree(eos.eos()).get("proofs"));
        assertFalse(MAPPER.valueToTree(eos.eos()).has("sub_slot_proofs"));
    }

    @Test
    @SuppressWarnings("deprecation")
    void legacyConstructorsAndAliasesRemainAvailableWithoutInventingSummaryData() throws Exception {
        Coin coin = new Coin("p", "h", 42L);
        assertEquals(BigInteger.valueOf(42), coin.amount());
        assertEquals(BigInteger.valueOf(43), new VDF("h", 43L, new Output("o")).numberOfIterations());
        assertEquals(BigInteger.ONE, new ChallengeChain(null, null, 1, null, null).newDifficulty());
        assertNull(new ChallengeChain(null, null, null, null, null).newDifficulty());
        assertEquals(BigInteger.TEN, new TransactionInfo("a", "b", "s", "10", "11", null).fees());
        assertEquals(0, new ProofOfSpace("c", "pk", null, null, "p", 32).version());
        assertEquals(BigInteger.valueOf(44), new CoinRecord(coin, 1, 0, false, false, 44L).timestamp());
        assertEquals(BigInteger.valueOf(45), new FoliageTransactionBlock("p", 45L, "f", "a", "r", "t").timestamp());
        Spend legacySpend = new Spend(List.of(), 0, 0, 0, 0, "c",
                List.of(Arrays.asList("p", "18446744073709551615", null)), 0, 0, "p", 0);
        assertEquals(U64, legacySpend.createCoin().get(0).get(1).bigIntegerValue());
        assertTrue(legacySpend.createCoin().get(0).get(2).isNull());
        assertEquals(BigInteger.ONE, new Conditions(1, List.of(), 0, 0, 1, 0, 1, 0, 0, List.of()).additionAmount());
        assertThrows(IllegalArgumentException.class,
                () -> new Conditions(1, List.of("ambiguous"), 0, 0, 1, 0, 1, 0, 0, List.of()));
        SpendBundle alias = MAPPER.readValue(BUNDLE.replace("coin_spends", "coin_solutions"), SpendBundle.class);
        assertEquals(1, alias.coinSpends().size());
        SpendBundleOld old = new SpendBundleOld(alias);
        assertEquals(alias, new SpendBundle(old));
        SpendBundleOld oldParsed = MAPPER.readValue(BUNDLE.replace("coin_spends", "coin_solutions"), SpendBundleOld.class);
        assertEquals(alias.coinSpends(), oldParsed.coinSolutions());
        assertEquals("7", MAPPER.readValue("{\"error\":7}", NpcResult.class).error());
        assertNotNull(MAPPER.readValue(SUB_SLOT.replace("\"proofs\"", "\"sub_slot_proofs\""), SubSlot.class).subSlotProofs());
        Block oldBlock = new Block(null, null, null, null, null, null, null, null, null, null,
                null, List.of("4294967295"), null);
        assertEquals(List.of(U32), oldBlock.transactionsGeneratorRefList());
        BlockRecord record = new BlockRecord(null, null, 0, null, 0, null, null, null, null,
                0, null, false, null, null, null, 0, 0, null, null, 0, false, 0, 0, 0, 0);
        assertNull(record.subEpochSummaryIncluded());
        assertThrows(IllegalArgumentException.class, () -> new BlockRecord(null, null, 0, null, 0,
                null, null, null, null, 0, null, false, null, null, null, 0, 0, null, null, 0, true, 0, 0, 0, 0));
        FeeEstimate legacyFee = new FeeEstimate(1, List.of(0), true, 1, 2, 3, null, 4, 5, 6, 7, 8,
                true, 9, 10, 11, List.of(12));
        assertEquals(List.of(BigInteger.valueOf(12)), legacyFee.estimates());
        assertEquals(List.of(BigInteger.ZERO), legacyFee.targetTimes());
    }

    @Test
    @SuppressWarnings("deprecation")
    void remainingLegacyBlockAndMempoolConstructorsPreserveRepresentableValues() throws Exception {
        BlockChainState.Peak peak = new BlockChainState.Peak(null, null, 0, null, 1, null, null,
                null, null, 2, null, false, null, null, null, 0, 3, null, null, 0, false, 4, 5, 6, 7);
        assertEquals(BigInteger.valueOf(7), peak.weight());
        assertNull(peak.subEpochSummaryIncluded());
        assertThrows(IllegalArgumentException.class, () -> new BlockChainState.Peak(null, null, 0, null,
                0, null, null, null, null, 0, null, false, null, null, null, 0, 0, null, null, 0, true, 0, 0, 0, 0));
        BlockChainState state = new BlockChainState("n", 1, true, 2, 3, 4,
                new BlockChainState.MemPoolFees(5), 6, 7, peak, BigInteger.TEN, 8,
                new BlockChainState.Sync(false, 9, 10, true));
        assertEquals(BigInteger.valueOf(8), state.subSlotIters());
        assertNull(state.averageBlockTime());
        VDF vdf = new VDF("c", 42L, new Output("o"));
        RewardChainBlock reward = new RewardChainBlock(vdf, "cc", null, 1, vdf, true,
                "pos", null, vdf, "rc", null, 2, BigInteger.TEN, 11L);
        assertEquals(BigInteger.valueOf(11), reward.weight());
        assertEquals(vdf, reward.infusedChallengeChainIpVdf());
        BlockHeader header = new BlockHeader(null, reward, null, null, null, null, "");
        assertEquals(reward.totalIters(), header.rewardChainBlock().totalIters());
        assertEquals(reward.challengeChainSpSignature(), header.rewardChainBlock().challengeChainSpSignature());
        assertNull(new BlockHeader(null, null, null, null, null, null, null).rewardChainBlock());
        assertNull(new TransactionInfo(null, null, null, null, null, null).cost());
        MempoolItem item = new MempoolItem(null, 1, 2, null, null, 3, null, "s");
        assertEquals(3L, item.heightAddedToMempool());
        assertEquals(BigInteger.TWO, item.fee());
        assertEquals(1.0, new SignagePointOrEOS(1L, false, true, null, null, null).timeReceived());
        assertEquals(1L, new BlockCountMetrics(1, 2, 3).compactBlocks());
        assertEquals(4L, new PoolTarget(4, "p").maxHeight());
        assertEquals(U32, MAPPER.readValue(COIN_RECORD, CoinRecord.class).spentBlockIndex());
    }

    @Test
    void responseListsAreSnapshotsRatherThanViewsOfCallerLists() throws Exception {
        List<Coin> coins = new ArrayList<>();
        coins.add(new Coin("p", "h", 1));
        BlockGenerator generator = new BlockGenerator(null, List.of(), coins, coins, "s", BigInteger.ZERO);
        coins.clear();
        assertEquals(1, generator.additions().size());
        assertEquals(1, generator.removals().size());
        assertThrows(UnsupportedOperationException.class, () -> generator.additions().clear());
        Conditions conditions = MAPPER.readValue(CONDITIONS, Conditions.class);
        assertThrows(UnsupportedOperationException.class, () -> conditions.spends().clear());
        assertThrows(UnsupportedOperationException.class, () -> conditions.aggSigUnsafe().get(0).clear());
        BlockRecord record = MAPPER.readValue(BLOCK_RECORD.replace("\"reward_claims_incorporated\":null",
                "\"reward_claims_incorporated\":[" + COIN + "]"), BlockRecord.class);
        assertThrows(UnsupportedOperationException.class, () -> record.rewardClaimsIncorporated().clear());
    }
}
