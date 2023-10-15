package com.example.main.events.model;

import com.example.main.events.dto.LocationType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "locations")
@DynamicUpdate
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Double lat;

    Double lon;

    Double radius;

    @Enumerated(EnumType.STRING)
    LocationType type;
}
