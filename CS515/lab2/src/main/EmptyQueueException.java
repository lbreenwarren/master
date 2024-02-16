 public class EmptyQueueException extends RuntimeException {
     public EmptyQueueException(String msg) {
         super(msg);
     }

     public EmptyQueueException(Throwable cause) {
         super(cause);
     }

     public EmptyQueueException(String msg, Throwable cause) {
         super(msg, cause);
     }
 }
