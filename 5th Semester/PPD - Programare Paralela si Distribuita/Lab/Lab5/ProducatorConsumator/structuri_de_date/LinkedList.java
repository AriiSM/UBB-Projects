package ppd.structuri_de_date;

import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    public void addOrUpdate(int id, int score, String country, Set<Integer> blackList) {
        if(blackList.contains(id)) {
            removeParticipant(id);
            return;
        }

        head.lock.lock();
        MyNode curr = head;

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

    public void removeParticipant(int id) {
        MyNode curr = head;
        MyNode next = head.next;

        curr.lock.lock();
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

    public void sort() {
        // Implementăm sortarea finală.
        synchronized (head) {
            MyNode sortedHead = new MyNode(-1, 0, "HEAD");
            MyNode current = head.next;
            while (current != tail) {
                MyNode next = current.next;
                insertInSortedOrder(sortedHead, current);
                current = next;
            }
            head.next = sortedHead.next;
        }
    }

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
}