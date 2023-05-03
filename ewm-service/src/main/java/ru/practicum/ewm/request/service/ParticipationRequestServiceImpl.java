package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.request.dao.ParticipationRequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.exception.DbConflictException;
import ru.practicum.ewm.util.exception.IncorrectIdException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.request.mapper.ParticipationRequestMapper.mapToParticipantRequestDto;
import static ru.practicum.ewm.request.mapper.ParticipationRequestMapper.mapToParticipationRequest;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository repo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    @Transactional
    @Override
    public ParticipationRequestDto save(long userId, long eventId) {
        final LocalDateTime created = LocalDateTime.now();
        final User requester = findRequester(userId);
        final Event event = eventRepo.findById(eventId).orElseThrow(() -> new IncorrectIdException(eventId, "event"));
        final List<ParticipationRequest> requests = repo.findAllByEvent(event);
        if (event.getInitiator().getId() == userId ||
                event.getState().equals(State.REJECTED) ||
                event.getState().equals(State.PENDING) ||
                requests.size() >= event.getParticipantLimit()) {
            throw new DbConflictException();
        } else if (event.isRequestModeration()) {
            return mapToParticipantRequestDto(
                    repo.saveAndFlush(mapToParticipationRequest(event, requester, Status.PENDING, created)));
        } else {
            return mapToParticipantRequestDto(
                    repo.saveAndFlush(mapToParticipationRequest(event, requester, Status.CONFIRMED, created)));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findByUserId(long userId) {
        return mapToParticipantRequestDto(repo.findAllByRequester(findRequester(userId)));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) {
        findRequester(userId);
        final ParticipationRequest participationRequest = repo.findById(requestId)
                .orElseThrow(() -> new IncorrectIdException(requestId, "request"));
        participationRequest.setStatus(Status.CANCELED);
        return mapToParticipantRequestDto(participationRequest);
    }

    private User findRequester(long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new IncorrectIdException(userId, "user"));
    }
}
