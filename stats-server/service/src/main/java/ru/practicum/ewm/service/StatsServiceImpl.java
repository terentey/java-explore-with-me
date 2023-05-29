package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.RatingStatsDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.dao.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repo;

    @Override
    public void save(EndpointHitDto endpointHitDto) {
        repo.save(endpointHitDto);
    }

    @Override
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return repo.findStats(start, end, uris, unique);
    }

    @Override
    public List<RatingStatsDto> findRating(LocalDateTime start, LocalDateTime end, List<String> uris, String uriBegins) {
        return repo.findRating(start, end, uris, uriBegins);
    }
}
