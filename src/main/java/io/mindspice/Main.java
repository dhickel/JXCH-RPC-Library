package io.mindspice;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.mindspice.http.*;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.object.Output;
import io.mindspice.util.ChiaUtils;
import io.mindspice.util.RPCException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) throws IOException, RPCException {
        YAMLMapper mapper = new YAMLMapper();
        NodeConfig nodeConfig = mapper.readValue(
                new File("/home/mindspice/code/Java/Projects/Fields/fields-monitor-server/chia-java-rpc-lib/config.yaml"),
                NodeConfig.class);

        RPCClient rpcClient = new RPCClient(nodeConfig);
        FarmerAPI farmer = new FarmerAPI(rpcClient);
        var node = new FullNodeAPI(rpcClient);
        var harvester = new HarvesterAPI(rpcClient);
        var wallet = new WalletAPI(rpcClient);
        //0x69f9ac19521afef52745eedf81f000eb55d74a5c4aa493a83721b87ef3c71b1f
//        System.out.println(node.getCoinRecordsByPuzzleHashes(
//                Collections.singletonList("0x29fcff2cf974a34fb86d32883e7859bd37266db5f6e6f0042dd8e785327606c3"),
//                3436588,
//                3436590,
//                true
//
//                ));
//    }

        // harvesters
        //  0x34d24ed90f25e00f664eca457f1299ab29f824624f0498a3a624d41e9ba25a43
        //  0x8185f5b89f0e5bfa329b76fd4ec253d69e1906f3499cb579dc2149b6136265f8

        var s = "offer1qqr83wcuu2rykcmqvpsxygqqemhmlaekcenaz02ma6hs5w600dhjlvfjn477nkwz369h88kll73h37fefnwk3qqnz8s0lle09pe3gtunfrhean42k0nf0yteszz8lnw55hc7kjlmfkeuu46l8ydjyz656fc9hnflu9nlppzfuhmpc0ljd3ljnphnz38r7ky3awjp593dkkuun7ml4wuy0kgtq6crtj2zfu6lt5mwfn7at930wqklhe2m6kq82re9mlyhl6rsurs74r8anjuwqkxwejkh739cmm0lek7z5kv82ka7kzfn6mum6kzw2nsnlmw752dlp6xn63jc7sssm7vdf62gcn55ls0e62qesgfjty95hks8a9237rltk9clmsaumjdka98f97lwy0n3873rf33vtttwy7las53nlllcydgv683fat79mm8txhrqk7h5fk2rdtxj98d67ld59randkghandjltpc9aclf22ncqcjp6j8mjehtfeks7vaj4whr4dfae65kt3w7ddh9a4qa492s4wgshr73aqvd5fvp685encfm8spyluq2zmuu7re07v89jchxvxc7ldm0qff47w42uk4kfjfxaflpn7x8xf8r7pwtz7jlureyvj2fg9g4t057kf395e2jdfrx5lj9wfn8j62f5ea8nqn3hf5hr2t90e2h2j23fffy2jj34948vs2ejev5u3nfwxtyj5n3nfmyj4j90fy4uen75eqh5527hedyj52f0egkykn206hyvhnkt6cmrqd3h6u55unp2q2ajjcwq76uauzgnvhkf5l7qxer4a9n6483ktvhnrkw8qcyfaz54hlmh223j8ll7dhy600njkw9lpu5pee5u5q26japn92nnxwf49uuf2tmy52z2gxt3te5zqudcsjzzvehvtqcexgzjwunmg6uwezz364ung5kc2fdh5k4l6jzahd4j59cs0dl65zxg88v5kfp0lnw34wfjdudw648fgn96a7g235ld7ap9da80gahmmasxgddnsl3as4rld0e0d57lw35dvmaak8elxsm2460h4vetnddvt5lvped77hy70x5h4a6kx26hgmjwu5asen5aeh8fm50spalar70m5g35nl4nxag89tagatta26uh79yqw6ylnlcxylxn487gkmadjg2l7dclpg4rvxa5eaw56csvjc8xrdvtcan5p3h8grr068xc8la8w07knslhjgepfzr27d09v7e47xkg4wdvd9a7n42tpvmlnlhk7azz7htatgpa96rpf5prntczgmrphdnc2tpsgm26hk9xys7evg8t6905hp0llmpkgam66cr0tmhnpwj4v3t2thv0xaa8lz0u082fmlndezrusf5dvfa9wc2avfnkyqft7jw8r8lut3cd6elnatrhhh6w43ums4d9w47wz96fj2xlr8wfml42hun57zcn5aglqjuqcd8kq36rrvnjq33vzph5zcgpjuyyvsrkyfyw5pyepcx8r0mdllhlq8elkdfjwu2cj2u9h3t83ya6yeser4hk36ha7z43jny0hhw4jfd6has6thlmex0hwrd92etylaumx8lwuadsd8u7cxkll094t03lhf208mkgka5aetm86t2cavmery8jldcw28gtt9qm9hmcav0fplhkwrxpzrkl76s62jhdcrf6vwurmawg5nf77hwwwqjalu7ddhm9tygc6zha2hxh0g5hszqqz53l64w3satt0";
        var id = "c4d9f06599e4ba30edfabecc72a03db7d7e86c003ab83520002a844cfebf2ef5";
        System.out.println(wallet.getOfferById(id, s));



    }

    public static void print(Object o) {
        System.out.println(o);
    }
}