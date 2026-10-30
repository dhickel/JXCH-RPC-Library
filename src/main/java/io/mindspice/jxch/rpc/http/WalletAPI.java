package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.NftDataKey;
import io.mindspice.jxch.rpc.enums.endpoints.Wallet;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.schemas.clawback.AutoClaim;
import io.mindspice.jxch.rpc.schemas.fullnode.Network;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.CoinRecord;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;
import io.mindspice.jxch.rpc.schemas.shared.Connection;
import io.mindspice.jxch.rpc.schemas.wallet.*;
import io.mindspice.jxch.rpc.schemas.wallet.WalletRpcResponses.*;
import io.mindspice.jxch.rpc.schemas.wallet.cat.Cat;
import io.mindspice.jxch.rpc.schemas.wallet.cat.CatAssetInfo;
import io.mindspice.jxch.rpc.schemas.wallet.cat.StrayCat;
import io.mindspice.jxch.rpc.schemas.wallet.did.*;
import io.mindspice.jxch.rpc.schemas.wallet.nft.*;
import io.mindspice.jxch.rpc.schemas.wallet.offers.*;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.util.RPCException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static io.mindspice.jxch.rpc.enums.endpoints.Wallet.*;


public class WalletAPI extends ChiaAPI {

    public WalletAPI(RPCClient client) {
        super(client, ChiaService.WALLET);
    }

    ////////////
    // SHARED //
    ////////////

