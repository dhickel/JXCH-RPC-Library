package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WalletBalance(

	@JsonProperty("wallet_id") int walletId,
	@JsonProperty("pending_change") int pendingChange,
	@JsonProperty("unspent_coin_count") int unspentCoinCount,
	@JsonProperty("fingerprint") long fingerprint,
	@JsonProperty("wallet_type") int walletType,
	@JsonProperty("confirmed_wallet_balance") int confirmedWalletBalance,
	@JsonProperty("pending_coin_removal_count") int pendingCoinRemovalCount,
	@JsonProperty("asset_id") String assetId,
	@JsonProperty("spendable_balance") int spendableBalance,
	@JsonProperty("max_send_amount") int maxSendAmount,
	@JsonProperty("unconfirmed_wallet_balance") int unconfirmedWalletBalance
) {
}