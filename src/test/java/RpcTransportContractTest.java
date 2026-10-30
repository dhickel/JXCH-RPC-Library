import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.FullNode;
import io.mindspice.jxch.rpc.http.RPCClient;
import io.mindspice.jxch.rpc.http.Request;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RpcTransportContractTest {
    @Test
    void rejectsDaemonEvenWhenCallerOverridesEndpointService() {
        var client = new RPCClient(new NodeConfig.Builder().build());
        var request = new Request(ChiaService.DAEMON, FullNode.HEALTHZ, new byte[]{'{', '}'});
        var failure = assertThrows(RPCException.class, () -> client.makeRequest(request));
        assertTrue(failure.getMessage().contains("WebSocket"));
    }
}
