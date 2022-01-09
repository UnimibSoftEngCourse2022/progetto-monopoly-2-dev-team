package util;

public class Pair<T, R> {
    private final T first;
    private final R second;

    public Pair(T first, R second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public R getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
