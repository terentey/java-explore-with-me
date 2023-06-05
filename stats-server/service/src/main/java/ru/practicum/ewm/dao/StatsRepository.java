package ru.practicum.ewm.dao;

import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.RatingStatsDto;
import ru.practicum.ewm.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository {
    void save(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    List<RatingStatsDto> findRating(LocalDateTime start, LocalDateTime end, List<String> uris, String uriBegins);
}
