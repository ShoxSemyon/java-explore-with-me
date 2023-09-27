package com.example.stats.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PUBLIC)
@Builder
@Entity
@Table(name = "STATS")
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String app;// — краткое название;

    String uri;// — краткое название;

    String ip;// — краткое название;

    @Column(name = "CREATED_AT")
    LocalDateTime createdAt;
}
