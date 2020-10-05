package edu.brown.cs.student.CovCal;

import java.sql.SQLException;

/**
 * Main class for CovCal; creates CovCal instance and corresponding controller.
 */
public final class Main {

  /**
   * Constructor for Main class that creates a controller with its corresponding
   * CovCal program instance.
   * @throws SQLException on querying error
   * @throws ClassNotFoundException when "sqlite" class not found
   */
  private Main() throws SQLException, ClassNotFoundException {
    new CovCalController(new CovCal());
  }

  /**
   * Main method for the main class that creates a new instance of Main.
   * @param args - program arguments
   */
  public static void main(String[] args) {
    try {
      new Main();
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("ERROR: Error initiating CovCal program");
    }
  }
}
