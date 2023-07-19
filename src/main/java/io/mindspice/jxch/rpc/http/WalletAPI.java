package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.NftDataKey;
import io.mindspice.jxch.rpc.enums.endpoints.Wallet;
import io.mindspice.mindlib.util.JsonUtils;
import io.mindspice.jxch.rpc.schemas.ApiResponse;
import io.mindspice.jxch.rpc.schemas.TypeRefs;
import io.mindspice.jxch.rpc.schemas.custom.NftBundle;
import io.mindspice.jxch.rpc.schemas.fullnode.Network;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.CoinRecord;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;
import io.mindspice.jxch.rpc.schemas.wallet.*;
import io.mindspice.jxch.rpc.schemas.wallet.cat.Cat;
import io.mindspice.jxch.rpc.schemas.wallet.cat.CatAssetInfo;
import io.mindspice.jxch.rpc.schemas.wallet.cat.StrayCat;
import io.mindspice.jxch.rpc.schemas.wallet.did.*;
import io.mindspice.jxch.rpc.schemas.wallet.nft.*;
import io.mindspice.jxch.rpc.schemas.wallet.offers.*;
import io.mindspice.jxch.rpc.util.RPCException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class WalletAPI extends SharedAPI {

    public WalletAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }

    //TODO this wallet rpc, I scrapped everything needs to be more concise with slew of arguments

    ////////////////////////
    /*  WALLET MANAGEMENT */
    ////////////////////////

    public byte[] createNewWalletAsBytes(JsonNode walletBuilder) throws RPCException {
        try {
            var req = new Request(Wallet.CREATE_NEW_WALLET, JsonUtils.writeBytes(walletBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException | RPCException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Wallet> createNewWallet(JsonNode walletBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createNewWalletAsBytes(walletBuilder));
            System.out.println(JsonUtils.writeString(jsonNode));
            return newResponse(jsonNode, Wallet.class, Wallet.CREATE_NEW_WALLET);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    //  TODO doc that -1 is for all
    public byte[] getWalletsAsBytes(int type, boolean includeData) throws RPCException {
        try {
            var data = JsonUtils.newSingleNode("include_data", includeData);
            if (type != -1) { data.put("type", type); }
            var req = new Request(Wallet.GET_WALLETS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Wallets> getWallets(int type, boolean includeData) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getWalletsAsBytes(type, includeData));
            return newResponse(jsonNode, Wallets.class, Wallet.GET_WALLETS);
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

    public byte[] catSpendAsBytes(JsonNode catSpendBuilder) throws RPCException {
        try {
            var req = new Request(Wallet.CAT_SPEND, JsonUtils.writeBytes(catSpendBuilder));
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

    public ApiResponse<TransactionStatus> catSpend(int walletId, long amountInMojo, String address) throws
            RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSpendAsBytes(walletId, amountInMojo, address));
            return newResponse(jsonNode, TransactionStatus.class, Wallet.CAT_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<TransactionStatus> catSpend(JsonNode catSpendBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(catSpendAsBytes(catSpendBuilder));
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

    public byte[] createOfferForIdsAsBytes(JsonNode offerBuilder) throws RPCException {
        try {
            var req = new Request(Wallet.CREATE_OFFER_FOR_IDS, JsonUtils.writeBytes(offerBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Offer> createOfferForIds(JsonNode offerBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createOfferForIdsAsBytes(offerBuilder));
            return newResponse(jsonNode, Offer.class, Wallet.CREATE_OFFER_FOR_IDS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getAllOffersAsBytes(JsonNode offerSearchBuilder) throws RPCException {
        try {
            var req = new Request(Wallet.GET_ALL_OFFERS, JsonUtils.writeBytes(offerSearchBuilder));
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

    public ApiResponse<Offers> getAllOffers(JsonNode offerSearchBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getAllOffersAsBytes(offerSearchBuilder));
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
            var data = new JsonUtils.ObjectBuilder()
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

    // TODO document that this returns a json node for infos
    public byte[] getOfferSummaryAsBytes(String offer, boolean showAdvanced) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("offer", offer)
                    .put("advanced", showAdvanced)
                    .buildNode();
            var req = new Request(Wallet.GET_OFFER_SUMMARY, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<OfferSummary> getOfferSummary(String offer, boolean showAdvanced) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOfferSummaryAsBytes(offer, showAdvanced));

            return newResponse(jsonNode, "summary", OfferSummary.class, Wallet.GET_OFFER_SUMMARY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getOffersCountAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_OFFERS_COUNT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<OfferCount> getOffersCount() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getOffersCountAsBytes());

            return newResponse(jsonNode, OfferCount.class, Wallet.GET_OFFERS_COUNT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getStrayCatsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_STRAY_CATS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<StrayCat>> getStrayCats() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getStrayCatsAsBytes());

            return newResponseList(jsonNode, "stray_cats", TypeRefs.STRAY_CAT_LIST, Wallet.GET_STRAY_CATS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] selectCoinsAsBytes(JsonNode selectCoinBuilder) throws RPCException {
        try {
            var req = new Request(Wallet.SELECT_COINS, JsonUtils.writeBytes(selectCoinBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Coin>> selectCoins(int walletId, long amount) throws RPCException {
        try {
            var reqNode = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("amount", amount)
                    .buildNode();
            var respNode = JsonUtils.readTree(selectCoinsAsBytes(reqNode));
            return newResponseList(respNode, "coins", TypeRefs.COIN_LIST, Wallet.SELECT_COINS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<List<Coin>> selectCoins(JsonNode selectCoinBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(selectCoinsAsBytes(selectCoinBuilder));
            return newResponseList(jsonNode, "coins", TypeRefs.COIN_LIST, Wallet.SELECT_COINS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] takeOfferAsBytes(JsonNode takeOfferbuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(takeOfferbuilder);
            var req = new Request(Wallet.TAKE_OFFER, data);
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
            System.out.println(JsonUtils.writeString(respNode));
            return newResponse(respNode, "trade_record", TradeRecord.class, Wallet.TAKE_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<TradeRecord> takeOffer(JsonNode takeOfferBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(takeOfferAsBytes(takeOfferBuilder));
            return newResponse(jsonNode, "trade_record", TradeRecord.class, Wallet.TAKE_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ////////////////
    /* DID WALLET */
    ////////////////

    public byte[] didCreateAttestAsBytes(int walletId, String coinName, String pubKey,
            String puzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("coin_name", coinName)
                    .put("pubkey", pubKey)
                    .put("puzhash", puzHash)
                    .buildBytes();
            var req = new Request(Wallet.DID_CREATE_ATTEST, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DIDAttest> didCreateAttestAs(int walletId, String coinName, String pubKey,
            String puzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didCreateAttestAsBytes(walletId, coinName, pubKey, puzHash));
            return newResponse(jsonNode, DIDAttest.class, Wallet.DID_CREATE_ATTEST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didCreateDIDBackupFileAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_CREATE_BACKUP_FILE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didCreateDIDBackupFile(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didCreateDIDBackupFileAsBytes(walletId));
            return newResponse(jsonNode, "backup_data", String.class, Wallet.DID_CREATE_BACKUP_FILE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didFindLostDIDAsBytes(String coinId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("coin_id", coinId);
            var req = new Request(Wallet.DID_FIND_LOST_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didFindLostDID(String coinId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didFindLostDIDAsBytes(coinId));
            return newResponse(jsonNode, "latest_coin_id", String.class, Wallet.DID_FIND_LOST_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetCurrentCoinInfoAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_GET_CURRENT_COIN_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DIDCoin> didGetCurrentCoinInfo(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetCurrentCoinInfoAsBytes(walletId));
            return newResponse(jsonNode, DIDCoin.class, Wallet.DID_GET_CURRENT_COIN_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetDIDAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_GET_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DID> didGetDID(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetDIDAsBytes(walletId));
            return newResponse(jsonNode, DID.class, Wallet.DID_GET_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetInfoAsBytes(String coinId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("coin_id", coinId);
            var req = new Request(Wallet.DID_GET_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DIDInfo> didGetInfo(String coinId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetInfoAsBytes(coinId));
            return newResponse(jsonNode, DIDInfo.class, Wallet.DID_GET_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetInformationNeededForRecoveryAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_GET_INFORMATION_NEEDED_FOR_RECOVERY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DIDRecoveryInfo> didGetInformationNeededForRecovery(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetInformationNeededForRecoveryAsBytes(walletId));
            return newResponse(jsonNode, DIDRecoveryInfo.class, Wallet.DID_GET_INFORMATION_NEEDED_FOR_RECOVERY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetMetaDataAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_GET_METADATA, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> didGetMetaData(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetMetaDataAsBytes(walletId));
            return newResponseNode(jsonNode, "meta_data", Wallet.DID_GET_METADATA);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didUpdateMetaDataAsBytes(int walletId, JsonNode metaData, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("meta_data", metaData)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(Wallet.DID_UPDATE_METADATA, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> didUpdateMetaData(int walletId, JsonNode metaData, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didUpdateMetaDataAsBytes(walletId, metaData, fee, reusePuzHash));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.DID_UPDATE_METADATA);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetPubKeyAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_GET_PUBKEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didGetPubKey(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetPubKeyAsBytes(walletId));
            return newResponse(jsonNode, "pubkey", String.class, Wallet.DID_GET_PUBKEY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetRecoveryListAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_GET_RECOVERY_LIST, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<DIDRecoveryList> didGetRecoveryList(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetRecoveryListAsBytes(walletId));
            return newResponse(jsonNode, DIDRecoveryList.class, Wallet.DID_GET_RECOVERY_LIST);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didGetWalletNameAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DID_GET_WALLET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> didGetWalletName(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didGetWalletNameAsBytes(walletId));
            return newResponse(jsonNode, "name", String.class, Wallet.DID_GET_WALLET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didMessageSpendAsBytes(int walletId, List<String> coinAnnouncements,
            List<String> puzzleAnnouncements) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("coin_announcements", coinAnnouncements)
                    .put("puzzle_announcements", puzzleAnnouncements)
                    .buildBytes();
            var req = new Request(Wallet.DID_MESSAGE_SPEND, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> didMessageSpendAs(int walletId, List<String> coinAnnouncements,
            List<String> puzzleAnnouncements) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    didMessageSpendAsBytes(walletId, coinAnnouncements, puzzleAnnouncements)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.DID_MESSAGE_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didRecoverySpendAsBytes(int walletId, List<String> attestData, String pubKey,
            String puzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("attest_data", attestData)
                    .put("pubkey", pubKey)
                    .put("puzhash", puzHash)
                    .buildBytes();
            var req = new Request(Wallet.DID_RECOVERY_SPEND, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> didRecoverySpend(int walletId, List<String> attestData,
            String pubKey, String puzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didRecoverySpendAsBytes(walletId, attestData, pubKey, puzHash));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.DID_RECOVERY_SPEND);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didSetWalletNameAsBytes(int walletId, String name) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("name", name)
                    .buildBytes();
            var req = new Request(Wallet.DID_SET_WALLET_NAME, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> didSetWalletName(int walletId, String name) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(didSetWalletNameAsBytes(walletId, name));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.DID_SET_WALLET_NAME);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didTransferDIDAsBytes(int walletId, String address, long fee, boolean withRecoveryInfo,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("inner_address", address)
                    .put("with_recovery_info", withRecoveryInfo)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(Wallet.DID_TRANSFER_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<TransactionStatus> didTransferDID(int walletId, String address, long fee,
            boolean withRecoveryInfo, boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    didTransferDIDAsBytes(walletId, address, fee, withRecoveryInfo, reusePuzHash)
            );
            return newResponse(jsonNode, TransactionStatus.class, Wallet.DID_TRANSFER_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] didUpdateRecoveryIdsAsBytes(int walletId, List<String> newIds,
            int numVerificationsReq, boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("new_list", newIds)
                    .put("num_verifications_required", numVerificationsReq)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(Wallet.DID_UPDATE_RECOVERY_IDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    ////////////////////
    /* KEY MANAGEMENT */
    ////////////////////

    public ApiResponse<Boolean> didUpdateRecoveryIds(int walletId, List<String> newIds,
            int numVerificationsReq, boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    didUpdateRecoveryIdsAsBytes(walletId, newIds, numVerificationsReq, reusePuzHash)
            );
            return newResponse(jsonNode, "success", Boolean.class, Wallet.DID_UPDATE_RECOVERY_IDS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] addKeyAsBytes(List<String> mnemonic) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("mnemonic", mnemonic);
            var req = new Request(Wallet.ADD_KEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> addKey(List<String> mnemonic) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(addKeyAsBytes(mnemonic));
            return newResponse(jsonNode, "fingerprint", Long.class, Wallet.ADD_KEY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] deleteKeyAsBytes(long fingerprint) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("fingerprint", fingerprint);
            var req = new Request(Wallet.DELETE_KEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteKey(long fingerprint) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteKeyAsBytes(fingerprint));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.DELETE_KEY);
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
            var req = new Request(Wallet.CHECK_DELETE_KEY, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<KeyInfo> checkDeleteKey(long fingerprint, int searchDepth) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(checkDeleteKeyAsBytes(fingerprint, searchDepth));
            return newResponse(jsonNode, KeyInfo.class, Wallet.CHECK_DELETE_KEY);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] deleteAllKeysAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.DELETE_ALL_KEYS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteAllKeys() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteAllKeysAsBytes());
            return newResponse(jsonNode, "success", Boolean.class, Wallet.DELETE_ALL_KEYS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] generateMnemonicAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GENERATE_MNEMONIC, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> generateMnemonic() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(generateMnemonicAsBytes());
            return newResponseList(jsonNode, "mnemonic", TypeRefs.STRING_LIST, Wallet.GENERATE_MNEMONIC);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getLoggedInFingerprintAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_LOGGED_IN_FINGERPRINT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Long> getLoggedInFingerprint() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getLoggedInFingerprintAsBytes());
            return newResponse(jsonNode, "fingerprint", Long.class, Wallet.GET_LOGGED_IN_FINGERPRINT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getPublicKeysAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_PUBLIC_KEYS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<String>> getPublicKeys() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getPublicKeysAsBytes());
            return newResponseList(
                    jsonNode, "public_key_fingerprints", TypeRefs.STRING_LIST, Wallet.GET_PUBLIC_KEYS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] logInAsBytes(long fingerprint) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("fingerprint", fingerprint);
            var req = new Request(Wallet.LOG_IN, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> logIn(long fingerprint) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(logInAsBytes(fingerprint));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.LOG_IN);
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
            var req = new Request(Wallet.VERIFY_SIGNATURE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> verifySignature(String pubKey, String message, String signature,
            String address) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(verifySignatureAsBytes(pubKey, message, signature, address));
            return newResponse(jsonNode, "isValid", Boolean.class, Wallet.VERIFY_SIGNATURE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    /////////////////
    /* POOL WALLET */
    /////////////////

    public byte[] pwStatusAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.PW_STATUS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<PoolWalletStatus> pwStatus(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pwStatusAsBytes(walletId));
            return newResponse(jsonNode, PoolWalletStatus.class, Wallet.PW_STATUS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pwSelfPoolAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.PW_SELF_POOL, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> pwSelfPool(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pwSelfPoolAsBytes(walletId));
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.PW_SELF_POOL);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pwJoinPoolAsBytes(int walletId, String targetPuzHash, String poolURL, int lockHeight,
            int fee) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("target_puzzlehash", targetPuzHash)
                    .put("pool_url", poolURL)
                    .put("relative_lock_height", lockHeight)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(Wallet.PW_JOIN_POOL, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> pwJoinPool(int walletId, String targetPuzHash, String poolURL,
            int lockHeight, int fee) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    pwJoinPoolAsBytes(walletId, targetPuzHash, poolURL, lockHeight, fee)
            );
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.PW_JOIN_POOL);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pwAbsorbRewardsAsBytes(int walletId, int fee) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(Wallet.PW_ABSORB_REWARDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> pwAbsorbRewards(int walletId, int fee) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pwAbsorbRewardsAsBytes(walletId, fee));
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.PW_ABSORB_REWARDS);
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
            var req = new Request(Wallet.DELETE_NOTIFICATIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteNotifications(List<String> ids) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteNotificationsAsBytes(ids));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.DELETE_NOTIFICATIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getNotificationsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_NOTIFICATIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<Notification>> getNotifications() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNotificationsAsBytes());
            return newResponseList(
                    jsonNode, "notifications", TypeRefs.NOTIFICATION_LIST, Wallet.GET_NOTIFICATIONS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] sendNotificationAsBytes(String address, String message, long amount,
            long fee) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("address", address)
                    .put("message", message)
                    .put("amount", amount)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(Wallet.SEND_NOTIFICATION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> sendNotification(String address, String message, long amount,
            long fee) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendNotificationAsBytes(address, message, amount, fee));
            return newResponse(jsonNode, "tx", Transaction.class, Wallet.SEND_NOTIFICATION);
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
            var req = new Request(Wallet.SIGN_MESSAGE_BY_ADDRESS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignedMessage> signMessageByAddress(String address, String message,
            boolean isHex) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(signMessageByAddressAsBytes(address, message, isHex));
            return newResponse(jsonNode, SignedMessage.class, Wallet.SIGN_MESSAGE_BY_ADDRESS);
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
            var req = new Request(Wallet.SIGN_MESSAGE_BY_ID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignedMessage> signMessageById(String id, String message, boolean isHex)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(signMessageByIdAsBytes(id, message, isHex));
            return newResponse(jsonNode, SignedMessage.class, Wallet.SIGN_MESSAGE_BY_ID);
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
            var req = new Request(Wallet.CREATE_SIGNED_TRANSACTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SignedTransaction> createSignedTransaction(JsonNode signedTransactionBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createSignedTransactionAsBytes(signedTransactionBuilder));
            return newResponse(
                    jsonNode, "signed_tx", SignedTransaction.class, Wallet.CREATE_SIGNED_TRANSACTION
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] deleteUnconfirmedTransactionsAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.DELETE_UNCONFIRMED_TRANSACTIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> deleteUnconfirmedTransactions(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(deleteUnconfirmedTransactionsAsBytes(walletId));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.DELETE_UNCONFIRMED_TRANSACTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] extendDerivationIndexAsBytes(int index) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("index", index);
            var req = new Request(Wallet.EXTEND_DERIVATION_INDEX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Integer> extendDerivationIndex(int index) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(extendDerivationIndexAsBytes(index));
            return newResponse(jsonNode, "index", Integer.class, Wallet.EXTEND_DERIVATION_INDEX);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getCurrentDerivationIndexAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_CURRENT_DERIVATION_INDEX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Integer> getCurrentDerivationIndex() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getCurrentDerivationIndexAsBytes());
            return newResponse(jsonNode, "index", Integer.class, Wallet.GET_CURRENT_DERIVATION_INDEX);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getFarmedAmountAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_FARMED_AMOUNT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<FarmedAmount> getFarmedAmount() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getFarmedAmountAsBytes());
            return newResponse(jsonNode, FarmedAmount.class, Wallet.GET_FARMED_AMOUNT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getNextAddressAsBytes(int walletId, boolean getNewAddress) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("new_address", getNewAddress)
                    .buildBytes();
            var req = new Request(Wallet.GET_NEXT_ADDRESS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> getNextAddress(int walletId, boolean getNewAddress) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNextAddressAsBytes(walletId, getNewAddress));
            return newResponse(jsonNode, "address", String.class, Wallet.GET_NEXT_ADDRESS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getSpendableCoinsAsBytes(JsonNode spendableCoinbuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(spendableCoinbuilder);
            var req = new Request(Wallet.GET_SPENDABLE_COINS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendableCoins> getSpendableCoins(JsonNode spendableCoinbuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getSpendableCoinsAsBytes(spendableCoinbuilder));
            return newResponse(jsonNode, SpendableCoins.class, Wallet.GET_SPENDABLE_COINS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTransactionAsBytes(String transactionId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("transaction_id", transactionId);
            var req = new Request(Wallet.GET_TRANSACTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> getTransaction(String transactionId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTransactionAsBytes(transactionId));
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.GET_TRANSACTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTransactionsAsBytes(int walletId, int start, int end, boolean reversed,
            String sortKey) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("start", start)
                    .put("end", end)
                    .put("reversed", reversed)
                    .buildNode();
            if (sortKey != null) { data.put("sort_key", sortKey); }
            var req = new Request(Wallet.GET_TRANSACTIONS, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> getTransactions(int walletId, int start, int end, boolean reversed,
            String sortKey) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getTransactionsAsBytes(walletId, start, end, reversed, sortKey)
            );
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.GET_TRANSACTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<List<Transaction>> getTransactions(int walletId, int start, int end, boolean reversed)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(
                    getTransactionsAsBytes(walletId, start, end, reversed, null)
            );
            return newResponseList(
                    jsonNode, "transactions", TypeRefs.TRANSACTIONS_LIST, Wallet.GET_TRANSACTIONS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    // FIXME untested, did dev on node without this endpoint should work though
    public byte[] getTransactionMemoAsBytes(String transactionId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("transaction_id", transactionId);
            var req = new Request(Wallet.GET_TRANSACTION_MEMO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> getTransactionMemo(String transactionId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTransactionMemoAsBytes(transactionId));
            return newResponseNode(jsonNode, Wallet.GET_TRANSACTION_MEMO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getTransactionCountAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.GET_TRANSACTION_COUNT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> getTransactionCount(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTransactionCountAsBytes(walletId));
            return newResponseNode(jsonNode, Wallet.GET_TRANSACTION_COUNT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getWalletBalanceAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.GET_WALLET_BALANCE, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<JsonNode> getWalletBalance(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getWalletBalanceAsBytes(walletId));
            return newResponseNode(jsonNode, Wallet.GET_WALLET_BALANCE);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] sendTransactionAsBytes(int walletId, String address, long amount, long fee)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("address", address)
                    .put("amount", amount)
                    .put("fee", fee)
                    .buildBytes();
            var req = new Request(Wallet.SEND_TRANSACTION, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public byte[] sendTransactionAsBytes(JsonNode transactionbuilder)
            throws RPCException {
        try {
            var req = new Request(Wallet.SEND_TRANSACTION, JsonUtils.writeBytes(transactionbuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> sendTransaction(JsonNode transactionBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendTransactionAsBytes(transactionBuilder));
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.SEND_TRANSACTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<Transaction> sendTransaction(int walletId, String address, long amount, long fee)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendTransactionAsBytes(walletId, address, amount, fee));
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.SEND_TRANSACTION);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] sendTransactionMultiAsBytes(JsonNode multiTransactionBuilder) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(multiTransactionBuilder);
            var req = new Request(Wallet.SEND_TRANSACTION_MULTI, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Transaction> sendTransactionMulti(JsonNode multiTransactionBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(sendTransactionMultiAsBytes(multiTransactionBuilder));
            return newResponse(jsonNode, "transaction", Transaction.class, Wallet.SEND_TRANSACTION_MULTI);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getHeightInfoAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_HEIGHT_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Integer> getHeightInfo() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getHeightInfoAsBytes());
            return newResponse(jsonNode, "height", Integer.class, Wallet.GET_HEIGHT_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getNetworkInfoAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_NETWORK_INFO, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Network> getNetworkInfo() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getNetworkInfoAsBytes());
            if (jsonNode.get("success").asBoolean()) {
                var network = new Network(
                        jsonNode.get("network_name").asText(),
                        jsonNode.get("network_prefix").asText()
                );
                return newResponse(network, Wallet.GET_NETWORK_INFO);
            } else {
                return newFailedResponse(jsonNode, Wallet.GET_NETWORK_INFO);
            }
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] getSyncStatusAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.GET_SYNC_STATUS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SyncStatus> getSyncStatus() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getSyncStatusAsBytes());
            return newResponse(jsonNode, SyncStatus.class, Wallet.GET_SYNC_STATUS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    //FIXME cant test on giganode
    public byte[] getTimestampForHeightAsBytes(int height) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("height", height);
            var req = new Request(Wallet.GET_TIMESTAMP_FOR_HEIGHT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> getTimestampForHeight(int height) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(getTimestampForHeightAsBytes(height));
            return newResponse(jsonNode, "timestamp", String.class, Wallet.GET_TIMESTAMP_FOR_HEIGHT);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    //TODO this need investigated more I think it can take memos as well, simpleTx is just add/rem and spendbundle
    public byte[] pushTransactionsAsBytes(List<SimpleTransaction> transactions) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("transactions", transactions);
            var req = new Request(Wallet.PUSH_TRANSACTIONS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> pushTransactions(List<SimpleTransaction> transactions) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pushTransactionsAsBytes(transactions));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.PUSH_TRANSACTIONS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] pushTxAsBytes(SpendBundle spendBundle) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("spend_bundle", spendBundle);
            var req = new Request(Wallet.PUSH_TX, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> pushTx(SpendBundle spendBundle) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(pushTxAsBytes(spendBundle));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.PUSH_TX);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] setWalletReSyncOnStartupAsBytes(boolean enable) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("enable", enable);
            var req = new Request(Wallet.SET_WALLET_RESYNC_ON_STARTUP, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> setWalletReSyncOnStartup(boolean enable) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(setWalletReSyncOnStartupAsBytes(enable));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.SET_WALLET_RESYNC_ON_STARTUP);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ////////////////
    /* NFT Wallet */
    ////////////////

    public byte[] nftAddUriAsBytes(int walletId, String uri, NftDataKey dataKey, String nftCoinId, long fee,
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
            var req = new Request(Wallet.NFT_ADD_URI, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftAddUri(int walletId, String uri, NftDataKey dataKey, String nftCoinId, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftAddUriAsBytes(
                    walletId, uri, dataKey, nftCoinId, fee, reusePuzHash)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.NFT_ADD_URI);
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
            var req = new Request(Wallet.NFT_CALCULATE_ROYALTIES, JsonUtils.writeBytes(data));
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
            return newResponseMap(jsonNode, TypeRefs.ROYALTY_MAP, Wallet.NFT_CALCULATE_ROYALTIES);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftCountNftsAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.NFT_COUNT_NFTS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Integer> nftCountNfts(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftCountNftsAsBytes(walletId));
            return newResponse(jsonNode, "count", Integer.class, Wallet.NFT_COUNT_NFTS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetByDidAsBytes(String did) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("did_id", did);
            var req = new Request(Wallet.NFT_GET_BY_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Integer> nftGetByDid(String did) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetByDidAsBytes(did));
            return newResponse(jsonNode, "wallet_id", Integer.class, Wallet.NFT_GET_BY_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetInfoAsBytes(String coinId, @Nullable Integer walletId) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder().buildNode();
            data.put("coin_id", coinId);
            if (walletId != null) { data.put("wallet_id", walletId); }
            var req = new Request(Wallet.NFT_GET_INFO, JsonUtils.writeBytes(data));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<NftInfo> nftGetInfo(String coinId, Integer walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetInfoAsBytes(coinId, walletId));
            return newResponse(jsonNode, "nft_info", NftInfo.class, Wallet.NFT_GET_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public ApiResponse<NftInfo> nftGetInfo(String coinId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetInfoAsBytes(coinId, null));
            return newResponse(jsonNode, "nft_info", NftInfo.class, Wallet.NFT_GET_INFO);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetNftsAsBytes(int walletId, int startIdx, int amountLimit) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("start_index", startIdx)
                    .put("num", amountLimit)
                    .buildBytes();
            var req = new Request(Wallet.NFT_GET_NFTS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<NftInfo>> nftGetNfts(int walletId, int startIdx, int amountLimit) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetNftsAsBytes(walletId, startIdx, amountLimit));
            return newResponseList(jsonNode, "nft_list", TypeRefs.NFT_INFO_LIST, Wallet.NFT_GET_NFTS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetWalletDidAsBytes(int walletId) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("wallet_id", walletId);
            var req = new Request(Wallet.NFT_GET_WALLET_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<String> nftGetWalletDid(int walletId) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetWalletDidAsBytes(walletId));
            return newResponse(jsonNode, "did_id", String.class, Wallet.NFT_GET_WALLET_DID);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftGetWalletsWithDidsAsBytes() throws RPCException {
        try {
            var data = JsonUtils.newEmptyNodeAsBytes();
            var req = new Request(Wallet.NFT_GET_WALLETS_WITH_DIDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<NftWallet>> nftGetWalletsWithDids() throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftGetWalletsWithDidsAsBytes());
            return newResponseList(
                    jsonNode, "nft_wallets", TypeRefs.NFT_WALLET_LIST, Wallet.NFT_GET_WALLETS_WITH_DIDS
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftMintBulkAsBytes(JsonNode bulkMintBuilder) throws RPCException {
        try {
            var req = new Request(Wallet.NFT_MINT_BULK, JsonUtils.writeBytes(bulkMintBuilder));
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
            var req = new Request(Wallet.NFT_MINT_NFT, JsonUtils.writeBytes(singleMintbuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftMintNft(JsonNode singleMintbuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftMintNftAsBytes(singleMintbuilder));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.NFT_MINT_NFT
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftSetDidBulkAsBytes(JsonNode setDidBulkBuilder) throws RPCException {
        try {
            var req = new Request(Wallet.NFT_SET_DID_BULK, JsonUtils.writeBytes(setDidBulkBuilder));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftSetDidBulk(JsonNode setdidBulkBuilder) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftSetDidBulkAsBytes(setdidBulkBuilder));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.NFT_SET_DID_BULK
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftSetNftDidAsBytes(int walletId, String nftCoinId, String did, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("nft_coin_id", nftCoinId)
                    .put("did_id", did)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(Wallet.NFT_SET_NFT_DID, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftSetNftDid(int walletId, String nftCoinId, String did, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftSetNftDidAsBytes(
                    walletId, nftCoinId, did, fee, reusePuzHash)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.NFT_SET_NFT_DID);
        } catch (
                IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftSetNftStatusAsBytes(int walletId, String nftCoinId, boolean inTransaction)
            throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("nft_coin_id", nftCoinId)
                    .put("in_transaction", inTransaction)
                    .buildBytes();
            var req = new Request(Wallet.NFT_SET_NFT_STATUS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Boolean> nftSetNftStatus(int walletId, String nftCoinId, boolean inTransaction)
            throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftSetNftStatusAsBytes(walletId, nftCoinId, inTransaction));
            return newResponse(jsonNode, "success", Boolean.class, Wallet.NFT_SET_NFT_STATUS);
        } catch (
                IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftTransferBulkAsBytes(JsonNode jsonNode) throws RPCException {
        try {
            var req = new Request(Wallet.NFT_TRANSFER_BULK, JsonUtils.writeBytes(jsonNode));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftTransferBulk(JsonNode json) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftTransferBulkAsBytes(json));
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.NFT_TRANSFER_BULK
            );
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] nftTransferNftAsBytes(int walletId, String nftCoinId, String targetAddress, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("wallet_id", walletId)
                    .put("nft_coin_id", nftCoinId)
                    .put("target_address", targetAddress)
                    .put("fee", fee)
                    .put("reuse_puzhash", reusePuzHash)
                    .buildBytes();
            var req = new Request(Wallet.NFT_TRANSFER_NFT, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> nftTransferNft(int walletId, String nftCoinId, String targetAddress, long fee,
            boolean reusePuzHash) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(nftTransferNftAsBytes(
                    walletId, nftCoinId, targetAddress, fee, reusePuzHash)
            );
            return newResponse(jsonNode, "spend_bundle", SpendBundle.class, Wallet.NFT_TRANSFER_NFT);
        } catch (
                IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    ///////////
    /* Coins */
    ///////////

    public byte[] getCoinRecordsByNamesAsBytes(List<String> names, int startHeight, int endHeight,
            boolean includeSpent) throws RPCException {
        try {
            var data = new JsonUtils.ObjectBuilder()
                    .put("names", names)
                    .put("start_height", startHeight)
                    .put("end_height", endHeight)
                    .put("include_spend_coins", includeSpent)
                    .buildBytes();
            var req = new Request(Wallet.GET_COIN_RECORDS_BY_NAMES, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<List<CoinRecord>> getCoinRecordsByNames(List<String> names, int startHeight, int endHeight,
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

    ////////////
    // CUSTOM //
    ////////////

    public byte[] aggregateSpendsAsBytes(List<SpendBundle> spends) throws RPCException {
        try {
            var data = JsonUtils.newSingleNodeAsBytes("spends", spends);
            var req = new Request(Wallet.AGGREGATE_SPENDS, data);
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<SpendBundle> aggregateSpends(List<SpendBundle> spends) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(aggregateSpendsAsBytes(spends));
            return newResponse(jsonNode, "agg_bundle", SpendBundle.class , Wallet.AGGREGATE_SPENDS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }
}
