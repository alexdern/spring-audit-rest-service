package ru.alexdern.spring.audit.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "events",
        indexes = {
                @Index(columnList = "event_dt", name = "events_event_dt_idx"),
                @Index(columnList = "event_type", name = "events_event_type_idx")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_ref")
    private User user;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_dt")
    private LocalDateTime eventDateTime;

    @Column(name = "event_tz")
    private String eventTimeZone;

    @Column(name = "server_host")
    private String serverHost;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "client_agent")
    private String clientAgent;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "message")
    private String message;


    public void setEventZonedDateTime(ZonedDateTime dt) {
        eventDateTime = dt.toLocalDateTime();
        eventTimeZone = dt.getZone().getId();
    }

    public void setEventTimestamp(String dt) {
        setEventZonedDateTime(ZonedDateTime.parse(dt));
    }

}
