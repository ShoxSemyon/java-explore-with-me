package com.example.main.users;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    @ReadOnlyProperty
    long id;

    @NotBlank
    @Size(max = 250, min = 2)
    String name;

    @Email
    @NotNull
    @Size(max = 254, min = 6)
    String email;
}
