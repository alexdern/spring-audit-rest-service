package ru.alexdern.spring.audit.controller;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.domain.User;
import ru.alexdern.spring.audit.dto.AuditEventDto;
import ru.alexdern.spring.audit.repository.EventRepository;

import ru.alexdern.spring.audit.service.EventService;
import ru.alexdern.spring.audit.service.UserService;
import ru.alexdern.spring.audit.util.SearchOperation;
import ru.alexdern.spring.audit.util.SearchSpecificationsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.alexdern.spring.audit.repository.specification.EventSpecification.*;


@RestController
@RequestMapping("/api/audit")
@Api(tags = {"audit"})
public class AuditController {


    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;


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
    }


    @GetMapping(value = "/journal", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditEventDto> journal() {
        return eventService.findAll()
                .stream()
                    .map(AuditEventDto::make)
                    .collect(Collectors.toList());
    }


    @GetMapping(value = "/journal/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditEventDto> filter(
            @RequestParam(value = "u", required = false) Long userId,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "t", required = false) String eventType,
            @RequestParam(value = "dt_start", required = false) String dtStart,
            @RequestParam(value = "dt_end", required = false) String dtEnd
    ) {
        return eventRepository.findAll(searchCriteria(userId, query, eventType, dtStart, dtEnd),
                Sort.by("eventDateTime").descending())
                .stream()
                    .map(AuditEventDto::make)
                    .collect(Collectors.toList());
    }



    /**
     * /journal/search?q=eventType:LOGIN,eventDateTime>2019-07-07T12:00:00
     * @param query
     * @return
     */
    @GetMapping(value = "/journal/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditEventDto> search(@RequestParam("q") String query) {

        SearchSpecificationsBuilder builder = new SearchSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(query + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }

        Specification<Event> spec = builder.build();

        return eventRepository.findAll(spec, Sort.by("eventDateTime").descending())
                .stream()
                    .map(AuditEventDto::make)
                    .collect(Collectors.toList());
    }


}
