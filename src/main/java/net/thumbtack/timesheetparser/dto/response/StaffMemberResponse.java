package net.thumbtack.timesheetparser.dto.response;

import net.thumbtack.timesheetparser.models.Project;

import java.util.List;

public class StaffMemberResponse {
    private int id;
    List<Project> projects;

    public StaffMemberResponse() {
    }

    public StaffMemberResponse(int id, List<Project> projects) {
        this.id = id;
        this.projects = projects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
