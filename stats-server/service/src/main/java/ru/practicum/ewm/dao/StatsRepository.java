package ru.practicum.ewm.dao;

import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;

import java.util.List;

public interface StatsRepository {
    void save(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findStats(String start, String end, boolean unique);

    List<ViewStatsDto> findStats(String start, String end, List<String> uris, boolean unique);
}
