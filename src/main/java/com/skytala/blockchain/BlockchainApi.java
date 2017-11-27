package com.skytala.blockchain;

import com.skytala.blockchain.message.MiningMessage;
import com.skytala.blockchain.message.ResolveMessage;
import com.skytala.blockchain.message.TransactionAddedMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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
        // First of all get in sync (as good as possible with the network, so that
        // it becomes more likely that our block get accepted
        blockchain.resolveConflicts();

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

        // The others must be notified, if a new block is mined, otherwise
        // the block will not accepted by the network
        for(Node node : blockchain.getNodes())
            node.resolveConflicts();

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

    @PostMapping("/nodes/register")
    public String registerNodes(@Valid @RequestBody Node node) {
        blockchain.registerNode(node);
        return "New node has been added";
    }

    /**
     * Copies all neighbours of the given node
     * Good, when entering the network so neighbours can easily collected
     * @param nodeToCopy The node, which neighbours will be downloaded and copied
     * @return An information, if it was successful (for display to the user)
     */
    @PostMapping("/nodes/copy")
    public String copyNode(@Valid @RequestBody Node nodeToCopy) {
        blockchain.registerNode(nodeToCopy);
        List<Node> nodesToAdd = nodeToCopy.readNeigbours();
        if(nodesToAdd == null)
            return "Could not fetch nodes";
        for(Node node : nodesToAdd)
            blockchain.registerNode(node);
        return nodesToAdd.size() + " Nodes added";
    }

    /**
     * Runs through all registered nodes and resolves the conflicts
     * This is the starting point of the consensus algorithm:
     * If an longer chain is found at the neighbours than the own chain
     * is replaced by this
     */
    @GetMapping("/nodes/resolve")
    public ResolveMessage consensus() {
        Boolean replaced = blockchain.resolveConflicts();
        if(replaced)
            return new ResolveMessage("Our chain was replaced", blockchain.getChain());
        else
            return new ResolveMessage("Our chain is authoritative", blockchain.getChain());
    }

    /**
     * Returns all registered nodes on this blockchain node as json
     * @return all nodes for this blockchain
     */
    @GetMapping("/nodes/all")
    public Set<Node> allNodes() {
        return blockchain.getNodes();
    }
}
