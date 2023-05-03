package ru.practicum.ewm.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDtoRequest {
    @NotBlank
    String annotation;
    @NotNull
    @Positive
    Long category;
    String description;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    @NotNull
    Boolean paid;
    @NotNull
    @Positive
    Integer participantLimit;
    @NotNull
    Boolean requestModeration;
    StateAction stateAction;
    @NotBlank
    String title;
}
