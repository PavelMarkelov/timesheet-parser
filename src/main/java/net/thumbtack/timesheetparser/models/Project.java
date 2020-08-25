package net.thumbtack.timesheetparser.models;

import java.time.LocalDate;
import java.util.Objects;

public class Project {

    private int id;
    private String name;
    private LocalDate start;
    private LocalDate end;
    private int countOfTrackingRows;
    private double numberOfHours;

    public Project() {
    }

    public Project(int countOfTrackingRows, String name, LocalDate start, LocalDate end, double numberOfHours) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.countOfTrackingRows = countOfTrackingRows;
        this.numberOfHours = numberOfHours;
    }

    public Project(Project project) {
        this.id = project.id;
        this.name = project.name;
        this.start = project.start;
        this.end = project.end;
        this.countOfTrackingRows = project.countOfTrackingRows;
        this.numberOfHours = project.numberOfHours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public int getCountOfTrackingRows() {
        return countOfTrackingRows;
    }

    public void setCountOfTrackingRows(int countOfTrackingRows) {
        this.countOfTrackingRows = countOfTrackingRows;
    }

    public double getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(double numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
