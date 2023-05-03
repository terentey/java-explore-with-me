package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventDtoRequest;
import ru.practicum.ewm.event.dto.EventDtoResponse;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EventService {
    EventDtoResponse save(EventDtoRequest eventDtoRequest, long userId);

    List<EventDtoResponse> findAll(String text,
                                   List<Long> categories,
                                   Boolean paid,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   boolean onlyAvailable,
                                   String sort,
                                   int from,
                                   int size,
                                   String ip);

    List<EventDtoResponse> findAll(List<Long> users,
                                   List<String> states,
                                   List<Long> categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   int from,
                                   int size);

    List<EventDtoResponse> findAll(long userId, int from, int size);

    EventDtoResponse findById(long eventId, String ip);

    EventDtoResponse findById(long userId, long eventId);

    List<ParticipationRequestDto> findRequestByUserIdAndById(long userId, long eventId);

    EventDtoResponse update(EventDtoRequest eventDtoRequest, long userId, long eventId);

    EventDtoResponse update(EventDtoRequest eventDtoRequest, long eventId);

    Map<String, List<ParticipationRequestDto>> updateRequests(EventRequestStatusUpdateRequest dto, long userId, long eventId);
}
