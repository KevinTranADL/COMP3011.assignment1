package comp3011.assignment1.exception;

public class ShutdownInProgressException extends RuntimeException {
    public ShutdownInProgressException(String message) {
        super(message);
    }
}