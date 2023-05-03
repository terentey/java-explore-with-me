package ru.practicum.ewm.util.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.util.exception.DbConflictException;
import ru.practicum.ewm.util.exception.IncorrectIdException;
import ru.practicum.ewm.util.exception.dto.ExceptionDto;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;
import static ru.practicum.ewm.util.exception.mapper.ExceptionMapper.mapToExceptionDto;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleArgumentValid(MissingServletRequestParameterException e) {
        log.error("Отсутствует параметр запроса: {}", e.getMessage());
        return new ResponseEntity<>(mapToExceptionDto(BAD_REQUEST, e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleDbConflictException(DataIntegrityViolationException e) {
        log.error("Конфликт данных {}", e.getMessage());
        return new ResponseEntity<>(mapToExceptionDto(CONFLICT, e.getMessage()), CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleDbConflictException(DbConflictException e) {
        log.error("Конфликт данных {}", e.getMessage());
        return new ResponseEntity<>(mapToExceptionDto(CONFLICT, e.getMessage()), CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleIncorrectId(IncorrectIdException e) {
        log.error("Неверный id {}", e.getMessage());
        return new ResponseEntity<>(mapToExceptionDto(NOT_FOUND, e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleArgumentValid(MethodArgumentNotValidException e) {
        log.error("Невалидное значение, переданное в контролер {}", e.getMessage());
        return new ResponseEntity<>(mapToExceptionDto(BAD_REQUEST, e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleArgumentValid(InvalidFormatException e) {
        log.error("Невалидное значение, переданное в контролер {}", e.getMessage());
        return new ResponseEntity<>(mapToExceptionDto(BAD_REQUEST, e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleArgumentValid(ConstraintViolationException e) {
        log.error("Невалидное значение, переданное в контролер {}", e.getMessage());
        return new ResponseEntity<>(mapToExceptionDto(BAD_REQUEST, e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> unhandledErrors(Throwable e) {
        log.error("Необработанное исключение {} {}", e.getMessage(), e.getLocalizedMessage());
        return new ResponseEntity<>(mapToExceptionDto(INTERNAL_SERVER_ERROR, e.getMessage()), INTERNAL_SERVER_ERROR);
    }
}
