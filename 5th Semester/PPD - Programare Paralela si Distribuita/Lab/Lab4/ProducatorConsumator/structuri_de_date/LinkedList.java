package ppd.structuri_de_date;
import java.util.Optional;

public class LinkedList {
    private MyNode head;  // Primul nod din listă
    private int size;  // Dimensiunea listei

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    // Adaugă un nod în listă
    public synchronized void add(int id, int points) {
        Optional<MyNode> existingNode = findAndRemove(id); // `findAndRemove` este sincronizat
        if (existingNode.isPresent()) {
            MyNode node = existingNode.get();
            node.addPoints(points);
            insertNode(node);
        } else {
            insertNode(new MyNode(id, points));
        }
    }

    // Găsește un nod după ID și îl elimină din listă
    public synchronized Optional<MyNode> findAndRemove(int id) {
        if (head == null) {
            return Optional.empty();
        }

        if (head.getId() == id) {
            MyNode node = head;
            head = head.getNext();
            size--;
            return Optional.of(node);
        }

        MyNode current = head;
        while (current.getNext() != null) {
            if (current.getNext().getId() == id) {
                MyNode node = current.getNext();
                current.setNext(current.getNext().getNext());
                size--;
                return Optional.of(node);
            }
            current = current.getNext();
        }
        return Optional.empty();
    }

    // Inserează un nod în listă în ordine descrescătoare după punctaj și crescătoare după ID
    private synchronized void insertNode(MyNode node) {
        if (head == null || head.getPoints() < node.getPoints() ||
                (head.getPoints() == node.getPoints() && head.getId() > node.getId())) {
            node.setNext(head);
            head = node;
        } else {
            MyNode current = head;
            // Caută locul unde trebuie inserat nodul
            while (current.getNext() != null &&
                    (current.getNext().getPoints() > node.getPoints() ||
                            // Dacă punctajele sunt egale, ordonează crescător după ID
                    (current.getNext().getPoints() == node.getPoints() && current.getNext().getId() < node.getId()))) {
                current = current.getNext();
            }
            node.setNext(current.getNext());
            current.setNext(node);
        }
        size++;
    }


    // Returnează dimensiunea listei
    public synchronized int size() {
        return size;
    }

    // Afiseaza lista
    public synchronized void print() {
        MyNode current = head;
        while (current != null) {
            System.out.println(current.getId() + " " + current.getPoints());
            current = current.getNext();
        }
    }

    // Returnează primul nod din listă
    public synchronized MyNode getHead() {
        return head;
    }
}