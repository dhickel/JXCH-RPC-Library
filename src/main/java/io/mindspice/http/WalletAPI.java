package io.mindspice.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Wallet;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.TypeRefs;
import io.mindspice.schemas.object.Coin;
import io.mindspice.schemas.object.SpendBundle;
import io.mindspice.schemas.wallet.*;
import io.mindspice.schemas.wallet.cat.Cat;
import io.mindspice.schemas.wallet.cat.CatAssetInfo;
import io.mindspice.schemas.wallet.cat.StrayCat;
import io.mindspice.schemas.wallet.did.*;
import io.mindspice.schemas.wallet.offers.*;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;

import java.io.IOException;
import java.util.List;


public class WalletAPI extends SharedAPI {

    public WalletAPI(RPCClient client) {
        super(client, ChiaService.FARMER);
    }

    //TODO this wallet rpc, I scrapped everything needs to be more concise with slew of arguments



    /*  WALLET MANAGEMENT */

    public byte[] createNewWalletAsBytes(ObjectNode jsonObj) throws RPCException {
        try {
            var req = new Request(Wallet.CREATE_NEW_WALLET, JsonUtils.writeBytes(jsonObj));
            return client.makeRequest(req);
        } catch (JsonProcessingException | RPCException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public ApiResponse<Wallet> createNewWallet(ObjectNode jsonObj) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(createNewWalletAsBytes(jsonObj));
            System.out.println(JsonUtils.writeString(jsonNode));
            return newResponse(jsonNode, Wallet.class, Wallet.CREATE_NEW_WALLET);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

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



    /* CAT WALLET */

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
            var req = new Request(Wallet.CAT_SPEND, JsonUtils.writeBytes(jsonObj));
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
            var req = new Request(Wallet.GET_ALL_OFFERS, JsonUtils.writeBytes(jsonObj));
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

    public byte[] selectCoinsAsBytes(ObjectNode jsonObj) throws RPCException {
        try {
            var req = new Request(Wallet.SELECT_COINS, JsonUtils.writeBytes(jsonObj));
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

    public ApiResponse<List<Coin>> selectCoins(ObjectNode jsonObj) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(selectCoinsAsBytes(jsonObj));
            return newResponseList(jsonNode, "coins", TypeRefs.COIN_LIST, Wallet.SELECT_COINS);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    public byte[] takeOfferAsBytes(ObjectNode jsonObj) throws RPCException {
        try {
            var data = JsonUtils.writeBytes(jsonObj);
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

    public ApiResponse<TradeRecord> takeOffer(ObjectNode jsonObj) throws RPCException {
        try {
            var jsonNode = JsonUtils.readTree(takeOfferAsBytes(jsonObj));
            return newResponse(jsonNode, "trade_record", TradeRecord.class, Wallet.TAKE_OFFER);
        } catch (IOException e) {
            throw new RPCException("Error reading response JSON", e);
        }
    }

    /* DID WALLET */

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
            return newResponseList(jsonNode,"mnemonic", TypeRefs.STRING_LIST, Wallet.GENERATE_MNEMONIC);
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

}
