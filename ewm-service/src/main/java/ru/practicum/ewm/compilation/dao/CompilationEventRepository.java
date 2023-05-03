package ru.practicum.ewm.compilation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.CompilationEvent;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Long> {
    List<CompilationEvent> findAllByCompilationIn(List<Compilation> compilations);

    @Query("select ce.event " +
            "from CompilationEvent ce " +
            "where ce.compilation = ?1")
    List<Event> findEventByCompilation(Compilation compilation);

    void deleteAllByEventIn(List<Event> events);

    @Modifying
    @Query("delete from CompilationEvent as ce where ce.compilation.id = ?1")
    void deleteAllByCompilationId(long compId);
}
