package tr.com.sy.users;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class UsersTablePopulator {

  public static final ZoneId ISTANBUL = ZoneId.of("Europe/Istanbul");

  private final EntityManager em;

  public UsersTablePopulator(EntityManager em) {
    this.em = Objects.requireNonNull(em, "EntityManager must not be null.");
  }


  @Transactional
  public void populateUsers() {
    int batchSize = 20; //em.unwrap(Session.class).getJdbcBatchSize(); Hibernate ya da spring bugı?
    int batched = 0;
    ZonedDateTime now = ZonedDateTime.now(ISTANBUL);
    ZonedDateTime epoch = now.minusMonths(10);
    for (int i = 0; i < 20_000; i++) {
      em.persist(
          createUser(
              epoch.toInstant().toEpochMilli(),
              now.toInstant().toEpochMilli()));
      if (++batched == batchSize) {
        em.flush();
        em.clear();
        batched = 0;
      }
    }
  }

  private User createUser(Long epoch, Long now) {
    var user = new User();
    user.setRegisteredAt(
        Instant.ofEpochMilli(ThreadLocalRandom.current().nextLong(epoch, now))
            .atZone(ISTANBUL));
    return user;
  }

}
