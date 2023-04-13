package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.dao.StatsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repo;

    @Override
    public void save(EndpointHitDto endpointHitDto) {
        repo.save(endpointHitDto);
    }

    @Override
    public List<ViewStatsDto> findStats(String start, String end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            return repo.findStats(start,
                    end,
                    unique);
        } else {
            return repo.findStats(start,
                    end,
                    uris.stream().map(u -> String.format("'%s'", u)).collect(Collectors.toList()),
                    unique);
        }
    }
}
