package com.skytala.blockchain.message;

import com.skytala.blockchain.Block;
import com.skytala.blockchain.Transaction;

import java.util.List;

/**
 * Just for the API as result
 */
public class MiningMessage {
    private String message;
    private Integer index;
    private List<Transaction> transactions;
    private Integer proof;
    private String previousHash;

    public MiningMessage(String message, Block block) {
       this.message = message;
        this.index = block.getIndex();
        this.transactions = block.getTransactions();
        this.proof = block.getProof();
        this.previousHash = block.getPreviousHash();
    }

    public String getMessage() {
        return message;
    }

    public Integer getIndex() {
        return index;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Integer getProof() {
        return proof;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}
