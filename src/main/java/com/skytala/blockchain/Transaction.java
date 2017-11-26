package com.skytala.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * One entry in the list of transaction within a block
 * If a new block is minded, the transaction is part of the block and
 * the block is part in the longest chain (and therefore outlives the
 * consensus algorithm) the transaction is committed, otherwise rolled
 * back.
 *
 * It can take a while, until you know, that your transaction was part
 * of the longest chain.
 * Because you want be able to add your transaction to the last block
 * and to the longest chain you know, you don't want, that it is too
 * easy to mine block. Because every time a new block is mined it can
 * be, that your transaction is rolled back.
 */
public class Transaction {
    @NotNull private String sender;
    @NotNull private String recipient;
    @NotNull private Integer amount;

    public Transaction(@JsonProperty("sender") String sender,
                       @JsonProperty("recipient") String recipient,
                       @JsonProperty("amount") Integer amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        if (recipient != null ? !recipient.equals(that.recipient) : that.recipient != null) return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;
    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", amount=" + amount +
                '}';
    }
}