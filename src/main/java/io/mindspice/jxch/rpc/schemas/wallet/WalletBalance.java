package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.enums.WalletType;

import java.math.BigInteger;

public record WalletBalance(
        @JsonProperty("wallet_id") long walletId,
        @JsonProperty("pending_change") BigInteger pendingChange,
        @JsonProperty("unspent_coin_count") long unspentCoinCount,
        @JsonProperty("fingerprint") Long fingerprint,
        @JsonProperty("wallet_type") WalletType walletType,
        @JsonProperty("confirmed_wallet_balance") BigInteger confirmedWalletBalance,
        @JsonProperty("pending_coin_removal_count") long pendingCoinRemovalCount,
        @JsonProperty("asset_id") String assetId,
        @JsonProperty("spendable_balance") BigInteger spendableBalance,
        @JsonProperty("max_send_amount") BigInteger maxSendAmount,
        @JsonProperty("unconfirmed_wallet_balance") BigInteger unconfirmedWalletBalance,
        @JsonProperty("pending_approval_balance") BigInteger pendingApprovalBalance
) {
    public WalletBalance(int walletId, long pendingChange, int unspentCoinCount, long fingerprint,
            WalletType walletType, long confirmedWalletBalance, int pendingCoinRemovalCount,
            String assetId, long spendableBalance, long maxSendAmount, long unconfirmedWalletBalance) {
        this(walletId, BigInteger.valueOf(pendingChange), unspentCoinCount, fingerprint, walletType,
                BigInteger.valueOf(confirmedWalletBalance), pendingCoinRemovalCount, assetId,
                BigInteger.valueOf(spendableBalance), BigInteger.valueOf(maxSendAmount),
                BigInteger.valueOf(unconfirmedWalletBalance), null);
    }

    public WalletBalance(int walletId, long pendingChange, int unspentCoinCount, long fingerprint,
            int walletType, long confirmedWalletBalance, int pendingCoinRemovalCount,
            String assetId, long spendableBalance, long maxSendAmount, long unconfirmedWalletBalance) {
        this(walletId, pendingChange, unspentCoinCount, fingerprint, WalletType.fromValue(walletType),
                confirmedWalletBalance, pendingCoinRemovalCount, assetId, spendableBalance,
                maxSendAmount, unconfirmedWalletBalance);
    }
}
