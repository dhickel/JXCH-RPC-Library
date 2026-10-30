package io.mindspice.jxch.rpc.http;

import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Crawler;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.crawler.IpsAfterTimestamp;
import io.mindspice.jxch.rpc.schemas.crawler.PeerCounts;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import java.io.IOException;
import java.math.BigInteger;

public class CrawlerAPI extends ChiaAPI {
    public CrawlerAPI(RPCClient client) { super(client, ChiaService.CRAWLER); }

    public byte[] getPeerCountsAsBytes() throws RPCException {
        try {
            return client.makeRequest(new Request(Crawler.GET_PEER_COUNTS, JsonUtils.newEmptyNodeAsBytes()));
        } catch (IOException e) { throw new RPCException("Error writing request JSON", e); }
    }

    public ApiResponse<PeerCounts> getPeerCounts() throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(getPeerCountsAsBytes()), "peer_counts", PeerCounts.class,
                    Crawler.GET_PEER_COUNTS);
        } catch (IOException e) { throw new RPCException("Error reading response JSON", e); }
    }

    private static BigInteger integer(Number value, String name) {
        if (value instanceof BigInteger big) return big;
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)
            return BigInteger.valueOf(value.longValue());
        throw new IllegalArgumentException(name + " must be an integer");
    }

    public byte[] getIpsAfterTimestampAsBytes(Number after, Number offset, Number limit) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder().put("after", integer(after, "after"))
                    .put("offset", integer(offset, "offset")).put("limit", integer(limit, "limit")).buildBytes();
            return client.makeRequest(new Request(Crawler.GET_IPS_AFTER_TIMESTAMP, data));
        } catch (IOException e) { throw new RPCException("Error writing request JSON", e); }
    }

    public ApiResponse<IpsAfterTimestamp> getIpsAfterTimestamp(Number after, Number offset, Number limit) throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(getIpsAfterTimestampAsBytes(after, offset, limit)),
                    IpsAfterTimestamp.class, Crawler.GET_IPS_AFTER_TIMESTAMP);
        } catch (IOException e) { throw new RPCException("Error reading response JSON", e); }
    }

    public byte[] getIpsAfterTimestampAsBytes(Number after) throws RPCException {
        return getIpsAfterTimestampAsBytes(after, 0, 10000);
    }

    public ApiResponse<IpsAfterTimestamp> getIpsAfterTimestamp(Number after) throws RPCException {
        return getIpsAfterTimestamp(after, 0, 10000);
    }
}
