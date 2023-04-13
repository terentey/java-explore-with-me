package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EndpointHit {
    int id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
