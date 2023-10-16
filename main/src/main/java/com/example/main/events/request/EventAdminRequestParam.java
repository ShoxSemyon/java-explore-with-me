package com.example.main.events.request;

import com.example.main.events.model.EventStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class EventAdminRequestParam {
    List<Long> users;
    List<EventStatus> states;
    List<Long> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;

    Integer from;

    Integer size;

    Double lat;

    Double lon;

    Double distance;

    public boolean usersStatesCategoriesStartEndIsNull() {
        return CollectionUtils.isEmpty(users) &&
                CollectionUtils.isEmpty(states) &&
                CollectionUtils.isEmpty(categories) &&
                rangeStart == null &&
                rangeEnd == null &&
                lat == null &&
                lon == null &&
                distance == null;
    }
}
