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
        return null;
    }

    @PostMapping("/transactions/new")
    public TransactionAddedMessage newTransaction(@Valid @RequestBody Transaction transaction) {
        return null;
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
