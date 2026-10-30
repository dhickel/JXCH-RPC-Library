package io.mindspice.jxch.rpc.http;

import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Solver;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.solver.SolverState;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;
import java.io.IOException;

public class SolverAPI extends ChiaAPI {
    public SolverAPI(RPCClient client) { super(client, ChiaService.SOLVER); }

    public byte[] getStateAsBytes() throws RPCException {
        try {
            return client.makeRequest(new Request(Solver.GET_STATE, JsonUtils.newEmptyNodeAsBytes()));
        } catch (IOException e) { throw new RPCException("Error writing request JSON", e); }
    }

    public ApiResponse<SolverState> getState() throws RPCException {
        try {
            return newResponse(JsonUtils.readTree(getStateAsBytes()), SolverState.class, Solver.GET_STATE);
        } catch (IOException e) { throw new RPCException("Error reading response JSON", e); }
    }
}
