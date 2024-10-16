package io.mindspice.jxch.rpc.schemas.wallet.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.wallet.Transaction;

import java.util.List;


public record DAOCreatePropResponse(
        String ProposalId,
        DAOResponse proposalResp
) {
    public DAOCreatePropResponse(
            @JsonProperty("proposal_id") String proposalId,
            @JsonProperty("tx") Transaction tx,
            @JsonProperty("tx_id") String txId,
            @JsonProperty("transactions") List<Transaction> transactions) {
        this(proposalId, new DAOResponse(tx, txId, transactions));
    }

    public Transaction tx() {
        return this.proposalResp.tx();
    }

    public String txId() {
        return this.proposalResp.txId();
    }

    public List<Transaction> transactions() {
        return this.proposalResp.transactions();
    }
}
