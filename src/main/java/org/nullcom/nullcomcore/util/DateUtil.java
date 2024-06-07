package org.nullcom.nullcomcore.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

  protected static final String format_ymdhms_date = "yyyy-MM-dd HH:mm:ss";
  protected static final String format_yy = "yyyy";
  protected static final String format_ymd = "yyyyMMdd";
  protected static final String format_ymd_date = "yyyy.MM.dd";
  protected static final String format_ymdhms = "yyyyMMddHHmmss";
  protected static final String format_ymdhmsS = "yyyyMMddHHmmssSSS";
  protected static final String format_ymdhmsS_date = "yyyy-MM-dd HH:mm:ss,SSS";

  protected final static String defaultTimeZone = "Asia/Seoul";

  public static ZonedDateTime getZonedDateTime() {
    return ZonedDateTime.now(ZoneId.of(defaultTimeZone));
  }

  public static ZonedDateTime getZonedDateTimeByZoneString(String zoneString) {
    return ZonedDateTime.parse(zoneString);
  }

  public static String getIso() {
    ZonedDateTime zonedDateTime = getZonedDateTime();
    return getIso(zonedDateTime);
  }

  public static String getIso(String zoneId) {
    ZonedDateTime zonedDateTime = getZonedDateTimeByZoneId(zoneId);
    return getIso(zonedDateTime);
  }

  private static String getIso(ZonedDateTime zonedDateTime) {
    return zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public static String getYmdhmsSToZoneString(String zoneString) {
    ZonedDateTime zonedDateTime = getZonedDateTimeByZoneString(zoneString);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format_ymdhms_date);
    return zonedDateTime.format(formatter);
  }

  public static String getTodayYy() {
    LocalDateTime ldtNow = getLocalDateTime();
    return ldtNow.format(DateTimeFormatter.ofPattern(format_yy));
  }

  public static String getTodayYmd() {
    LocalDateTime ldtNow = getLocalDateTime();
    return ldtNow.format(DateTimeFormatter.ofPattern(format_ymd));
  }

  public static LocalDate ymdToLocalDate(String ymd) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format_ymd);
    return LocalDate.parse(ymd, formatter);
  }

  public static LocalDateTime ymdToLocalDateTime(String ymdhms) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format_ymdhms);
    return LocalDateTime.parse(ymdhms, formatter);
  }


  /*
   * LocalDate 관련
   */
  public static String getLocalDateYmdhms() {
    return getYmdhms(getLocalDateTime());
  }

  public static String getLocalDateYmdhms_dateformat() {
    return getYmdhms_dateformat(getLocalDateTime());
  }

  public static LocalDateTime getLocalDateTime() {
    return LocalDateTime.now();
  }

  public static String getYmd(LocalDate localDate) {
    return localDate.format(DateTimeFormatter.ofPattern(format_ymd));
  }

  public static String getYmd(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern(format_ymd));
  }

  public static String getYmdhms(LocalDate localDate) {
    return localDate.format(DateTimeFormatter.ofPattern(format_ymdhms));
  }

  public static String getYmdhms(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern(format_ymdhms));
  }

  public static String getYmdhms_dateformat(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern(format_ymdhms_date));
  }
  /*
   * LocalDate 관련
   */

  public static ZonedDateTime getZonedDateTimeByZoneId(String zoneId) {
    return ZonedDateTime.now(ZoneId.of(zoneId));
  }

  /**
   * 기준일(stdYmd)가 staYmd와 endYmd 사이에 존재하면 true
   *
   * @param staYmd 기준일
   * @param endYmd 시작일
   * @param stdYmd 종료일
   * @return
   */
  public static boolean isBetween(String staYmd, String endYmd, String stdYmd) {

    LocalDate stdLdt = ymdToLocalDate(stdYmd);
    LocalDate staLdt = ymdToLocalDate(staYmd);
    LocalDate endLdt = ymdToLocalDate(endYmd);

    if( staLdt.isBefore(stdLdt) && endLdt.isAfter(stdLdt) ) {
      return true;
    }
    return false;
  }

  /**
   * 기준일(stdYmd)가 staYmd와 endYmd 사이에 존재거나, 동일하면 true
   *
   * @param staYmd 기준일
   * @param endYmd 시작일
   * @param stdYmd 종료일
   * @return
   */
  public static boolean isBetweenEqual(String staYmd, String endYmd, String stdYmd) {

    LocalDate stdLdt = ymdToLocalDate(stdYmd);
    LocalDate staLdt = ymdToLocalDate(staYmd);
    LocalDate endLdt = ymdToLocalDate(endYmd);

    if( (staLdt.isBefore(stdLdt)||staLdt.isEqual(stdLdt)) && (endLdt.isAfter(stdLdt)||endLdt.isEqual(stdLdt)) ) {
      return true;
    }
    return false;
  }

  public static boolean gtYmd(String ymd1, String ymd2) {
    if (parseInt(ymd1) > parseInt(ymd2)) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean gtEqYmd(String ymd1, String ymd2) {
    if (parseInt(ymd1) >= parseInt(ymd2)) {
      return true;
    } else {
      return false;
    }
  }

  public static LocalDate getLocalDate(String ymd) {
    LocalDate ldt = null;
    if (ymd != null && ymd.length() == 8) {
      ldt = LocalDate.of(parseInt(ymd.substring(0, 4)), parseInt(ymd.substring(4, 6)),
          parseInt(ymd.substring(6, 8)));
    }
    return ldt;
  }

  /**
   * YYYYMMDD 스타일의 문자열로 LocalDate 컨버전
   * @param ldt 변경하고자 하는 LocalDate
   * @return YYYYMMDD 스타일의 문자열
   */
  public static String getYmdString(LocalDate ldt) {
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyyMMdd");
    return ldt.format(pattern);
  }

  public static int parseInt(String str) {
    return Integer.parseInt(StringUtil.nvl(str, "0"));
  }


  public static Period getDiff(String staYmd, String endYmd) {
    LocalDate ldSta = getLocalDate(staYmd);
    LocalDate ldEnd = getLocalDate(endYmd);

    Period diff = Period.between(ldSta, ldEnd);
    return diff;
  }

  public static String getYmdMin(String ymd1, String ymd2) {
    LocalDate ld1 = getLocalDate(ymd1);
    LocalDate ld2 = getLocalDate(ymd2);
    if(ld1.isBefore(ld2)) {
      return ymd1;
    }else {
      return ymd2;
    }
  }

  public static String getYmdMax(String ymd1, String ymd2) {
    LocalDate ld1 = getLocalDate(ymd1);
    LocalDate ld2 = getLocalDate(ymd2);
    if(ld1.isAfter(ld2)) {
      return ymd1;
    }else {
      return ymd2;
    }
  }

  public static String getYesterdayYmd(String ymd) {
    LocalDate ld = getLocalDate(ymd);
    LocalDate yd = ld.minusDays(1);
    return getYmd(yd);
  }

}
