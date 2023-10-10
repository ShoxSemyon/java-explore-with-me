package com.example.main.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class NewCompilationDto {

    Boolean pinned;

    @NotBlank(groups = NotNullCom.class)
    @Size(min = 1, max = 50)
    String title;

    List<Long> events;
}
