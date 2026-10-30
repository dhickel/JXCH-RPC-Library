import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.endpoints.FullNode;
import io.mindspice.jxch.rpc.http.FullNodeAPI;
import io.mindspice.jxch.rpc.http.RPCClient;
import io.mindspice.jxch.rpc.http.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class FullNodeStopNodeTest {
    private static class CapturingClient extends RPCClient {
        Request request;
        String response = "{\"success\":true}";

        CapturingClient() {
            super(new NodeConfig.Builder().build());
        }

        @Override
        public byte[] makeRequest(Request request) {
            this.request = request;
            return response.getBytes(StandardCharsets.UTF_8);
        }
    }

    @Test
    void stopNodeUsesStopRouteAndPreservesFailure() throws Exception {
        var client = new CapturingClient();
        var api = new FullNodeAPI(client);
        var success = api.stopNode();
        assertEquals("/stop_node", client.request.endpoint);
        assertEquals("{}", new String(client.request.data, StandardCharsets.UTF_8));
        assertEquals(FullNode.STOP_NODE, success.endpoint());
        assertEquals(true, success.data().orElseThrow());

        client.response = "{\"success\":false,\"error\":\"denied\"}";
        var failure = api.stopNode();
        assertEquals("/stop_node", client.request.endpoint);
        assertFalse(failure.success());
        assertEquals("denied", failure.error());
        assertTrue(failure.data().isEmpty());
    }
}
