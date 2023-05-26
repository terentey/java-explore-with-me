package ru.practicum.ewm.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.rating.dao.RatingRepository;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.model.RatingEvent;
import ru.practicum.ewm.rating.model.RatingEventKey;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.exception.IncorrectIdException;

import static ru.practicum.ewm.event.model.State.PUBLISHED;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    @Transactional
    @Override
    public void save(long eventId, long userId, Rating rating) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IncorrectIdException(userId, "user"));
        Event event = eventRepo.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new IncorrectIdException(eventId, "event"));
        repo.saveAndFlush(new RatingEvent(new RatingEventKey(userId, eventId), rating, user, event));
    }

    @Transactional
    @Override
    public void delete(long eventId, long userId) {
        repo.deleteByEventAndUser(
                eventRepo.findByIdAndState(eventId, PUBLISHED)
                        .orElseThrow(() -> new IncorrectIdException(eventId, "event")),
                userRepo.findById(userId)
                        .orElseThrow(() -> new IncorrectIdException(userId, "user")));
    }
}
