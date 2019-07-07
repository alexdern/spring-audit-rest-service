package ru.alexdern.spring.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alexdern.spring.audit.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.userExternalID = :eid")
    Optional<User> findUserByEID(@Param("eid") Long eid);

    Optional<User> findByUsername(String username);

}
