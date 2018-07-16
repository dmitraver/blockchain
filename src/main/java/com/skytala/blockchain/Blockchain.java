package com.skytala.blockchain;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.skytala.infrastructure.Utils.*;

/**
 Responsible for managing the chain. It will store transactions
 and have some helper methods for adding new blocks to the chain.
 */
public class Blockchain {

    private List<Block> chain = new LinkedList<>();
    private List<Transaction> currentTransactions = new LinkedList<>();
    private Set<Node> nodes = new HashSet<>();

    /**
     * Create a new Block in the Blockchain
     * @param proof The proof given by the Proof of Work algorithm
     * @param previousHash Hash of the previous hash
     * @return A new Block
     */
    public Block newBlock(Integer proof, String previousHash) {
        if(previousHash==null)
            previousHash = hash(chain.get(chain.size()-1));

        Block block = new Block();
        block.setIndex(chain.size());
        block.setTimestamp(time());
        block.setProof(proof);
        block.setPreviousHash(previousHash);
        block.setTransactions(currentTransactions);

        currentTransactions = new LinkedList<>();//<-|Reset the current
        chain.add(block);                        //  |list of transactions

        return block;
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
        return newTransaction(new Transaction(sender, recipient, amount));
    }

    public Integer newTransaction(Transaction transaction) {
        currentTransactions.add(transaction);
        return lastIndex();
    }

    /**
     * Simple Proof of Work Algorithm:
     * - Find a number p' such that hash(pp') contains leading
     *   4 zeroes, where p is the previous p'
     * - p is the previous proof, and p' is the new proof
     * @param lastProof
     * @return the new proof
     */
    public static Integer proofOfWork(Integer lastProof) {
        Integer proof = 0;
        while(!validProof(lastProof, proof))
            proof += 1;
        return proof;
    }

    /**
     * Validates the Proof: Does hash(last_proof, proof) contain
     * 4 leading zeroes?
     * @param lastProof Previous Proof
     * @param proof Current Proof
     * @return True if correct, False if not
     */
    private static boolean validProof(Integer lastProof, Integer proof) {
        String guessHash = sha256(lastProof.toString()+proof.toString());
        return guessHash.endsWith("0000");
        /*
        To adjust the difficulty of the algorithm, we could modify the
        number of leading zeroes. But 4 is sufficient.
        You’ll find out that the addition of a single leading zero makes
        a mammoth difference to the time required to find a solution.
         */
    }

    /**
     * Creates a SHA-256 hash of a Block, encodes as
     * JSON and then Hashed
     * @param block Block
     * @return The SHA-256 Hash of this Block
     */
    public String hash(Block block) {
        String blockString = asJson(block);
        return sha256(blockString);
    }

    public Block lastBlock() {
        if(chain.size() == 0)
            return null;
        return chain.get(lastIndex());
    }

    public Integer lastIndex() {
        return chain.size() - 1;
    }

    public List<Block> getChain() {
        return chain;
    }

    public List<Transaction> getCurrentTransactions() {
        return currentTransactions;
    }

    /**
     * Add a new node to the list of nodes
     * @param address Address of node. Eg. 'http://192.168.0.5:5000'
     */
    public void registerNode(URL address) {
        registerNode(new Node(address));
    }

    public void registerNode(Node node) {
        this.nodes.add(node);
    }

    /**
     * Determine if a given blockchain is valid
     * A conflict is when one node has a different chain to another node.
     * To resolve this, we’ll make the rule that the longest valid
     * chain is authoritative. In other words, the longest chain on the network
     * is the de-facto one. Using this algorithm, we reach Consensus amongst the
     * nodes in our network.
     * @param chain A blockchain
     * @return True if valid, False if not
     */
    public boolean validChain(List<Block> chain) {
        Block lastBlock = chain.get(0);
        Integer currentIndex = 1;

        // Check the whole chain, if the block<->hash valid
        while(currentIndex < chain.size()) {
            Block block = chain.get(currentIndex);
            System.out.println("Last Block: "+lastBlock);
            System.out.println("Block: "+ block);
            System.out.println("-----");


            // Check that the hash of the block is correct
            if(!block.getPreviousHash().equals(hash(lastBlock)))
                return false;

            // check that the Proof of Work is correct
            if(!validProof(lastBlock.getProof(), block.getProof()) )
                return false;

            lastBlock = block;
            currentIndex += 1;
        }
        return true;
    }

    /**
     * This is our Consensus Algorithm, it resolves conflicts
     * by replacing our chain with the longest one in the network.
     * @return True if our chain was replaced, False if not
     */
    public boolean resolveConflicts() {
        Set<Node> neighbours = nodes;
        List<Block> newChain = new LinkedList<>();

        // We are only looking for chains longer than ours
        Integer maxLength = chain.size();

        // Grab and verify the chains from all the nodes in our network
        for(Node node : neighbours) {
            Chain nodeChain = node.readChain();
            if(nodeChain != null) {
                Integer length = nodeChain.getLength();
                List<Block> chain = nodeChain.getChain();

                if(length > maxLength && validChain(chain)) {
                    maxLength = length;
                    newChain = chain;
                }
            }
        }

        // Replace our chain if we discovered a new, valid chain longer than ours
        if(!newChain.isEmpty()) {
            this.chain = newChain;
            return true;
        }

        return false;
    }

    public Set<Node> getNodes() {
        return nodes;
    }
}
