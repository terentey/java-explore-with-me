package ru.practicum.ewm.util.exception.mapper;

import org.springframework.http.HttpStatus;
import ru.practicum.ewm.util.exception.dto.ExceptionDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ExceptionDto mapToExceptionDto(HttpStatus status, String message) {
        return ExceptionDto
                .builder()
                .status(status.name())
                .reason(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}
