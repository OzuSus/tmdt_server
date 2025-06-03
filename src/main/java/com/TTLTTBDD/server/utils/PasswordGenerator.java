package com.TTLTTBDD.server.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final SecureRandom random = new SecureRandom();

    public String generatePassword(int length) {
        if (length < 8) throw new IllegalArgumentException("Password must be at least 8 characters long");

        List<Character> passwordChars = new ArrayList<>();

        // Ensure required character types are included
        passwordChars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        passwordChars.add(LOWER.charAt(random.nextInt(LOWER.length()))); // optional: ensure lowercase

        String allAllowed = UPPER + LOWER + DIGITS + SPECIAL;

        while (passwordChars.size() < length) {
            passwordChars.add(allAllowed.charAt(random.nextInt(allAllowed.length())));
        }

        // Shuffle to avoid predictable character placement
        Collections.shuffle(passwordChars, random);

        // Convert to string
        StringBuilder password = new StringBuilder();
        for (char ch : passwordChars) {
            password.append(ch);
        }

        return password.toString();
    }

}

