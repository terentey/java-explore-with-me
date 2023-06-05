package ru.practicum.ewm.rating.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.RatingStatsDto;
import ru.practicum.ewm.rating.dto.RatingDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RatingMapper {
    public List<RatingDto> mapToRatingDto(List<RatingStatsDto> ratingStats) {
        return ratingStats.stream().map(RatingMapper::mapToRatingDto).collect(Collectors.toList());
    }

    public RatingDto mapToRatingDto(RatingStatsDto ratingStat) {
        String[] uri = ratingStat.getUri().split("/");
        return mapToRatingDto(uri[5], Long.parseLong(uri[4]), ratingStat.getTimestamp());
    }

    public RatingDto mapToRatingDto(String rating, long eventId, LocalDateTime timestamp) {
        return RatingDto
                .builder()
                .rating(rating)
                .eventId(eventId)
                .timestamp(timestamp)
                .build();
    }
}
