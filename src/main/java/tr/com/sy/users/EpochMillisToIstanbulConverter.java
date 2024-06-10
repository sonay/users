package tr.com.sy.users;

import jakarta.persistence.AttributeConverter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

class EpochMillisToIstanbulConverter implements AttributeConverter<ZonedDateTime, Long> {

  @Override
  public Long convertToDatabaseColumn(ZonedDateTime attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.toInstant().toEpochMilli();
  }

  @Override
  public ZonedDateTime convertToEntityAttribute(Long dbData) {
    if (dbData == null) {
      return null;
    }
    return Instant.ofEpochMilli(dbData).atZone(ZoneId.of("Europe/Istanbul"));
  }

}
