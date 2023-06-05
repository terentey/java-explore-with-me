package ru.practicum.ewm.exception;

public class UrisOrUriBeginsException extends RuntimeException {
    private final String message;

    public UrisOrUriBeginsException() {
        this.message = "В запросе должно присутствовать или uris, или uriBegins";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
