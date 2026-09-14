package comp3011.assignment1.dto;

public class ShutdownResponse {
    private final String message;
    public ShutdownResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}