    public byte[] closeConnectionAsBytes(String nodeId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("node_id", nodeId);
            var req = new Request(Wallet.CLOSE_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> closeConnection(String nodeId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(closeConnectionAsBytes(nodeId));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.CLOSE_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getConnectionsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_CONNECTIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Connection>> getConnections() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getConnectionsAsBytes());
            return newResponseList(jsonNode, "connections", TypeRefs.CONNECTION_LIST, Wallet.GET_CONNECTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getRoutesAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_ROUTES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> getRoutes() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getRoutesAsBytes());
            return newResponseList(jsonNode, "routes", TypeRefs.STRING_LIST, Wallet.GET_ROUTES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] healthzAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.HEALTHZ, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> healthz() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(healthzAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Wallet.HEALTHZ);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] openConnectionAsBytes(String ip, int port) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("ip", ip)
                    .put("port", port)
                    .buildBytes();
            var req = new Request(Wallet.OPEN_CONNECTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> openConnection(String ip, int port) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(openConnectionAsBytes(ip, port));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.OPEN_CONNECTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] stopNodeAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.STOP_NODE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> stopNode() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(stopNodeAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Wallet.STOP_NODE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ////////////////////////
    /*  WALLET MANAGEMENT */
    ////////////////////////

    public byte[] createNewWalletAsBytes(JsonNode walletBuilder) throws RPCException {
        try {
            var req = new Request(CREATE_NEW_WALLET, JsonUtils.writeBytes(walletBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<CreateWalletResult> createNewWallet(JsonNode walletBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createNewWalletAsBytes(walletBuilder));
            return newResponse(jsonNode, CreateWalletResult.class, CREATE_NEW_WALLET);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getWalletsAsBytes(int type, boolean includeData) throws RPCException {
        try {
            var data = JsonUtils.newSingleNode("include_data", includeData);
            if (type != -1) { data.put("type", type); }
            var req = new Request(GET_WALLETS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Wallets> getWallets(int type, boolean includeData) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getWalletsAsBytes(type, includeData));
            return newResponse(jsonNode, Wallets.class, GET_WALLETS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<Wallets> getWallets(boolean includeData) throws RPCException {
        return getWallets(-1, includeData);
    }

    /////////////////
    /* CAT WALLET */
    ////////////////

    public byte[] cancelOfferAsBytes(String tradeId, long fee, boolean cancelOnChain) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("trade_id", tradeId)
                    .put("fee", fee)
                    .put("secure", cancelOnChain)
                    .buildBytes();
            var req = new Request(CANCEL_OFFER, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> cancelOffer(String tradeId, long fee, boolean cancelOnChain) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(cancelOfferAsBytes(tradeId, fee, cancelOnChain));
            return newResponse(jsonNode, "success", Boolean.class, CANCEL_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] cancelAllOffersAsBytes(boolean cancelOnChain) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("secure", cancelOnChain)
                    .put("cancel_all", true)
                    .buildBytes();
            var req = new Request(CANCEL_OFFERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> cancelAllOffers(boolean cancelOnChain) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(cancelAllOffersAsBytes(cancelOnChain));
            return newResponse(jsonNode, "success", Boolean.class, CANCEL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] cancelOffersAsBytes(String assetId, int batchSize, long batchFee, boolean cancelOnChain)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("secure", cancelOnChain)
                    .put("asset_id", assetId)
                    .put("batch_size", batchSize)
                    .put("batch_fee", batchFee)
                    .buildBytes();
            var req = new Request(CANCEL_OFFERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> cancelOffers(String assetId, int batchSize, long batchFee, boolean cancelOnChain)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(cancelOffersAsBytes(assetId, batchSize, batchFee, cancelOnChain));
            return newResponse(jsonNode, "success", Boolean.class, CANCEL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catAssetIdToNameAsBytes(String catAssetId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("asset_id", catAssetId);
            var req = new Request(CAT_ASSET_ID_TO_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<CatAssetInfo> catAssetIdToName(String assetId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catAssetIdToNameAsBytes(assetId));
            return newResponse(jsonNode, CatAssetInfo.class, CAT_ASSET_ID_TO_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catGetAssetIdAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(CAT_GET_ASSET_ID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> catGetAssetId(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catGetAssetIdAsBytes(walletId));
            return newResponse(jsonNode, "asset_id", String.class, CAT_GET_ASSET_ID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catGetNameAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(CAT_GET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> catGetName(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catGetNameAsBytes(walletId));
            return newResponse(jsonNode, "name", String.class, CAT_GET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catSetNameAsBytes(long walletId, String name) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("name", name)
                    .buildBytes();
            var req = new Request(CAT_SET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> catSetName(long walletId, String name) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSetNameAsBytes(walletId, name));
            return newResponse(jsonNode, "success", Boolean.class, CAT_SET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catSpendAsBytes(JsonNode catSpendBuilder) throws RPCException {
        try {
            var req = new Request(CAT_SPEND, JsonUtils.writeBytes(catSpendBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public byte[] catSpendAsBytes(long walletId, long amountInMojo, String address) throws RPCException {
        var data = new JsonUtils.ObjectBuilder()
                .put("wallet_id", walletId)
                .put("amount", amountInMojo)
                .put("inner_address", address)
                .buildNode();
        return catSpendAsBytes(data);
    }

    public ApiResponse<TransactionStatus> catSpend(long walletId, long amountInMojo, String address) throws
            RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSpendAsBytes(walletId, amountInMojo, address));
            return newResponse(jsonNode, TransactionStatus.class, CAT_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<TransactionStatus> catSpend(JsonNode catSpendBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSpendAsBytes(catSpendBuilder));
            return newResponse(jsonNode, TransactionStatus.class, CAT_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] checkOfferValidityAsBytes(String offer) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("offer", offer);
            var req = new Request(CHECK_OFFER_VALIDITY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<OfferValidity> checkOfferValidity(String offer) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(checkOfferValidityAsBytes(offer));
            return newResponse(jsonNode, OfferValidity.class, CHECK_OFFER_VALIDITY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] createOfferForIdsAsBytes(JsonNode offerBuilder) throws RPCException {
        try {
            var req = new Request(CREATE_OFFER_FOR_IDS, JsonUtils.writeBytes(offerBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Offer> createOfferForIds(JsonNode offerBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createOfferForIdsAsBytes(offerBuilder));
            return newResponse(jsonNode, Offer.class, CREATE_OFFER_FOR_IDS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getAllOffersAsBytes(JsonNode offerSearchBuilder) throws RPCException {
        try {
            var req = new Request(GET_ALL_OFFERS, JsonUtils.writeBytes(offerSearchBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Offers> getAllOffers() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllOffersAsBytes(JsonUtils.newEmptyNode()));
            return newResponse(jsonNode, Offers.class, GET_ALL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<Offers> getAllOffers(JsonNode offerSearchBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllOffersAsBytes(offerSearchBuilder));
            return newResponse(jsonNode, Offers.class, GET_ALL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getCatListAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_CAT_LIST, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Cat>> getCatList() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCatListAsBytes());
            return newResponseList(jsonNode, "cat_list", TypeRefs.CAT_LIST, GET_CAT_LIST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated(since = "2.7.5")
    public byte[] getOfferByBytes(String tradeId, String fileContents) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("trade_id", tradeId)
                    .buildNode();
            if (fileContents != null) {
                if (!fileContents.equalsIgnoreCase("true") && !fileContents.equalsIgnoreCase("false")) {
                    throw new RPCException("file_contents must be a boolean, not file contents");
                }
                data.put("file_contents", Boolean.parseBoolean(fileContents));
            }
            var req = new Request(GET_OFFER, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Offer> getOfferById(String tradeId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOfferByBytes(tradeId, null));
            return newResponse(jsonNode, Offer.class, GET_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<Offer> getOfferById(String tradeId, String fileContents) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOfferByBytes(tradeId, fileContents));
            return newResponse(jsonNode, Offer.class, GET_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getOfferSummaryAsBytes(String offer, boolean showAdvanced) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("offer", offer)
                    .put("advanced", showAdvanced)
                    .buildNode();
            var req = new Request(GET_OFFER_SUMMARY, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<OfferSummaryResult> getOfferSummary(String offer, boolean showAdvanced) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOfferSummaryAsBytes(offer, showAdvanced));
            if (!jsonNode.path("success").asBoolean()) return newFailedResponse(jsonNode, GET_OFFER_SUMMARY);
            var summary = jsonNode.path("summary");
            boolean dataLayer = summary.path("offered").isArray();
            return newResponse(new OfferSummaryResult(jsonNode.path("id").asText(),
                    dataLayer ? null : JsonUtils.readJson(summary, OfferSummary.class),
                    dataLayer ? JsonUtils.readJson(summary, DataLayerOfferSummary.class) : null), GET_OFFER_SUMMARY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getOffersCountAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_OFFERS_COUNT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<OfferCount> getOffersCount() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOffersCountAsBytes());

            return newResponse(jsonNode, OfferCount.class, GET_OFFERS_COUNT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getStrayCatsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_STRAY_CATS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<StrayCat>> getStrayCats() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getStrayCatsAsBytes());

            return newResponseList(jsonNode, "stray_cats", TypeRefs.STRAY_CAT_LIST, GET_STRAY_CATS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] selectCoinsAsBytes(JsonNode selectCoinBuilder) throws RPCException {
        try {
            var req = new Request(SELECT_COINS, JsonUtils.writeBytes(selectCoinBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Coin>> selectCoins(long walletId, long amount) throws RPCException {
        try {
            var reqNode = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("amount", amount)
                    .buildNode();
            var respNode = JsonUtils.readTree(selectCoinsAsBytes(reqNode));
            return newResponseList(respNode, "coins", TypeRefs.COIN_LIST, SELECT_COINS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<List<Coin>> selectCoins(JsonNode selectCoinBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(selectCoinsAsBytes(selectCoinBuilder));
            return newResponseList(jsonNode, "coins", TypeRefs.COIN_LIST, SELECT_COINS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] takeOfferAsBytes(JsonNode takeOfferbuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(takeOfferbuilder);
            var req = new Request(TAKE_OFFER, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<TradeRecord> takeOffer(String offer, long fee) throws RPCException {
        try {
            var reqNode = new JsonUtils.ObjectBuilder()
                    .put("offer", offer)
                    .put("fee", fee)
                    .buildNode();

            var respNode = JsonUtils.readTree(takeOfferAsBytes(reqNode));
            return newResponse(respNode, "trade_record", TradeRecord.class, TAKE_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<TradeRecord> takeOffer(JsonNode takeOfferBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(takeOfferAsBytes(takeOfferBuilder));
            return newResponse(jsonNode, "trade_record", TradeRecord.class, TAKE_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ////////////////
    /* DID WALLET */
    ////////////////

    @Deprecated(since = "2.7.5", forRemoval = true)
    public byte[] didCreateAttestAsBytes(int walletId, String coinName, String pubKey,
            String puzHash) throws RPCException {
        throw new RPCException("did_create_attest was removed from Chia; DID recovery is no longer supported");
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public ApiResponse<DIDAttest> didCreateAttestAs(int walletId, String coinName, String pubKey,
            String puzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didCreateAttestAsBytes(walletId, coinName, pubKey, puzHash));
            return newResponse(jsonNode, DIDAttest.class, DID_CREATE_ATTEST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didCreateDIDBackupFileAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(DID_CREATE_BACKUP_FILE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didCreateDIDBackupFile(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didCreateDIDBackupFileAsBytes(walletId));
            return newResponse(jsonNode, "backup_data", String.class, DID_CREATE_BACKUP_FILE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didFindLostDIDAsBytes(String coinId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("coin_id", coinId);
            var req = new Request(DID_FIND_LOST_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didFindLostDID(String coinId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didFindLostDIDAsBytes(coinId));
            return newResponse(jsonNode, "latest_coin_id", String.class, DID_FIND_LOST_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetCurrentCoinInfoAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(DID_GET_CURRENT_COIN_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DIDCoin> didGetCurrentCoinInfo(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetCurrentCoinInfoAsBytes(walletId));
            return newResponse(jsonNode, DIDCoin.class, DID_GET_CURRENT_COIN_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetDIDAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(DID_GET_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DID> didGetDID(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetDIDAsBytes(walletId));
            return newResponse(jsonNode, DID.class, DID_GET_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetInfoAsBytes(String coinId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("coin_id", coinId);
            var req = new Request(DID_GET_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DIDInfo> didGetInfo(String coinId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetInfoAsBytes(coinId));
            return newResponse(jsonNode, DIDInfo.class, DID_GET_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public byte[] didGetInformationNeededForRecoveryAsBytes(int walletId) throws RPCException {
        throw new RPCException("did_get_information_needed_for_recovery was removed from Chia");
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public ApiResponse<DIDRecoveryInfo> didGetInformationNeededForRecovery(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetInformationNeededForRecoveryAsBytes(walletId));
            return newResponse(jsonNode, DIDRecoveryInfo.class, DID_GET_INFORMATION_NEEDED_FOR_RECOVERY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetMetaDataAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(DID_GET_METADATA, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> didGetMetaData(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetMetaDataAsBytes(walletId));
            return newResponseNode(jsonNode, "metadata", DID_GET_METADATA);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didUpdateMetaDataAsBytes(long walletId, JsonNode metaData, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("metadata", metaData)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(DID_UPDATE_METADATA, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> didUpdateMetaData(long walletId, JsonNode metaData, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didUpdateMetaDataAsBytes(walletId, metaData, fee, reusePuzHash));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, DID_UPDATE_METADATA);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetPubKeyAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(DID_GET_PUBKEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didGetPubKey(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetPubKeyAsBytes(walletId));
            return newResponse(jsonNode, "pubkey", String.class, DID_GET_PUBKEY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public byte[] didGetRecoveryListAsBytes(int walletId) throws RPCException {
        throw new RPCException("did_get_recovery_list was removed from Chia");
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public ApiResponse<DIDRecoveryList> didGetRecoveryList(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetRecoveryListAsBytes(walletId));
            return newResponse(jsonNode, DIDRecoveryList.class, DID_GET_RECOVERY_LIST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetWalletNameAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(DID_GET_WALLET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didGetWalletName(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetWalletNameAsBytes(walletId));
            return newResponse(jsonNode, "name", String.class, DID_GET_WALLET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didMessageSpendAsBytes(long walletId, List<String> coinAnnouncements,
            List<String> puzzleAnnouncements) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("coin_announcements", coinAnnouncements)
                    .put("puzzle_announcements", puzzleAnnouncements)
                    .buildBytes();
            var req = new Request(DID_MESSAGE_SPEND, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> didMessageSpendAs(long walletId, List<String> coinAnnouncements,
            List<String> puzzleAnnouncements) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    didMessageSpendAsBytes(walletId, coinAnnouncements, puzzleAnnouncements)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, DID_MESSAGE_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public byte[] didRecoverySpendAsBytes(int walletId, List<String> attestData, String pubKey,
            String puzHash) throws RPCException {
        throw new RPCException("did_recovery_spend was removed from Chia");
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public ApiResponse<SpendBundle> didRecoverySpend(int walletId, List<String> attestData,
            String pubKey, String puzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didRecoverySpendAsBytes(walletId, attestData, pubKey, puzHash));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, DID_RECOVERY_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didSetWalletNameAsBytes(long walletId, String name) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("name", name)
                    .buildBytes();
            var req = new Request(DID_SET_WALLET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> didSetWalletName(long walletId, String name) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didSetWalletNameAsBytes(walletId, name));
            return newResponse(jsonNode, "success", Boolean.class, DID_SET_WALLET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didTransferDIDAsBytes(long walletId, String address, long fee, boolean withRecoveryInfo,
            boolean reusePuzHash) throws RPCException {
        if (!withRecoveryInfo) {
            throw new RPCException("Chia 2.7.5 requires with_recovery_info=true");
        }
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("inner_address", address)
                    .put("with_recovery_info", withRecoveryInfo)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(DID_TRANSFER_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<TransactionStatus> didTransferDID(long walletId, String address, long fee,
            boolean withRecoveryInfo, boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    didTransferDIDAsBytes(walletId, address, fee, withRecoveryInfo, reusePuzHash)
            );
            return newResponse(jsonNode, TransactionStatus.class, DID_TRANSFER_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated(since = "2.7.5", forRemoval = true)
    public byte[] didUpdateRecoveryIdsAsBytes(int walletId, List<String> newIds,
            int numVerificationsReq, boolean reusePuzHash) throws RPCException {
        throw new RPCException("did_update_recovery_ids was removed from Chia");
    }

    ////////////////////
    /* KEY MANAGEMENT */
    ////////////////////

    @Deprecated(since = "2.7.5", forRemoval = true)
    public ApiResponse<Boolean> didUpdateRecoveryIds(int walletId, List<String> newIds,
            int numVerificationsReq, boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    didUpdateRecoveryIdsAsBytes(walletId, newIds, numVerificationsReq, reusePuzHash)
            );
            return newResponse(jsonNode, "success", Boolean.class, DID_UPDATE_RECOVERY_IDS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] addKeyAsBytes(List<String> mnemonic) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("mnemonic", mnemonic);
            var req = new Request(ADD_KEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> addKey(List<String> mnemonic) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(addKeyAsBytes(mnemonic));
            return newResponse(jsonNode, "fingerprint", Long.class, ADD_KEY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] deleteKeyAsBytes(long fingerprint) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("fingerprint", fingerprint);
            var req = new Request(DELETE_KEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteKey(long fingerprint) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteKeyAsBytes(fingerprint));
            return newResponse(jsonNode, "success", Boolean.class, DELETE_KEY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] checkDeleteKeyAsBytes(long fingerprint, int searchDepth) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("fingerprint", fingerprint)
                    .put("max_ph_to_search", searchDepth)
                    .buildBytes();
            var req = new Request(CHECK_DELETE_KEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<KeyInfo> checkDeleteKey(long fingerprint, int searchDepth) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(checkDeleteKeyAsBytes(fingerprint, searchDepth));
            return newResponse(jsonNode, KeyInfo.class, CHECK_DELETE_KEY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] deleteAllKeysAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(DELETE_ALL_KEYS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteAllKeys() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteAllKeysAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, DELETE_ALL_KEYS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] generateMnemonicAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GENERATE_MNEMONIC, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> generateMnemonic() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(generateMnemonicAsBytes());
            return newResponseList(jsonNode, "mnemonic", TypeRefs.STRING_LIST, GENERATE_MNEMONIC);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getLoggedInFingerprintAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_LOGGED_IN_FINGERPRINT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> getLoggedInFingerprint() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getLoggedInFingerprintAsBytes());
            return newResponse(jsonNode, "fingerprint", Long.class, GET_LOGGED_IN_FINGERPRINT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getPublicKeysAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_PUBLIC_KEYS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<PublicKeys> getPublicKeys() throws RPCException {
        return walletResponse(getPublicKeysAsBytes(), null, PublicKeys.class, GET_PUBLIC_KEYS);
    }

    public byte[] logInAsBytes(long fingerprint) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("fingerprint", fingerprint);
            var req = new Request(LOG_IN, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> logIn(long fingerprint) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(logInAsBytes(fingerprint));
            return newResponse(jsonNode, "success", Boolean.class, LOG_IN);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] verifySignatureAsBytes(String pubKey, String message, String signature,
            String address) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("pubkey", pubKey)
                    .put("message", message)
                    .put("signature", signature)
                    .put("address", address)
                    .buildBytes();
            var req = new Request(VERIFY_SIGNATURE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> verifySignature(String pubKey, String message, String signature,
            String address) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(verifySignatureAsBytes(pubKey, message, signature, address));
            return newResponse(jsonNode, "isValid", Boolean.class, VERIFY_SIGNATURE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    /////////////////
    /* POOL WALLET */
    /////////////////

    public byte[] pwStatusAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(PW_STATUS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<PoolWalletStatus> pwStatus(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pwStatusAsBytes(walletId));
            return newResponse(jsonNode, PoolWalletStatus.class, PW_STATUS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pwSelfPoolAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(PW_SELF_POOL, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> pwSelfPool(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pwSelfPoolAsBytes(walletId));
            return newResponse(jsonNode, "transaction", Transaction.class, PW_SELF_POOL);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pwJoinPoolAsBytes(long walletId, String targetPuzHash, String poolURL, long lockHeight,
            long fee) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("target_puzzlehash", targetPuzHash)
                    .put("pool_url", poolURL)
                    .put("relative_lock_height", lockHeight)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(PW_JOIN_POOL, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> pwJoinPool(long walletId, String targetPuzHash, String poolURL,
            long lockHeight, long fee) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    pwJoinPoolAsBytes(walletId, targetPuzHash, poolURL, lockHeight, fee)
            );
            return newResponse(jsonNode, "transaction", Transaction.class, PW_JOIN_POOL);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pwAbsorbRewardsAsBytes(long walletId, long fee) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(PW_ABSORB_REWARDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> pwAbsorbRewards(long walletId, long fee) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pwAbsorbRewardsAsBytes(walletId, fee));
            return newResponse(jsonNode, "transaction", Transaction.class, PW_ABSORB_REWARDS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ///////////////////
    /* NOTIFICATIONS */
    ///////////////////

    public byte[] deleteNotificationsAsBytes(List<String> ids) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("ids", ids);
            var req = new Request(DELETE_NOTIFICATIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteNotifications(List<String> ids) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteNotificationsAsBytes(ids));
            return newResponse(jsonNode, "success", Boolean.class, DELETE_NOTIFICATIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getNotificationsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_NOTIFICATIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Notification>> getNotifications() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNotificationsAsBytes());
            return newResponseList(
                    jsonNode, "notifications", TypeRefs.NOTIFICATION_LIST, GET_NOTIFICATIONS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] sendNotificationAsBytes(String address, String message, long amount,
            long fee) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("target", address)
                    .put("message", message)
                    .put("amount", amount)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(SEND_NOTIFICATION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> sendNotification(String address, String message, long amount,
            long fee) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendNotificationAsBytes(address, message, amount, fee));
            return newResponse(jsonNode, "tx", Transaction.class, SEND_NOTIFICATION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] signMessageByAddressAsBytes(String address, String message, boolean isHex)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("address", address)
                    .put("message", message)
                    .put("is_hex", isHex)
                    .buildBytes();
            var req = new Request(SIGN_MESSAGE_BY_ADDRESS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignedMessage> signMessageByAddress(String address, String message,
            boolean isHex) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(signMessageByAddressAsBytes(address, message, isHex));
            return newResponse(jsonNode, SignedMessage.class, SIGN_MESSAGE_BY_ADDRESS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] signMessageByIdAsBytes(String id, String message, boolean isHex)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("id", id)
                    .put("message", message)
                    .put("is_hex", isHex)
                    .buildBytes();
            var req = new Request(SIGN_MESSAGE_BY_ID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignedMessage> signMessageById(String id, String message, boolean isHex)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(signMessageByIdAsBytes(id, message, isHex));
            return newResponse(jsonNode, SignedMessage.class, SIGN_MESSAGE_BY_ID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ////////////
    /* WALLET */
    ////////////

    public byte[] createSignedTransactionAsBytes(JsonNode signedTransactionBuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(signedTransactionBuilder);
            var req = new Request(CREATE_SIGNED_TRANSACTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignedTransaction> createSignedTransaction(JsonNode signedTransactionBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createSignedTransactionAsBytes(signedTransactionBuilder));
            return newResponse(
                    jsonNode, "signed_tx", SignedTransaction.class, CREATE_SIGNED_TRANSACTION
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] deleteUnconfirmedTransactionsAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(DELETE_UNCONFIRMED_TRANSACTIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteUnconfirmedTransactions(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteUnconfirmedTransactionsAsBytes(walletId));
            return newResponse(jsonNode, "success", Boolean.class, DELETE_UNCONFIRMED_TRANSACTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] extendDerivationIndexAsBytes(long index) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("index", index);
            var req = new Request(EXTEND_DERIVATION_INDEX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> extendDerivationIndex(long index) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(extendDerivationIndexAsBytes(index));
            return newResponse(jsonNode, "index", Long.class, EXTEND_DERIVATION_INDEX);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getCurrentDerivationIndexAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_CURRENT_DERIVATION_INDEX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> getCurrentDerivationIndex() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCurrentDerivationIndexAsBytes());
            return newResponse(jsonNode, "index", Long.class, GET_CURRENT_DERIVATION_INDEX);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getFarmedAmountAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_FARMED_AMOUNT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<FarmedAmount> getFarmedAmount() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getFarmedAmountAsBytes());
            return newResponse(jsonNode, FarmedAmount.class, GET_FARMED_AMOUNT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getNextAddressAsBytes(long walletId, boolean getNewAddress) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("new_address", getNewAddress)
                    .buildBytes();
            var req = new Request(GET_NEXT_ADDRESS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> getNextAddress(long walletId, boolean getNewAddress) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNextAddressAsBytes(walletId, getNewAddress));
            return newResponse(jsonNode, "address", String.class, GET_NEXT_ADDRESS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getSpendableCoinsAsBytes(JsonNode spendableCoinbuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(spendableCoinbuilder);
            var req = new Request(GET_SPENDABLE_COINS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendableCoins> getSpendableCoins(JsonNode spendableCoinbuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getSpendableCoinsAsBytes(spendableCoinbuilder));
            return newResponse(jsonNode, SpendableCoins.class, GET_SPENDABLE_COINS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTransactionAsBytes(String transactionId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("transaction_id", transactionId);
            var req = new Request(GET_TRANSACTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> getTransaction(String transactionId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTransactionAsBytes(transactionId));
            return newResponse(jsonNode, "transaction", Transaction.class, GET_TRANSACTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTransactionsAsBytes(long walletId, long start, long end, boolean reversed,
            String sortKey) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("start", start)
                    .put("end", end)
                    .put("reverse", reversed)
                    .buildNode();
            if (sortKey != null) { data.put("sort_key", sortKey); }
            var req = new Request(GET_TRANSACTIONS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Transaction>> getTransactions(long walletId, long start, long end, boolean reversed,
            String sortKey) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getTransactionsAsBytes(walletId, start, end, reversed, sortKey)
            );
            return newResponseList(jsonNode, "transactions", TypeRefs.TRANSACTIONS_LIST, GET_TRANSACTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<List<Transaction>> getTransactions(long walletId, long start, long end, boolean reversed)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getTransactionsAsBytes(walletId, start, end, reversed, null)
            );
            return newResponseList(
                    jsonNode, "transactions", TypeRefs.TRANSACTIONS_LIST, GET_TRANSACTIONS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTransactionMemoAsBytes(String transactionId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("transaction_id", transactionId);
            var req = new Request(GET_TRANSACTION_MEMO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> getTransactionMemo(String transactionId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTransactionMemoAsBytes(transactionId));
            return newResponseNode(jsonNode, GET_TRANSACTION_MEMO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTransactionCountAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(GET_TRANSACTION_COUNT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> getTransactionCount(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTransactionCountAsBytes(walletId));
            return newResponseNode(jsonNode, GET_TRANSACTION_COUNT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getWalletBalanceAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(GET_WALLET_BALANCE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<WalletBalance> getWalletBalance(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getWalletBalanceAsBytes(walletId));
            return newResponse(jsonNode, "wallet_balance", WalletBalance.class, GET_WALLET_BALANCE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getWalletBalancesAsBytes(List<? extends Number> walletIds) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_ids", walletIds);
            var req = new Request(GET_WALLET_BALANCES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<WalletBalance>> getWalletBalances(List<? extends Number> walletIds) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getWalletBalancesAsBytes(walletIds));
            if (!jsonNode.path("success").asBoolean()) {
                return newFailedResponse(jsonNode, GET_WALLET_BALANCES);
            }
            Map<String, WalletBalance> balances = JsonUtils.readJson(
                    jsonNode.path("wallet_balances").traverse(), new TypeReference<Map<String, WalletBalance>>() {});
            return newResponse(List.copyOf(balances.values()), GET_WALLET_BALANCES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] sendTransactionAsBytes(long walletId, String address, long amount, long fee)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("address", address)
                    .put("amount", amount)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(SEND_TRANSACTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public byte[] sendTransactionAsBytes(JsonNode transactionbuilder)
            throws RPCException {
        try {
            var req = new Request(SEND_TRANSACTION, JsonUtils.writeBytes(transactionbuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> sendTransaction(JsonNode transactionBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendTransactionAsBytes(transactionBuilder));
            return newResponse(jsonNode, "transaction", Transaction.class, SEND_TRANSACTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<Transaction> sendTransaction(long walletId, String address, long amount, long fee)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendTransactionAsBytes(walletId, address, amount, fee));
            return newResponse(jsonNode, "transaction", Transaction.class, SEND_TRANSACTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] sendTransactionMultiAsBytes(JsonNode multiTransactionBuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(multiTransactionBuilder);
            var req = new Request(SEND_TRANSACTION_MULTI, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> sendTransactionMulti(JsonNode multiTransactionBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendTransactionMultiAsBytes(multiTransactionBuilder));
            return newResponse(jsonNode, "transaction", Transaction.class, SEND_TRANSACTION_MULTI);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getHeightInfoAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_HEIGHT_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> getHeightInfo() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getHeightInfoAsBytes());
            return newResponse(jsonNode, "height", Long.class, GET_HEIGHT_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getNetworkInfoAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_NETWORK_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Network> getNetworkInfo() throws RPCException {
        return walletResponse(getNetworkInfoAsBytes(), null, Network.class, GET_NETWORK_INFO);
    }

    public byte[] getSyncStatusAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_SYNC_STATUS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SyncStatus> getSyncStatus() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getSyncStatusAsBytes());
            return newResponse(jsonNode, SyncStatus.class, GET_SYNC_STATUS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTimestampForHeightAsBytes(long height) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("height", height);
            var req = new Request(GET_TIMESTAMP_FOR_HEIGHT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> getTimestampForHeight(long height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTimestampForHeightAsBytes(height));
            return newResponse(jsonNode, "timestamp", String.class, GET_TIMESTAMP_FOR_HEIGHT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated(since = "2.7.5")
    public byte[] pushTransactionsAsBytes(List<SimpleTransaction> transactions) throws RPCException {
        throw new RPCException("push_transactions requires complete Transaction records; use the JsonNode overload");
    }

    @Deprecated(since = "2.7.5")
    public ApiResponse<Boolean> pushTransactions(List<SimpleTransaction> transactions) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pushTransactionsAsBytes(transactions));
            return newResponse(jsonNode, "success", Boolean.class, PUSH_TRANSACTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pushTxAsBytes(SpendBundle spendBundle) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("spend_bundle", spendBundle);
            var req = new Request(PUSH_TX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> pushTx(SpendBundle spendBundle) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pushTxAsBytes(spendBundle));
            return newResponse(jsonNode, "success", Boolean.class, PUSH_TX);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] setWalletReSyncOnStartupAsBytes(boolean enable) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("enable", enable);
            var req = new Request(SET_WALLET_RESYNC_ON_STARTUP, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> setWalletReSyncOnStartup(boolean enable) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(setWalletReSyncOnStartupAsBytes(enable));
            return newResponse(jsonNode, "success", Boolean.class, SET_WALLET_RESYNC_ON_STARTUP);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ////////////////
    /* NFT Wallet */
    ////////////////

    public byte[] nftAddUriAsBytes(long walletId, String uri, NftDataKey dataKey, String nftCoinId, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("uri", uri)
                    .put("key", dataKey.key)
                    .put("nft_coin_id", nftCoinId)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(NFT_ADD_URI, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftAddUri(long walletId, String uri, NftDataKey dataKey, String nftCoinId, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftAddUriAsBytes(
                    walletId, uri, dataKey, nftCoinId, fee, reusePuzHash)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, NFT_ADD_URI);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftCalculateRoyaltiesAsBytes(@Nullable List<RoyaltyAsset> royaltyAssets,
            @Nullable List<FungibleAsset> fungibleAssets) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder().buildNode();
            if (royaltyAssets != null) { data.putPOJO("royalty_assets", royaltyAssets); }
            if (fungibleAssets != null) { data.putPOJO("fungible_assets", fungibleAssets); }
            var req = new Request(NFT_CALCULATE_ROYALTIES, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Map<String, List<RoyaltyCalculation>>> nftCalculateRoyalties(
            @Nullable List<RoyaltyAsset> royaltyAssets, @Nullable List<FungibleAsset> fungibleAssets)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftCalculateRoyaltiesAsBytes(royaltyAssets, fungibleAssets));
            if (!jsonNode.path("success").asBoolean()) return newFailedResponse(jsonNode, NFT_CALCULATE_ROYALTIES);
            var royalties = JsonUtils.newEmptyNode();
            jsonNode.fields().forEachRemaining(entry -> {
                if (!entry.getKey().equals("success")) royalties.set(entry.getKey(), entry.getValue());
            });
            Map<String, List<RoyaltyCalculation>> result = JsonUtils.readJson(royalties.traverse(), TypeRefs.ROYALTY_MAP);
            return newResponse(java.util.Collections.unmodifiableMap(result), NFT_CALCULATE_ROYALTIES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftCountNftsAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(NFT_COUNT_NFTS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<java.math.BigInteger> nftCountNfts(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftCountNftsAsBytes(walletId));
            return newResponse(jsonNode, "count", java.math.BigInteger.class, NFT_COUNT_NFTS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetByDidAsBytes(String did) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("did_id", did);
            var req = new Request(NFT_GET_BY_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> nftGetByDid(String did) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetByDidAsBytes(did));
            return newResponse(jsonNode, "wallet_id", Long.class, NFT_GET_BY_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    @Deprecated(since = "2.7.5")
    public byte[] nftGetInfoAsBytes(String coinId, @Nullable Integer walletId) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder().buildNode();
            data.put("coin_id", coinId);
            var req = new Request(NFT_GET_INFO, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    @Deprecated(since = "2.7.5")
    public ApiResponse<NftInfo> nftGetInfo(String coinId, Integer walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetInfoAsBytes(coinId, walletId));
            return newResponse(jsonNode, "nft_info", NftInfo.class, NFT_GET_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<NftInfo> nftGetInfo(String coinId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetInfoAsBytes(coinId, null));
            return newResponse(jsonNode, "nft_info", NftInfo.class, NFT_GET_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetNftsAsBytes(long walletId, long startIdx, long amountLimit) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("start_index", startIdx)
                    .put("num", amountLimit)
                    .buildBytes();
            var req = new Request(NFT_GET_NFTS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<NftInfo>> nftGetNfts(long walletId, long startIdx, long amountLimit) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetNftsAsBytes(walletId, startIdx, amountLimit));
            return newResponseList(jsonNode, "nft_list", TypeRefs.NFT_INFO_LIST, NFT_GET_NFTS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetWalletDidAsBytes(long walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(NFT_GET_WALLET_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> nftGetWalletDid(long walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetWalletDidAsBytes(walletId));
            return newResponse(jsonNode, "did_id", String.class, NFT_GET_WALLET_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetWalletsWithDidsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(NFT_GET_WALLETS_WITH_DIDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<NftWallet>> nftGetWalletsWithDids() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetWalletsWithDidsAsBytes());
            return newResponseList(
                    jsonNode, "nft_wallets", TypeRefs.NFT_WALLET_LIST, NFT_GET_WALLETS_WITH_DIDS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftMintBulkAsBytes(JsonNode bulkMintBuilder) throws RPCException {
        try {
            var req = new Request(NFT_MINT_BULK, JsonUtils.writeBytes(bulkMintBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<NftBundle> nftMintBulk(JsonNode bulkMintBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftMintBulkAsBytes(bulkMintBuilder));
            return newResponse(jsonNode, NftBundle.class, Wallet.NFT_MINT_BULK);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }
    public byte[] nftMintNftAsBytes(JsonNode singleMintbuilder) throws RPCException {
        try {
            var req = new Request(NFT_MINT_NFT, JsonUtils.writeBytes(singleMintbuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftMintNft(JsonNode singleMintbuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftMintNftAsBytes(singleMintbuilder));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, NFT_MINT_NFT
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftSetDidBulkAsBytes(JsonNode setDidBulkBuilder) throws RPCException {
        try {
            var req = new Request(NFT_SET_DID_BULK, JsonUtils.writeBytes(setDidBulkBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftSetDidBulk(JsonNode setdidBulkBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftSetDidBulkAsBytes(setdidBulkBuilder));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, NFT_SET_DID_BULK
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftSetNftDidAsBytes(long walletId, String nftCoinId, String did, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("nft_coin_id", nftCoinId)
                    .put("did_id", did)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(NFT_SET_NFT_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftSetNftDid(long walletId, String nftCoinId, String did, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftSetNftDidAsBytes(
                    walletId, nftCoinId, did, fee, reusePuzHash)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, NFT_SET_NFT_DID);
        } catch (
                IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftSetNftStatusAsBytes(long walletId, String nftCoinId, boolean inTransaction)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("coin_id", nftCoinId)
                    .put("in_transaction", inTransaction)
                    .buildBytes();
            var req = new Request(NFT_SET_NFT_STATUS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> nftSetNftStatus(long walletId, String nftCoinId, boolean inTransaction)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftSetNftStatusAsBytes(walletId, nftCoinId, inTransaction));
            return newResponse(jsonNode, "success", Boolean.class, NFT_SET_NFT_STATUS);
        } catch (
                IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftTransferBulkAsBytes(JsonNode jsonNode) throws RPCException {
        try {
            var req = new Request(NFT_TRANSFER_BULK, JsonUtils.writeBytes(jsonNode));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftTransferBulk(JsonNode json) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftTransferBulkAsBytes(json));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, NFT_TRANSFER_BULK
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftTransferNftAsBytes(long walletId, String nftCoinId, String targetAddress, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("nft_coin_id", nftCoinId)
                    .put("target_address", targetAddress)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(NFT_TRANSFER_NFT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftTransferNft(long walletId, String nftCoinId, String targetAddress, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftTransferNftAsBytes(
                    walletId, nftCoinId, targetAddress, fee, reusePuzHash)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, NFT_TRANSFER_NFT);
        } catch (
                IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ///////////
    /* Coins */
    ///////////

    public byte[] getCoinRecordsAsBytes(JsonNode coinRecordBuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(coinRecordBuilder);
            var req = new Request(Wallet.GET_COIN_RECORDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<WalletCoinRecords> getCoinRecords(JsonNode coinRecordBuilder) throws RPCException {
        return walletResponse(getCoinRecordsAsBytes(coinRecordBuilder), null, WalletCoinRecords.class, GET_COIN_RECORDS);
    }

    public byte[] getCoinRecordsByNamesAsBytes(List<String> names, long startHeight, long endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("names", names)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spent_coins", includeSpent)
                    .buildBytes();
            var req = new Request(GET_COIN_RECORDS_BY_NAMES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByNames(List<String> names, long startHeight, long endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getCoinRecordsByNamesAsBytes(names, startHeight, endHeight, includeSpent)
            );
            return newResponseList(
                    jsonNode, "coin_records", TypeRefs.COIN_RECORD_LIST, Wallet.GET_COIN_RECORDS_BY_NAMES
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }



    //////////////
    // Clawback //
    //////////////

    public byte[] spendClawbackCoinsAsBytes(List<String> coinIds, long fee, int batchSize) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("coin_ids", coinIds)
                    .put("fee", fee)
                    .put("batch_size", batchSize)
                    .buildBytes();
            var req = new Request(SPEND_CLAWBACK_COINS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> spendClawbackCoins(List<String> coinIds, long fee, int batchSize)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(spendClawbackCoinsAsBytes(coinIds, fee, batchSize));
            return newResponseList(
                    jsonNode, "transaction_ids", TypeRefs.STRING_LIST, Wallet.SPEND_CLAWBACK_COINS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getAutoClaimAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(GET_AUTO_CLAIM, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<AutoClaim> getAutoClaim() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAutoClaimAsBytes());
            return newResponse(jsonNode, AutoClaim.class, Wallet.GET_AUTO_CLAIM);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] setAutoClaimAsBytes(boolean enabled, long txFee, long minAmount, int batchSize)
            throws RPCException {
        return setAutoClaimAsBytes(enabled, BigInteger.valueOf(txFee), BigInteger.valueOf(minAmount), batchSize);
    }

    public byte[] setAutoClaimAsBytes(boolean enabled, BigInteger txFee, BigInteger minAmount, int batchSize)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("enabled", enabled)
                    .put("tx_fee", txFee)
                    .put("min_amount", minAmount)
                    .put("batch_size", batchSize)
                    .buildBytes();
            var req = new Request(SET_AUTO_CLAIM, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<AutoClaim> setAutoClaim(boolean enabled, long txFee, long minAmount, int batchSize)
            throws RPCException {
        return setAutoClaim(enabled, BigInteger.valueOf(txFee), BigInteger.valueOf(minAmount), batchSize);
    }

    public ApiResponse<AutoClaim> setAutoClaim(boolean enabled, BigInteger txFee, BigInteger minAmount, int batchSize)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(setAutoClaimAsBytes(enabled, txFee, minAmount, batchSize));
            return newResponse(jsonNode, AutoClaim.class, Wallet.SET_AUTO_CLAIM);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] selectCoinsAsBytes(long walletId, long amount) throws RPCException {
        return selectCoinsAsBytes(new JsonUtils.ObjectBuilder().put("wallet_id", walletId).put("amount", amount).buildNode());
    }

    public byte[] getCoinRecordsByNamesAsBytes(JsonNode request) throws RPCException {
        return walletRequest(GET_COIN_RECORDS_BY_NAMES, request);
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByNames(JsonNode request) throws RPCException {
        return walletList(getCoinRecordsByNamesAsBytes(request), "coin_records", new TypeReference<List<CoinRecord>>() {}, GET_COIN_RECORDS_BY_NAMES);
    }

    public byte[] takeOfferAsBytes(String offer, long fee) throws RPCException {
        return takeOfferAsBytes(new JsonUtils.ObjectBuilder().put("offer", offer).put("fee", fee).buildNode());
    }

    public byte[] getAllOffersAsBytes() throws RPCException {
        return getAllOffersAsBytes(JsonUtils.newEmptyNode());
    }

    public byte[] nftGetInfoAsBytes(String coinId) throws RPCException {
        return nftGetInfoAsBytes(coinId, true);
    }

    public byte[] getTransactionsAsBytes(long walletId, long start, long end, boolean reverse) throws RPCException {
        return getTransactionsAsBytes(walletId, start, end, reverse, null);
    }

    public byte[] getOfferAsBytes(String tradeId, boolean fileContents) throws RPCException {
        return walletRequest(GET_OFFER, new JsonUtils.ObjectBuilder().put("trade_id", tradeId)
                .put("file_contents", fileContents).buildNode());
    }

    public ApiResponse<Offer> getOffer(String tradeId, boolean fileContents) throws RPCException {
        return walletResponse(getOfferAsBytes(tradeId, fileContents), null, Offer.class, GET_OFFER);
    }

    public byte[] getHeightInfoAsBytes(boolean usePeakHeight) throws RPCException {
        return walletRequest(GET_HEIGHT_INFO, JsonUtils.newSingleNode("use_peak_height", usePeakHeight));
    }

    public ApiResponse<HeightInfo> getHeightInfo(boolean usePeakHeight) throws RPCException {
        return walletResponse(getHeightInfoAsBytes(usePeakHeight), null, HeightInfo.class, GET_HEIGHT_INFO);
    }

    public byte[] getTransactionsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(GET_TRANSACTIONS, request);
    }

    public ApiResponse<List<Transaction>> getTransactions(JsonNode request) throws RPCException {
        return walletList(getTransactionsAsBytes(request), "transactions", new TypeReference<List<Transaction>>() {}, GET_TRANSACTIONS);
    }

    public byte[] getTransactionCountAsBytes(JsonNode request) throws RPCException {
        return walletRequest(GET_TRANSACTION_COUNT, request);
    }

    public ApiResponse<Long> getTransactionCount(JsonNode request) throws RPCException {
        return walletResponse(getTransactionCountAsBytes(request), "count", Long.class, GET_TRANSACTION_COUNT);
    }

    public byte[] getNotificationsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(GET_NOTIFICATIONS, request);
    }

    public ApiResponse<List<Notification>> getNotifications(JsonNode request) throws RPCException {
        return walletList(getNotificationsAsBytes(request), "notifications", new TypeReference<List<Notification>>() {}, GET_NOTIFICATIONS);
    }

    public byte[] getNextAddressAsBytes(long walletId, boolean newAddress, boolean saveDerivations) throws RPCException {
        return walletRequest(GET_NEXT_ADDRESS, new JsonUtils.ObjectBuilder().put("wallet_id", walletId)
                .put("new_address", newAddress).put("save_derivations", saveDerivations).buildNode());
    }

    public ApiResponse<String> getNextAddress(long walletId, boolean newAddress, boolean saveDerivations) throws RPCException {
        return walletResponse(getNextAddressAsBytes(walletId, newAddress, saveDerivations), "address", String.class, GET_NEXT_ADDRESS);
    }

    public byte[] getFarmedAmountAsBytes(boolean includePoolRewards) throws RPCException {
        return walletRequest(GET_FARMED_AMOUNT, JsonUtils.newSingleNode("include_pool_rewards", includePoolRewards));
    }

    public ApiResponse<FarmedAmount> getFarmedAmount(boolean includePoolRewards) throws RPCException {
        return walletResponse(getFarmedAmountAsBytes(includePoolRewards), null, FarmedAmount.class, GET_FARMED_AMOUNT);
    }

    public byte[] addKeyAsBytes(List<String> mnemonic, @Nullable String label) throws RPCException {
        var request = JsonUtils.newSingleNode("mnemonic", mnemonic);
        if (label != null) request.put("label", label);
        return walletRequest(ADD_KEY, request);
    }

    public ApiResponse<Long> addKey(List<String> mnemonic, @Nullable String label) throws RPCException {
        return walletResponse(addKeyAsBytes(mnemonic, label), "fingerprint", Long.class, ADD_KEY);
    }

    public byte[] verifySignatureAsBytes(String pubKey, String message, String signature,
            @Nullable String address, @Nullable String signingMode) throws RPCException {
        var request = new JsonUtils.ObjectBuilder().put("pubkey", pubKey).put("message", message)
                .put("signature", signature).buildNode();
        if (address != null) request.put("address", address);
        if (signingMode != null) request.put("signing_mode", signingMode);
        return walletRequest(VERIFY_SIGNATURE, request);
    }

    public ApiResponse<Boolean> verifySignature(String pubKey, String message, String signature,
            @Nullable String address, @Nullable String signingMode) throws RPCException {
        return walletResponse(verifySignatureAsBytes(pubKey, message, signature, address, signingMode),
                "isValid", Boolean.class, VERIFY_SIGNATURE);
    }

    public byte[] signMessageByAddressAsBytes(String address, String message, boolean isHex, boolean safeMode) throws RPCException {
        return walletRequest(SIGN_MESSAGE_BY_ADDRESS, new JsonUtils.ObjectBuilder().put("address", address)
                .put("message", message).put("is_hex", isHex).put("safe_mode", safeMode).buildNode());
    }

    public ApiResponse<SignedMessage> signMessageByAddress(String address, String message,
            boolean isHex, boolean safeMode) throws RPCException {
        return walletResponse(signMessageByAddressAsBytes(address, message, isHex, safeMode), null, SignedMessage.class, SIGN_MESSAGE_BY_ADDRESS);
    }

    public byte[] signMessageByIdAsBytes(String id, String message, boolean isHex, boolean safeMode) throws RPCException {
        return walletRequest(SIGN_MESSAGE_BY_ID, new JsonUtils.ObjectBuilder().put("id", id)
                .put("message", message).put("is_hex", isHex).put("safe_mode", safeMode).buildNode());
    }

    public ApiResponse<SignedMessage> signMessageById(String id, String message, boolean isHex, boolean safeMode) throws RPCException {
        return walletResponse(signMessageByIdAsBytes(id, message, isHex, safeMode), null, SignedMessage.class, SIGN_MESSAGE_BY_ID);
    }

    public byte[] nftGetInfoAsBytes(String coinId, boolean latest) throws RPCException {
        return walletRequest(NFT_GET_INFO, new JsonUtils.ObjectBuilder().put("coin_id", coinId).put("latest", latest).buildNode());
    }

    public ApiResponse<NftInfo> nftGetInfo(String coinId, boolean latest) throws RPCException {
        return walletResponse(nftGetInfoAsBytes(coinId, latest), "nft_info", NftInfo.class, NFT_GET_INFO);
    }

    public byte[] nftGetNftsAsBytes(@Nullable Integer walletId, long startIndex, long num) throws RPCException {
        var request = new JsonUtils.ObjectBuilder().put("start_index", startIndex).put("num", num).buildNode();
        if (walletId != null) request.put("wallet_id", walletId);
        return walletRequest(NFT_GET_NFTS, request);
    }

    public ApiResponse<List<NftInfo>> nftGetNfts(@Nullable Integer walletId, long startIndex, long num) throws RPCException {
        return walletList(nftGetNftsAsBytes(walletId, startIndex, num), "nft_list", new TypeReference<List<NftInfo>>() {}, NFT_GET_NFTS);
    }

    public byte[] nftCountNftsAsBytes(@Nullable Integer walletId) throws RPCException {
        return walletRequest(NFT_COUNT_NFTS, walletId == null ? JsonUtils.newEmptyNode() : JsonUtils.newSingleNode("wallet_id", walletId));
    }

    public ApiResponse<java.math.BigInteger> nftCountNfts(@Nullable Integer walletId) throws RPCException {
        return walletResponse(nftCountNftsAsBytes(walletId), "count", java.math.BigInteger.class, NFT_COUNT_NFTS);
    }

    public byte[] didGetInfoAsBytes(String coinId, boolean latest) throws RPCException {
        return walletRequest(DID_GET_INFO, new JsonUtils.ObjectBuilder().put("coin_id", coinId).put("latest", latest).buildNode());
    }

    public ApiResponse<DIDInfo> didGetInfo(String coinId, boolean latest) throws RPCException {
        return walletResponse(didGetInfoAsBytes(coinId, latest), null, DIDInfo.class, DID_GET_INFO);
    }

    public byte[] didFindLostDIDAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DID_FIND_LOST_DID, request);
    }

    public ApiResponse<String> didFindLostDID(JsonNode request) throws RPCException {
        return walletResponse(didFindLostDIDAsBytes(request), "latest_coin_id", String.class, DID_FIND_LOST_DID);
    }

    public byte[] cancelOfferAsBytes(JsonNode request) throws RPCException {
        return walletRequest(CANCEL_OFFER, request);
    }

    public ApiResponse<TransactionResult> cancelOffer(JsonNode request) throws RPCException {
        return walletResponse(cancelOfferAsBytes(request), null, TransactionResult.class, CANCEL_OFFER);
    }

    public byte[] cancelOffersAsBytes(JsonNode request) throws RPCException {
        return walletRequest(CANCEL_OFFERS, request);
    }

    public ApiResponse<TransactionResult> cancelOffers(JsonNode request) throws RPCException {
        return walletResponse(cancelOffersAsBytes(request), null, TransactionResult.class, CANCEL_OFFERS);
    }

    public byte[] pushTransactionsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(PUSH_TRANSACTIONS, request);
    }

    public ApiResponse<TransactionResult> pushTransactions(JsonNode request) throws RPCException {
        return walletResponse(pushTransactionsAsBytes(request), null, TransactionResult.class, PUSH_TRANSACTIONS);
    }

    public byte[] spendClawbackCoinsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(SPEND_CLAWBACK_COINS, request);
    }

    public ApiResponse<List<String>> spendClawbackCoins(JsonNode request) throws RPCException {
        return walletList(spendClawbackCoinsAsBytes(request), "transaction_ids", new TypeReference<List<String>>() {}, SPEND_CLAWBACK_COINS);
    }

    public byte[] didUpdateMetaDataAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DID_UPDATE_METADATA, request);
    }

    public ApiResponse<SpendBundle> didUpdateMetaData(JsonNode request) throws RPCException {
        return walletResponse(didUpdateMetaDataAsBytes(request), "spend_bundle", SpendBundle.class, DID_UPDATE_METADATA);
    }

    public byte[] didMessageSpendAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DID_MESSAGE_SPEND, request);
    }

    public ApiResponse<SpendBundle> didMessageSpend(JsonNode request) throws RPCException {
        return walletResponse(didMessageSpendAsBytes(request), "spend_bundle", SpendBundle.class, DID_MESSAGE_SPEND);
    }

    public byte[] didTransferDIDAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DID_TRANSFER_DID, request);
    }

    public ApiResponse<TransactionStatus> didTransferDID(JsonNode request) throws RPCException {
        return walletResponse(didTransferDIDAsBytes(request), null, TransactionStatus.class, DID_TRANSFER_DID);
    }

    public byte[] pwJoinPoolAsBytes(JsonNode request) throws RPCException {
        return walletRequest(PW_JOIN_POOL, request);
    }

    public ApiResponse<Transaction> pwJoinPool(JsonNode request) throws RPCException {
        return walletResponse(pwJoinPoolAsBytes(request), "transaction", Transaction.class, PW_JOIN_POOL);
    }

    public byte[] pwSelfPoolAsBytes(JsonNode request) throws RPCException {
        return walletRequest(PW_SELF_POOL, request);
    }

    public ApiResponse<Transaction> pwSelfPool(JsonNode request) throws RPCException {
        return walletResponse(pwSelfPoolAsBytes(request), "transaction", Transaction.class, PW_SELF_POOL);
    }

    public byte[] pwAbsorbRewardsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(PW_ABSORB_REWARDS, request);
    }

    public ApiResponse<Transaction> pwAbsorbRewards(JsonNode request) throws RPCException {
        return walletResponse(pwAbsorbRewardsAsBytes(request), "transaction", Transaction.class, PW_ABSORB_REWARDS);
    }

    public byte[] sendNotificationAsBytes(JsonNode request) throws RPCException {
        return walletRequest(SEND_NOTIFICATION, request);
    }

    public ApiResponse<Transaction> sendNotification(JsonNode request) throws RPCException {
        return walletResponse(sendNotificationAsBytes(request), "tx", Transaction.class, SEND_NOTIFICATION);
    }

    public byte[] nftAddUriAsBytes(JsonNode request) throws RPCException {
        return walletRequest(NFT_ADD_URI, request);
    }

    public ApiResponse<SpendBundle> nftAddUri(JsonNode request) throws RPCException {
        return walletResponse(nftAddUriAsBytes(request), "spend_bundle", SpendBundle.class, NFT_ADD_URI);
    }

    public byte[] nftSetNftDidAsBytes(JsonNode request) throws RPCException {
        return walletRequest(NFT_SET_NFT_DID, request);
    }

    public ApiResponse<SpendBundle> nftSetNftDid(JsonNode request) throws RPCException {
        return walletResponse(nftSetNftDidAsBytes(request), "spend_bundle", SpendBundle.class, NFT_SET_NFT_DID);
    }

    public byte[] nftTransferNftAsBytes(JsonNode request) throws RPCException {
        return walletRequest(NFT_TRANSFER_NFT, request);
    }

    public ApiResponse<SpendBundle> nftTransferNft(JsonNode request) throws RPCException {
        return walletResponse(nftTransferNftAsBytes(request), "spend_bundle", SpendBundle.class, NFT_TRANSFER_NFT);
    }

    private byte[] walletRequest(Wallet endpoint, JsonNode request) throws RPCException {
        if (request == null || !request.isObject()) {
            throw new RPCException("Wallet request must be a JSON object");
        }
        try {
            return client.makeRequest(new Request(endpoint, JsonUtils.writeBytes(request)));
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    private <T> ApiResponse<T> walletResponse(byte[] response, String field, Class<T> type,
            Wallet endpoint) throws RPCException {
        try {
            return field == null ? newResponse(JsonUtils.readTree(response), type, endpoint)
                    : newResponse(JsonUtils.readTree(response), field, type, endpoint);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    private <T> ApiResponse<List<T>> walletList(byte[] response, String field,
            TypeReference<List<T>> type, Wallet endpoint) throws RPCException {
        try {
            return newResponseList(JsonUtils.readTree(response), field, type, endpoint);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getPrivateKeyAsBytes(long fingerprint) throws RPCException {
        return walletRequest(GET_PRIVATE_KEY, JsonUtils.newSingleNode("fingerprint", fingerprint));
    }

    public ApiResponse<PrivateKey> getPrivateKey(long fingerprint) throws RPCException {
        return walletResponse(getPrivateKeyAsBytes(fingerprint), "private_key", PrivateKey.class, GET_PRIVATE_KEY);
    }

    public byte[] getFullNodePeerCountAsBytes() throws RPCException {
        return walletRequest(GET_FULL_NODE_PEER_COUNT, JsonUtils.newEmptyNode());
    }

    public ApiResponse<java.math.BigInteger> getFullNodePeerCount() throws RPCException {
        return walletResponse(getFullNodePeerCountAsBytes(), "peer_count", java.math.BigInteger.class, GET_FULL_NODE_PEER_COUNT);
    }

    public byte[] getFeeEstimateAsBytes() throws RPCException {
        return walletRequest(GET_FEE_ESTIMATE, JsonUtils.newEmptyNode());
    }

    public ApiResponse<java.math.BigInteger> getFeeEstimate() throws RPCException {
        return walletResponse(getFeeEstimateAsBytes(), "fee_per_cost", java.math.BigInteger.class, GET_FEE_ESTIMATE);
    }

    public byte[] getPuzzleAndSolutionAsBytes(String coinName) throws RPCException {
        return walletRequest(GET_PUZZLE_AND_SOLUTION, JsonUtils.newSingleNode("coin_name", coinName));
    }

    public ApiResponse<PuzzleAndSolution> getPuzzleAndSolution(String coinName) throws RPCException {
        return walletResponse(getPuzzleAndSolutionAsBytes(coinName), null, PuzzleAndSolution.class, GET_PUZZLE_AND_SOLUTION);
    }

    public byte[] splitCoinsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(SPLIT_COINS, request);
    }

    public ApiResponse<TransactionResult> splitCoins(JsonNode request) throws RPCException {
        return walletResponse(splitCoinsAsBytes(request), null, TransactionResult.class, SPLIT_COINS);
    }

    public byte[] splitCoinsAsBytes(long walletId, int numberOfCoins, long amountPerCoin,
            String targetCoinId, long fee) throws RPCException {
        return splitCoinsAsBytes(new JsonUtils.ObjectBuilder().put("wallet_id", walletId)
                .put("number_of_coins", numberOfCoins).put("amount_per_coin", amountPerCoin)
                .put("target_coin_id", targetCoinId).put("fee", fee).buildNode());
    }

    public ApiResponse<TransactionResult> splitCoins(long walletId, int numberOfCoins, long amountPerCoin,
            String targetCoinId, long fee) throws RPCException {
        return walletResponse(splitCoinsAsBytes(walletId, numberOfCoins, amountPerCoin, targetCoinId, fee),
                null, TransactionResult.class, SPLIT_COINS);
    }

    public byte[] combineCoinsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(COMBINE_COINS, request);
    }

    public ApiResponse<TransactionResult> combineCoins(JsonNode request) throws RPCException {
        return walletResponse(combineCoinsAsBytes(request), null, TransactionResult.class, COMBINE_COINS);
    }

    public byte[] registerRemoteCoinsAsBytes(long walletId, List<String> coinIds) throws RPCException {
        return walletRequest(REGISTER_REMOTE_COINS, new JsonUtils.ObjectBuilder()
                .put("wallet_id", walletId).put("coin_ids", coinIds).buildNode());
    }

    public ApiResponse<Boolean> registerRemoteCoins(long walletId, List<String> coinIds) throws RPCException {
        return walletResponse(registerRemoteCoinsAsBytes(walletId, coinIds), "success", Boolean.class, REGISTER_REMOTE_COINS);
    }

    public byte[] createNewDlAsBytes(JsonNode request) throws RPCException {
        return walletRequest(CREATE_NEW_DL, request);
    }

    public ApiResponse<CreateDataLayerResult> createNewDl(JsonNode request) throws RPCException {
        return walletResponse(createNewDlAsBytes(request), null, CreateDataLayerResult.class, CREATE_NEW_DL);
    }

    public byte[] createNewDlAsBytes(String root, long fee) throws RPCException {
        return createNewDlAsBytes(new JsonUtils.ObjectBuilder().put("root", root).put("fee", fee).buildNode());
    }

    public ApiResponse<CreateDataLayerResult> createNewDl(String root, long fee) throws RPCException {
        return walletResponse(createNewDlAsBytes(root, fee), null, CreateDataLayerResult.class, CREATE_NEW_DL);
    }

    public byte[] dlTrackNewAsBytes(String launcherId) throws RPCException {
        return walletRequest(DL_TRACK_NEW, JsonUtils.newSingleNode("launcher_id", launcherId));
    }

    public ApiResponse<Boolean> dlTrackNew(String launcherId) throws RPCException {
        return walletResponse(dlTrackNewAsBytes(launcherId), "success", Boolean.class, DL_TRACK_NEW);
    }

    public byte[] dlStopTrackingAsBytes(String launcherId) throws RPCException {
        return walletRequest(DL_STOP_TRACKING, JsonUtils.newSingleNode("launcher_id", launcherId));
    }

    public ApiResponse<Boolean> dlStopTracking(String launcherId) throws RPCException {
        return walletResponse(dlStopTrackingAsBytes(launcherId), "success", Boolean.class, DL_STOP_TRACKING);
    }

    public byte[] dlLatestSingletonAsBytes(String launcherId, boolean onlyConfirmed) throws RPCException {
        return walletRequest(DL_LATEST_SINGLETON, new JsonUtils.ObjectBuilder()
                .put("launcher_id", launcherId).put("only_confirmed", onlyConfirmed).buildNode());
    }

    public ApiResponse<Singleton> dlLatestSingleton(String launcherId, boolean onlyConfirmed) throws RPCException {
        return walletResponse(dlLatestSingletonAsBytes(launcherId, onlyConfirmed), "singleton", Singleton.class, DL_LATEST_SINGLETON);
    }

    public byte[] dlSingletonsByRootAsBytes(String launcherId, String root) throws RPCException {
        return walletRequest(DL_SINGLETONS_BY_ROOT, new JsonUtils.ObjectBuilder()
                .put("launcher_id", launcherId).put("root", root).buildNode());
    }

    public ApiResponse<List<Singleton>> dlSingletonsByRoot(String launcherId, String root) throws RPCException {
        return walletList(dlSingletonsByRootAsBytes(launcherId, root), "singletons",
                new TypeReference<List<Singleton>>() {}, DL_SINGLETONS_BY_ROOT);
    }

    public byte[] dlUpdateRootAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DL_UPDATE_ROOT, request);
    }

    public ApiResponse<UpdateDataLayerResult> dlUpdateRoot(JsonNode request) throws RPCException {
        return walletResponse(dlUpdateRootAsBytes(request), null, UpdateDataLayerResult.class, DL_UPDATE_ROOT);
    }

    public byte[] dlUpdateRootAsBytes(String launcherId, String newRoot, long fee) throws RPCException {
        return dlUpdateRootAsBytes(new JsonUtils.ObjectBuilder().put("launcher_id", launcherId)
                .put("new_root", newRoot).put("fee", fee).buildNode());
    }

    public ApiResponse<UpdateDataLayerResult> dlUpdateRoot(String launcherId, String newRoot, long fee) throws RPCException {
        return walletResponse(dlUpdateRootAsBytes(launcherId, newRoot, fee), null, UpdateDataLayerResult.class, DL_UPDATE_ROOT);
    }

    public byte[] dlUpdateMultipleAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DL_UPDATE_MULTIPLE, request);
    }

    public ApiResponse<TransactionResult> dlUpdateMultiple(JsonNode request) throws RPCException {
        return walletResponse(dlUpdateMultipleAsBytes(request), null, TransactionResult.class, DL_UPDATE_MULTIPLE);
    }

    public byte[] dlUpdateMultipleAsBytes(Map<String, String> updates, long fee) throws RPCException {
        return dlUpdateMultipleAsBytes(new JsonUtils.ObjectBuilder().put("updates", updates).put("fee", fee).buildNode());
    }

    public ApiResponse<TransactionResult> dlUpdateMultiple(Map<String, String> updates, long fee) throws RPCException {
        return walletResponse(dlUpdateMultipleAsBytes(updates, fee), null, TransactionResult.class, DL_UPDATE_MULTIPLE);
    }

    public byte[] dlHistoryAsBytes(String launcherId, @Nullable Long minGeneration,
            @Nullable Long maxGeneration, @Nullable Long numResults) throws RPCException {
        var request = JsonUtils.newSingleNode("launcher_id", launcherId);
        if (minGeneration != null) request.put("min_generation", minGeneration);
        if (maxGeneration != null) request.put("max_generation", maxGeneration);
        if (numResults != null) request.put("num_results", numResults);
        return walletRequest(DL_HISTORY, request);
    }

    public ApiResponse<SingletonHistory> dlHistory(String launcherId, @Nullable Long minGeneration,
            @Nullable Long maxGeneration, @Nullable Long numResults) throws RPCException {
        return walletResponse(dlHistoryAsBytes(launcherId, minGeneration, maxGeneration, numResults),
                null, SingletonHistory.class, DL_HISTORY);
    }

    public byte[] dlOwnedSingletonsAsBytes() throws RPCException {
        return walletRequest(DL_OWNED_SINGLETONS, JsonUtils.newEmptyNode());
    }

    public ApiResponse<OwnedSingletons> dlOwnedSingletons() throws RPCException {
        return walletResponse(dlOwnedSingletonsAsBytes(), null, OwnedSingletons.class, DL_OWNED_SINGLETONS);
    }

    public byte[] dlGetMirrorsAsBytes(String launcherId) throws RPCException {
        return walletRequest(DL_GET_MIRRORS, JsonUtils.newSingleNode("launcher_id", launcherId));
    }

    public ApiResponse<List<Mirror>> dlGetMirrors(String launcherId) throws RPCException {
        return walletList(dlGetMirrorsAsBytes(launcherId), "mirrors", new TypeReference<List<Mirror>>() {}, DL_GET_MIRRORS);
    }

    public byte[] dlNewMirrorAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DL_NEW_MIRROR, request);
    }

    public ApiResponse<TransactionResult> dlNewMirror(JsonNode request) throws RPCException {
        return walletResponse(dlNewMirrorAsBytes(request), null, TransactionResult.class, DL_NEW_MIRROR);
    }

    public byte[] dlNewMirrorAsBytes(String launcherId, long amount, List<String> urls, long fee) throws RPCException {
        return dlNewMirrorAsBytes(new JsonUtils.ObjectBuilder().put("launcher_id", launcherId)
                .put("amount", amount).put("urls", urls).put("fee", fee).buildNode());
    }

    public ApiResponse<TransactionResult> dlNewMirror(String launcherId, long amount, List<String> urls, long fee) throws RPCException {
        return walletResponse(dlNewMirrorAsBytes(launcherId, amount, urls, fee), null, TransactionResult.class, DL_NEW_MIRROR);
    }

    public byte[] dlDeleteMirrorAsBytes(JsonNode request) throws RPCException {
        return walletRequest(DL_DELETE_MIRROR, request);
    }

    public ApiResponse<TransactionResult> dlDeleteMirror(JsonNode request) throws RPCException {
        return walletResponse(dlDeleteMirrorAsBytes(request), null, TransactionResult.class, DL_DELETE_MIRROR);
    }

    public byte[] dlDeleteMirrorAsBytes(String coinId, long fee) throws RPCException {
        return dlDeleteMirrorAsBytes(new JsonUtils.ObjectBuilder().put("coin_id", coinId).put("fee", fee).buildNode());
    }

    public ApiResponse<TransactionResult> dlDeleteMirror(String coinId, long fee) throws RPCException {
        return walletResponse(dlDeleteMirrorAsBytes(coinId, fee), null, TransactionResult.class, DL_DELETE_MIRROR);
    }

    public byte[] dlVerifyProofAsBytes(JsonNode proof) throws RPCException {
        return walletRequest(DL_VERIFY_PROOF, proof);
    }

    public ApiResponse<VerifiedProof> dlVerifyProof(JsonNode proof) throws RPCException {
        return walletResponse(dlVerifyProofAsBytes(proof), null, VerifiedProof.class, DL_VERIFY_PROOF);
    }

    public byte[] vcMintAsBytes(JsonNode request) throws RPCException {
        return walletRequest(VC_MINT, request);
    }

    public ApiResponse<VcMintResult> vcMint(JsonNode request) throws RPCException {
        return walletResponse(vcMintAsBytes(request), null, VcMintResult.class, VC_MINT);
    }

    public byte[] vcMintAsBytes(String didId, @Nullable String targetAddress, long fee) throws RPCException {
        var request = new JsonUtils.ObjectBuilder().put("did_id", didId).put("fee", fee).buildNode();
        if (targetAddress != null) request.put("target_address", targetAddress);
        return vcMintAsBytes(request);
    }

    public ApiResponse<VcMintResult> vcMint(String didId, @Nullable String targetAddress, long fee) throws RPCException {
        return walletResponse(vcMintAsBytes(didId, targetAddress, fee), null, VcMintResult.class, VC_MINT);
    }

    public byte[] vcGetAsBytes(String vcId) throws RPCException {
        return walletRequest(VC_GET, JsonUtils.newSingleNode("vc_id", vcId));
    }

    public ApiResponse<VcRecord> vcGet(String vcId) throws RPCException {
        return walletResponse(vcGetAsBytes(vcId), "vc_record", VcRecord.class, VC_GET);
    }

    public byte[] vcGetListAsBytes(long start, long end) throws RPCException {
        return walletRequest(VC_GET_LIST, new JsonUtils.ObjectBuilder().put("start", start).put("end", end).buildNode());
    }

    public ApiResponse<VcList> vcGetList(long start, long end) throws RPCException {
        return walletResponse(vcGetListAsBytes(start, end), null, VcList.class, VC_GET_LIST);
    }

    public byte[] vcSpendAsBytes(String vcId, @Nullable String newPuzhash, @Nullable String newProofHash,
            @Nullable String providerInnerPuzhash, long fee) throws RPCException {
        var request = new JsonUtils.ObjectBuilder().put("vc_id", vcId).put("fee", fee).buildNode();
        if (newPuzhash != null) request.put("new_puzhash", newPuzhash);
        if (newProofHash != null) request.put("new_proof_hash", newProofHash);
        if (providerInnerPuzhash != null) request.put("provider_inner_puzhash", providerInnerPuzhash);
        return vcSpendAsBytes(request);
    }

    public ApiResponse<TransactionResult> vcSpend(String vcId, @Nullable String newPuzhash, @Nullable String newProofHash,
            @Nullable String providerInnerPuzhash, long fee) throws RPCException {
        return walletResponse(vcSpendAsBytes(vcId, newPuzhash, newProofHash, providerInnerPuzhash, fee),
                null, TransactionResult.class, VC_SPEND);
    }

    public byte[] vcSpendAsBytes(JsonNode request) throws RPCException {
        return walletRequest(VC_SPEND, request);
    }

    public ApiResponse<TransactionResult> vcSpend(JsonNode request) throws RPCException {
        return walletResponse(vcSpendAsBytes(request), null, TransactionResult.class, VC_SPEND);
    }

    public byte[] vcAddProofsAsBytes(Map<String, String> proofs) throws RPCException {
        return walletRequest(VC_ADD_PROOFS, JsonUtils.newSingleNode("proofs", proofs));
    }

    public ApiResponse<Boolean> vcAddProofs(Map<String, String> proofs) throws RPCException {
        return walletResponse(vcAddProofsAsBytes(proofs), "success", Boolean.class, VC_ADD_PROOFS);
    }

    public byte[] vcGetProofsForRootAsBytes(String root) throws RPCException {
        return walletRequest(VC_GET_PROOFS_FOR_ROOT, JsonUtils.newSingleNode("root", root));
    }

    public ApiResponse<Map<String, String>> vcGetProofsForRoot(String root) throws RPCException {
        try {
            return newResponseMap(JsonUtils.readTree(vcGetProofsForRootAsBytes(root)), "proofs",
                    new TypeReference<Map<String, String>>() {}, VC_GET_PROOFS_FOR_ROOT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] vcRevokeAsBytes(JsonNode request) throws RPCException {
        return walletRequest(VC_REVOKE, request);
    }

    public ApiResponse<TransactionResult> vcRevoke(JsonNode request) throws RPCException {
        return walletResponse(vcRevokeAsBytes(request), null, TransactionResult.class, VC_REVOKE);
    }

    public byte[] vcRevokeAsBytes(String vcParentId, long fee) throws RPCException {
        return vcRevokeAsBytes(new JsonUtils.ObjectBuilder().put("vc_parent_id", vcParentId).put("fee", fee).buildNode());
    }

    public ApiResponse<TransactionResult> vcRevoke(String vcParentId, long fee) throws RPCException {
        return walletResponse(vcRevokeAsBytes(vcParentId, fee), null, TransactionResult.class, VC_REVOKE);
    }

    public byte[] crcatApprovePendingAsBytes(JsonNode request) throws RPCException {
        return walletRequest(CRCAT_APPROVE_PENDING, request);
    }

    public ApiResponse<TransactionResult> crcatApprovePending(JsonNode request) throws RPCException {
        return walletResponse(crcatApprovePendingAsBytes(request), null, TransactionResult.class, CRCAT_APPROVE_PENDING);
    }

    public byte[] crcatApprovePendingAsBytes(long walletId, long minAmountToClaim, long fee) throws RPCException {
        return crcatApprovePendingAsBytes(new JsonUtils.ObjectBuilder().put("wallet_id", walletId)
                .put("min_amount_to_claim", minAmountToClaim).put("fee", fee).buildNode());
    }

    public ApiResponse<TransactionResult> crcatApprovePending(long walletId, long minAmountToClaim, long fee) throws RPCException {
        return walletResponse(crcatApprovePendingAsBytes(walletId, minAmountToClaim, fee), null, TransactionResult.class, CRCAT_APPROVE_PENDING);
    }

    public byte[] gatherSigningInfoAsBytes(JsonNode request) throws RPCException {
        return walletRequest(GATHER_SIGNING_INFO, request);
    }

    public ApiResponse<SigningInstructions> gatherSigningInfo(JsonNode request) throws RPCException {
        return walletResponse(gatherSigningInfoAsBytes(request), "signing_instructions", SigningInstructions.class, GATHER_SIGNING_INFO);
    }

    public byte[] applySignaturesAsBytes(JsonNode request) throws RPCException {
        return walletRequest(APPLY_SIGNATURES, request);
    }

    public ApiResponse<List<SignerTransaction>> applySignatures(JsonNode request) throws RPCException {
        return walletList(applySignaturesAsBytes(request), "signed_transactions", new TypeReference<List<SignerTransaction>>() {}, APPLY_SIGNATURES);
    }

    public byte[] submitTransactionsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(SUBMIT_TRANSACTIONS, request);
    }

    public ApiResponse<List<String>> submitTransactions(JsonNode request) throws RPCException {
        return walletList(submitTransactionsAsBytes(request), "mempool_ids", new TypeReference<List<String>>() {}, SUBMIT_TRANSACTIONS);
    }

    public byte[] executeSigningInstructionsAsBytes(JsonNode request) throws RPCException {
        return walletRequest(EXECUTE_SIGNING_INSTRUCTIONS, request);
    }

    public ApiResponse<List<SigningResponse>> executeSigningInstructions(JsonNode request) throws RPCException {
        return walletList(executeSigningInstructionsAsBytes(request), "signing_responses", new TypeReference<List<SigningResponse>>() {}, EXECUTE_SIGNING_INSTRUCTIONS);
    }
}
