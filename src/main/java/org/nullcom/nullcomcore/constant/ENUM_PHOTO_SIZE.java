package org.nullcom.nullcomcore.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
public enum ENUM_PHOTO_SIZE {
  XS(24, "_xs"), S(32, "_s"), M(40, "_m"), L(48, "_l"), XL(56, "_xl");

  @Getter
  @Accessors(fluent = true)
  private final int value;

  @Getter
  @Accessors(fluent = true)
  private final String sizeName;

}
