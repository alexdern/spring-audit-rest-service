package ru.alexdern.spring.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alexdern.spring.audit.domain.User;
import ru.alexdern.spring.audit.exception.UserNotFoundException;
import ru.alexdern.spring.audit.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;


    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public Optional<User> findByEID(Long id) {
        return repo.findUserByEID(id);
    }

    public List<User> findAll() {
        return repo.findAll();
    }


    public User getOne(Long id) {
        return repo.getOne(id);
    }

    public User create(User user) {
        return repo.save(user);
    }

    public void update(User user) {
        repo.save(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}
