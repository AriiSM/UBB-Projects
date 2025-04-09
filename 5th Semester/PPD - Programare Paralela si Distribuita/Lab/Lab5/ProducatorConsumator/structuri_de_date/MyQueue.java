package ppd.structuri_de_date;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue {
    private final int[][] queue;
    private int head, tail, size;
    private final int capacity;
    private final Lock lock;
    private final Condition EMPTY;
    private final Condition FULL;

    public MyQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new int[capacity][];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
        this.lock = new ReentrantLock();
        this.EMPTY = lock.newCondition();
        this.FULL = lock.newCondition();
    }

    public void put(int[] entry) throws InterruptedException {
        lock.lock();
        try {
            while (size == capacity) {
                EMPTY.await(); //asteapta sa se goleasca
            }
            queue[tail] = entry;
            tail = (tail + 1) % capacity;
            size++;
            FULL.signal(); //ii anunta pe consumatori ca s-a umplut coada
        } finally {
            lock.unlock();
        }
    }

    public int[] take() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                FULL.await(); //asteapta sa se umple
            }
            int[] entry = queue[head];
            head = (head + 1) % capacity;
            size--;
            EMPTY.signal(); // ii anunta pe producatori ca s-a golit coada
            return entry;
        } finally {
            lock.unlock();
        }
    }
}