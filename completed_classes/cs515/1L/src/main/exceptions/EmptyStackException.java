package exceptions;

public class EmptyStackException extends Exception { 
    
    public EmptyStackException(String errorMessage) {
        super(errorMessage);
    }

}