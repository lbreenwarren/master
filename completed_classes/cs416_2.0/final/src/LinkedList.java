import java.util.Scanner;
import java.util.ArrayList;

public class LinkedList<T>{

    Node head;
    Node tail;

    public class Node{ //inner class
        T data;
        Node next;
        Node prev;

        public Node(T data){
            this.data = data;
        }
    }

    public void addHead( T data ){
        Node n = new Node( data );

        if(head == null){ //empty list
            head = n;
            tail = n;
            return;
        }
        head.prev = n;
        n.next = head;

        head = n;
    }

    public void addTail( T data ){
        Node n = new Node( data );
        if(tail == null){ //empty list
            head = n;
            tail = n;
            return;
        }
        tail.next = n;
        n.prev = tail;
        tail = n;
    }

    public T remHead( ){
        Node ret = head;

        if(head==null) //empty list
            return null;
        if(head==tail){ //empty after removing tail
            head=null;
            tail=null;
            return ret.data;
        }
        head = head.next;
        head.prev = null;
        return ret.data;
    }

    public T remTail( ){
        Node ret = tail;

        if(tail==null) //empty list
            return null;
        if(head==tail){ //empty after removing tail
            head=null;
            tail=null;
            return ret.data;
        }

        tail = tail.prev;
        if(tail==null)
            head = null;
        else{
            tail.next = null;
        }

        return ret.data;
    }

    public T getHead( ){
        if(head == null) //empty list
            return null;
        return head.data;
    }

    public T getTail( ){
        if(tail == null) //empty list
            return null;
        return tail.data;
    }

    public String toString(){
        String ret = "";

        Node curr = head;
        while( curr != null ){
            ret += curr.data + " ";
            curr = curr.next;
        }

        return ret;
    }
}
