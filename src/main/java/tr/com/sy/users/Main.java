package tr.com.sy.users;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  ApplicationRunner applicationRunner(UsersTablePopulator populator, UsersService usersService) {
    return args -> {
      populator.populateUsers();
      var lastTenDays = usersService.registeredUserCountInLastTenDays();
      System.out.println("lastTenDays = " + lastTenDays);
      var monthly = usersService.registeredUserCountsInLastTenMonths();
      monthly.forEach(System.out::println);
    };
  }

}
