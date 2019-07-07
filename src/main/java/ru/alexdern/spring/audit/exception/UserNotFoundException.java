package ru.alexdern.spring.audit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private Long id;

    public UserNotFoundException() {
    }

    public UserNotFoundException(Long id) {
        super(String.format("User for ID='%s' not found.", id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
