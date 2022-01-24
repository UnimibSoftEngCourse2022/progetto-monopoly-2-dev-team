package it.monopoly.view;

public interface Observable<T> {

    void register(Observer<T> observer);

    void deregister(Observer<T> observer);
}
