package io.mindspice.util.jsondump;

import io.mindspice.schemas.farmer.Plot;

import java.util.List;

public record JsonMember(
    int totalCount,
    boolean success,
    int page,
    String error,
    int pageCount,
    String nodeId,
    List<Plot> plots
) {
}