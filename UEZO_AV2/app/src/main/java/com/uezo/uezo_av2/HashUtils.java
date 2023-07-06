package com.uezo.uezo_av2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public static String hashString(String input) {
        try {
            // Create an instance of MessageDigest with the desired algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string to bytes
            byte[] inputBytes = input.getBytes();

            // Hash the input bytes
            byte[] hashedBytes = digest.digest(inputBytes);

            // Convert the hashed bytes to a hexadecimal string representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Return the hashed string
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
