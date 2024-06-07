package org.nullcom.nullcomcore.util;



import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {
  public static String nvl(String str1) {
    return nvl(str1, null);
  }

  public static String nvl(String str1, String str2) {
    if (!isEmpty(str1)) {
      return str1;
    } else if (!isEmpty(str2)) {
      return str2;
    } else {
      return "";
    }
  }

  public static boolean isValueInt(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean isValueLong(String value) {
    try {
      Long.parseLong(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean isValueDouble(String value) {
    try {
      Double.parseDouble(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean inString(String val, String... froms) {
    boolean rtn = false;
    val = nvl(val);

    for (String from : froms) {
      if (val.equals(from)) return true;
    }
    return rtn;
  }

}
