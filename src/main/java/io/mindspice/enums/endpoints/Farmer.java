package io.mindspice.enums.endpoints;

import io.mindspice.enums.ChiaService;


public enum Farmer implements Endpoint{
    GET_SIGNAGE_POINT,
    GET_SIGNAGE_POINTS,
    GET_REWARD_TARGETS,
    SET_REWARD_TARGETS,
    GET_POOL_STATE,
    SET_PAYOUT_INSTRUCTIONS,
    GET_HARVESTERS,
    GET_HARVESTERS_SUMMARY,
    GET_HARVESTER_PLOTS_VALID,
    GET_HARVESTER_PLOTS_INVALID,
    GET_HARVESTER_PLOTS_KEYS_MISSING,
    GET_HARVESTER_PLOTS_DUPLICATES,
    GET_POOL_LOGIN_LINK;


    @Override
    public ChiaService getService() {
        return ChiaService.FARMER;
    }


    @Override
    public String getPath() {
        return "/" + name().toLowerCase();
    }
}
