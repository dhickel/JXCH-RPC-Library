import io.mindspice.jxch.rpc.NodeConfig;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Daemon;
import io.mindspice.jxch.rpc.http.*;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.farmer.*;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ServicesContractTest {
    record Case(String method, String request, String response, Object... args) { }
    static final String PLOT = "{\"filename\":\"plot\",\"size\":128,\"plot_id\":\"0xabc\",\"pool_public_key\":null,\"pool_contract_puzzle_hash\":\"0xdef\",\"plot_public_key\":\"0x123\",\"file_size\":5000000000,\"time_modified\":4000000000,\"compression_level\":7,\"plot_index\":65535,\"meta_group\":255}";
    static final String SIGNAGE = "{\"signage_point\":{\"difficulty\":18446744073709551615,\"sub_slot_iters\":18446744073709551615,\"signage_point_index\":1},\"proofs\":[[\"plot-name\",{\"challenge\":\"0xabc\",\"proof\":\"0xdef\",\"size\":32}]]}";
    static final String POOL = "{\"pool_config\":{\"key_derivation_index\":4294967295,\"version\":1},\"current_difficulty\":null,\"authentication_token_timeout\":null,\"points_found_24h\":[[1700000000,18446744073709551615]],\"points_acknowledged_24h\":[[1700000001,9]],\"pool_errors_24h\":[[1700000002,{\"error_code\":5,\"error_message\":\"denied\"}]],\"valid_partials_24h\":[[1700000003,2]],\"next_farmer_update\":1700000000.75,\"next_pool_info_update\":1700000001.25}";
    static List<Case> farmerCases() {
        var pageRequest = "{\"node_id\":\"node\",\"page\":0,\"page_size\":500,\"filter\":[],\"reverse\":false}";
        var page = "{\"success\":true,\"page\":0,\"page_count\":1,\"plots\":[]}";
        return List.of(
                new Case("getSignagePoint", "{\"sp_hash\":\"0xabc\"}", "{\"success\":true," + SIGNAGE.substring(1), "0xabc"),
                new Case("getSignagePoints", "{}", "{\"success\":true,\"signage_points\":[" + SIGNAGE + "]}"),
                new Case("getRewardTargets", "{\"search_for_private_key\":true,\"max_ph_to_search\":500}", "{\"success\":true,\"farmer_target\":\"xch1a\",\"pool_target\":\"xch1b\",\"have_farmer_sk\":true,\"have_pool_sk\":false}", true, 500),
                new Case("setRewardTargets", "{\"farmer_target\":\"xch1a\",\"pool_target\":\"xch1b\"}", "{\"success\":true}", "xch1a", "xch1b"),
                new Case("getPoolState", "{}", "{\"success\":true,\"pool_state\":[" + POOL + "]}"),
                new Case("setPayoutInstructions", "{\"launcher_id\":\"0xabc\",\"payout_instructions\":\"xch1a\"}", "{\"success\":true}", "0xabc", "xch1a"),
                new Case("getHarvesters", "{}", "{\"success\":true,\"harvesters\":[]}"),
                new Case("getHarvestersSummary", "{}", "{\"success\":true,\"harvesters\":[]}"),
                new Case("getHarvesterPlotsValid", "{\"node_id\":\"node\",\"page\":0,\"page_size\":500,\"filter\":[],\"reverse\":false,\"sort_key\":\"filename\"}", page, "node"),
                new Case("getHarvesterPlotsInvalid", pageRequest, page, "node"),
                new Case("getHarvesterPlotsKeysMissing", pageRequest, page, "node"),
                new Case("getHarvesterPlotsDuplicates", pageRequest, page, "node"),
                new Case("getPoolLoginLink", "{\"launcher_id\":\"0xabc\"}", "{\"success\":true,\"login_link\":\"https://pool/login\"}", "0xabc"),
                new Case("connectToSolver", "{\"host\":\"solver.example\",\"port\":8666}", "{\"success\":true}", "solver.example", 8666)
        );
    }
    static List<Case> harvesterCases() throws Exception {
        return List.of(
                new Case("getPlots", "{}", "{\"success\":true,\"plots\":[" + PLOT + "],\"failed_to_open_filenames\":[\"bad\"],\"not_found_filenames\":[\"missing\"]}"),
                new Case("refreshPlots", "{}", "{\"success\":true}"),
                new Case("deletePlot", "{\"filename\":\"plot\"}", "{\"success\":true}", "plot"),
                new Case("addPlotDirectory", "{\"dirname\":\"/plots\"}", "{\"success\":true}", "/plots"),
                new Case("getPlotDirectories", "{}", "{\"success\":true,\"directories\":[\"/plots\"]}"),
                new Case("removePlotDirectory", "{\"dirname\":\"/plots\"}", "{\"success\":true}", "/plots"),
                new Case("getHarvesterConfig", "{}", "{\"success\":true,\"gpu_index\":2,\"disable_cpu_affinity\":true,\"parallel_decompressor_count\":4,\"use_gpu_harvesting\":true,\"enforce_gpu_index\":true,\"decompressor_thread_count\":8,\"recursive_plot_scan\":true,\"refresh_parameter_interval_seconds\":120}"),
                new Case("updateHarvesterConfig", "{\"gpu_index\":2,\"use_gpu_harvesting\":false,\"refresh_parameter_interval_seconds\":3}", "{\"success\":true}", JsonUtils.readTree("{\"gpu_index\":2,\"use_gpu_harvesting\":false,\"refresh_parameter_interval_seconds\":3}"))
        );
    }
    static void checkCases(ChiaAPI api, ChiaService service, CommonContractTest.Client client, List<Case> cases) throws Exception {
        for (var c : cases) {
            client.response = c.response;
            var result = (ApiResponse<?>) CommonContractTest.invoke(api, c.method, c.args);
            assertTrue(result.success(), c.method);
            assertTrue(result.data().isPresent(), c.method);
            assertEquals(service, client.last.service);
            assertEquals(CommonContractTest.route(c.method), client.last.endpoint);
            assertEquals(JsonUtils.readTree(c.request), JsonUtils.readTree(client.last.data), c.method);
            assertEquals(client.last.endpoint, result.endpoint().getPath());
            assertEquals(service, result.endpoint().getService());
            assertEquals(client.getAddressFor(service) + client.last.endpoint, result.requestURI());
            if (!c.method.startsWith("getHarvesterPlots")) {
                var bytes = (byte[]) CommonContractTest.invoke(api, c.method + "AsBytes", c.args);
                assertEquals(c.response, new String(bytes, java.nio.charset.StandardCharsets.UTF_8));
                assertEquals(JsonUtils.readTree(c.request), JsonUtils.readTree(client.last.data));
            }
            client.response = "{\"success\":false,\"error\":\"denied\"}";
            var failed = (ApiResponse<?>) CommonContractTest.invoke(api, c.method, c.args);
            assertFalse(failed.success());
            assertTrue(failed.data().isEmpty());
            assertEquals("denied", failed.error());
            client.response = "not-json";
            assertThrows(RPCException.class, () -> CommonContractTest.invoke(api, c.method, c.args));
        }
    }
    @Test void allFarmerAndHarvesterRoutesPayloadsParsedBytesAndFailures() throws Exception {
        var client = new CommonContractTest.Client();
        checkCases(new FarmerAPI(client), ChiaService.FARMER, client, farmerCases());
        checkCases(new HarvesterAPI(client), ChiaService.HARVESTER, client, harvesterCases());
        var inventory = JsonUtils.readTree(getClass().getResourceAsStream("/chia-2.7.5-routes.json").readAllBytes());
        for (var group : Map.of("farmer", farmerCases(), "harvester", harvesterCases()).entrySet()) {
            var expected = new HashSet<String>();
            inventory.get(group.getKey()).forEach(n -> expected.add("/" + n.asText()));
            assertEquals(expected, group.getValue().stream().map(c -> CommonContractTest.route(c.method)).collect(java.util.stream.Collectors.toSet()));
        }
    }

    @Test void crawlerAndSolverRoutesDefaultsTypedFixturesAndFailures() throws Exception {
        var client = new CommonContractTest.Client();
        var crawler = new CrawlerAPI(client);
        var counts = "{\"success\":true,\"peer_counts\":{\"total_last_5_days\":5,\"reliable_nodes\":3,\"ipv4_last_5_days\":4,\"ipv6_last_5_days\":1,\"versions\":{\"2.7.5\":5}}}";
        checkCases(crawler, ChiaService.CRAWLER, client, List.of(
                new Case("getPeerCounts", "{}", counts),
                new Case("getIpsAfterTimestamp", "{\"after\":1700000000,\"offset\":0,\"limit\":10000}", "{\"success\":true,\"ips\":[\"::1\"],\"total\":1}", 1700000000L)));
        client.response = counts;
        assertEquals(BigInteger.valueOf(5), crawler.getPeerCounts().data().orElseThrow().versions().get("2.7.5"));
        client.response = "{\"success\":true,\"ips\":[\"127.0.0.1\"],\"total\":12}";
        var ips = crawler.getIpsAfterTimestamp(2L, 4, 5).data().orElseThrow();
        assertEquals(BigInteger.valueOf(12), ips.total());
        assertEquals(List.of("127.0.0.1"), ips.ips());
        assertEquals(JsonUtils.readTree("{\"after\":2,\"offset\":4,\"limit\":5}"), JsonUtils.readTree(client.last.data));
        var solver = new SolverAPI(client);
        checkCases(solver, ChiaService.SOLVER, client, List.of(new Case("getState", "{}", "{\"success\":true,\"started\":true}")));
        client.response = "{\"success\":true,\"started\":false}";
        assertFalse(solver.getState().data().orElseThrow().started());
    }

    @Test void modernFixturesPlotsSignageAndPoolTuples() throws Exception {
        var plot = JsonUtils.<Plot>readJson(PLOT, Plot.class);
        assertEquals(BigInteger.valueOf(4000000000L), plot.timeModified());
        assertEquals(BigInteger.valueOf(5000000000L), plot.fileSize());
        assertEquals(7, plot.compressionLevel());
        assertEquals(65535, plot.plotIndex());
        assertEquals(255, plot.metaGroup());
        var signage = JsonUtils.<SignagePointBundle>readJson(SIGNAGE, SignagePointBundle.class);
        assertEquals("plot-name", signage.proofs().get(0).plotIdentifier());
        assertEquals("0xdef", signage.proofs().get(0).proofOfSpace().proof());
        assertEquals(new BigInteger("18446744073709551615"), signage.spInfo().difficulty());
        var pool = JsonUtils.<PoolState>readJson(POOL, PoolState.class);
        assertNull(pool.currentDifficulty());
        assertNull(pool.authenticationTokenTimeout());
        assertEquals(new BigInteger("18446744073709551615"), pool.pointsFound24h().get(0).points());
        assertEquals("denied", pool.poolErrors24h().get(0).error().get("error_message").textValue());
        assertEquals(1700000000.75, pool.nextFarmerUpdate());
        assertEquals(4294967295L, pool.poolConfig().keyDerivationIndex());
        var details = JsonUtils.<HarvesterDetails>readJson("{\"total_plot_size\":123,\"total_effective_plot_size\":18446744073709551615,\"harvesting_mode\":1,\"syncing\":null,\"plots\":[" + PLOT + "]}", HarvesterDetails.class);
        assertEquals(new BigInteger("18446744073709551615"), details.totalEffectivePlotSize());
        assertEquals(1, details.harvestingMode());
        assertNull(details.syncing());
        var summary = JsonUtils.<HarvesterSummary>readJson("{\"plots\":5,\"total_effective_plot_size\":42,\"harvesting_mode\":null,\"syncing\":{\"initial\":true,\"plot_files_total\":6,\"plot_files_processed\":2}}", HarvesterSummary.class);
        assertEquals(5, summary.plots());
        assertEquals(BigInteger.valueOf(42), summary.totalEffectivePlotSize());
        assertTrue(summary.syncing().initial());
        var config = JsonUtils.<io.mindspice.jxch.rpc.schemas.harvester.HarvesterConfig>readJson(
                "{\"refresh_parameter_interval_seconds\":4294967295}", io.mindspice.jxch.rpc.schemas.harvester.HarvesterConfig.class);
        assertEquals(4294967295L, config.refreshParameterIntervalSeconds());
    }

    // harvester_protocol.py Plot/Plot2 use uint64 for both fields; harvester.get_plots emits them unchanged.
    @Test void plotUint64FieldsPreserveTheFullWireRange() throws Exception {
        var max = new BigInteger("18446744073709551615");
        var wire = PLOT.replace("5000000000", max.toString()).replace("4000000000", max.toString());
        var client = new CommonContractTest.Client();
        client.response = "{\"success\":true,\"plots\":[" + wire + "],\"failed_to_open_filenames\":[],\"not_found_filenames\":[]}";
        var plot = new HarvesterAPI(client).getPlots().data().orElseThrow().plots().get(0);
        assertEquals(max, plot.fileSize());
        assertEquals(max, plot.timeModified());
        assertEquals(max, JsonUtils.toNode(plot).get("file_size").bigIntegerValue());
        assertEquals(max, JsonUtils.toNode(plot).get("time_modified").bigIntegerValue());
        client.response = "{\"success\":true,\"page\":0,\"page_count\":1,\"plots\":[" + wire + "]}";
        assertEquals(plot, new FarmerAPI(client).getHarvesterPlotsValid("node").data().orElseThrow().get(0));
        var legacy = new Plot(null, 123, "key", "plot", 32, null, 456L, "id");
        assertEquals(BigInteger.valueOf(123), legacy.timeModified());
        assertEquals(BigInteger.valueOf(456), legacy.fileSize());
    }

    // harvester_rpc_api.py uses .get("interval_seconds"); a configured partial refresh mapping returns null.
    @Test void harvesterConfigPreservesNullableRefreshInterval() throws Exception {
        var client = new CommonContractTest.Client();
        client.response = harvesterCases().get(6).response.replace("\"refresh_parameter_interval_seconds\":120",
                "\"refresh_parameter_interval_seconds\":null");
        var config = new HarvesterAPI(client).getHarvesterConfig().data().orElseThrow();
        assertNull(config.refreshParameterIntervalSeconds());
        assertEquals(2, config.gpuIndex());
        assertTrue(config.disableCpuAffinity());
    }

    // plot_sync.receiver.Receiver.to_dict(False/True) returns lists for detail and counts for summary.
    @Test void nonEmptyHarvesterResponsesPreserveBothReceiverShapes() throws Exception {
        var client = new CommonContractTest.Client();
        var api = new FarmerAPI(client);
        String fields = "\"connection\":{\"node_id\":\"node\",\"host\":\"::1\",\"port\":65535},"
                + "\"total_plot_size\":5000000000,\"total_effective_plot_size\":18446744073709551615,"
                + "\"last_sync_time\":1700000000.125,\"harvesting_mode\":1,"
                + "\"syncing\":{\"initial\":false,\"plot_files_processed\":4294967294,\"plot_files_total\":4294967295}";
        client.response = "{\"success\":true,\"harvesters\":[{" + fields + ",\"plots\":[" + PLOT + "],"
                + "\"failed_to_open_filenames\":[\"bad\"],\"no_key_filenames\":[\"key\"],\"duplicates\":[\"duplicate\"]}]}";
        var detail = api.getHarvesters().data().orElseThrow().get(0);
        assertEquals(List.of("bad"), detail.failedToOpenFilenames());
        assertEquals(List.of("key"), detail.noKeyFilenames());
        assertEquals(List.of("duplicate"), detail.duplicates());
        assertEquals("plot", detail.plots().get(0).filename());
        assertEquals(BigInteger.valueOf(5000000000L), detail.totalPlotSize());
        assertEquals(1700000000.125, detail.lastSyncTime());
        assertEquals(4294967294L, detail.syncing().plotFilesProcessed());
        assertEquals(4294967295L, detail.syncing().plotFilesTotal());
        assertEquals(new HarvesterConnection(65535, "::1", "node"), detail.harvesterConnection());
        client.response = "{\"success\":true,\"harvesters\":[{" + fields
                + ",\"plots\":7,\"failed_to_open_filenames\":1,\"no_key_filenames\":2,\"duplicates\":3}]}";
        var summary = api.getHarvestersSummary().data().orElseThrow().get(0);
        assertEquals(7, summary.plots());
        assertEquals(1, summary.failedToOpenFilenames());
        assertEquals(2, summary.noKeyFilenames());
        assertEquals(3, summary.duplicates());
        assertEquals(detail.harvesterConnection(), summary.harvesterConnection());
        assertEquals(detail.totalEffectivePlotSize(), summary.totalEffectivePlotSize());
        assertEquals(detail.totalPlotSize(), summary.totalPlotSize());
        assertEquals(detail.syncing(), summary.syncing());
        assertEquals(detail.harvestingMode(), summary.harvestingMode());
    }

    @Test void allPlotPaginationFamiliesAndTypedFilters() throws Exception {
        class Pages extends CommonContractTest.Client {
            final List<Request> requests = new ArrayList<>();
            final ArrayDeque<String> replies = new ArrayDeque<>();
            @Override public byte[] makeRequest(Request request) {
                last = request;
                requests.add(request);
                return replies.remove().getBytes(java.nio.charset.StandardCharsets.UTF_8);
            }
        }
        var client = new Pages();
        var farmer = new FarmerAPI(client);
        for (var name : List.of("getHarvesterPlotsValid", "getHarvesterPlotsInvalid", "getHarvesterPlotsKeysMissing", "getHarvesterPlotsDuplicates")) {
            client.requests.clear();
            String item = name.endsWith("Valid") ? PLOT : "\"plot\"";
            client.replies.add("{\"success\":true,\"page\":0,\"page_count\":2,\"plots\":[" + item + "]}");
            client.replies.add("{\"success\":true,\"page\":1,\"page_count\":2,\"plots\":[" + item + "]}");
            var response = (ApiResponse<?>) CommonContractTest.invoke(farmer, name, "0xab", List.of("plot"), 2, true);
            assertEquals(2, ((List<?>) response.data().orElseThrow()).size());
            assertEquals(2, client.requests.size());
            assertEquals(1, JsonUtils.readTree(client.requests.get(1).data).get("page").asInt());
            var filter = JsonUtils.readTree(client.requests.get(0).data).get("filter");
            assertEquals(name.endsWith("Valid") ? JsonUtils.readTree("[{\"key\":\"filename\",\"value\":\"plot\"}]") : JsonUtils.readTree("[\"plot\"]"), filter);
            client.replies.add("{\"success\":true,\"page\":0,\"page_count\":2,\"plots\":[" + item + "]}");
            client.replies.add("{\"success\":false,\"error\":\"page failed\"}");
            var failed = (ApiResponse<?>) CommonContractTest.invoke(farmer, name, "node");
            assertFalse(failed.success());
            assertTrue(failed.data().isEmpty());
            assertEquals("page failed", failed.error());
            client.replies.add("{\"success\":true,\"page\":0,\"page_count\":1,\"plots\":[]}");
            var empty = (ApiResponse<?>) CommonContractTest.invoke(farmer, name, "node");
            assertEquals(List.of(), empty.data().orElseThrow());
            client.replies.add("{\"success\":true}");
            CommonContractTest.invoke(farmer, name + "AsBytes", "node", List.of(), 3, 4, true);
            assertEquals(3, JsonUtils.readTree(client.last.data).get("page").asInt());
            assertEquals(4, JsonUtils.readTree(client.last.data).get("page_size").asInt());
        }
        client.replies.add("{\"success\":true}");
        farmer.getHarvesterPlotsValidAsBytes("node", List.of(new PlotFilter("pool_public_key", null)), "file_size", 0, 20, false);
        assertEquals(JsonUtils.readTree("[{\"key\":\"pool_public_key\",\"value\":null}]"), JsonUtils.readTree(client.last.data).get("filter"));
        assertEquals("file_size", JsonUtils.readTree(client.last.data).get("sort_key").asText());
        client.replies.add("{\"success\":true}");
        assertTrue(farmer.setRewardTargets(null, "xch1pool").success());
        assertEquals(JsonUtils.readTree("{\"pool_target\":\"xch1pool\"}"), JsonUtils.readTree(client.last.data));
    }

    @Test void newServiceConfigurationBuilderYamlPortsCertsOverridesAndDaemonFailures() throws Exception {
        var defaults = new NodeConfig.Builder().build();
        for (var entry : Map.of(ChiaService.CRAWLER, 8561, ChiaService.SOLVER, 8667, ChiaService.TIMELORD, 8557, ChiaService.DATA_LAYER, 8562).entrySet()) {
            assertEquals("https://localhost:" + entry.getValue(), defaults.getAddressOf(entry.getKey()));
            assertThrows(IllegalStateException.class, () -> defaults.getCertPair(entry.getKey()));
            var config = new NodeConfig.Builder().setAddress("node").setPortOverride(entry.getKey(), 1234)
                    .addServiceCert(entry.getKey(), "cert", "key").build();
            assertEquals("https://node:1234", config.getAddressOf(entry.getKey()));
            assertArrayEquals(new String[]{"cert", "key"}, config.getCertPair(entry.getKey()));
        }
        var yaml = new com.fasterxml.jackson.dataformat.yaml.YAMLMapper().readValue(
                "solverPort: 4321\nsolverCrt: solver.crt\nsolverKey: solver.key\ntimelordCrt: time.crt\ntimelordKey: time.key\n", NodeConfig.class);
        assertEquals("https://localhost:4321", yaml.getAddressOf(ChiaService.SOLVER));
        assertEquals("https://localhost:8557", yaml.getAddressOf(ChiaService.TIMELORD));
        assertEquals("https://localhost:8562", yaml.getAddressOf(ChiaService.DATA_LAYER));
        assertArrayEquals(new String[]{"solver.crt", "solver.key"}, yaml.getCertPair(ChiaService.SOLVER));
        assertArrayEquals(new String[]{"time.crt", "time.key"}, yaml.getCertPair(ChiaService.TIMELORD));
        var client = new CommonContractTest.Client();
        var daemon = new DaemonClient(client);
        assertThrows(RPCException.class, daemon::get_status_bytes);
        assertThrows(RPCException.class, daemon::get_status);
        assertThrows(UnsupportedOperationException.class, DaemonClient::exit);
        for (var endpoint : Daemon.values()) assertThrows(UnsupportedOperationException.class, endpoint::getPath);
        assertNull(client.last);
        assertTrue(Daemon.class.isAnnotationPresent(Deprecated.class));
        assertTrue(DaemonClient.class.isAnnotationPresent(Deprecated.class));
    }
}
