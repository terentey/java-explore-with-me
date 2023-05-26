package ru.practicum.ewm.rating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.service.RatingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/users/{userId}")
public class PrivateRatingController {
    private final RatingService service;

    @PutMapping("/like")
    public void saveLike(@PathVariable long eventId, @PathVariable long userId) {
        service.save(eventId, userId, Rating.LIKE);
    }

    @PutMapping("/dislike")
    public void saveDislike(@PathVariable long eventId, @PathVariable long userId) {
        service.save(eventId, userId, Rating.DISLIKE);
    }

    @DeleteMapping("/rating")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long eventId, @PathVariable long userId) {
        service.delete(eventId, userId);
    }
}
