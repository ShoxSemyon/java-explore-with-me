package com.example.main.category;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class CategoryDto {
    @ReadOnlyProperty
    long id;

    @NotBlank
    @Size(max = 50, min = 1)
    String name;
}
