package ru.practicum.ewm.exception;

import java.time.LocalDateTime;

public class EndBeforeStartException extends RuntimeException {
    private final String message;

    public EndBeforeStartException() {
        this.message = "End before start";
    }

    public EndBeforeStartException(LocalDateTime start, LocalDateTime end) {
        this.message = String.format("End time (%s) before start time (%s)", end.toString(), start.toString());
    }

    @Override
    public String getMessage() {
        return message;
    }
}
