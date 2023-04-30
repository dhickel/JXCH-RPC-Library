package io.mindspice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.mindspice.enums.ChiaService;
import io.mindspice.util.JsonUtils;

import java.io.File;
import java.io.IOException;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NodeConfig {
    private String address = "localhost";
    private int crawlerPort = 8561;
    private int dataLayerPort = 8561;
    private int daemonPort = 55400;
    private int fullNodePort = 8555;
    private int farmerPort = 8559;
    private int harvesterPort = 8560;
    private int walletPort = 9256;
    /* Certs */
    private String crawlerCrt;
    private String crawlerKey;
    private String dataLayerCrt;
    private String dataLayerKey;
    private String daemonCrt;
    private String daemonKey;
    private String fullNodeCrt;
    private String fullNodeKey;
    private String farmerCrt;
    private String farmerKey;
    private String harvesterCrt;
    private String harvesterKey;
    private String walletCrt;
    private String walletKey;


    private NodeConfig(Builder b) {
        this.address = b.address;
        this.crawlerPort = b.crawlerPort;
        this.dataLayerPort = b.dataLayerPort;
        this.daemonPort = b.daemonPort;
        this.fullNodePort = b.fullNodePort;
        this.farmerPort = b.farmerPort;
        this.harvesterPort = b.harvesterPort;
        this.walletPort = b.walletPort;
        this.crawlerCrt = b.crawlerCrt;
        this.crawlerKey = b.crawlerKey;
        this.dataLayerCrt = b.dataLayerCrt;
        this.dataLayerKey = b.dataLayerKey;
        this.daemonCrt = b.daemonCrt;
        this.daemonKey = b.daemonKey;
        this.fullNodeCrt = b.fullNodeCrt;
        this.fullNodeKey = b.fullNodeKey;
        this.farmerCrt = b.farmerCrt;
        this.farmerKey = b.farmerKey;
        this.harvesterCrt = b.harvesterCrt;
        this.harvesterKey = b.harvesterKey;
        this.walletCrt = b.walletCrt;
        this.walletKey = b.walletKey;
    }



    public static NodeConfig loadConfig(String configPath) throws IOException {
        return  JsonUtils.getMapper().readValue(new File(configPath), NodeConfig.class);
    }



    public String[] getCertPair(ChiaService service) throws IllegalStateException {
        String[] certPair = null;
        switch (service) {
            case CRAWLER -> certPair = new String[]{crawlerCrt, crawlerKey};
            case DATA_LAYER -> certPair = new String[]{dataLayerCrt, dataLayerKey};
            case DAEMON -> certPair = new String[]{daemonCrt, daemonKey};
            case FARMER -> certPair = new String[]{farmerCrt, farmerKey};
            case FULL_NODE -> certPair = new String[]{fullNodeCrt, fullNodeKey};
            case HARVESTER -> certPair = new String[]{harvesterCrt, harvesterKey};
            case WALLET -> certPair = new String[]{walletCrt, walletKey};
        }
        if (certPair == null || certPair[0] == null || certPair[1] == null) {
            throw new IllegalArgumentException("Null string in certificate pair, ensure certificate " +
                                                       "fields related to ChiaService have been assigned");
        }
        return certPair;
    }


    public String getAddressOf(ChiaService service) {
        String addr = "https://" + address + ":";
        switch (service) {
            case CRAWLER -> addr += crawlerPort;
            case DAEMON -> addr += daemonPort;
            case DATA_LAYER -> addr += dataLayerPort;
            case FULL_NODE -> addr += fullNodePort;
            case HARVESTER -> addr += harvesterPort;
            case WALLET -> addr += walletPort;
            case FARMER -> addr += farmerPort;
        }
        return addr;
    }


    public String getAddress() { return address; }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Config: ");
        sb.append("\n  address: \"").append(address).append('\"');
        sb.append(",\n  crawlerPort: ").append(crawlerPort);
        sb.append(",\n  dataLayerPort: ").append(dataLayerPort);
        sb.append(",\n  daemonPort: ").append(daemonPort);
        sb.append(",\n  fullNodePort: ").append(fullNodePort);
        sb.append(",\n  farmerPort: ").append(farmerPort);
        sb.append(",\n  harvesterPort: ").append(harvesterPort);
        sb.append(",\n  walletPort: ").append(walletPort);
        sb.append(",\n  crawlerCrt: \"").append(crawlerCrt).append('\"');
        sb.append(",\n  crawlerKey: \"").append(crawlerKey).append('\"');
        sb.append(",\n  dataLayerCrt: \"").append(dataLayerCrt).append('\"');
        sb.append(",\n  dataLayerKey: \"").append(dataLayerKey).append('\"');
        sb.append(",\n  daemonCrt: \"").append(daemonCrt).append('\"');
        sb.append(",\n  daemonKey: \"").append(daemonKey).append('\"');
        sb.append(",\n  fullNodeCrt: \"").append(fullNodeCrt).append('\"');
        sb.append(",\n  fullNodeKey: \"").append(fullNodeKey).append('\"');
        sb.append(",\n  farmerCrt: \"").append(farmerCrt).append('\"');
        sb.append(",\n  farmerKey: \"").append(farmerKey).append('\"');
        sb.append(",\n  harvesterCrt: \"").append(harvesterCrt).append('\"');
        sb.append(",\n  harvesterKey: \"").append(harvesterKey).append('\"');
        sb.append(",\n  walletCrt: \"").append(walletCrt).append('\"');
        sb.append(",\n  walletKey: \"").append(walletKey).append('\"');
        sb.append("\n");
        return sb.toString();
    }


    public static class Builder {
        private String address = "localhost";
        private int crawlerPort = 8561;
        private int dataLayerPort = 8561;
        private int daemonPort = 55400;
        private int fullNodePort = 8555;
        private int farmerPort = 8559;
        private int harvesterPort = 8560;
        private int walletPort = 9256;
        /* Certs */
        private String crawlerCrt;
        private String crawlerKey;
        private String dataLayerCrt;
        private String dataLayerKey;
        private String daemonCrt;
        private String daemonKey;
        private String fullNodeCrt;
        private String fullNodeKey;
        private String farmerCrt;
        private String farmerKey;
        private String harvesterCrt;
        private String harvesterKey;
        private String walletCrt;
        private String walletKey;


        public Builder addServiceCert(ChiaService chiaService, String crtPath, String keyPath) {
            switch (chiaService) {
                case CRAWLER -> {
                    crawlerCrt = crtPath;
                    crawlerKey = keyPath;
                }
                case DATA_LAYER -> {
                    dataLayerCrt = crtPath;
                    dataLayerKey = keyPath;
                }
                case DAEMON -> {
                    daemonCrt = crtPath;
                    daemonKey = keyPath;
                }
                case FARMER -> {
                    farmerCrt = crtPath;
                    farmerKey = keyPath;
                }
                case FULL_NODE -> {
                    fullNodeCrt = crtPath;
                    fullNodeKey = keyPath;
                }
                case HARVESTER -> {
                    harvesterCrt = crtPath;
                    harvesterKey = keyPath;
                }
                case WALLET -> {
                    walletCrt = crtPath;
                    walletKey = keyPath;
                }
            }
            return this;
        }


        public Builder setPortOverride(ChiaService chiaService, int port) {
            switch (chiaService) {
                case CRAWLER -> crawlerPort = port;
                case DAEMON -> daemonPort = port;
                case DATA_LAYER -> dataLayerPort = port;
                case FULL_NODE -> fullNodePort = port;
                case HARVESTER -> harvesterPort = port;
                case WALLET -> walletPort = port;
                case FARMER -> farmerPort = port;
            }
            return this;
        }


        public NodeConfig build() {
            return new NodeConfig(this);
        }

    }
}
