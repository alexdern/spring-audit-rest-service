package ru.alexdern.spring.audit.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.alexdern.spring.audit.domain.Event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EventSpecification {

    public static Specification<Event> eventByUserEID(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("userExternalID"), id);
        /*
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("user").get("userExternalID"), id);
            }
        };
        */
    }

}
