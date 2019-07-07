package ru.alexdern.spring.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.alexdern.spring.audit.domain.Event;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    /**
     * Event Data
     */
    public String timestamp;
    public String event_type;
    public String server_host;
    public String session_key;
    public String client_ip;
    public String client_agent;
    public String message;

    public static EventDto fromEvent(Event e) {
        return EventDto.builder()
                .timestamp(e.getEventDateTime().toString())
                .event_type(e.getEventType())
                .server_host(e.getServerHost())
                .session_key(e.getSessionKey())
                .client_ip(e.getClientIp())
                .client_agent(e.getClientAgent())
                .message(e.getMessage())
                .build();
    }

    public void assignTo(Event e) {
        e.setEventTimestamp(timestamp);
        e.setEventType(event_type);
        e.setServerHost(server_host);
        e.setClientIp(client_ip);
        e.setClientAgent(client_agent);
        e.setSessionKey(session_key);
    }

}
