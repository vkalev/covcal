package edu.brown.cs.student.Organization;

import edu.brown.cs.student.Utilities.DateTimeUtil;

import java.sql.Date;

/**
 * Class representing a request for a shift cover; purpose of this class is to
 * send instances of it to the frontend of CovCal.
 */
public class ShiftRequest {
  private int id;
  private int orgId;
  private Date date;
  private int startTime;
  private int endTime;
  private String createdById;
  private String userImg;
  private String nameAsker;
  private int monthNum;
  private int yearNum;
  private int dayOfMonth;
  private int startHour;
  private int endHour;
  private int startMinutes;
  private int endMinutes;
  private String displayStartTime;
  private String displayEndTime;
  private boolean isAccepted;
  private String donorName;

  /**
   * Constructor for ShiftRequest class.
   * @param id - ID of the request
   * @param orgID - ID of the group the request is for
   * @param date - date of the shift
   * @param start - start time of the shift
   * @param end - end time of the shift
   * @param createdById - ID of user requesting
   * @param nameAsker - name of the user requesting
   * @param imgAsker - user image url of the user requesting
   */
  public ShiftRequest(int id, int orgID, Date date, int start, int end,
                      String createdById, String nameAsker, String imgAsker) {
    this.id = id;
    this.orgId = orgID;
    this.date = date;
    this.startTime = start;
    this.endTime = end;
    this.createdById = createdById;
    this.nameAsker = nameAsker;
    this.userImg = imgAsker;
    this.monthNum = DateTimeUtil.getMonthNum(this.date);
    this.yearNum = DateTimeUtil.getYearNum(this.date);
    this.dayOfMonth = DateTimeUtil.getDayOfMonth(this.date);
    this.startHour = DateTimeUtil.getHours(this.startTime);
    this.endHour = DateTimeUtil.getHours(this.endTime);
    this.startMinutes = DateTimeUtil.getMinutes(this.startTime);
    this.endMinutes = DateTimeUtil.getMinutes(this.endTime);
    this.displayStartTime = DateTimeUtil.getDisplayTime(this.startTime);
    this.displayEndTime = DateTimeUtil.getDisplayTime(this.endTime);
  }

  /**
   * Getter for the request's ID.
   * @return id field
   */
  public int getID() {
    return this.id;
  }

  /**
   * Getter for the request's organization ID.
   * @return orgId field
   */
  public int getOrgID() {
    return this.orgId;
  }

  /**
   * Getter for the request's date.
   * @return date field
   */
  public Date getDate() {
    return this.date;
  }

  /**
   * Getter for the request's start time.
   * @return startTime field
   */
  public int getStartTime() {
    return this.startTime;
  }

  /**
   * Getter for the request's end time.
   * @return endTime field
   */
  public int getEndTime() {
    return this.endTime;
  }

  /**
   * Getter for the request's creator's ID.
   * @return createdById field
   */
  public String getCreatedById() {
    return this.createdById;
  }

  /**
   * Setter for updating information when the request's accepted status changes.
   * @param accepted - boolean representing whether request is now accepted
   * @param empName - name of the employee accepting, null if isAccepted is false
   */
  public void setAcceptedInfo(boolean accepted, String empName) {
    this.isAccepted = accepted;
    assert accepted || empName == null;
    this.donorName = empName;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ShiftRequest) {
      ShiftRequest req = (ShiftRequest) o;
      return this.id == req.id && this.orgId == req.orgId && this.startTime == req.startTime
              && this.endTime == req.endTime && this.createdById.equals(req.createdById)
              && this.userImg.equals(req.userImg) && this.nameAsker.equals(req.nameAsker);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.id;
  }
}
