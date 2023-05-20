package ru.practicum.ewm.util.exception;

public class DbConflictException extends RuntimeException {
    private final String message;

    public DbConflictException() {
        this.message = "db conflict exception";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
