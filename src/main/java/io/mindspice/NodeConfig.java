package io.mindspice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.mindspice.enums.ChiaService;

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
        YAMLMapper mapper = new YAMLMapper();
        return  mapper.readValue(new File(configPath), NodeConfig.class);
    }

     NodeConfig() {
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
            throw new IllegalStateException("Null string in certificate pair, ensure certificate " +
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
        String sb = "Config: " + "\n  address: \"" + address + '\"' +
                ",\n  crawlerPort: " + crawlerPort +
                ",\n  dataLayerPort: " + dataLayerPort +
                ",\n  daemonPort: " + daemonPort +
                ",\n  fullNodePort: " + fullNodePort +
                ",\n  farmerPort: " + farmerPort +
                ",\n  harvesterPort: " + harvesterPort +
                ",\n  walletPort: " + walletPort +
                ",\n  crawlerCrt: \"" + crawlerCrt + '\"' +
                ",\n  crawlerKey: \"" + crawlerKey + '\"' +
                ",\n  dataLayerCrt: \"" + dataLayerCrt + '\"' +
                ",\n  dataLayerKey: \"" + dataLayerKey + '\"' +
                ",\n  daemonCrt: \"" + daemonCrt + '\"' +
                ",\n  daemonKey: \"" + daemonKey + '\"' +
                ",\n  fullNodeCrt: \"" + fullNodeCrt + '\"' +
                ",\n  fullNodeKey: \"" + fullNodeKey + '\"' +
                ",\n  farmerCrt: \"" + farmerCrt + '\"' +
                ",\n  farmerKey: \"" + farmerKey + '\"' +
                ",\n  harvesterCrt: \"" + harvesterCrt + '\"' +
                ",\n  harvesterKey: \"" + harvesterKey + '\"' +
                ",\n  walletCrt: \"" + walletCrt + '\"' +
                ",\n  walletKey: \"" + walletKey + '\"' +
                "\n";
        return sb;
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

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public String getAddress() {
            return address;
        }


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
