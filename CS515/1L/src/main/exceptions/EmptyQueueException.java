package exceptions;

public class EmptyQueueException extends Exception { 
    
    public EmptyQueueException(String errorMessage) {
        super(errorMessage);
    }

}