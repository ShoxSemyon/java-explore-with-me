package com.example.main.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class EventRequestStatusUpdateRequestDto {
    @NotNull
    List<Long> requestIds;

    @NotNull
    UpadateRequestStatus status;
}
