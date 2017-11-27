package com.skytala.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A container (with business meaning) for a list of blocks
 * Nothing special, but you can convert it into JSON, therefore (and for
 * the business-meaning) it is useful instead of a List<Block>
 */
public class Chain {

    private List<Block> chain;
    private Integer length;

    public Chain(@JsonProperty("chain") List<Block> chain) {
        this.chain = chain;
        this.length = chain.size();
    }

    public List<Block> getChain() {
        return chain;
    }

    public Integer getLength() {
        return length;
    }
}
