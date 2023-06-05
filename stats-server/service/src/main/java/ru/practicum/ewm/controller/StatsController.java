package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.RatingStatsDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.exception.EndBeforeStartException;
import ru.practicum.ewm.exception.UrisOrUriBeginsException;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void save(@RequestBody EndpointHitDto endpointHitDto) {
        service.save(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> findStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") boolean unique) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new EndBeforeStartException(start, end);
        }
        return service.findStats(start, end, uris, unique);
    }

    @GetMapping("/rating")
    public List<RatingStatsDto> findRating(@RequestParam(required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false) String uriBegins) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new EndBeforeStartException(start, end);
        } else if ((uris == null && uriBegins == null) || (uris != null && uriBegins != null)) {
            throw new UrisOrUriBeginsException();
        }
        return service.findRating(start, end, uris, uriBegins);
    }

    @ExceptionHandler({EndBeforeStartException.class, UrisOrUriBeginsException.class})
    public ResponseEntity<Map<String, String>> handleEndAfterStartException(RuntimeException e) {
        log.error("Невалидное значение, переданное в контролер {}", e.getMessage());
        return new ResponseEntity<>(Map.of(
                "status", BAD_REQUEST.name(),
                "reason", BAD_REQUEST.getReasonPhrase(),
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now().toString()),
                BAD_REQUEST);
    }
}
