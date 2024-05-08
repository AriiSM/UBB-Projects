package com.example.socialnetwork_1v.utils;

import com.example.socialnetwork_1v.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
