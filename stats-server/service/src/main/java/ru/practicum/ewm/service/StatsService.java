package ru.practicum.ewm.service;

import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;

import java.util.List;

public interface StatsService {
    void save(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findStats(String start, String end, List<String> uris, boolean unique);
}
