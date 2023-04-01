package io.mindspice.schemas;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mindspice.schemas.Objects.*;

import java.util.HashMap;
import java.util.List;


public class TypeRefs {
    public static final TypeReference<List<BlockRecord>> BLOCK_RECORD_LIST = new TypeReference<>() { };
    public static final TypeReference<List<Block>> BLOCK_LIST = new TypeReference<>() { };
    public static final TypeReference<List<CoinRecord>> COIN_RECORD_LIST = new TypeReference<>() { };
    public static final TypeReference<List<CoinSolution>> COIN_SOLUTION_LIST = new TypeReference<>() { };
    public static final TypeReference<List<BlockHeader>> BLOCK_HEADER_LIST = new TypeReference<>() { };
    public static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() { };
    public static final TypeReference<HashMap<String, MempoolItem>> MEMPOOL_HASHMAP = new TypeReference<>() { };
}
