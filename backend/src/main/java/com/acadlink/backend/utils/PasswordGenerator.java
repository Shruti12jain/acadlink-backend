
package com.acadlink.backend.utils;
import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+[]{}<>?";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        for (int i = 4; i < length; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }

        
        return password.chars()
                .mapToObj(c -> (char) c)
                .sorted((a, b) -> random.nextInt(3) - 1)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    
         
}
