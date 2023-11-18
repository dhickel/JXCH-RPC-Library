package io.mindspice.jxch.rpc.http;

import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.util.CertPairStore;
import io.mindspice.jxch.rpc.util.RPCException;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RPCClient {
    private CloseableHttpClient client;
    private NodeConfig config;
    private List<ChiaService> availableServices;
    private volatile boolean doDebug = false;

    public RPCClient(NodeConfig config, int connectionTimeout, int requestTimeout, int socketTimeOut,
            PoolingHttpClientConnectionManager poolingManager) throws IllegalStateException {
        this.config = config;
        var pairStore = new CertPairStore();

        var serviceList = new ArrayList<ChiaService>();
        for (var service : ChiaService.values()) {
            try {
                pairStore.addKey(service.name(), config.getCertPair(service));
                serviceList.add(service);
            } catch (IllegalStateException e) {
                System.out.println("No cert pair found for: " + service);
                System.out.println("Ignorable if you don't plan on using above service.");
            }
        }
        availableServices = Collections.unmodifiableList(serviceList);
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectionTimeout)
                    .setConnectionRequestTimeout(requestTimeout)
                    .setSocketTimeout(socketTimeOut).build();

            SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(pairStore.getKeyStore(), "".toCharArray())
                    .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                    .build();

            var clientBuilder = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);

            if (poolingManager != null) { clientBuilder.setConnectionManager(poolingManager); }
            client = clientBuilder.build();

        } catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to construct HttpClient.", e);
        }
    }

    public RPCClient(NodeConfig config) {
        this(config, 10_000, 60_000, 60_000, null);
    }


    public void setDebug(boolean doDebug) {
        this.doDebug = doDebug;
    }

    public byte[] makeRequest(Request req) throws RPCException {

        try {
            var uri = new URI(config.getAddressOf(req.service) + req.endpoint);
            var httpPost = new HttpPost(uri);
            httpPost.setEntity(new ByteArrayEntity(req.data));
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

            if (doDebug) {
                System.out.println("Request | Uri: " + uri + " | data: " + new String(req.data, StandardCharsets.UTF_8));
            }
            try (CloseableHttpResponse response = client.execute(httpPost);
                 InputStream content = response.getEntity().getContent()) {
                byte[] bytes = content.readAllBytes();
                if (doDebug) {
                    System.out.println("Response: " + new String(bytes, StandardCharsets.UTF_8));
                }
                EntityUtils.consume(response.getEntity());
                return bytes;
            }


        } catch (URISyntaxException e) {
            throw new RPCException("URI error on RPC request", e);
        } catch (IOException e) {
            throw new RPCException("Byte read error on RPC request", e);

        }
    }

    public String getAddressFor(ChiaService cs) {
        return config.getAddressOf(cs);
    }

    public String getAddress() {
        return config.getAddress();
    }

    public List<ChiaService> getAvailableServices() {
        return availableServices;
    }
}