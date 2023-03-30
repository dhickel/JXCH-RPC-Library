package io.mindspice;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.Multi;
import io.mindspice.http.RPCClient;
import io.mindspice.http.requests.FullNodeAPI;
import io.mindspice.schemas.fullnode.Blocks;
import io.mindspice.util.RPCException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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

        System.out.println(node.getBlockRecords(5,10));

    }
}