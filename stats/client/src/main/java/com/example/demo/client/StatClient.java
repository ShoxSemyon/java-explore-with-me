package com.example.demo.client;

import com.example.stats.dto.EndpointHitDto;
import com.example.stats.dto.ViewStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Service
public class StatClient extends BaseClient {

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ""))
                        .requestFactory(SimpleClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<EndpointHitDto> create(EndpointHitDto endpointHitDto) {
        return makeAndSendRequest(HttpMethod.POST,
                "/hit",
                null, endpointHitDto,
                new ParameterizedTypeReference<EndpointHitDto>() {
                });
    }

    public ResponseEntity<List<ViewStatsDto>> getAll(Boolean unique,
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         List<String> uris) {

        Map<String, Object> parameters = Map.of(
                "start", format.format(start),
                "end", format.format(end),
                "uris",String.join(",",uris),
                "unique", unique
        );
        return makeAndSendRequest(HttpMethod.GET,
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                parameters, null,
                new ParameterizedTypeReference<List<ViewStatsDto>>() {
                });
    }

}
