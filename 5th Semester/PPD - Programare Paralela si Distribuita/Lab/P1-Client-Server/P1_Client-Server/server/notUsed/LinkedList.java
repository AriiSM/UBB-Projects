package ppd.server.notUsed;

import ppd.common.CompetitorBlock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LinkedList {
    private final MyNode head;  // Primul nod din listă
    private final MyNode tail;

    public LinkedList() {
        this.head = new MyNode(-1, 0, "HEAD");
        this.tail = new MyNode(-1, 0, "TAIL");
        head.next = tail;
    }

    public MyNode getHead() {
        return head;
    }

    public MyNode getTail() {
        return tail;
    }

    // Dimensiunea listei
    public int size() {
        int size = 0;
        MyNode current = head.next;

        while (current != null && current != tail) { // Verificăm dacă `current` nu este null
            size++;
            current = current.next; // Trecem la nodul următor
        }

        return size;
    }

    // Metoda care transformă lista într-o listă de obiecte CompetitorBlock
    public List<CompetitorBlock> toList() {
        List<CompetitorBlock> result = new ArrayList<>();
        MyNode current = head.next; // Începem de la primul nod valid
        while (current != null && current != tail) {
            CompetitorBlock concurent = new CompetitorBlock(current.country, List.of(new int[]{current.id, current.points}));
            result.add(concurent);
            current = current.next;
        }
        return result;
    }

    // Metoda care adaugă sau actualizează un concurent în listă
    public void addOrUpdate(int id, int score, String country, Set<Integer> blackList) {
        if(blackList.contains(id)) {
            removeParticipant(id);
            return;
        }

        head.lock.lock();
        MyNode curr = head;
        System.out.println("[LINKED LIST] Adding participant with ID " + id + " and score " + score + " from country " + country);
        logAction("[LINKED LIST] Adding participant with ID " + id + " and score " + score + " from country " + country);
        try {
            MyNode next = head.next;
            while (next != tail && next.id < id) {
                next.lock.lock();
                curr.lock.unlock();
                curr = next;
                next = next.next;
            }

            if (next != tail && next.id == id) {
                next.points += score;
            } else {
                MyNode newNode = new MyNode(id, score, country);
                newNode.next = next;
                curr.next = newNode;
            }
        } finally {
            curr.lock.unlock();
        }
    }

    // Metoda care elimină un concurent din listă
    public void removeParticipant(int id) {
        MyNode curr = head;
        MyNode next = head.next;

        curr.lock.lock();
        System.out.println("[LINKED LIST] Removing participant with ID " + id);
        logAction("[LINKED LIST] Removing participant with ID " + id);
        try {
            while (next != null && next != tail) {
                next.lock.lock();
                try {
                    if (next.id == id) {
                        // Eliminare nod
                        curr.next = next.next;
                        next.next = null;
                        return;
                    }
                } finally {
                    next.lock.unlock();
                }
                curr.lock.unlock();
                curr = next;
                next = next.next;
                curr.lock.lock();
            }
        } finally {
            curr.lock.unlock();
        }
    }

    // Metoda care sortează lista
    public void sort() {
        synchronized (head) { // Sincronizare pentru consistență
            MyNode sortedHead = new MyNode(-1, 0, "HEAD");
            MyNode current = head.next;

            while (current != null && current != tail) { // Adăugăm verificarea pentru `null`
                MyNode next = current.next; // Salvează următorul nod
                insertInSortedOrder(sortedHead, current); // Inserează nodul în lista sortată
                current = next; // Treci la următorul nod
            }
            head.next = sortedHead.next; // Înlocuiește vechea listă cu lista sortată
        }
    }

    // Metoda care inserează un nod într-o listă sortată
    private void insertInSortedOrder(MyNode sortedHead, MyNode newNode) {
        MyNode current = sortedHead;
        while (current.next != null &&
                (current.next.points > newNode.points ||
                        (current.next.points == newNode.points && current.next.id < newNode.id))) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
    }


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