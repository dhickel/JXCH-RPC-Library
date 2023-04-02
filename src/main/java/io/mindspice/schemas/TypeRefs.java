package io.mindspice.schemas;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mindspice.schemas.farmer.*;
import io.mindspice.schemas.object.*;

import java.util.List;
import java.util.Map;


public class TypeRefs {
    public static final TypeReference<List<BlockRecord>> BLOCK_RECORD_LIST = new TypeReference<>() { };
    public static final TypeReference<List<Block>> BLOCK_LIST = new TypeReference<>() { };
    public static final TypeReference<List<CoinRecord>> COIN_RECORD_LIST = new TypeReference<>() { };
    public static final TypeReference<List<CoinSolution>> COIN_SOLUTION_LIST = new TypeReference<>() { };
    public static final TypeReference<List<BlockHeader>> BLOCK_HEADER_LIST = new TypeReference<>() { };
    public static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() { };
    public static final TypeReference<Map<String, MempoolItem>> MEMPOOL_MAP = new TypeReference<>() { };
    public static final TypeReference<List<Plot>> PLOT_LIST = new TypeReference<>() { };
    public static final TypeReference<List<HarvesterDetails>> HARVESTER_DETAILS_LIST = new TypeReference<>() { };
    public static final TypeReference<List<HarvesterSummary>> HARVESTER_SUMMARY_LIST = new TypeReference<>() { };
    public static final TypeReference<List<PoolState>> POOL_STATE_LIST = new TypeReference<>() { };
    public static final TypeReference<List<RewardTarget>> REWARD_TARGET_LIST = new TypeReference<>() { };
}
