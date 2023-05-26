package dev.lukman.maybank.utils;

import java.security.SecureRandom;

public class GenerateRandomString {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    private GenerateRandomString() {
        throw new UnsupportedOperationException("GenerateRandomString is a utility class and should not be instantiated.");
    }

    public static String generateSecureRandomString(int length) {
        StringBuilder returnValue = new StringBuilder("0x");

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }
}
