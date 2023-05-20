package ru.practicum.ewm.util.exception;

public class IncorrectIdException extends RuntimeException {
    private final String message;

    public IncorrectIdException() {
        message = "Unknown id: INCORRECT_ID";
    }

    public IncorrectIdException(long id) {
        message = String.format("Unknown id: %d", id);
    }

    public IncorrectIdException(long id, String name) {
        message = String.format("Unknown %s_id: %d", name, id);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
