package ppd.structuri_de_date;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyNode {
    int id;
    int points;
    String country;
    MyNode next;
    final Lock lock;

    // Constructor
    public MyNode(int id, int points, String country) {
        this.id = id;
        this.points = points;
        this.country = country;
        this.next = null;
        this.lock = new ReentrantLock();
    }


    public MyNode getNext() {
        return next;
    }

    public String getCountry() {
        return country;
    }

    public int getPoints() {
        return points;
    }

    public int getId() {
        return id;
    }


}
