package com.skytala.blockchain;

import java.util.List;

/**
 * The basic building block of the blockchain
 * Each block can contain many transactions (depends on the blockchain implementation)
 * Each block can have an fixed size (if you want. In Bitcoin this is the case)
 * This blocks are the append-only list of the blockchain
 */
public class Block {
    private Integer index;   // For the global index normally an auto-increment
    private Long timestamp;  // in Unix time
    private List<Transaction> transactions; // the actual content of the blockchain
    private Integer proof;   // That the block actually fits in the chain
    /**
     * Each new block contains within itself, the hash of the previous Block.
     * This is crucial because it’s what gives blockchains immutability:
     * If an attacker corrupted an earlier Block in the chain then all subsequent
     * blocks will contain incorrect hashes.
     */
    private String previousHash;


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Integer getProof() {
        return proof;
    }

    public void setProof(Integer proof) {
        this.proof = proof;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return index != null ? index.equals(block.index) : block.index == null;
    }

    @Override
    public int hashCode() {
        return index != null ? index.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", transactions=" + transactions +
                ", proof=" + proof +
                ", previousHash='" + previousHash + '\'' +
                '}';
    }
}
