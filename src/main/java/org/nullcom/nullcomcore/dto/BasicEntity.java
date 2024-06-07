package org.nullcom.nullcomcore.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder
@FieldNameConstants
public abstract class BasicEntity implements EntityDto{
  private String modUserId;
  private String modTimeZone;
  private String hostId;
}
