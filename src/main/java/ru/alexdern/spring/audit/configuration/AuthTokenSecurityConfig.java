package ru.alexdern.spring.audit.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import ru.alexdern.spring.audit.configuration.auth.PreAuthTokenHeaderFilter;

@Configuration
@EnableWebSecurity
public class AuthTokenSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHORIZATION_HEADER = "X-API-KEY";


    @Value("${api.http.auth.token}")
    private String authToken;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter(AUTHORIZATION_HEADER);

        filter.setAuthenticationManager((Authentication authentication) -> {
            String principal = (String) authentication.getPrincipal();
            if (!authToken.equals(principal)) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity
                .antMatcher("/api/**")
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(filter)
                    .addFilterBefore(new ExceptionTranslationFilter(
                            new Http403ForbiddenEntryPoint()),
                            filter.getClass())
                .authorizeRequests()
                    //.antMatchers("/").permitAll()
                    .anyRequest()
                    .authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/webjars/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }


}
