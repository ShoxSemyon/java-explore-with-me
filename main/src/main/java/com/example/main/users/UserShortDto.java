package com.example.main.users;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class UserShortDto {
    long id;

    String name;
}
