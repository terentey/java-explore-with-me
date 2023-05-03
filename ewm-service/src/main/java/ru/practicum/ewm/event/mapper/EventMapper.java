package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventDtoRequest;
import ru.practicum.ewm.event.dto.EventDtoResponse;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.mapper.LocationMapper.mapToLocationDto;

public class EventMapper {
    public static List<EventDtoResponse> mapToEventDtoResponse(List<Event> events,
                                                               Map<Event, List<ParticipationRequest>> requests) {
        return events
                .stream()
                .map(e -> mapToEventDtoResponse(e, requests.getOrDefault(e, Collections.emptyList()).size()))
                .collect(Collectors.toList());
    }

    public static EventDtoResponse mapToEventDtoResponse(Event event, long confirmedRequests) {
        EventDtoResponse eventDtoResponse = mapToEventDtoResponse(event);
        eventDtoResponse.setConfirmedRequests(confirmedRequests);
        return eventDtoResponse;
    }

    public static EventDtoResponse mapToEventDtoResponse(Event event) {
        EventDtoResponse eventDtoResponse = EventDtoResponse
                .builder()
                .annotation(event.getAnnotation())
                .category(new EventDtoResponse.Category(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(0)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(new EventDtoResponse.Initiator(event.getInitiator().getId(), event.getInitiator().getName()))
                .location(mapToLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
        if (event.getPublishedOn() != null) {
            eventDtoResponse.setPublishedOn(event.getPublishedOn());
        }
        return eventDtoResponse;
    }

    public static Event mapToEvent(EventDtoRequest eventDtoRequest,
                                   Category category,
                                   User initiator,
                                   Location location) {
        Event event = new Event();
        event.setAnnotation(eventDtoRequest.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(eventDtoRequest.getDescription());
        event.setEventDate(eventDtoRequest.getEventDate());
        event.setInitiator(initiator);
        event.setLocation(location);
        event.setPaid(eventDtoRequest.getPaid());
        event.setParticipantLimit(eventDtoRequest.getParticipantLimit());
        event.setRequestModeration(eventDtoRequest.getRequestModeration());
        event.setState(State.PENDING);
        event.setTitle(eventDtoRequest.getTitle());
        return event;
    }
}
