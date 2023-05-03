package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;

import java.util.List;

public interface CompilationService {
    CompilationDtoResponse save(CompilationDtoRequest compilationDtoRequest);

    List<CompilationDtoResponse> findAll(Boolean pinned, int from, int size);

    CompilationDtoResponse findById(long compId);

    CompilationDtoResponse update(long compId, CompilationDtoRequest compilationDtoRequest);

    void delete(long compId);
}
