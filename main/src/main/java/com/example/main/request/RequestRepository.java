package com.example.main.request;

import com.example.main.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByIdAndAndRequester_Id(Long requestId,
                                                  Long userId);

    List<Request> findAllByRequester_id(Long userId);

    List<Request> findAllByEvent_Initiator_IdAndEvent_Id(Long userId, Long eventId);

    List<Request> findAllByEvent_Initiator_IdAndEvent_IdAndIdIn(Long userId,
                                                          Long eventId,
                                                          List<Long> ids);

}
