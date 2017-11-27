package com.skytala.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytala.blockchain.Chain;
import com.skytala.blockchain.Node;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Utils {
    public static Long time() {
        return new Timestamp(System.currentTimeMillis()).getTime();
    }

    public static String asJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String randomName() {
        return UUID.randomUUID().toString();
    }

    public static String sha256(String toEncode) {
        try {
            return sha256Throwing(toEncode);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String sha256Throwing(String toEncode) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                toEncode.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (byte aHash : hash) {
            String hex = Integer.toHexString(0xff & aHash);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static Chain readChain(URL url, String route) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(concat(url, route), Chain.class);
        } catch (IOException e) {
            System.err.println(url);
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static List<Node> readNodes(URL url, String route) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(concat(url, route), new TypeReference<List<Node>>(){});
        } catch (IOException e) {
            System.err.println(url);
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static URL concat(URL url, String route) {
        try {
            return new URL(url.toString()+route);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean get(URL url, String route) {
        try {
            new Scanner(concat(url, route).openStream(), "UTF-8").useDelimiter("\\A").next();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
