package edu.brown.cs.student.Utilities;

import edu.brown.cs.student.Organization.ShiftRequest;
import java.util.Comparator;

/**
 * Comparator class for ShiftRequest objects in Organization package.
 */
public class ShiftRequestComparator implements Comparator<ShiftRequest> {

  @Override
  public int compare(ShiftRequest o1, ShiftRequest o2) {
    if (DateTimeUtil.compareSQLDates(o1.getDate(), o2.getDate()) == 0) {
      return Integer.compare(o1.getStartTime(), o2.getStartTime());
    }
    return DateTimeUtil.compareSQLDates(o1.getDate(), o2.getDate());
  }
}
