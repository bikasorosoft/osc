package io.osc.bikas.session.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "session")
public class Session {

    @Id
    private String sessionId;
    private String userId;
    private String device;

    private LocalDateTime loginTIme;
    private LocalDateTime logoutTime;
}
