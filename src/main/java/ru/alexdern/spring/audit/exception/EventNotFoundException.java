package ru.alexdern.spring.audit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException {

    private Long id;

    public EventNotFoundException() {
    }

    public EventNotFoundException(Long id) {
        super(String.format("Event for ID='%s' not found.", id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
