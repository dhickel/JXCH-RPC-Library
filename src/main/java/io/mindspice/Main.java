package io.mindspice;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.mindspice.enums.ChiaService;
import io.mindspice.http.RPCClient;
import io.mindspice.http.requests.DaemonClient;
import io.mindspice.http.requests.FullNodeAPI;
import io.mindspice.util.RPCException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws IOException, RPCException {
        YAMLMapper mapper = new YAMLMapper();
        NodeConfig nodeConfig = mapper.readValue(
                new File("/home/mindspice/code/Java/Projects/Fields/fields-monitor-server/chia-java-rpc-lib/config.yaml"),
                NodeConfig.class);

        RPCClient rpcClient = new RPCClient(nodeConfig);
        FullNodeAPI node = new FullNodeAPI(rpcClient);

        System.out.println(node.getBlock("0x74a4cef4ab25ef67164138318d940ab7875348d740757ecaebf2980b730668eb"));

    }
}