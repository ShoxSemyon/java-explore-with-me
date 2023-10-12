package com.example.main.compilations;

import com.example.main.events.dto.EventShortDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class CompilationDto {

    long id;

    Boolean pinned;


    String title;


    List<EventShortDto> events;
}
