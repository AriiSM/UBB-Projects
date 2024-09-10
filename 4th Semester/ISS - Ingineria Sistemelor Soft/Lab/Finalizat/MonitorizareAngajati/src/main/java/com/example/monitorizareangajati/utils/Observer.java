package com.example.monitorizareangajati.utils;

import com.example.monitorizareangajati.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
