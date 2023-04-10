package io.mindspice;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.mindspice.http.*;
import io.mindspice.util.ChiaUtils;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;

import java.io.File;
import java.io.IOException;
import java.util.List;


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

        var s = "offer1qqzh3wcuu2rykcmqvpsxygqqwc7hynr6hum6e0mnf72sn7uvvkpt68eyumkhelprk0adeg42nlelk2mpagr90qq0a37v8lc9r20mfemen65ptl9mj3yh05wwakm49lr78y90zkpcc6u5h7z64khh2pf209cxjm7gjxz9wx6aawdsl8vslm8rqu0mu49a7jmz9gtvseha057h8lvllukadv6wxxqxmz0dx60k2043l8uee3ud2xtha7lhq3wj5kd0ragpqaf6lelxakkvk5g50ymhjceu7nanhflhs6hf0n8a6tuyfm89eakag3avtl4l6twjwmydzaq28zq3zer53cm65wejrgany8l5we6rsmsxczpyd5h6shm9zj70lmk9qlm5uuajdkaf8e97l0y0np87pzf4sudtm0yjldk5enlmlc8fwsd7hg5hdut0647aytg0asdgk4cev4vhum56xwcl440eugt4fa5utqhhra9f20qrzg82glwtxad8x6renk246uw448h82jew9me4kuhk5rk542z4ezzu06qup3k39s8grnx0p8vrqynlspgt0nnc09lesuktzuesmrmahdup9xhe64tj6kexfym48ux0ccueyu0c9evt6tls0c3nff9q4z4d7n6l85j23f9l9zcj6dfl2u3j7we02vct6290tycj6v4fx53nj0eyk2ctxwx4ynzd9t9j4zuv9v4y4uknkwe56uj2ev9v5z5jktfykv4n6v9skj323hx88ujj6fft9nznjvel2t2d7jeq45hlrlcqfxgykjq89vuqmm943yrmwe0dtf3qdh7mnct6f7fwwzpw4ectl7ay0rdv0ze2gu0aur4wlsdl4lml2vhdqhcfxn64wut7mmlsxhj57gvdqv9qe7d89q9w76ptjw39kykrxff9x5umzfzl9uun74alh3qrwwfl4ujrtt6yh76zqfelxxj43h7gcryy3h7szrjyrhvuenjdf08z2nwlf4pumh6wy49as22rfyy3c4u6quwvugfppxvmkysrkhzll7d0t2cvqes663ltlzevalhg7n9xez0fxaxtlxea2739vfszujygcpfz6u0k37ulz7d3kv44638wng40wlwgdpqxd7kn4mazx7d5lmcft8qqgjmyl7atelw5zu7e4ett0l7deh9q4td60nhvm2m8dugyn0eav2uhva0t4h5d54zpl8g6gnym3g6nsw348qcymneh7ar2jrv83gk06ss2fmpmhta9te0625xdyg027utecw80ws4dlcnt3d6vtn7ds7pg4mfkm4f40qegsw2kx6pautgjndk39xmyz079760uu0dlqhmhyrm3uhx9kxk8clv08n535lu0yg579g63ha4e3yfhemfktvu5llcw28tvflqzuj7qv8zed0vxu7sprw72zg2g8r5ehnp5a4cx0nhlpcnrlvshjl6pm93008dl37l9n4aldmqtef69nhleh37mc699gh3wv9up80am5c99vgpj4avlwzk06hes0wh4we83hl5g6jlzx4ud90m98ryckmtyehwahajukq7n0kft8l2qhmpj6zcp0kxpcrnpnzqpr484csrggqteyyuql4rcy2jpvcfk8hrll0lskukd7hkq9x6rdsmmu79khej8aj50rrtv9a2p8vtz7cgvzps9vj9mmvxvhvzavf0lnfz0m3fr4fajwnqm902nxaw4qdwc5s0lvh8759cm4e4d2h9k5h43xxl2s045axm0lq7arxv4xepyt9dew2h8qsy4k005xxd3u8muur8m658rpj6ahvmla72dzmjklagl7dzd09tzewaw7zxfcrj4rat3ve67qr3xjx2skcq2z7";
        var id = "c4d9f06599e4ba30edfabecc72a03db7d7e86c003ab83520002a844cfebf2ef5";

        //System.out.println(wallet.getOfferSummary(s, false));
        //System.out.println(wallet.selectCoins(1, 1000));

        var jnode = JsonUtils.newSingleNode("test", "value");

        var wall = new ChiaUtils.CatWalletBuilder()
                .setName("test")
                .isNewWallet(false)
                .setAssetId("0xea830317f831a23b178aa653e50484568d30d2c5b34d8140e71247ead05961c7")
                .setFee(1)
                .build();

        System.out.println(wallet.getTransactions(1,730,736,false));

    }

    public static void print(Object o) {
        System.out.println(o);
    }
}