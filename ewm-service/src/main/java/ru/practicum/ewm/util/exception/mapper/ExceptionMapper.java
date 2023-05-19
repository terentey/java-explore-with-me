package ru.practicum.ewm.util.exception.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import ru.practicum.ewm.util.exception.dto.ExceptionDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class ExceptionMapper {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ExceptionDto mapToExceptionDto(HttpStatus status, String message) {
        return ExceptionDto
                .builder()
                .status(status.name())
                .reason(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}
