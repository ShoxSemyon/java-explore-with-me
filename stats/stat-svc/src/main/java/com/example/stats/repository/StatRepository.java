package com.example.stats.repository;

import com.example.stats.model.EndpointHit;
import com.example.stats.model.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT s.app as app, s.uri as uri, count(*) as hints " +
            "FROM stats s " +
            "WHERE s.created_at BETWEEN :start AND :end AND (s.uri in (:uris) or :uris is null)" +
            "GROUP BY s.app, s.uri ORDER BY hints DESC", nativeQuery = true)
    List<ViewStats> findAll(@Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end,
                            @Param("uris") List<String> uris);


    @Query(value = "SELECT s.app as app, s.uri as uri, count(DISTINCT ip) as hints " +
            "FROM stats s " +
            "WHERE s.created_at BETWEEN :start AND :end AND (s.uri in (:uris) or :uris is null)" +
            "GROUP BY s.app, s.uri ORDER BY hints DESC", nativeQuery = true)
    List<ViewStats> findAllUnique(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end,
                                  @Param("uris") List<String> uris);

}

