package org.example.cmd.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

public class Hash {

    public static String stringToSha256(String text){
        MessageDigest digest = null;
        try {
            digest = getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return new String(hash);
    }
}
