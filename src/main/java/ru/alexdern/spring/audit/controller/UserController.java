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
import ru.alexdern.spring.audit.domain.User;
import ru.alexdern.spring.audit.dto.EventDto;
import ru.alexdern.spring.audit.dto.UserDto;
import ru.alexdern.spring.audit.exception.UserNotFoundException;
import ru.alexdern.spring.audit.service.EventService;
import ru.alexdern.spring.audit.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/users")
@Api(tags = {"users"})
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAll() {
        return userService.findAll()
                .stream()
                    .map(UserDto::fromUser)
                    .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getById(@PathVariable("id") Long id) {
        return UserDto.fromUser(userService.findByEID(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @GetMapping(value = "/{id}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAllEvents(@PathVariable("id") Long user_eid) {
        return eventService.findAllByUserEID(user_eid);
    }

}
