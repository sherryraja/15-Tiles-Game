package local.exceptions;

public class NoSuchGameException extends RuntimeException {
    public NoSuchGameException(String message) {
        super(message);
    }
}