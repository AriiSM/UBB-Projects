package ppd.server.notUsed;

import ppd.common.CompetitorBlock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue {
    private final CompetitorBlock[][] queue;
    private int head, tail, size;
    private final int capacity;
    private final Lock lock;
    private final Condition EMPTY;
    private final Condition FULL;

    public MyQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new CompetitorBlock[capacity][];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
        this.lock = new ReentrantLock();
        this.EMPTY = lock.newCondition();
        this.FULL = lock.newCondition();
    }

    public int size() {
        return size;
    }

    // Metoda care adaugă un element în coadă
    public void put(CompetitorBlock[] entry) throws InterruptedException {
        lock.lock();
        try {
            while (size == capacity) {
                //System.out.println("[QUEUE] Full, waiting...");
                FULL.await();  // Așteaptă până când există loc
            }
            queue[tail] = entry;
            tail = (tail + 1) % capacity;
            size++;
            if (size % 10 == 0) {
                System.out.println("[QUEUE] Added entry. Size: " + size);
                logAction("[QUEUE] Added entry. Size: " + size);
            }
            EMPTY.signal();  // Semnalizează că există un element
        } finally {
            lock.unlock();
        }
    }

    // Metoda care scoate un element din coadă
    public CompetitorBlock[] take() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                //System.out.println("[QUEUE] Empty, waiting...");
                EMPTY.await();  // Așteaptă până când există elemente
            }
            CompetitorBlock[] entry = queue[head];
            head = (head + 1) % capacity;
            size--;
            if (size % 10 == 0) {
                System.out.println("[QUEUE] Took entry. Size: " + size);
                logAction("[QUEUE] Took entry. Size: " + size);
            }
            FULL.signal();  // Semnalizează că există loc liber
            return entry;
        } finally {
            lock.unlock();
        }
    }

    // Metoda care scrie în fișierul de log
    private void logAction(String message) {
        String currentDir = System.getProperty("user.dir");
        currentDir += "/src/main/java/ppd/server";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentDir + "/server_log.txt", true))) {

            // Log în fișier
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[SERVER] Error writing to log file: " + e.getMessage());
        }
    }
}
