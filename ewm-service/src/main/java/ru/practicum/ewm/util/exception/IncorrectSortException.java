package ru.practicum.ewm.util.exception;

public class IncorrectSortException extends RuntimeException {
    private final String message;

    public IncorrectSortException() {
        message = "Unknown sort: UNSUPPORTED_SORT";
    }

    public IncorrectSortException(String sort) {
        message = String.format("Unknown sort: %s", sort);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
