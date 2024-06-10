package tr.com.sy.users;

import java.time.YearMonth;

public record MonthlyUserRegistrationStatistics(YearMonth month, Long count) {
}
