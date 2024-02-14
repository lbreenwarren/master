public class LinkedList{

    public class Node{ //inner class
        int data;
        Node next;
        Node prev;

        public Node(int data){
            this.data = data;
        }
    }
    
    public Node head;
    public Node tail;

    public LinkedList(){
        head = null;
        tail = null;
    }
    
    public int countOdd() {
        int total = 0;
        Node tmp = head;
        while (tmp != null) {
            if (tmp.data % 2 != 0) {
                ++total;
            }
            tmp = tmp.next;
        }

        return total;
    }

    public void addInOrder(int val) {
        Node tmp = head;
        boolean nodeFound = false;

        while (tmp != null && !nodeFound) {
            if(tmp.data >= val) {
                nodeFound = true;
            } else {
                tmp = tmp.next;
            }
        }

        if (tmp == head) {
            addHead(val);
        } else if (tmp == null) {
            addTail(val);
        } else {
            Node newNode = new Node(val);
            newNode.next = tmp;
            newNode.prev = tmp.prev;
            tmp.prev.next = newNode;
            tmp.prev = newNode;

        }

    }

    public void addHead( int data ){
        Node n = new Node( data );
        if(head == null){ //empty list
            tail = n;
        }
        else{
            head.prev = n;
        }
        n.next = head;
        
        head = n;
    }

    public void addTail( int data ){
        Node n = new Node( data );
        if(tail == null){ //empty list
            head = n;
        }
        else{
            tail.next = n;
        }
        n.prev = tail;
        tail = n;
    }
    
    public Node getHead() {
        return head;
    }
    
    public Node getTail() {
        return tail;
    }
    
    public String toString() {
        String res = "";
        
        Node curr = head;
        
        if (curr != null) {
            res += curr.data;
            curr = curr.next;
        }
        
        while (curr != null) {
            res += ", " + curr.data;
            curr = curr.next;
        }

        return res;
    }
}
