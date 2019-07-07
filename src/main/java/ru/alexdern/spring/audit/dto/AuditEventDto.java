package ru.alexdern.spring.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.domain.User;

import java.util.function.Supplier;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventDto {

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

    /**
     * User Data
     */
    public Long company_id;
    public Long user_id;
    public String user_timezone;
    public String user_language;
    public String username;
    public String login;


    public static AuditEventDto make(Event e) {
        return AuditEventDto.builder()
                // Event
                .timestamp(e.getEventDateTime().toString())
                .event_type(e.getEventType())
                .server_host(e.getServerHost())
                .session_key(e.getSessionKey())
                .client_ip(e.getClientIp())
                .client_agent(e.getClientAgent())
                .message(e.getMessage())
                // User
                .company_id(e.getUser().getCompanyExternalID())
                .user_id(e.getUser().getUserExternalID())
                .user_timezone(e.getUser().getUserTimeZone())
                .user_language(e.getUser().getUserLanguage())
                .username(e.getUser().getUsername())
                .login(e.getUser().getLogin())
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


    public void assignTo(User u) {
        u.setCompanyExternalID(company_id);
        u.setUserExternalID(user_id);
        u.setUserTimeZone(user_timezone);
        u.setUserLanguage(user_language);
        u.setUsername(username);
        u.setLogin(login);
    }


    @Override
    public String toString() {
        return "AuditEventDto{" +
                "timestamp='" + timestamp + '\'' +
                ", event_type='" + event_type + '\'' +
                ", server_host='" + server_host + '\'' +
                ", session_key='" + session_key + '\'' +
                ", client_ip='" + client_ip + '\'' +
                ", client_agent='" + client_agent + '\'' +
                ", company_id=" + company_id +
                ", user_id=" + user_id +
                ", user_timezone='" + user_timezone + '\'' +
                ", user_language='" + user_language + '\'' +
                ", username='" + username + '\'' +
                ", login='" + login + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
