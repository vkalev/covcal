package edu.brown.cs.student.Utilities;

import java.util.List;

/**
 * General utility class for CovCal program.
 */
public final class CovCalUtil {

  /**
   * Private constructor for CovCalUtil.
   */
  private CovCalUtil() {
  }

  /**
   * Method for formatting positions to a proper String.
   * @param positions - list of positions
   * @return a String representing the positions list
   */
  public static String positionGenerator(List<String> positions) {
    if (positions == null || positions.isEmpty()) {
      return "";
    } else {
      // We construct a parsable String containing all of the positions
      StringBuilder pList = new StringBuilder("(");
      for (int i = 0; i < positions.size() - 1; i++) {
        pList.append("'").append(positions.get(i)).append("', ");
      }
      // add the last position, without the comma
      pList.append("'").append(positions.get(positions.size() - 1)).append("')");
      return String.valueOf(pList);
    }
  }
}
