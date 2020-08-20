package net.thumbtack.timesheetparser.dao;

import net.thumbtack.timesheetparser.models.Project;

import java.util.List;

public interface DeveloperProjectsDao {

    void save(String developerName, Project project);

    boolean isStaffMemberExist(String staffMember);

    List<Project> getStaffMemberProjects(String staffMember, int numberOfMonths);

    boolean isEmptyDatabase();
}
