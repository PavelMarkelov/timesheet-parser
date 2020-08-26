package net.thumbtack.timesheetparser.models;

import java.time.LocalDate;

public class WorkgroupMember {
    private StaffMember workgroupMember;
    private LocalDate start;
    private LocalDate end;
    private double numberOfHours;

    public WorkgroupMember() {
    }

    public WorkgroupMember(StaffMember workgroupMember, LocalDate start, LocalDate end, double numberOfHours) {
        this.workgroupMember = workgroupMember;
        this.start = start;
        this.end = end;
        this.numberOfHours = numberOfHours;
    }

    public WorkgroupMember(StaffMember workgroupMember, Project project) {
        this.workgroupMember = workgroupMember;
        this.start = project.getStart();
        this.end = project.getEnd();
        this.numberOfHours = project.getNumberOfHours();
    }

    public StaffMember getWorkgroupMember() {
        return workgroupMember;
    }

    public void setWorkgroupMember(StaffMember workgroupMember) {
        this.workgroupMember = workgroupMember;
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

    public double getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(double numberOfHours) {
        this.numberOfHours = numberOfHours;
    }
}
