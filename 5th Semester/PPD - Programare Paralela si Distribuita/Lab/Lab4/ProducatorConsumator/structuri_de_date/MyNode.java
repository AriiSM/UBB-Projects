package ppd.structuri_de_date;

public class MyNode {
    private int id;
    private int points;
    private MyNode next;

    // Constructor
    public MyNode(int id, int points) {
        this.id = id;
        this.points = points;
        this.next = null;
    }


    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public MyNode getNext() {
        return next;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }

    public void addPoints(int points) {
        if (points >= 0 && this.points >= 0) {
            this.points += points;
        } else {
            System.err.println("Ignor punctaj invalid pentru nodul "+id);
        }
    }


    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", points=" + points +
                ", next=" + next +
                '}';
    }
}
