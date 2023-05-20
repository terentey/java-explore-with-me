package ru.practicum.ewm.util.exception;

public class CustomDateSerializerException extends RuntimeException {
    private final String message;

    public CustomDateSerializerException() {
        this.message = "custom date serializer exception";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
