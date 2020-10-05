package edu.brown.cs.student.Utilities;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Util class dealing with Dates and times.
 */
public final class DateTimeUtil {
  private static Calendar cal = Calendar.getInstance();
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
  private static final int NUM_ZEROES_HOURS = 100;
  private static final int NUM_ZEROES_YEARS = 10000;
  private static final int NUM_HOURS_HALF = 12;
  private static final int FIRST_DOUBLE_DIG = 10;

  /**
   * Private constructor for DateTimeUtil.
   */
  private DateTimeUtil() {
  }

  /**
   * Method formatting current date to dateFormat.
   * @return String representation of current date
   */
  public static String newFormattedDate() {
    Date date = new Date(new java.util.Date().getTime());
    return DATE_FORMAT.format(date);
  }

  /**
   * Getter for Calendar instance corresponding with current date and time.
   * @return Calendar instance representing current date and time
   */
  public static Calendar getCurrCalendar() {
    java.sql.Date currDate = new java.sql.Date(new java.util.Date().getTime());
    Calendar currCal = Calendar.getInstance();
    currCal.setTime(currDate);
    return currCal;
  }

  /**
   * Getter for current time.
   * @return current time represented as an integer (1425 = 2:25pm)
   */
  public static int getCurrTime() {
    java.util.Date time = new java.util.Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    String[] timeArr = dateFormat.format(time).split(":");
    return (Integer.parseInt(timeArr[0]) * NUM_ZEROES_HOURS) + Integer.parseInt(timeArr[1]);
  }

  /**
   * Getter for day of month given a Date.
   * @param date - the Date instance
   * @return day of month for the date
   */
  public static int getDayOfMonth(java.sql.Date date) {
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Getter for month number given a Date.
   * @param date - the Date instance
   * @return month number for the date
   */
  public static int getMonthNum(java.sql.Date date) {
    cal.setTime(date);
    return cal.get(Calendar.MONTH);
  }

  /**
   * Getter for year number given a Date.
   * @param date - the Date instance
   * @return year number for the date
   */
  public static int getYearNum(java.sql.Date date) {
    cal.setTime(date);
    return cal.get(Calendar.YEAR);
  }

  /**
   * Getter for number of hours given a time.
   * @param time - the time as an integer
   * @return number of hours for time
   */
  public static int getHours(int time) {
    return time / NUM_ZEROES_HOURS;
  }

  /**
   * Getter for number of minutes given a time.
   * @param time - the time as an integer
   * @return number of minutes for time
   */
  public static int getMinutes(int time) {
    return time % NUM_ZEROES_HOURS;
  }

  /**
   * Getter for pretty String representation of time.
   * @param time - the time as an integer
   * @return String of representation of time for display (7:34pm)
   */
  public static String getDisplayTime(int time) {
    int minutes = time % NUM_ZEROES_HOURS;
    String amPm = "";
    int hours = time / NUM_ZEROES_HOURS;
    if (hours >= NUM_HOURS_HALF) {
      amPm = "pm";
      if (hours != NUM_HOURS_HALF) {
        hours = hours - NUM_HOURS_HALF;
      }
    } else {
      if (hours == 0) {
        hours = NUM_HOURS_HALF;
      }
      amPm = "am";
    }
    if (minutes < FIRST_DOUBLE_DIG) {
      return hours + ":0" + minutes + amPm;
    }
    return hours + ":" + minutes + amPm;
  }

  /**
   * Method determining whether the given date and time has passed the current
   * date and time.
   * @param date - the date as SQL Date instance
   * @param endTime - the time as an integer
   * @return true if the date and endTime has passed, false otherwise
   */
  public static boolean dateTimeHasPassed(java.sql.Date date, int endTime) {
    Calendar currCal = DateTimeUtil.getCurrCalendar();
    int currTime = DateTimeUtil.getCurrTime();
    Calendar shiftCal = Calendar.getInstance();
    shiftCal.setTime(date);
    if (currCal.get(Calendar.YEAR) != shiftCal.get(Calendar.YEAR)) {
      return currCal.get(Calendar.YEAR) > shiftCal.get(Calendar.YEAR);
    }
    if (currCal.get(Calendar.MONTH) != shiftCal.get(Calendar.MONTH)) {
      return currCal.get(Calendar.MONTH) > shiftCal.get(Calendar.MONTH);
    }
    if (currCal.get(Calendar.DAY_OF_MONTH) != shiftCal.get(Calendar.DAY_OF_MONTH)) {
      return currCal.get(Calendar.DAY_OF_MONTH) > shiftCal.get(Calendar.DAY_OF_MONTH);
    }
    return currTime >= endTime;
  }

  /**
   * Getter for integer representation of SQL Date instance.
   * @param date - date as SQL Date instance
   * @return integer representation of date (1:45pm = 1345)
   */
  public static int dateToIntCoverter(java.sql.Date date) {
    int result = getYearNum(date) * NUM_ZEROES_YEARS;
    result += getMonthNum(date) * NUM_ZEROES_HOURS;
    return result + getDayOfMonth(date);
  }

  /**
   * Compare for SQL Date instances.
   * @param date1 - first SQL Date instance
   * @param date2 - second SQL Date instance
   * @return 1 if date1 further in the future than date2, -1 if the opposite is
   *         true, 0 if date1 and date2 have same value
   */
  public static int compareSQLDates(java.sql.Date date1, java.sql.Date date2) {
    return Integer.compare(dateToIntCoverter(date1), dateToIntCoverter(date2));
  }
}
