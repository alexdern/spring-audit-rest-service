package ru.alexdern.spring.audit.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.domain.User;
import ru.alexdern.spring.audit.dto.AuditEventDto;
import ru.alexdern.spring.audit.repository.EventRepository;
import ru.alexdern.spring.audit.repository.UserRepository;
import ru.alexdern.spring.audit.service.EventService;
import ru.alexdern.spring.audit.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/audit")
@Api(tags = {"audit"})
public class AuditController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;


    @Transactional
    @ResponseStatus(value= HttpStatus.CREATED)
    @PostMapping(value = "/collector", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a event resource.",
            notes = "Returns the URL of the new resource in the Location header.")
    public Event collectorPost(@RequestBody AuditEventDto auditEventDto, HttpServletRequest request) {

        System.out.println(auditEventDto);

        User user = userService
                .findByEID(auditEventDto.user_id)
                .orElseGet(User::new);

        auditEventDto.assignTo(user);

        // entity -> dynamic-update: true
        userService.update(user);


        Event event = new Event();
        event.setUser(user);
        auditEventDto.assignTo(event);
        eventService.create(event);
        return event;

        // Redirect to location
        /*
        URI location = ServletUriComponentsBuilder
                //.fromCurrentRequest()
                .fromContextPath(request)
                .path("/api/events/{id}")
                .buildAndExpand(event.getId())
                .toUri();
        return created(location).build();
        */

    }

    @GetMapping(value = "/journal", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditEventDto> journal() {
        return eventService.findAll()
                .stream()
                .map(AuditEventDto::make)
                .collect(Collectors.toList());
    }

}
