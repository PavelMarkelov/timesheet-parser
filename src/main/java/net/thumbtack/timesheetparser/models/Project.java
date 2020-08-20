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
        this.countOfTrackingRows = countOfTrackingRows;
        this.name = name;
        this.start = start;
        this.end = end;
        this.numberOfHours = numberOfHours;
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
