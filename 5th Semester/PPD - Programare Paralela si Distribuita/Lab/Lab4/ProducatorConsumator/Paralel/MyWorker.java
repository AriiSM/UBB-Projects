package ppd.Paralel;

import ppd.structuri_de_date.LinkedList;
import ppd.structuri_de_date.MyNode;
import ppd.structuri_de_date.MyQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyWorker {
    public static final MyNode STOPPER = new MyNode(-1, -1);
    private final String directory = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab4\\ProducatorConsumator\\src\\main\\java\\ppd\\data";
    private final MyQueue<MyNode> queue;

    // Lock pentru sincronizare pe BlackList
    private final Lock blackListLock = new ReentrantLock();

    public MyWorker(int capacity) {
        this.queue = new MyQueue<>(capacity);
    }

    public MyWorker() {
        this.queue = new MyQueue<>(100000);
    }

    // Producează fișierele pentru fiecare țară și problemă
    public void produce(int country, int startProblem, int endProblem, List<Integer> blackList) throws InterruptedException {
        for (int problem = startProblem; problem <= endProblem; problem++) {
            String fileName = directory + "\\RezultateC" + country + "_P" + problem + ".txt";
            try (Scanner scanner = new Scanner(new File(fileName))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split("[,\\s]+");
                        if (parts.length == 2) {
                            int id = Integer.parseInt(parts[0]);
                            int points = Integer.parseInt(parts[1]);

                            blackListLock.lock();
                            try {
                                if (points == -1) {
                                    if (!blackList.contains(id)) {
                                        blackList.add(id);
                                    }
                                    continue;
                                }

                                if (!blackList.contains(id)) {
                                    queue.enqueue(new MyNode(id, points));
                                }
                            } finally {
                                blackListLock.unlock();
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("Fișierul nu a fost găsit: " + fileName);
            }
        }
    }


    // Consumă elementele din coadă și le adaugă în LinkedList, dacă nu sunt pe BlackList
    public void consume(LinkedList results, List<Integer> blackList) throws InterruptedException {
        List<MyNode> tempList = new ArrayList<>();

        while (true) {
            MyNode node = queue.dequeue();
            if (node == STOPPER) {
                break;
            }

            blackListLock.lock();  // Blochează pentru verificarea BlackList
            try {
                // Verifică dacă nu este în BlackList
                if (!blackList.contains(node.getId())) {
                    tempList.add(node);
                }
            } finally {
                blackListLock.unlock(); // Deblochează BlackList
            }
        }

        // Sortează lista temporară după puncte și apoi după ID
        Collections.sort(tempList, Comparator.comparingInt(MyNode::getPoints)
                .thenComparingInt(MyNode::getId));

        // Adaugă nodurile sortate în LinkedList
        for (MyNode node : tempList) {
            results.add(node.getId(), node.getPoints());
        }
    }

    public MyQueue<MyNode> getQueue() {
        return queue;
    }
}