package tut.ac.za.bookingapps2.Service;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
    private static final int PASSWORD_LENGTH = 12;

    private static Random random = new SecureRandom();

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder();

        // Ensure at least one lowercase letter
        password.append(getRandomCharacter(CHAR_LOWER));

        // Ensure at least one uppercase letter
        password.append(getRandomCharacter(CHAR_UPPER));

        // Ensure at least one digit
        password.append(getRandomCharacter(DIGIT));

        // Ensure at least one special character
        password.append(getRandomCharacter(SPECIAL_CHARS));

        // Fill the remaining characters with a random mix of allowed characters
        for (int i = password.length(); i < PASSWORD_LENGTH; i++) {
            String allowedChars = CHAR_LOWER + CHAR_UPPER + DIGIT + SPECIAL_CHARS;
            password.append(getRandomCharacter(allowedChars));
        }

        // Shuffle the characters to make the password more random
        return shuffleString(password.toString());
    }

    private static char getRandomCharacter(String characters) {
        return characters.charAt(random.nextInt(characters.length()));
    }

    private static String shuffleString(String string) {
        char[] chars = string.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}