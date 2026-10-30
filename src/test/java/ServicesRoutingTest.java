import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.http.*;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServicesRoutingTest {
    @org.junit.jupiter.api.Test void endpointPathsAreLocaleIndependent() {
        var original = java.util.Locale.getDefault();
        try {
            java.util.Locale.setDefault(java.util.Locale.forLanguageTag("tr-TR"));
            org.junit.jupiter.api.Assertions.assertEquals("/get_signage_point", io.mindspice.jxch.rpc.enums.endpoints.Farmer.GET_SIGNAGE_POINT.getPath());
            org.junit.jupiter.api.Assertions.assertEquals("/refresh_plots", io.mindspice.jxch.rpc.enums.endpoints.Harvester.REFRESH_PLOTS.getPath());
        } finally { java.util.Locale.setDefault(original); }
    }
    static class RecordingClient extends RPCClient {
        final List<Request> requests = new ArrayList<>();
        final List<String> responses = new ArrayList<>();
        RecordingClient() { super(new NodeConfig.Builder().build()); }
        @Override public byte[] makeRequest(Request request) {
            requests.add(request);
            return responses.remove(0).getBytes(StandardCharsets.UTF_8);
        }
        void respond(String json) { responses.add(json); }
    }

    @Test void portsAndHarvesterRoutes() throws Exception {
        assertEquals("https://localhost:8562", new NodeConfig.Builder().build().getAddressOf(ChiaService.DATA_LAYER));
        var client = new RecordingClient();
        var harvester = new HarvesterAPI(client);
        client.respond("{\"success\":true}");
        var result = harvester.refreshPlots();
        assertTrue(result.success());
        assertEquals(ChiaService.HARVESTER, client.requests.get(0).service);
        assertEquals("/refresh_plots", client.requests.get(0).endpoint);
        client.respond("{\"success\":true}");
        harvester.stopNode();
        assertEquals("/stop_node", client.requests.get(1).endpoint);
    }

    @Test void farmerPagesAndError() throws Exception {
        var client = new RecordingClient();
        var farmer = new FarmerAPI(client);
        client.respond("{\"success\":true,\"page\":0,\"page_count\":2,\"plots\":[\"first\"]}");
        client.respond("{\"success\":true,\"page\":1,\"page_count\":2,\"plots\":[\"second\"]}");
        assertEquals(List.of("first", "second"), farmer.getHarvesterPlotsInvalid("node").data().orElseThrow());
        assertEquals("/get_harvester_plots_invalid", client.requests.get(0).endpoint);
        assertTrue(new String(client.requests.get(1).data, StandardCharsets.UTF_8).contains("\"page\":1"));
        client.respond("{\"success\":true,\"page\":0,\"page_count\":2,\"plots\":[]}");
        client.respond("{\"success\":false,\"error\":\"bad page\"}");
        var failed = farmer.getHarvesterPlotsInvalid("node");
        assertFalse(failed.success());
        assertEquals("bad page", failed.error());
    }

    @Test void daemonDoesNotPretendHttpIsSupported() {
        var daemon = new DaemonClient(new RecordingClient());
        assertThrows(RPCException.class, daemon::get_status_bytes);
        assertThrows(UnsupportedOperationException.class, DaemonClient::exit);
    }
}
