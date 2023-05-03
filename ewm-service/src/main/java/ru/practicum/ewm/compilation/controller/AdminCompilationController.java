package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.compilation.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDtoResponse save(@RequestBody @Validated CompilationDtoRequest compilationDtoRequest) {
        return service.save(compilationDtoRequest);
    }

    @PatchMapping("/{compId}")
    public CompilationDtoResponse update(@PathVariable long compId,
                                         @RequestBody CompilationDtoRequest compilationDtoRequest) {
        return service.update(compId, compilationDtoRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        service.delete(compId);
    }
}
