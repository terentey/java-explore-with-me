package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class EwmClient extends BaseClient {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EwmClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> hit(int id, String app, String uri, String ip) {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setId(id);
        dto.setApp(app);
        dto.setUri(uri);
        dto.setIp(ip);
        dto.setTimestamp(LocalDateTime.now());

        HttpEntity<EndpointHitDto> request = new HttpEntity<>(dto, defaultHeaders());

        return prepareGatewayResponse(rest.exchange("/hit", HttpMethod.POST, request, Object.class));
    }

    public ResponseEntity<Object> stats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(null, defaultHeaders());
        Map<String, String> parameters = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", String.join("&uris=", uris),
                "unique", String.valueOf(unique));

        return prepareGatewayResponse(rest.exchange("/stats", HttpMethod.GET, request, Object.class, parameters));
    }
}
