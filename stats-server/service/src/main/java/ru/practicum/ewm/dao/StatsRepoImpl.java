package ru.practicum.ewm.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsRepoImpl implements StatsRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JdbcTemplate jdbc;

    @Override
    public void save(EndpointHitDto endpointHitDto) {
        String sql = "INSERT INTO endpoint_hit(app, uri, ip, timestamp) VALUES (?, ?, ?, ?)";
        jdbc.update(sql,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());
    }

    @Override
    public List<ViewStatsDto> findStats(String start, String end, boolean unique) {
        String sql;
        if (unique) {
            sql = "SELECT res.app, res.uri, COUNT(res.ip) AS hit " +
                    "FROM (SELECT DISTINCT eh.app, eh.uri, eh.ip " +
                    "FROM endpoint_hit AS eh " +
                    "WHERE eh.timestamp BETWEEN ? AND ?) AS res " +
                    "GROUP BY res.app, res.uri " +
                    "ORDER BY hit DESC";
        } else {
            sql = "SELECT eh.app, eh.uri, COUNT(eh.id) AS hit " +
                    "FROM endpoint_hit AS eh " +
                    "WHERE eh.timestamp BETWEEN ? AND ? " +
                    "GROUP BY eh.app, eh.uri " +
                    "ORDER BY hit DESC";
        }
        return jdbc.query(sql,
                ((rs, rowNum) -> viewStatsDtoMapper(rs)),
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER));
    }

    @Override
    public List<ViewStatsDto> findStats(String start, String end, List<String> uris, boolean unique) {
        String sql;
        String uri = String.join(", ", uris);
        if (unique) {
            sql = String.format("SELECT res.app, res.uri, COUNT(res.ip) AS hit " +
                    "FROM (SELECT DISTINCT eh.app, eh.uri, eh.ip " +
                    "FROM endpoint_hit AS eh " +
                    "WHERE eh.timestamp BETWEEN ? AND ? AND eh.uri IN(%s)) AS res " +
                    "GROUP BY res.app, res.uri " +
                    "ORDER BY hit DESC", uri);
        } else {
            sql = String.format("SELECT eh.app, eh.uri, COUNT(eh.id) AS hit " +
                    "FROM endpoint_hit AS eh " +
                    "WHERE eh.timestamp BETWEEN ? AND ? AND eh.uri IN(%s) " +
                    "GROUP BY eh.app, eh.uri " +
                    "ORDER BY hit DESC", uri);
        }
        return jdbc.query(sql,
                ((rs, rowNum) -> viewStatsDtoMapper(rs)),
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER));
    }

    private ViewStatsDto viewStatsDtoMapper(ResultSet rs) throws SQLException {
        ViewStatsDto viewStatsDto = new ViewStatsDto();
        viewStatsDto.setApp(rs.getString(1));
        viewStatsDto.setUri(rs.getString(2));
        viewStatsDto.setHits(rs.getInt(3));
        return viewStatsDto;
    }
}
