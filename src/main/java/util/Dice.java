package util;

public class Dice {
    public static Pair<Integer, Integer> roll() {
        int first = (int) (Math.random() * 6) + 1;
        int second = (int) (Math.random() * 6) + 1;
        return new Pair<>(first, second);
    }
}
