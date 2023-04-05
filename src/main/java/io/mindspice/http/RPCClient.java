package io.mindspice.http;

import io.mindspice.NodeConfig;
import io.mindspice.enums.ChiaService;
import io.mindspice.util.CertPairStore;
import io.mindspice.util.RPCException;
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
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;


public class RPCClient {
    private CloseableHttpClient client;
    private NodeConfig config;

    public RPCClient(NodeConfig config) throws IllegalStateException {
        this.config = config;
        var pairStore = new CertPairStore();

        for (var service : ChiaService.values()) {
            try {
                pairStore.addKey(service.name(), config.getCertPair(service));
            } catch (IllegalStateException e) {
                // TODO log this, it may happen if users does use all keys, but could be one failing to load
            }
        }
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(60_000)
                    .setConnectionRequestTimeout(60_000)
                    .setSocketTimeout(60_000).build();

            SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(pairStore.getKeyStore(), "".toCharArray())
                    .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                    .build();

            client = HttpClients
                    .custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
        } catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to construct HttpClient.", e);
        }
    }

    public byte[] makeRequest(Request req) throws RPCException {

        try {
            var uri = new URI(config.getAddressOf(req.service) + req.endpoint);
            System.out.println(uri.toString());
            var httpPost = new HttpPost(uri);
            httpPost.setEntity(new ByteArrayEntity(req.data));
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

            System.out.println("Request method: " + httpPost.getMethod());
            System.out.println("Request URI: " + httpPost.getURI());
            System.out.println("Request headers: " + Arrays.toString(httpPost.getAllHeaders()));
            System.out.println("Request payload: " + EntityUtils.toString(httpPost.getEntity()));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                InputStream content = response.getEntity().getContent();
                byte[] bytes = content.readAllBytes();
                System.out.println(new String(bytes));
                return bytes;
                //return content.readAllBytes();
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
}
