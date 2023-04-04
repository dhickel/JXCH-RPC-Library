package io.mindspice.enums.endpoints;

import io.mindspice.enums.ChiaService;


public enum Harvester implements Endpoint {
    GET_PLOTS,
    REFRESH_PLOTS,
    DELETE_PLOT,
    ADD_PLOT_DIRECTORY,
    GET_PLOT_DIRECTORIES,
    REMOVE_PLOT_DIRECTORY;


    @Override
    public ChiaService getService() {
        return io.mindspice.enums.ChiaService.HARVESTER;
    }


    @Override
    public String getPath() {
        return "/" + name().toLowerCase();
    }


}
