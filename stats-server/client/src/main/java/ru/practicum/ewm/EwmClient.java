package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EwmClient extends BaseClient {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EwmClient(@Value("${stats-server.url}") String serverUrl,
                     RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void hit(String app, String uri, String ip) {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp(app);
        dto.setUri(uri);
        dto.setIp(ip);
        dto.setTimestamp(LocalDateTime.now());

        HttpEntity<EndpointHitDto> request = new HttpEntity<>(dto, defaultHeaders());

        prepareGatewayResponse(rest.exchange("/hit", HttpMethod.POST, request, String.class));
    }

    public ResponseEntity<String> rating(LocalDateTime start, LocalDateTime end, List<String> uris, String uriBegins) {
        HttpEntity<List<ViewStatsDto>> request = new HttpEntity<>(null, defaultHeaders());
        Map<String, Object> parameters = new HashMap<>();
        String url = "/rating?%s";
        if (start != null) {
            parameters.put("start", start.format(FORMATTER));
            url = String.format(url, "start={start}&%s");
        }
        if (end != null) {
            parameters.put("end", end.format(FORMATTER));
            url = String.format(url, "end={end}&%s");
        }
        if (uris != null) {
            parameters.put("uris", String.join(",", uris));
            url = String.format(url, "uris={uris}");
        }
        if (uriBegins != null) {
            parameters.put("uriBegins", uriBegins);
            url = String.format(url, "uriBegins={uriBegins}");
        }

        return prepareGatewayResponse(rest.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class,
                parameters));
    }

    public ResponseEntity<String> stats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        HttpEntity<List<ViewStatsDto>> request = new HttpEntity<>(null, defaultHeaders());
        Map<String, Object> parameters = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", String.join(",", uris),
                "unique", String.valueOf(unique));

        return prepareGatewayResponse(rest.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET, request, String.class, parameters));
    }
}
