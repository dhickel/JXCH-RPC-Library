package io.mindspice.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Wallet;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.TypeRefs;
import io.mindspice.schemas.wallet.*;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;

import java.io.IOException;
import java.util.List;


public class WalletAPI extends SharedAPI {

    public WalletAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }

    //TODO this wallet rpc, I scrapped everything needs to be more concise with slew of arguments



    /* Cat Wallet */

    public byte[] cancelOfferAsBytes(String tradeId, long fee, boolean cancelOnChain) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("trade_id", tradeId)
                    .put("fee", fee)
                    .put("secure", cancelOnChain)
                    .buildBytes();
            var req = new Request(Wallet.CANCEL_OFFER, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> cancelOffer(String tradeId, long fee, boolean cancelOnChain) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(cancelOfferAsBytes(tradeId, fee, cancelOnChain));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.CANCEL_OFFER);
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
            var req = new Request(Wallet.CANCEL_OFFERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> cancelAllOffers(boolean cancelOnChain) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(cancelAllOffersAsBytes(cancelOnChain));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.CANCEL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] cancelOffersAsBytes(String assetId, int batchSize, int batchFee, boolean cancelOnChain)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("secure", cancelOnChain)
                    .put("asset_id", assetId)
                    .put("batch_size", batchSize)
                    .put("batch_fee", batchFee)
                    .buildBytes();
            var req = new Request(Wallet.CANCEL_OFFERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> cancelOffers(String assetId, int batchSize, int batchFee, boolean cancelOnChain)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(cancelOffersAsBytes(assetId, batchSize, batchFee, cancelOnChain));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.CANCEL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catAssetIdToNameAsBytes(String catAssetId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("asset_id", catAssetId);
            var req = new Request(Wallet.CAT_ASSET_ID_TO_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<CatAssetInfo> catAssetIdToName(String assetId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catAssetIdToNameAsBytes(assetId));
            return newResponse(jsonNode, CatAssetInfo.class, Wallet.CAT_ASSET_ID_TO_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catGetAssetIdAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.CAT_GET_ASSET_ID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> catGetAssetId(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catGetAssetIdAsBytes(walletId));
            return newResponse(jsonNode, "asset_id", String.class, Wallet.CAT_GET_ASSET_ID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catGetNameAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.CAT_GET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> catGetName(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catGetNameAsBytes(walletId));
            return newResponse(jsonNode, "asset_id", String.class, Wallet.CAT_GET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catSetNameAsBytes(int walletId, String name) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("name", name)
                    .buildBytes();
            var req = new Request(Wallet.CAT_SET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> catSetName(int walletId, String name) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSetNameAsBytes(walletId, name));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.CAT_SET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] catSpendAsBytes(ObjectNode jsonObj) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(jsonObj);
            var req = new Request(Wallet.CAT_SPEND, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException | RPCException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public byte[] catSpendAsBytes(int walletId, long amountInMojo, String address) throws RPCException {
        var data = new JsonUtils.ObjectBuilder()
                .put("wallet_id", walletId)
                .put("amount", amountInMojo)
                .put("inner_address", address)
                .buildNode();
        return catSpendAsBytes(data);
    }

    public ApiResponse<TransactionStatus> catSpend(int walletId, long amountInMojo, String address) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSpendAsBytes(walletId, amountInMojo, address));
            return newResponse(jsonNode, TransactionStatus.class, Wallet.CAT_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<TransactionStatus> catSpendAs(ObjectNode jsonObj) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSpendAsBytes(jsonObj));
            return newResponse(jsonNode, TransactionStatus.class, Wallet.CAT_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] checkOfferValidityAsBytes(String offer) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("offer", offer);
            var req = new Request(Wallet.CHECK_OFFER_VALIDITY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<OfferValidity> checkOfferValidity(String offer) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(checkOfferValidityAsBytes(offer));
            return newResponse(jsonNode, OfferValidity.class, Wallet.CHECK_OFFER_VALIDITY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] createOfferForIdsAsBytes(ObjectNode jsonObj) throws RPCException {
        try {
            var req = new Request(Wallet.CREATE_OFFER_FOR_IDS, JsonUtils.writeBytes(jsonObj));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Offer> createOfferForIds(ObjectNode jsonObj) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createOfferForIdsAsBytes(jsonObj));
            return newResponse(jsonNode, Offer.class, Wallet.CREATE_OFFER_FOR_IDS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getAllOffersAsBytes(ObjectNode jsonObj) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(jsonObj);
            var req = new Request(Wallet.GET_ALL_OFFERS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Offers> getAllOffers() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllOffersAsBytes(JsonUtils.newEmptyNode()));
            return newResponse(jsonNode, Offers.class, Wallet.GET_ALL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<Offers> getAllOffers(ObjectNode jsonObj) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllOffersAsBytes(jsonObj));
            return newResponse(jsonNode, Offers.class, Wallet.GET_ALL_OFFERS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getCatListAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_CAT_LIST, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Cat>> getCatList() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCatListAsBytes());
            return newResponseList(jsonNode, "cat_list", TypeRefs.CAT_LIST, Wallet.GET_CAT_LIST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getOfferByBytes(String tradeId, String fileContents) throws RPCException {
        try {
            var data= new JsonUtils.ObjectBuilder()
                    .put("trade_id", tradeId)
                    .buildNode();
            if (fileContents != null) { data.put("file_contents", fileContents); }
            var req = new Request(Wallet.GET_OFFER, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Offer> getOfferById(String tradeId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOfferByBytes(tradeId, null));
            return newResponse(jsonNode, Offer.class, Wallet.GET_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<Offer> getOfferById(String tradeId, String fileContents) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOfferByBytes(tradeId, fileContents));
            return newResponse(jsonNode, Offer.class, Wallet.GET_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }





}
