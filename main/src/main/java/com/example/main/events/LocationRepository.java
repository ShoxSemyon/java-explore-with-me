package com.example.main.events;

import com.example.main.events.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, QuerydslPredicateExecutor<Location> {
}
