package com.example.main.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class EventRequestStatusUpdateResultDto {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}
