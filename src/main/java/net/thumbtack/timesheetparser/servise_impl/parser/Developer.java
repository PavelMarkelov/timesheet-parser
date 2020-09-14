package net.thumbtack.timesheetparser.servise_impl.parser;

import java.time.LocalDate;

public class Developer {

  private final String name;
  private final LocalDate startDate;
  private final String projectName;

  private LocalDate endDate;
  private double numberOfHours = 0;

  public Developer(final String name, final String projectName, final LocalDate startDate) {
    this.name = name;
    this.projectName = projectName;
    this.startDate = startDate;
  }

  public String getProjectName() {
    return projectName;
  }

  public void addHours(final double numberOfHours) {
    this.numberOfHours += numberOfHours;
  }

  public void setEndDate(final LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getName() {
    return name;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate == null ? startDate : endDate;
  }

  public double getNumberOfHours() {
    return numberOfHours;
  }
}
