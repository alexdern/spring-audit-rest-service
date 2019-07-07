package ru.alexdern.spring.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alexdern.spring.audit.domain.Event;
import ru.alexdern.spring.audit.exception.EventNotFoundException;
import ru.alexdern.spring.audit.repository.EventRepository;

import java.util.List;

import static ru.alexdern.spring.audit.repository.specification.EventSpecification.eventByUserEID;


@Service
public class EventService {

    @Autowired
    private EventRepository repo;


    public Event findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    public List<Event> findAll() {
        return repo.findAll(Sort.by("eventDateTime").descending());
    }

    public List<Event> findAllByUserEID(Long user_eid) {
        return repo.findAll(eventByUserEID(user_eid));
    }


    public Event getOne(Long id) {
        return repo.getOne(id);
    }

    public Event create(Event event) {
        return repo.save(event);
    }

    public void update(Event event) {
        repo.save(event);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


}
