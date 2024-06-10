package tr.com.sy.users;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Objects;

@Repository
public class UsersDao {

  private final EntityManager entityManager;

  public UsersDao(EntityManager entityManager) {
    this.entityManager = Objects.requireNonNull(entityManager, "entityManager must not be null");
  }

  public Long registeredUserCountBetween(ZonedDateTime start, ZonedDateTime end) {
    Objects.requireNonNull(start, "start must not be null");
    Objects.requireNonNull(end, "end must not be null");
    var cb = entityManager.getCriteriaBuilder();
    var cq = cb.createQuery(Long.class);
    var users = cq.from(User.class);
    var startParam = cb.parameter(ZonedDateTime.class);
    var endParam = cb.parameter(ZonedDateTime.class);
    cq.select(cb.count(users.get(User_.id)))
        .where(
            cb.and(
                cb.greaterThanOrEqualTo(
                    users.get(User_.registeredAt), startParam),
                cb.lessThan(
                    users.get(User_.registeredAt), endParam)
            )
        );
    return entityManager.createQuery(cq)
        .setParameter(startParam, start)
        .setParameter(endParam, end)
        .getSingleResult();
  }

  public Long registeredUserCountBetweenWithHQL(ZonedDateTime start, ZonedDateTime end) {
    Objects.requireNonNull(start, "start must not be null");
    Objects.requireNonNull(end, "end must not be null");
    // HQL bence daha güzel, sade.
    return entityManager.createQuery("""
            select count(u.id)
            from Users u
            where u.registeredAt >= ?1 and u.registeredAt < ?2
            """, Long.class)
        .setParameter(1, start)
        .setParameter(2, end)
        .getSingleResult();
  }
}
