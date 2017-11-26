package com.skytala.blockchain.message;

/**
 * Just for the API as result
 */
public class TransactionAddedMessage {
    private String message;

    public static TransactionAddedMessage of(String content) {
        TransactionAddedMessage m = new TransactionAddedMessage();
        m.message = content;
        return m;
    }

    private TransactionAddedMessage() {}

    public String getMessage() {
        return message;
    }
}
