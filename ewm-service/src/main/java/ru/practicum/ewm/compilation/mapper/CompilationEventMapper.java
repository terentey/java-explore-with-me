package ru.practicum.ewm.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.CompilationEvent;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationEventMapper {
    public static List<CompilationEvent> mapToCompilationEvent(Compilation compilation, List<Event> events) {
        return events.stream().map(e -> mapToCompilationEvent(compilation, e)).collect(Collectors.toList());
    }

    public static CompilationEvent mapToCompilationEvent(Compilation compilation, Event event) {
        CompilationEvent compilationEvent = new CompilationEvent();
        compilationEvent.setCompilation(compilation);
        compilationEvent.setEvent(event);
        return compilationEvent;
    }
}
