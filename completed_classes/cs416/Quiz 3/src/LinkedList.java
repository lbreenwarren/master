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
    
    // YOUR CODE HERE

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

    public int countOdd() {
        Node tmp = head;
        int count = 0;
        while (tmp != null) {
            if (tmp.data % 2 != 0) {
                ++count;
            }
            tmp = tmp.next;
        }
        return count;
    }

    public void addInOrder (int val) {
        if (head == null) {
            addHead(val);
            return;
        }

        Node tmp = getHead();
        while (tmp != null) {
            if (tmp == getHead() && tmp.data > val) {
                addHead(val);
                return;
            } else if (tmp == getTail()) {
                addTail(val);
                return;
            } else if (tmp.next.data > val) {
                Node newNode = new Node(val);
                tmp.next.prev = newNode;
                newNode.next = tmp.next;
                tmp.next = newNode;
                newNode.prev = tmp;
                return;
            }
            tmp = tmp.next;


        }

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
