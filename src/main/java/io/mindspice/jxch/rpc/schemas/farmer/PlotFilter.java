package io.mindspice.jxch.rpc.schemas.farmer;

public record PlotFilter(String key,
        @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS)
        String value) { }
