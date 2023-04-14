package ru.practicum.ewm;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;

public class EwmClient extends BaseClient {
    private static final String SERVER_URL = "http://localhost:9090";

    public EwmClient(RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(SERVER_URL))
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
}
