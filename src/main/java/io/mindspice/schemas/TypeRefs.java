package io.mindspice.schemas;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mindspice.schemas.fullnode.Block;

import java.util.List;


public class TypeRefs {
    public static final TypeReference<List<Block>> BLOCK_LIST = new TypeReference<List<Block>>() { };
}
