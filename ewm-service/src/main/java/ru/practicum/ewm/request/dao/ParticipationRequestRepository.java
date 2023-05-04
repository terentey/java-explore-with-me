package ru.practicum.ewm.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventIn(List<Event> event);

    List<ParticipationRequest> findAllByEvent(Event event);

    long countByEvent(Event event);

    @Query("select pe " +
            "from ParticipationRequest as pe " +
            "where pe.event = :event AND pe.status = :status")
    List<ParticipationRequest> findAllByEventAndStatus(Event event, Status status);

    List<ParticipationRequest> findAllByRequester(User requester);
}
