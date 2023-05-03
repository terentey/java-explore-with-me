package ru.practicum.ewm.compilation.mapper;

import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventDtoResponse;

import java.util.List;

public class CompilationMapper {
    public static CompilationDtoResponse mapToCompilationDtoResponse(Compilation compilation,
                                                                     List<EventDtoResponse> events) {
        return CompilationDtoResponse
                .builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation mapToCompilation(CompilationDtoRequest compilationDtoRequest) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDtoRequest.getPinned());
        compilation.setTitle(compilationDtoRequest.getTitle());
        return compilation;
    }
}
