package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.enums.WalletType;


public record WalletBalance(

	@JsonProperty("wallet_id") int walletId,
	@JsonProperty("pending_change") long pendingChange,
	@JsonProperty("unspent_coin_count") int unspentCoinCount,
	@JsonProperty("fingerprint") long fingerprint,
	@JsonProperty("wallet_type") WalletType walletType,
	@JsonProperty("confirmed_wallet_balance") long confirmedWalletBalance,
	@JsonProperty("pending_coin_removal_count") int pendingCoinRemovalCount,
	@JsonProperty("asset_id") String assetId,
	@JsonProperty("spendable_balance") long spendableBalance,
	@JsonProperty("max_send_amount") long maxSendAmount,
	@JsonProperty("unconfirmed_wallet_balance") long unconfirmedWalletBalance
) {
	public WalletBalance(@JsonProperty("wallet_id") int walletId,
			@JsonProperty("pending_change") long pendingChange,
			@JsonProperty("unspent_coin_count") int unspentCoinCount,
			@JsonProperty("fingerprint") long fingerprint,
			@JsonProperty("wallet_type") int walletType,
			@JsonProperty("confirmed_wallet_balance") long confirmedWalletBalance,
			@JsonProperty("pending_coin_removal_count") int pendingCoinRemovalCount,
			@JsonProperty("asset_id") String assetId,
			@JsonProperty("spendable_balance") long spendableBalance,
			@JsonProperty("max_send_amount") long maxSendAmount,
			@JsonProperty("unconfirmed_wallet_balance") long unconfirmedWalletBalance) {
		this(
		walletId,
		pendingChange,
		unspentCoinCount,
		fingerprint,
		WalletType.fromValue(walletType),
		confirmedWalletBalance,
		pendingCoinRemovalCount,
		assetId,
		spendableBalance,
		maxSendAmount,
		unconfirmedWalletBalance
		);
	}
}