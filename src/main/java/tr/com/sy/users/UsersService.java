package tr.com.sy.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class UsersService {

  private static final ZoneId ZONE = UsersTablePopulator.ISTANBUL;

  private final UsersDao usersDao;

  public UsersService(UsersDao usersDao) {
    this.usersDao = Objects.requireNonNull(usersDao, "usersDao must not be null.");
  }


  /**
   * İçinde bulunduğumuz ayı kapsamaz.
   */
  @Transactional
  public List<MonthlyUserRegistrationStatistics> registeredUserCountsInLastTenMonths() {
    // ay başı gece yarısı olması önemli yoksa diğer ayların değeri kayar
    // bu ayı da kapsasın istiyorsan commentli kodu aç.
    ZonedDateTime end = ZonedDateTime.now(ZONE)
        //.plusMonths(1)
        .withDayOfMonth(1)
        .truncatedTo(ChronoUnit.DAYS);
    ZonedDateTime start = end.minusMonths(10); // Bunu parametre olarak alıp değiştirebilirsin tabii :)

    return Stream.iterate(start, end::isAfter, month -> month.plusMonths(1))
        .map(month ->
            new MonthlyUserRegistrationStatistics(
                YearMonth.from(month),
                usersDao.registeredUserCountBetween(month, month.plusMonths(1))
            ))
        .toList();
  }

  /**
   * Son 10 gün derken içinde bulunduğumuz andan 10 gün geriye sayıyoruz.
   * Başka bir tanımı: bugün dahil değil, son 10 gün olabilir.
   * Başka bir tanımı: son 9 gün artı bugün şu ana kadarı vs.
   * java.time API ile günleri istediğin tanıma ayarlayabilirsin.
   */
  Long registeredUserCountInLastTenDays() {
    ZonedDateTime end = ZonedDateTime.now(ZONE);
    ZonedDateTime start = end.minusDays(10);
    return usersDao.registeredUserCountBetweenWithHQL(start, end);
  }

}
