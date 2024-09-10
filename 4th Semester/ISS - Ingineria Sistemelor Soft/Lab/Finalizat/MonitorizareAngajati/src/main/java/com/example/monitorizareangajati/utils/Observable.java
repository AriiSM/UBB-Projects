package com.example.monitorizareangajati.utils;

import com.example.monitorizareangajati.utils.events.Event;

public interface Observable <E extends Event>{
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObserver(E t);
}