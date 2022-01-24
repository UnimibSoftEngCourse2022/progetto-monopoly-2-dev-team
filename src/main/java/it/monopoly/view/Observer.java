package it.monopoly.view;

public interface Observer<T> {
    void notify(T obj);
}
