package io.osc.bikas.session.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "session")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Id
    private String sessionId;
    private String userId;
    private String deviceType;

    private LocalDateTime loginTIme;
    private LocalDateTime logoutTime;
}
