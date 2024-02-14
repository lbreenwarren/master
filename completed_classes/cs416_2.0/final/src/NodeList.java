import java.util.Scanner;
import java.util.ArrayList;

public class NodeList{

    Node head;
    Node tail;

    public class Node{ //inner class
        String key;
        int data;
        Node next;
        Node prev;

        public Node(String key, int data){
            this.key = key;
            this.data = data;
        }
    }

    public ArrayList<Integer> findAll(String value) {
        Node tmp = head;
        ArrayList<Integer> ret = new ArrayList<Integer>();
        while (tmp != null) {
            if (tmp.key.equals(value)) {
                ret.add(tmp.data);
            }
        }

        return ret;
    }

    public void addHead( String key, int data ){
        Node n = new Node( key, data );
        if(head == null){ //empty list
            tail = n;
        }
        else{
            head.prev = n;
        }
        n.next = head;

        head = n;
    }

    public void addTail( String key, int data ){
        Node n = new Node( key, data );
        if(tail == null){ //empty list
            head = n;
        }
        tail.next = n;
        n.prev = tail;
        tail = n;
    }

    public String toString(){
        String ret = "";

        Node curr = head;
        while( curr != null ){
            ret += "( " + curr.key + ", " + curr.data + " ) ";
            curr = curr.next;
        }

        return ret;
    }
}
