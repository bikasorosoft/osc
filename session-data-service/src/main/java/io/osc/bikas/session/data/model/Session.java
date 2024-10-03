package io.osc.bikas.session.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    private String sessionId;

    private String userId;
    private String deviceType;

    private LocalDateTime loginTIme;
    private LocalDateTime logoutTime;

}
