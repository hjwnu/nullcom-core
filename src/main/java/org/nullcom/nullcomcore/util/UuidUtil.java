package org.nullcom.nullcomcore.util;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidUtil {
  public static Serializable generate() {
    return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
  }

  public static String generateString() {
    return generate().toString();
  }

  public static String generateMetaCd() {
    return Long.toString(ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong(), Character.MAX_RADIX)
        .toUpperCase();
  }
}
