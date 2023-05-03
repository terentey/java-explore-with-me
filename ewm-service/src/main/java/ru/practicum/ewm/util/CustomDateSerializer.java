package ru.practicum.ewm.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;
import ru.practicum.ewm.util.exception.CustomDateSerializerException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@JsonComponent
public class CustomDateSerializer {

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String date = jsonParser.getText();
            if (date == null || date.isBlank()) {
                throw new CustomDateSerializerException();
            }
            try {
                return LocalDateTime.parse(date, FORMATTER);
            } catch (DateTimeParseException e) {
                throw new CustomDateSerializerException();
            }
        }
    }
}
