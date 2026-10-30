import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Wallet;
import io.mindspice.jxch.rpc.http.RPCClient;
import io.mindspice.jxch.rpc.http.Request;
import io.mindspice.jxch.rpc.http.WalletAPI;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceTest {
    private static class RecordingClient extends RPCClient {
        Request request;
        String response = "{\"success\":true}";

        RecordingClient() {
            super(new NodeConfig.Builder().setPortOverride(ChiaService.WALLET, 12345).build());
        }

        @Override
        public byte[] makeRequest(Request request) {
            this.request = request;
            return response.getBytes(StandardCharsets.UTF_8);
        }
    }

    @Test
    void stopUsesWalletRouteAndAddress() throws Exception {
        var client = new RecordingClient();
        var result = new WalletAPI(client).stopNode();
        assertEquals(ChiaService.WALLET, client.request.service);
        assertEquals("/stop_node", client.request.endpoint);
        assertEquals("{}", new String(client.request.data, StandardCharsets.UTF_8));
        assertEquals("https://localhost:12345/stop_node", result.requestURI());
        assertEquals(Wallet.STOP_NODE, result.endpoint());
        assertTrue(result.data().orElseThrow());
    }

    @Test
    void healthAndStopPropagateErrors() throws Exception {
        var client = new RecordingClient();
        var api = new WalletAPI(client);
        client.response = "{\"success\":false,\"error\":\"unavailable\"}";
        var health = api.healthz();
        assertEquals("/healthz", client.request.endpoint);
        assertFalse(health.success());
        assertEquals("unavailable", health.error());
        assertTrue(health.data().isEmpty());
        var stop = api.stopNode();
        assertEquals("/stop_node", client.request.endpoint);
        assertFalse(stop.success());
        assertEquals("unavailable", stop.error());
        assertTrue(stop.data().isEmpty());
    }
}
