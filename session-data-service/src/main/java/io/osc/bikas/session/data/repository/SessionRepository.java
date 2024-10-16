package io.osc.bikas.session.data.repository;

import io.osc.bikas.session.data.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findByUserIdAndSessionId(String userId, String sessionId);
}
