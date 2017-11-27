package com.skytala.blockchain;

import com.skytala.blockchain.message.MiningMessage;
import com.skytala.blockchain.message.TransactionAddedMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.skytala.infrastructure.Utils.randomName;

@RestController
public class BlockchainApi {

    private Blockchain blockchain = new Blockchain();
    private String nodeIdentifier = randomName();

    public BlockchainApi() {
        System.out.println("Your ID is: "+nodeIdentifier);
    }


    /**
     * Our mining endpoint is where the magic happens, and it’s easy.
     * It has to do three things:
     * - Calculate the Proof of Work
     * - Reward the miner (us) by adding a transaction granting us 1 coin
     * - Forge the new Block by adding it to the chain
     */
    @GetMapping("/mine")
    public MiningMessage mine() {
        // We run the proof of work algorithm to get the next proof...
        Block lastBlock = blockchain.lastBlock();
        Integer lastProof = blockchain.lastIndex();
        Integer proof = Blockchain.proofOfWork(lastProof);

        // We must receive a reward for finding the proof
        // The sender is "0" to signify that this node has mined a new coin
        blockchain.newTransaction("0", nodeIdentifier, 1);

        // Forge the new Block by adding it to the chain
        String previousHash = blockchain.hash(lastBlock);
        Block block = blockchain.newBlock(proof, previousHash);

        return new MiningMessage("New Block Forged", block);
    }

    @PostMapping("/transactions/new")
    public TransactionAddedMessage newTransaction(@Valid @RequestBody Transaction transaction) {
        Integer index = blockchain.newTransaction(transaction);
        return TransactionAddedMessage.of("Transaction will be added to Block "+index);
    }

    /**
     * The full chain of blocks that is known for this local blockchain
     * This is no promise, that it is the longest chain in network
     * @return the chain of blocks
     */
    @GetMapping("/chain")
    public Chain fullChain() {
        return new Chain(blockchain.getChain());
    }

}
