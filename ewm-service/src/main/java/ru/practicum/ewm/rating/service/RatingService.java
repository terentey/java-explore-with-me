package ru.practicum.ewm.rating.service;

import ru.practicum.ewm.rating.model.Rating;

public interface RatingService {
    void save(long eventId, long userId, Rating rating);

    void delete(long eventId, long userId);
}
