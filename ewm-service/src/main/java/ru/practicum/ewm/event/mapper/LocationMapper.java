package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.model.Location;

@UtilityClass
public class LocationMapper {
    public static LocationDto mapToLocationDto(Location location) {
        return LocationDto
                .builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location mapToLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }
}
