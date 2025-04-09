package ppd.Paralel;

import ppd.structuri_de_date.LinkedList;
import ppd.structuri_de_date.MyNode;
import ppd.structuri_de_date.MyQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Worker pentru consumatori
public class MyWorker implements Runnable {
    private final MyQueue queue;
    private final LinkedList list;
    private final Set<Integer> blacklist;

    public MyWorker(MyQueue queue, LinkedList list, Set<Integer> blacklist) {
        this.queue = queue;
        this.list = list;
        this.blacklist = blacklist;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int[] entry = queue.take();
                if (entry[0] == -1) { // Semnal STOP
                    break;
                }
                int id = entry[0];
                int score = entry[1];
                String country = "Country" + entry[2];
                list.addOrUpdate(id, score, country, blacklist);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}