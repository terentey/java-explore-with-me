package ru.practicum.ewm.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.RatingStatsDto;
import ru.practicum.ewm.ViewStatsDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatsRepoImpl implements StatsRepository {
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
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        String sql;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                sql = "SELECT eh.app, eh.uri, COUNT(DISTINCT eh.ip) AS hit " +
                        "FROM endpoint_hit AS eh " +
                        "WHERE eh.timestamp BETWEEN ? AND ? " +
                        "GROUP BY eh.app, eh.uri " +
                        "ORDER BY hit DESC";
            } else {
                sql = "SELECT eh.app, eh.uri, COUNT(eh.ip) AS hit " +
                        "FROM endpoint_hit AS eh " +
                        "WHERE eh.timestamp BETWEEN ? AND ? " +
                        "GROUP BY eh.app, eh.uri " +
                        "ORDER BY hit DESC";
            }
        } else {
            String uri = uris.stream().map(u -> String.format("'%s'", u)).collect(Collectors.joining(", "));
            if (unique) {
                sql = String.format("SELECT eh.app, eh.uri, COUNT(DISTINCT eh.ip) AS hit " +
                        "FROM endpoint_hit AS eh " +
                        "WHERE eh.timestamp BETWEEN ? AND ? AND eh.uri IN(%s) " +
                        "GROUP BY eh.app, eh.uri " +
                        "ORDER BY hit DESC", uri);
            } else {
                sql = String.format("SELECT eh.app, eh.uri, COUNT(eh.ip) AS hit " +
                        "FROM endpoint_hit AS eh " +
                        "WHERE eh.timestamp BETWEEN ? AND ? AND eh.uri IN(%s) " +
                        "GROUP BY eh.app, eh.uri " +
                        "ORDER BY hit DESC", uri);
            }
        }
        return jdbc.query(sql, ((rs, rowNum) -> viewStatsDtoMapper(rs)), start, end);
    }

    @Override
    public List<RatingStatsDto> findRating(LocalDateTime start, LocalDateTime end, List<String> uris, String uriBegins) {
        String sql = "SELECT eh.app, eh.uri, eh.timestamp " +
                "FROM endpoint_hit AS eh " +
                "WHERE eh.uri %s %s " +
                "ORDER BY eh.timestamp";
        String uri;
        if (uris == null) {
            uri = String.format("LIKE '%s/%%/like' OR eh.uri LIKE '%s/%%/dislike' OR eh.uri LIKE '%s/%%/rating'",
                    uriBegins,
                    uriBegins,
                    uriBegins);
        } else {
            uri = String.format("IN(%s)",
                    uris.stream().map(u -> String.format("'%s'", u)).collect(Collectors.joining(", ")));
        }
        if (start == null && end != null) {
            sql = String.format(sql, uri, "AND eh.timestamp < ?");
            return jdbc.query(sql,((rs, rowNum) -> ratingStatsDtoMapper(rs)), end);
        } else if (start != null && end == null) {
            sql = String.format(sql, uri, "AND eh.timestamp > ?");
            return jdbc.query(sql,((rs, rowNum) -> ratingStatsDtoMapper(rs)), start);
        } else if (start == null) {
            sql = String.format(sql, uri, "");
            return jdbc.query(sql,((rs, rowNum) -> ratingStatsDtoMapper(rs)));
        } else {
            sql = String.format(sql, uri, "AND eh.timestamp BETWEEN ? AND ?");
            return jdbc.query(sql,((rs, rowNum) -> ratingStatsDtoMapper(rs)), start, end);
        }
    }

    private RatingStatsDto ratingStatsDtoMapper(ResultSet rs) throws SQLException {
        RatingStatsDto ratingStatsDto = new RatingStatsDto();
        ratingStatsDto.setApp(rs.getString(1));
        ratingStatsDto.setUri(rs.getString(2));
        ratingStatsDto.setTimestamp(rs.getTimestamp(3).toLocalDateTime());
        return ratingStatsDto;
    }

    private ViewStatsDto viewStatsDtoMapper(ResultSet rs) throws SQLException {
        ViewStatsDto viewStatsDto = new ViewStatsDto();
        viewStatsDto.setApp(rs.getString(1));
        viewStatsDto.setUri(rs.getString(2));
        viewStatsDto.setHits(rs.getInt(3));
        return viewStatsDto;
    }
}
