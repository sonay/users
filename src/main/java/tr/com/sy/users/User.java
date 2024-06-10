package tr.com.sy.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity(name = "Users")
// Postgresql "user" isimli tabloya izin vermez.
// Yolu var ama işleri karıştırmayalım. Sizin tablonun adı ile değiştirebilirsin.
@Getter @Setter
@SequenceGenerator(
    name = User.SEQUENCE_GENERATOR,
    sequenceName = "users_id_seq",
    allocationSize = 20 // jdbc batchsize'ı kadar olması iyi
)
public class User {

  public static final String SEQUENCE_GENERATOR = "users_id_seq_gen";

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = SEQUENCE_GENERATOR
  )
  private Long id;

  @Convert(converter = EpochMillisToIstanbulConverter.class)
  ZonedDateTime registeredAt;

  private String name;

  @PrePersist
  public void setSomeRandomName() {
    if (name == null) {
      name = "user " + getId();
    }
  }

  @Override
  public int hashCode() {
    return 3;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof User that) {
      return Objects.equals(this.getId(), that.getId());
    }
    return false;
  }

  @Override
  public String toString() {
    return "%s - #%d".formatted(getClass().getSimpleName(), getId());
  }

}

