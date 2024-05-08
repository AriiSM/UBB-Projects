package com.example.socialnetwork_1v.utils;

import com.example.socialnetwork_1v.utils.events.Event;

public interface Observable <E extends Event>{
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObserver(E t);
}
