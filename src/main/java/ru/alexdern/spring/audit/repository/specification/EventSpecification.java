package ru.alexdern.spring.audit.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.alexdern.spring.audit.domain.Event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> byUserEID(Long id) {
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

    public static Specification<Event> searchCriteria(Long userId, String like, String eventType, String dtStart, String dtEnd) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();
                if(userId != null) {
                    predicates.add(builder.and(builder.equal(root.get("user").get("userExternalID"), userId)));
                }
                if(eventType != null) {
                    predicates.add(builder.and(builder.equal(root.get("eventType"), eventType)));
                }
                if (like != null) {
                    predicates.add(builder.or(
                            builder.like(root.get("user").get("username"), "%" + like + "%"),
                            builder.like(root.get("user").get("login"), "%" + like + "%"))
                    );
                }
                if (dtStart != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("eventDateTime"), LocalDateTime.parse(dtStart)));
                }
                if (dtEnd != null) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("eventDateTime"), LocalDateTime.parse(dtEnd)));
                }
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }


    public static Specification<Event> byUserLike(String text) {
        return (root, query, builder) -> {
            //builder.like(root.get("user").get("username"), "%" + text + "%");
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.or(
                    builder.like(root.get("user").get("username"), "%" + text + "%"),
                    builder.like(root.get("user").get("login"), "%" + text + "%"))
            );
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<Event> byType(String type) {
        return (root, query, builder) -> builder.equal(root.get("eventType"), type);
    }

    public static Specification<Event> atDateStart(String dt) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("eventDateTime"), dt);
    }

    public static Specification<Event> atDateEnd(String dt) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("eventDateTime"), dt);
    }


}
