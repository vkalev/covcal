package edu.brown.cs.student.Organization;

import java.util.Date;

/**
 * This is the class that mirrors our employment table in the database. It is useful to
 * hold the info about a user's employment at an organization.
 */
public class Employee {
  private String id;
  private String name;
  private Date startDate;
  private String positions;
  private String email;
  private String imageUrl;

  /**
   * Constructor for Employee class that initializes fields.
   * @param empId - ID of this employee
   * @param date - start date of employee
   * @param positionList - positions of the employee
   * @param userEmail - email of the employee
   * @param username - name of the employee
   * @param image - image url of the employee
   */
  public Employee(String empId, Date date, String positionList, String userEmail, String username,
                  String image) {
    this.id = empId;
    this.startDate = date;
    this.positions = positionList;
    this.email = userEmail;
    this.name = username;
    this.imageUrl = image;
  }

  /**
   * Getter for the ID of the employee.
   * @return the id field
   */
  public String getId() {
    return this.id;
  }

  /**
   * Getter for the name of the employee.
   * @return the name field
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter for the image url of the employee.
   * @return the imageUrl field
   */
  public String getImageUrl() {
    return this.imageUrl;
  }

  /**
   * Getter for the email of the employee.
   * @return the email field
   */
  public String getEmail() {
    return this.email;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Employee) {
      Employee emp = (Employee) o;
      return this.id.equals(emp.id) && this.name.equals(emp.name)
              && this.startDate.equals(emp.startDate)
              && this.email.equals(emp.email) && this.imageUrl.equals(emp.imageUrl);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
