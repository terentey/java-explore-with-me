package ru.practicum.ewm.util.exception.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionDto {
    String status;
    String reason;
    String message;
    String timestamp;
}
