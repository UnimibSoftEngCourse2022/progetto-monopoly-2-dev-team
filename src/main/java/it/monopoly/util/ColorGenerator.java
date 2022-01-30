package it.monopoly.util;

public class ColorGenerator {
    public static String getRandomColor() {
        String letters = "0123456789ABCDEF";
        StringBuilder color = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            color.append(letters.charAt((int) (Math.random() * 16)));
        }
        return color.toString();
    }
}
