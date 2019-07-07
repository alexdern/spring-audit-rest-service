package ru.alexdern.spring.audit.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users",
        indexes = {
                @Index(columnList = "user_eid", name = "users_user_eid_idx"),
                @Index(columnList = "login", name = "users_login_idx")
        }
)
@DynamicUpdate
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "company_eid")
    private Long companyExternalID;

    @Column(name = "user_eid")
    private Long userExternalID;

    @Column(name = "user_tz")
    private String userTimeZone;

    @Column(name = "user_lang")
    private String userLanguage;

    @Column(name = "username")
    private String username;

    @Column(name = "login")
    private String login;

}
