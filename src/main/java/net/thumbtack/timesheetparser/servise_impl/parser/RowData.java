package net.thumbtack.timesheetparser.servise_impl.parser;

import java.time.LocalDate;

public class RowData {

  private final String project;
  private final String staffMember;
  private final LocalDate date;
  private final double numberOfHours;

  public RowData(String project, String staffMember, LocalDate date, double numberOfHours) {
    this.project = project;
    this.staffMember = staffMember;
    this.date = date;
    this.numberOfHours = numberOfHours;
  }

  public String getProjectName() {
    return project;
  }

  public String getStaffMember() {
    return staffMember;
  }

  public LocalDate getDate() {
    return date;
  }

  public double getNumberOfHours() {
    return numberOfHours;
  }
}
