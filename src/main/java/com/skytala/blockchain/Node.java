package com.skytala.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skytala.infrastructure.Utils;

import java.net.URL;
import java.util.List;

/**
 * Represents one known node in the blockchain network. Not every node has to be known
 * A Node is identified with its url (how it can reached)
 */
public class Node {

    private URL url;

    public Node(@JsonProperty("url") URL url) {
        this.url = url;
    }

    public URL url() {
        return url;
    }

    public Chain readChain() {
        return Utils.readChain(url, "/chain");
    }

    public List<Node> readNeigbours() {
        return Utils.readNodes(url, "/nodes/all");
    }

    public URL getUrl() {
        return url;
    }

    public void resolveConflicts() {
        Utils.get(url, "/nodes/resolve");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return url != null ? url.equals(node.url) : node.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Node{" +
                "url=" + url +
                '}';
    }
}
