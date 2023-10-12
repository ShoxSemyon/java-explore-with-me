package com.example.main.events;

import com.example.main.events.model.Event;
import com.example.main.events.model.EventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Optional<Event> findByIdAndInitiator_Id(Long eventId, Long userId);

    List<Event> findAllByInitiator_id(Long userId, Pageable pageable);

    Optional<Event> findByIdAndState(Long eventId, EventStatus status);

    List<Event> findAllByIdIn(List<Long> ids);
}
