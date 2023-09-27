package com.example.stats.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStatsDto {

    String app;// — краткое название;

    String uri;// — краткое название;

    long hits;
}
