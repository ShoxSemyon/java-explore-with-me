package com.example.main.request;

import com.example.main.events.model.Event;
import com.example.main.users.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "requests")
@DynamicUpdate
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    User requester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    Event event;

    @Column(name = "created")
    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    RequestStatus status;
}
