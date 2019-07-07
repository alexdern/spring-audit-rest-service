package ru.alexdern.spring.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.alexdern.spring.audit.domain.User;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * User Data
     */
    public Long company_id;
    public Long user_id;
    public String user_timezone;
    public String user_language;
    public String username;
    public String login;

    public static UserDto fromUser(User u) {
        return UserDto.builder()
                .company_id(u.getCompanyExternalID())
                .user_id(u.getUserExternalID())
                .user_timezone(u.getUserTimeZone())
                .user_language(u.getUserLanguage())
                .username(u.getUsername())
                .login(u.getLogin())
                .build();
    }

    public void assignTo(User u) {
        u.setCompanyExternalID(company_id);
        u.setUserExternalID(user_id);
        u.setUserTimeZone(user_timezone);
        u.setUserLanguage(user_language);
        u.setUsername(username);
        u.setLogin(login);
    }

}
