package ppd.structuri_de_date;

public class MyQueue<T> {
    private T[] elements;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public MyQueue() {
        this.capacity = Integer.MAX_VALUE;
    }

    @SuppressWarnings("unchecked")
    public MyQueue(int initialCapacity) {
        this.capacity = initialCapacity;
        this.elements = (T[]) new Object[capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    // Adaugă un element la coadă (enqueue)
    public synchronized void enqueue(T element) throws InterruptedException {
        while (isFull()) {
            wait(); // Așteaptă dacă coada e plină
        }
        elements[rear] = element;
        rear = (rear + 1) % capacity;
        size++;
        notifyAll(); // Notifică consumatorii că există elemente noi
    }

    // Scoate un element din coadă (dequeue)
    public synchronized T dequeue() throws InterruptedException {
        while (isEmpty()) {
            wait(); // Așteaptă dacă coada este goală
        }
        T element = elements[front];
        front = (front + 1) % capacity;
        size--;
        notifyAll(); // Notifică producătorii că există spațiu
        return element;
    }

    // Verifică dacă coada este goală
    public synchronized boolean isEmpty() {
        return size == 0;
    }

    // Verifică dacă coada este plină
    public synchronized boolean isFull() {
        return size == capacity;
    }

    // Returnează numărul de elemente din coadă
    public synchronized int getSize() {
        return size;
    }

    // Afișează coada pentru debug
    public synchronized void printQueue() {
        System.out.print("Queue: ");
        for (int i = 0; i < size; i++) {
            System.out.print(elements[(front + i) % capacity] + " ");
        }
        System.out.println();
    }
}
