import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.http.*;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.farmer.*;
import io.mindspice.jxch.rpc.schemas.harvester.HarvesterConfig;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServicesSharedNumericTest {
    static final BigInteger U64 = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
    static final BigInteger U128 = BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE);
    static final BigInteger HUGE = BigInteger.ONE.shiftLeft(160);

    static <T> T decode(String wire, Class<T> type) throws IOException { return JsonUtils.readJson(wire, type); }
    static void exact(BigInteger expected, Number actual) { assertEquals(expected, new BigInteger(actual.toString())); }

    // rpc_server.py: NodeType, uint16(port), and logging.Handler.setLevel accept distinct domains.
    @Test void sharedRequestNumericDomainsAndServiceBoundMetadata() throws Exception {
        var client = new CommonContractTest.Client();
        var api = new SolverAPI(client);
        client.response = "{\"success\":true,\"connections\":[]}";
        for (int type = 1; type <= 8; type++) {
            var response = api.getConnections(type);
            assertEquals(type, JsonUtils.readTree(client.last.data).get("node_type").intValue());
            assertEquals(ChiaService.SOLVER, response.endpoint().getService());
        }
        for (int invalid : new int[]{Integer.MIN_VALUE, 0, 9, Integer.MAX_VALUE}) {
            assertThrows(IllegalArgumentException.class, () -> api.getConnectionsAsBytes(invalid));
        }
        for (var serviceApi : List.of(api, new HarvesterAPI(client))) {
            client.response = "{\"success\":true}";
            for (int port : new int[]{0, 65535}) {
                assertTrue(serviceApi.openConnection("::1", port).success());
                assertEquals(port, JsonUtils.readTree(client.last.data).get("port").intValue());
            }
            for (int port : new int[]{-1, 65536}) {
                assertThrows(IllegalArgumentException.class, () -> serviceApi.openConnectionAsBytes("host", port));
            }
        }
        client.response = "{\"success\":true,\"level\":\"Level 10\",\"available_levels\":[\"INFO\"],\"errors\":[]}";
        for (Number level : List.of(-1, 10L, HUGE)) {
            assertTrue(api.setLogLevel(level).success());
            exact(new BigInteger(level.toString()), JsonUtils.readTree(client.last.data).get("level").bigIntegerValue());
            assertEquals(client.response, new String(api.setLogLevelAsBytes(level), StandardCharsets.UTF_8));
        }
        for (Number level : List.of(1.0, 1.25f, new BigDecimal("1.0"))) {
            assertThrows(IllegalArgumentException.class, () -> api.setLogLevelAsBytes(level));
        }
        api.setLogLevelAsBytes("DEBUG");
        assertEquals("DEBUG", JsonUtils.readTree(client.last.data).get("level").textValue());
        api.setLogLevelAsBytes(null);
        assertTrue(JsonUtils.readTree(client.last.data).get("level").isNull());
        api.setLogLevel(null);
        assertTrue(JsonUtils.readTree(client.last.data).get("level").isNull());
        assertThrows(IllegalArgumentException.class, () -> api.setLogLevelAsBytes(true));
        client.response = "{\"success\":false,\"errors\":[\"invalid level\"]}";
        assertTrue(api.setLogLevel(HUGE).error().contains("invalid level"));
    }

    // default_get_connections + WSChiaConnection counters are Python ints, not int64.
    @Test void connectionCountersNullablePortsAndFullNodeOptionalPeakWidths() throws Exception {
        var client = new CommonContractTest.Client();
        client.response = "{\"success\":true,\"connections\":[{\"bytes_read\":" + HUGE
                + ",\"bytes_written\":" + HUGE.add(BigInteger.ONE)
                + ",\"local_port\":null,\"peer_server_port\":null,\"peer_port\":65535,\"type\":8,"
                + "\"creation_time\":1700000000.125,\"last_message_time\":1700000000.75,"
                + "\"peak_height\":4294967295,\"peak_weight\":" + U128 + "}]}";
        var connection = new SolverAPI(client).getConnections().data().orElseThrow().get(0);
        exact(HUGE, connection.bytesRead());
        exact(HUGE.add(BigInteger.ONE), connection.bytesWritten());
        assertNull(connection.localPort());
        assertNull(connection.peerServerPort());
        assertEquals(65535, connection.peerPort());
        assertEquals(8, connection.type());
        assertEquals(1700000000.125, connection.creationTime());
        assertEquals(1700000000.75, connection.lastMessageTime());
        assertEquals(4294967295L, connection.peakHeight());
        assertEquals(U128, connection.peakWeight());
        assertEquals(HUGE, JsonUtils.toNode(connection).get("bytes_read").bigIntegerValue());
        assertNull(decode("{\"type\":null,\"peak_height\":null,\"peak_weight\":null}", Connection.class).type());
    }

    // harvester_rpc_api.py int(...) configuration fields are unbounded; refresh is optional uint32.
    @Test void harvesterConfigurationPreservesPythonIntsAndNullableRefresh() throws Exception {
        var client = new CommonContractTest.Client();
        var api = new HarvesterAPI(client);
        var configuration = JsonUtils.readTree("{\"gpu_index\":" + HUGE.negate()
                + ",\"parallel_decompressor_count\":" + HUGE + ",\"decompressor_thread_count\":4294967296,"
                + "\"refresh_parameter_interval_seconds\":4294967295,\"use_gpu_harvesting\":true}");
        client.response = "{\"success\":true}";
        assertTrue(api.updateHarvesterConfig(configuration).success());
        assertEquals(configuration, JsonUtils.readTree(client.last.data));
        var built = new JsonUtils.ObjectBuilder().put("gpu_index", HUGE).put("parallel_decompressor_count", 2L)
                .put("refresh_parameter_interval_seconds", 3).put("extra", Map.of("nested", List.of(HUGE, true))).buildNode();
        api.updateHarvesterConfigAsBytes(built);
        assertEquals(JsonUtils.readTree(JsonUtils.writeBytes(built)), JsonUtils.readTree(client.last.data));
        assertThrows(IllegalArgumentException.class, () -> api.updateHarvesterConfigAsBytes(
                new JsonUtils.ObjectBuilder().put("gpu_index", 1.25).buildNode()));
        client.response = "{\"success\":true," + configuration.toString().substring(1);
        var config = api.getHarvesterConfig().data().orElseThrow();
        exact(HUGE.negate(), config.gpuIndex());
        exact(HUGE, config.parallelDecompressorCount());
        exact(BigInteger.valueOf(4294967296L), config.decompressorThreadCount());
        assertEquals(4294967295L, config.refreshParameterIntervalSeconds());
        assertTrue(config.useGpuHarvesting());
        assertNull(decode("{\"refresh_parameter_interval_seconds\":null}", HarvesterConfig.class).refreshParameterIntervalSeconds());
        for (String key : List.of("gpu_index", "parallel_decompressor_count", "decompressor_thread_count",
                "refresh_parameter_interval_seconds")) {
            for (String invalid : List.of("1.25", "1.0", "null", "\"3\"")) {
                var request = JsonUtils.readTree("{\"" + key + "\":" + invalid + "}");
                assertThrows(IllegalArgumentException.class, () -> api.updateHarvesterConfigAsBytes(request));
            }
        }
        for (String invalid : List.of("-1", "0", "2", "4294967296", HUGE.toString())) {
            var request = JsonUtils.readTree("{\"refresh_parameter_interval_seconds\":" + invalid + "}");
            assertThrows(IllegalArgumentException.class, () -> api.updateHarvesterConfigAsBytes(request));
        }
        assertThrows(IllegalArgumentException.class, () -> api.updateHarvesterConfigAsBytes(null));
        assertThrows(IllegalArgumentException.class, () -> api.updateHarvesterConfigAsBytes(JsonUtils.readTree("[]")));
        assertThrows(IllegalArgumentException.class, () -> new HarvesterConfig(1.25, false, 1, false, false, 1,
                false, null, true, null));
    }

    @Test void allEightHarvesterHandlersKeepPayloadsBytesAndFailureEnvelopes() throws Exception {
        var client = new CommonContractTest.Client();
        var api = new HarvesterAPI(client);
        var cases = List.of(
                new CommonContractTest.Case("getPlots", "{}", "{\"success\":true,\"plots\":[],\"not_found_filenames\":[\"missing\"],\"failed_to_open_filenames\":[\"bad\"]}"),
                new CommonContractTest.Case("refreshPlots", "{}", "{\"success\":true}"),
                new CommonContractTest.Case("deletePlot", "{\"filename\":\"plot\"}", "{\"success\":true}", "plot"),
                new CommonContractTest.Case("addPlotDirectory", "{\"dirname\":\"plots\"}", "{\"success\":true}", "plots"),
                new CommonContractTest.Case("getPlotDirectories", "{}", "{\"success\":true,\"directories\":[\"plots\"]}"),
                new CommonContractTest.Case("removePlotDirectory", "{\"dirname\":\"plots\"}", "{\"success\":true}", "plots"),
                new CommonContractTest.Case("getHarvesterConfig", "{}", "{\"success\":true,\"gpu_index\":4294967296,\"refresh_parameter_interval_seconds\":null}"),
                new CommonContractTest.Case("updateHarvesterConfig", "{\"refresh_parameter_interval_seconds\":3}", "{\"success\":true}",
                        JsonUtils.readTree("{\"refresh_parameter_interval_seconds\":3}")));
        for (var c : cases) {
            client.response = c.response();
            var response = (ApiResponse<?>) CommonContractTest.invoke(api, c.method(), c.args());
            assertTrue(response.success());
            assertEquals(ChiaService.HARVESTER, response.endpoint().getService());
            assertEquals(CommonContractTest.route(c.method()), client.last.endpoint);
            assertEquals(JsonUtils.readTree(c.request()), JsonUtils.readTree(client.last.data));
            assertArrayEquals(c.response().getBytes(StandardCharsets.UTF_8),
                    (byte[]) CommonContractTest.invoke(api, c.method() + "AsBytes", c.args()));
            client.response = "{\"success\":false,\"error\":\"failed\"}";
            var failure = (ApiResponse<?>) CommonContractTest.invoke(api, c.method(), c.args());
            assertFalse(failure.success());
            assertTrue(failure.data().isEmpty());
            assertEquals("failed", failure.error());
            client.response = "not-json";
            assertThrows(RPCException.class, () -> CommonContractTest.invoke(api, c.method(), c.args()));
        }
    }

    // Receiver.to_dict: counts are len(...), sizes are sums, syncing is uint32, time_done is nullable float.
    @Test void farmerHarvesterShapesPreserveCountsSizesAndNullTimes() throws Exception {
        String fields = "\"duplicates\":" + HUGE + ",\"failed_to_open_filenames\":4294967296,"
                + "\"no_key_filenames\":9223372036854775808,\"plots\":" + HUGE + ","
                + "\"last_sync_time\":null,\"total_plot_size\":" + HUGE + ",\"total_effective_plot_size\":" + HUGE
                + ",\"harvesting_mode\":2,\"connection\":{\"port\":65535},"
                + "\"syncing\":{\"initial\":false,\"plot_files_total\":4294967295,\"plot_files_processed\":4294967295}";
        var client = new CommonContractTest.Client();
        client.response = "{\"success\":true,\"harvesters\":[{" + fields + "}]}";
        var summary = new FarmerAPI(client).getHarvestersSummary().data().orElseThrow().get(0);
        exact(HUGE, summary.duplicates());
        exact(BigInteger.valueOf(4294967296L), summary.failedToOpenFilenames());
        exact(BigInteger.ONE.shiftLeft(63), summary.noKeyFilenames());
        exact(HUGE, summary.plots());
        assertEquals(HUGE, summary.totalPlotSize());
        assertEquals(HUGE, summary.totalEffectivePlotSize());
        assertNull(summary.lastSyncTime());
        assertEquals(2, summary.harvestingMode());
        assertEquals(65535, summary.harvesterConnection().port());
        assertEquals(4294967295L, summary.syncing().plotFilesTotal());
        assertEquals(4294967295L, summary.syncing().plotFilesProcessed());
        var details = decode("{\"last_sync_time\":null,\"syncing\":null,\"harvesting_mode\":null,\"total_plot_size\":"
                + HUGE + ",\"total_effective_plot_size\":" + HUGE + "}", HarvesterDetails.class);
        assertNull(details.lastSyncTime());
        assertNull(details.syncing());
        assertNull(details.harvestingMode());
        assertEquals(HUGE, details.totalPlotSize());
        assertEquals(HUGE, details.totalEffectivePlotSize());
        assertEquals(1700000000.125, decode("{\"last_sync_time\":1700000000.125}", HarvesterDetails.class).lastSyncTime());
    }

    // Pool state combines uint64 protocol values, unbounded cumulative counters, uint32 event times and floats.
    @Test void poolStateCountersTupleEventsAndConfigRemainExact() throws Exception {
        var fields = JsonUtils.newEmptyNode();
        for (String key : List.of("plot_count", "points_found_since_start", "points_acknowledged_since_start",
                "valid_partials_since_start", "invalid_partials_since_start", "insufficient_partials_since_start",
                "stale_partials_since_start", "missing_partials_since_start")) fields.put(key, HUGE);
        fields.put("current_points", U64).put("current_difficulty", U64).put("authentication_token_timeout", 255);
        fields.put("next_pool_info_update", 1700000000.125).put("next_farmer_update", 1700000000.75);
        fields.set("pool_config", JsonUtils.readTree("{\"key_derivation_index\":" + HUGE.negate() + ",\"version\":" + HUGE + "}"));
        for (String key : List.of("points_found_24h", "points_acknowledged_24h", "valid_partials_24h", "invalid_partials_24h",
                "insufficient_partials_24h", "stale_partials_24h", "missing_partials_24h")) {
            fields.set(key, JsonUtils.readTree("[[4294967295," + U64 + "],[0,null]]"));
        }
        var pool = JsonUtils.<PoolState>readJson(fields, PoolState.class);
        for (Number counter : List.of(pool.plotCount(), pool.pointsFoundSinceStart(), pool.pointsAcknowledgedSinceStart(),
                pool.validPartialsSinceStart(), pool.invalidPartialsSinceStart(), pool.insufficientPartialsSinceStart(),
                pool.stalePartialsSinceStart(), pool.missingPartialsSinceStart())) exact(HUGE, counter);
        assertEquals(U64, pool.currentPoints());
        assertEquals(U64, pool.currentDifficulty());
        assertEquals(255L, pool.authenticationTokenTimeout());
        exact(HUGE.negate(), pool.poolConfig().keyDerivationIndex());
        exact(HUGE, pool.poolConfig().version());
        assertEquals(1700000000.125, pool.nextPoolInfoUpdate());
        assertEquals(1700000000.75, pool.nextFarmerUpdate());
        for (var events : List.of(pool.pointsFound24h(), pool.pointsAcknowledged24h(), pool.validPartials24h(),
                pool.invalidPartials24h(), pool.insufficientPartials24h(), pool.stalePartials24h(), pool.missingPartials24h())) {
            assertEquals(4294967295L, events.get(0).timestamp());
            assertEquals(U64, events.get(0).points());
            assertNull(events.get(1).points());
        }
        var initial = decode("{\"authentication_token_timeout\":null,\"current_difficulty\":null,\"pool_config\":{\"key_derivation_index\":-1,\"version\":1}}", PoolState.class);
        assertNull(initial.authenticationTokenTimeout());
        assertNull(initial.currentDifficulty());
        exact(BigInteger.valueOf(-1), initial.poolConfig().keyDerivationIndex());
    }

    // make_pool_protocol_request and farmer_api.py append whole remote dictionaries, not just ErrorResponse fields.
    @Test void poolErrorsKeepArbitraryJsonIncludingNestedAndHugeValues() throws Exception {
        var error = JsonUtils.readTree("{\"error_code\":" + HUGE + ",\"error_message\":\"denied\","
                + "\"details\":{\"amount\":" + U64 + ",\"retry\":1.25,\"items\":[null,true,{\"key\":\"value\"}]}}");
        var client = new CommonContractTest.Client();
        client.response = "{\"success\":true,\"pool_state\":[{\"pool_errors_24h\":[[4294967295," + error + "]]}]}";
        var event = new FarmerAPI(client).getPoolState().data().orElseThrow().get(0).poolErrors24h().get(0);
        assertEquals(4294967295L, event.timestamp());
        assertEquals(error, event.error());
        assertEquals(error, JsonUtils.toNode(event).get(1));
    }

    @Test void plotsSignageAndOptionalFiltersMatchSerializers() throws Exception {
        String plot = "{\"size\":255,\"file_size\":" + U64 + ",\"time_modified\":" + U64
                + ",\"compression_level\":255,\"plot_index\":65535,\"meta_group\":255,\"pool_public_key\":null}";
        var client = new CommonContractTest.Client();
        client.response = "{\"success\":true,\"plots\":[" + plot + "],\"failed_to_open_filenames\":[\"bad\"],\"not_found_filenames\":[\"missing\"]}";
        var plots = new HarvesterAPI(client).getPlots().data().orElseThrow();
        assertEquals(List.of("bad"), plots.failedToOpenFilenames());
        assertEquals(List.of("missing"), plots.notFoundFilenames());
        var value = plots.plots().get(0);
        assertEquals(U64, value.fileSize());
        assertEquals(U64, value.timeModified());
        assertEquals(255, value.size());
        assertEquals(65535, value.plotIndex());
        assertEquals(255, value.metaGroup());
        assertEquals(255, value.compressionLevel());
        var oldPlot = decode("{\"size\":32,\"compression_level\":null}", Plot.class);
        assertNull(oldPlot.plotIndex());
        assertNull(oldPlot.metaGroup());
        assertNull(oldPlot.compressionLevel());
        assertEquals(BigInteger.valueOf(1), new Plot(null, 1, "key", "name", 32, null, 2L, "id").timeModified());
        var sp = decode("{\"signage_point\":{\"difficulty\":" + U64 + ",\"sub_slot_iters\":" + U64
                + ",\"signage_point_index\":255},\"proofs\":[]}", SignagePointBundle.class).spInfo();
        assertEquals(U64, sp.difficulty());
        assertEquals(U64, sp.subSlotIters());
        assertEquals(255, sp.signagePointIndex());
        assertEquals(JsonUtils.readTree("{\"key\":\"pool_public_key\",\"value\":null}"), JsonUtils.toNode(new PlotFilter("pool_public_key", null)));
        assertEquals("65535", JsonUtils.toNode(new PlotFilter("plot_index", "65535")).get("value").textValue());
        var targets = decode("{\"farmer_target\":\"xch1farmer\",\"pool_target\":\"xch1pool\"}", RewardTarget.class);
        assertNull(targets.haveFarmerSk());
        assertNull(targets.havePoolSk());
    }

    @Test void everyIntegralResponseFieldRejectsFractionalAndFloatingPointCoercion() throws Exception {
        var fields = Map.<Class<?>, List<String>>ofEntries(
                Map.entry(Connection.class, List.of("local_port", "bytes_read", "peer_port", "peak_weight", "type", "peer_server_port", "bytes_written", "peak_height")),
                Map.entry(HarvesterConfig.class, List.of("gpu_index", "parallel_decompressor_count", "decompressor_thread_count", "refresh_parameter_interval_seconds")),
                Map.entry(HarvesterConnection.class, List.of("port")),
                Map.entry(HarvesterDetails.class, List.of("total_plot_size", "total_effective_plot_size", "harvesting_mode")),
                Map.entry(HarvesterSummary.class, List.of("duplicates", "failed_to_open_filenames", "no_key_filenames", "plots", "total_plot_size", "total_effective_plot_size", "harvesting_mode")),
                Map.entry(Plot.class, List.of("time_modified", "size", "file_size", "compression_level", "plot_index", "meta_group")),
                Map.entry(SignagePointBundle.SpInfo.class, List.of("difficulty", "signage_point_index", "sub_slot_iters")),
                Map.entry(Syncing.class, List.of("plot_files_total", "plot_files_processed")),
                Map.entry(PoolState.class, List.of("plot_count", "current_points", "authentication_token_timeout", "current_difficulty", "points_found_since_start", "points_acknowledged_since_start", "valid_partials_since_start", "invalid_partials_since_start", "insufficient_partials_since_start", "stale_partials_since_start", "missing_partials_since_start")),
                Map.entry(PoolState.PoolConfig.class, List.of("key_derivation_index", "version")),
                Map.entry(PoolState.PoolError.class, List.of("error_code")));
        for (var entry : fields.entrySet()) {
            for (var field : entry.getValue()) {
                for (String invalid : List.of("1.25", "1.0", "1e2", "\"1\"", "true")) {
                    assertThrows(IOException.class, () -> decode("{\"" + field + "\":" + invalid + "}", entry.getKey()),
                            entry.getKey().getSimpleName() + "." + field + " = " + invalid);
                }
            }
        }
        for (var wire : List.of("[1.25,2]", "[1,2.25]", "[1.0,2]")) {
            assertThrows(IOException.class, () -> decode(wire, PoolState.PointEvent.class));
        }
        assertThrows(IOException.class, () -> decode("[1.25,{}]", PoolState.ErrorEvent.class));
        assertThrows(IOException.class, () -> decode("{\"peak_height\":" + HUGE + "}", Connection.class));
        assertThrows(IOException.class, () -> decode("{\"plot_index\":" + HUGE + "}", Plot.class));
    }
}
