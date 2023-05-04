package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dao.CompilationEventRepository;
import ru.practicum.ewm.compilation.dao.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDtoCreationRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.compilation.dto.CompilationDtoUpdateRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.CompilationEvent;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.EventDtoResponse;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dao.ParticipationRequestRepository;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.util.exception.IncorrectIdException;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static ru.practicum.ewm.compilation.mapper.CompilationEventMapper.mapToCompilationEvent;
import static ru.practicum.ewm.compilation.mapper.CompilationMapper.mapToCompilation;
import static ru.practicum.ewm.compilation.mapper.CompilationMapper.mapToCompilationDtoResponse;
import static ru.practicum.ewm.event.mapper.EventMapper.mapToEventDtoResponse;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationEventRepository compilationEventRepo;
    private final CompilationRepository compilationRepo;
    private final EventRepository eventRepo;
    private final ParticipationRequestRepository participationRequestRepo;

    @Transactional
    @Override
    public CompilationDtoResponse save(CompilationDtoCreationRequest compilationDtoRequest) {
        final List<Event> events = eventRepo.findAllById(compilationDtoRequest.getEvents());
        if (events == null) {
            throw new IncorrectIdException();
        }
        final Compilation compilation = compilationRepo.saveAndFlush(mapToCompilation(compilationDtoRequest));
        compilationEventRepo.saveAllAndFlush(mapToCompilationEvent(compilation, events));
        return mapToCompilationDtoResponse(compilation, setParticipationRequest(events));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDtoResponse> findAll(Boolean pinned, int from, int size) {
        final List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepo.findAll(PageRequest.of(from / size, size)).getContent();
        } else {
            compilations = compilationRepo.findAllByPinned(pinned, PageRequest.of(from / size, size));
        }
        final Map<Compilation, List<Event>> byCompilation = compilationEventRepo
                .findAllByCompilationIn(compilations)
                .stream()
                .collect(groupingBy(CompilationEvent::getCompilation, mapping(CompilationEvent::getEvent, toList())));
        return compilations
                .stream()
                .map(c -> mapToCompilationDtoResponse(c, setParticipationRequest(byCompilation.getOrDefault(c, emptyList()))))
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDtoResponse findById(long compId) {
        final Compilation compilation = compilationRepo
                .findById(compId)
                .orElseThrow(() -> new IncorrectIdException(compId, "compilation"));
        return mapToCompilationDtoResponse(compilation, setParticipationRequest(compilationEventRepo
                .findEventByCompilation(compilation)));
    }

    @Transactional
    @Override
    public CompilationDtoResponse update(long compId, CompilationDtoUpdateRequest compilationDtoRequest) {
        final Compilation compilation = compilationRepo
                .findById(compId)
                .orElseThrow(() -> new IncorrectIdException(compId, "compilation"));
        final List<Event> oldEvents = compilationEventRepo.findEventByCompilation(compilation);
        if (oldEvents == null) {
            throw new IncorrectIdException();
        }
        final List<Long> updatedEventIds = compilationDtoRequest.getEvents();
        final List<Event> updatedEvents = eventRepo.findAllById(updatedEventIds);
        final List<Long> oldEventIds = oldEvents.stream().map(Event::getId).collect(toList());

        if (!updatedEventIds.containsAll(oldEventIds) || updatedEventIds.size() != oldEventIds.size()) {
            compilationEventRepo.saveAllAndFlush(mapToCompilationEvent(compilation,
                    updatedEvents.stream().filter(e -> !oldEvents.contains(e)).collect(toList())));
            compilationEventRepo.deleteAllByEventIn(oldEvents
                    .stream()
                    .filter(e -> !oldEvents.contains(e)).collect(toList()));
        }
        if (compilationDtoRequest.getPinned() != null) {
            compilation.setPinned(compilationDtoRequest.getPinned());
        }
        if (compilationDtoRequest.getTitle() != null && !compilationDtoRequest.getTitle().isBlank()) {
            compilation.setTitle(compilationDtoRequest.getTitle());
        }
        return mapToCompilationDtoResponse(compilation, setParticipationRequest(updatedEvents));
    }

    @Transactional
    @Override
    public void delete(long compId) {
        compilationEventRepo.deleteAllByCompilationId(compId);
        compilationRepo.deleteById(compId);
    }

    private List<EventDtoResponse> setParticipationRequest(List<Event> events) {
        final Map<Event, List<ParticipationRequest>> requests = participationRequestRepo
                .findAllByEventIn(events)
                .stream()
                .collect(groupingBy(ParticipationRequest::getEvent, toList()));
        return mapToEventDtoResponse(events, requests);
    }
}
