package com.example.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Builder
public class EndpointHitDto {

    @ReadOnlyProperty
    long id;

    @NotNull
    String app;// — краткое название;

    @NotNull
    String uri;// — краткое название;

    @NotNull
    String ip;// — краткое название;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime timestamp;
}
