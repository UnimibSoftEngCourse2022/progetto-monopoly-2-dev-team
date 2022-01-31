package it.monopoly.util;

import java.util.Random;

public class ColorGenerator {
    private static final Random random = new Random();

    private ColorGenerator() {
    }

    public static String getRandomColor() {
        String letters = "0123456789ABCDEF";
        StringBuilder color = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            color.append(letters.charAt(random.nextInt(16)));
        }
        return color.toString();
    }
}
