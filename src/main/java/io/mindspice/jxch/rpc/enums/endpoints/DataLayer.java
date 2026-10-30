package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;
import java.util.Locale;

public enum DataLayer implements Endpoint {
    WALLET_LOG_IN, CREATE_DATA_STORE, GET_OWNED_STORES, BATCH_UPDATE,
    MULTISTORE_BATCH_UPDATE, SUBMIT_PENDING_ROOT, SUBMIT_ALL_PENDING_ROOTS,
    GET_VALUE, GET_KEYS, GET_KEYS_VALUES, GET_ANCESTORS, GET_ROOT, GET_LOCAL_ROOT,
    GET_ROOTS, DELETE_KEY, INSERT, SUBSCRIBE, UNSUBSCRIBE, ADD_MIRROR,
    DELETE_MIRROR, GET_MIRRORS, REMOVE_SUBSCRIPTIONS, SUBSCRIPTIONS, GET_KV_DIFF,
    GET_ROOT_HISTORY, ADD_MISSING_FILES, MAKE_OFFER, TAKE_OFFER, VERIFY_OFFER,
    CANCEL_OFFER, GET_SYNC_STATUS, CHECK_PLUGINS, CLEAR_PENDING_ROOTS, GET_PROOF,
    VERIFY_PROOF;

    @Override public ChiaService getService() { return ChiaService.DATA_LAYER; }
    @Override public String getPath() { return "/" + name().toLowerCase(Locale.ROOT); }
}
