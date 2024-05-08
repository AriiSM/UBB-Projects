package com.example.restaurante.observer;

public interface Observable <E>{
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObserver(E t);
}