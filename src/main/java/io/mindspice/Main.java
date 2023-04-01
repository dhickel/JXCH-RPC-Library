package io.mindspice;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.http.RPCClient;
import io.mindspice.http.requests.FullNodeAPI;

import io.mindspice.schemas.Objects.MempoolItem;
import io.mindspice.schemas.Objects.SpendBundle;
import io.mindspice.schemas.fullnode.AdditionsAndRemovals;
import io.mindspice.schemas.fullnode.BlockCountMetrics;
import io.mindspice.util.RPCException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException, RPCException {
        YAMLMapper mapper = new YAMLMapper();
        NodeConfig nodeConfig = mapper.readValue(
                new File("/home/mindspice/code/Java/Projects/Fields/fields-monitor-server/chia-java-rpc-lib/config.yaml"),
                NodeConfig.class);

        RPCClient rpcClient = new RPCClient(nodeConfig);
        FullNodeAPI node = new FullNodeAPI(rpcClient);
        //0x69f9ac19521afef52745eedf81f000eb55d74a5c4aa493a83721b87ef3c71b1f
//        System.out.println(node.getCoinRecordsByPuzzleHashes(
//                Collections.singletonList("0x29fcff2cf974a34fb86d32883e7859bd37266db5f6e6f0042dd8e785327606c3"),
//                3436588,
//                3436590,
//                true
//
//                ));
//    }

        var s = "{\n" +
                "  \"spend_bundle\": {\n" +
                "    \"aggregated_signature\": \"0xa5e5ea1f5ae2335a72fe0a7ed7ca39e8f142e2e1f6e37a348482290e88eb9cea2d973acf6145e34d0afeee7ba22f99850641e21a549b2c092bb49aa393acd938825bccca9413c1a268ba44367bc8433cd0fc0eb82e87bebe23817aa695bdb566\",\n" +
                "    \"coin_spends\": [\n" +
                "      {\n" +
                "        \"coin\": {\n" +
                "          \"amount\": 1750000000000,\n" +
                "          \"parent_coin_info\": \"0xccd5bb71183532bff220ba46c268991a00000000000000000000000000004082\",\n" +
                "          \"puzzle_hash\": \"0x94c6db00186900418ef7c1f05e127ee1a647cbe6e514ec3bc57acb7bbe6dfb10\"\n" +
                "        },\n" +
                "        \"puzzle_reveal\": \"0xff02ffff01ff02ffff01ff02ffff03ff0bffff01ff02ffff03ffff09ff05ffff1dff0bffff1effff0bff0bffff02ff06ffff04ff02ffff04ff17ff8080808080808080ffff01ff02ff17ff2f80ffff01ff088080ff0180ffff01ff04ffff04ff04ffff04ff05ffff04ffff02ff06ffff04ff02ffff04ff17ff80808080ff80808080ffff02ff17ff2f808080ff0180ffff04ffff01ff32ff02ffff03ffff07ff0580ffff01ff0bffff0102ffff02ff06ffff04ff02ffff04ff09ff80808080ffff02ff06ffff04ff02ffff04ff0dff8080808080ffff01ff0bffff0101ff058080ff0180ff018080ffff04ffff01b0aec9c2e5984fe928406abca942d55ec6b56340af8315bfefa55889dbaade669b9fd3f330af2af44c2a0626d383e64757ff018080\",\n" +
                "        \"solution\": \"0xff80ffff01ffff33ffa03fa549a708302b401c45cf387f8f03b4f76b7c9eabf567bea974f61dedf721e0ff840098968080ffff33ffa055b9fe4c9ce0cef8ad574bf5a9158dc0db7848b96be1a98ab2806d8f0a376a08ff860197738845808080ff8080\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"cost\": 0,\n" +
                "  \"target_times\": [\n" +
                "    0\n" +
                "  ],\n" +
                "  \"spend_type\": 0,\n" +
                "  \"spend_count\": 1\n" +
                "}";

        System.out.println(node.getAllMempoolItems());
    }
}