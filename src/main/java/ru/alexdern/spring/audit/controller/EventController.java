package ru.alexdern.spring.audit.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.service.EventService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/events")
@Api(tags = {"events"})
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAll() {
        return eventService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable("id") Long id) {
        return ok(eventService.findById(id));
    }

}
