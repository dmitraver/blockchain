package com.skytala.blockchain;

import java.util.LinkedList;
import java.util.List;

/**
 Responsible for managing the chain. It will store transactions
 and have some helper methods for adding new blocks to the chain.
 */
public class Blockchain {

    private List<Block> chain = new LinkedList<>();
    private List<Transaction> currentTransactions = new LinkedList<>();

    /**
     * Create a new Block in the Blockchain
     * @param proof The proof given by the Proof of Work algorithm
     * @param previousHash Hash of the previous hash
     * @return A new Block
     */
    public Block newBlock(Integer proof, String previousHash) {
        return null;
    }

    public Block newBlock(Integer proof) {
        return newBlock(proof, null);
    }

    /**
     * We’ll need a way of adding transactions to a Block.
     * This method is responsible for this:
     * Creates a new transaction to go into the next mined Block
     * @param sender Address of the Sender
     * @param recipient Address of the Recipient
     * @param amount Amoun
     * @return The indes of the Block that will hold this transaction
     */
    public Integer newTransaction(String sender, String recipient, Integer amount) {
        return null;
    }

    public Integer newTransaction(Transaction transaction) {
        return null;
    }

    /**
     * Creates a SHA-256 hash of a Block, encodes as
     * JSON and then Hashed
     * @param block Block
     * @return The SHA-256 Hash of this Block
     */
    public String hash(Block block) {
        return null;
    }

    public Block lastBlock() {
        return null;
    }
}
