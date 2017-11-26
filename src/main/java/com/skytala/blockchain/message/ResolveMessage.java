package com.skytala.blockchain.message;

import com.skytala.blockchain.Block;

import java.util.List;

/**
 * Just for the API as result
 */
public class ResolveMessage {
    private String message;
    private List<Block> newChain;

    public ResolveMessage(String message, List<Block> newChain) {
        this.message = message;
        this.newChain = newChain;
    }

    public String getMessage() {
        return message;
    }

    public List<Block> getNewChain() {
        return newChain;
    }
}
