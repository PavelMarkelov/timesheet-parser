package net.thumbtack.timesheetparser.dao;

import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import net.thumbtack.timesheetparser.models.WorkgroupMember;
import net.thumbtack.timesheetparser.servise_impl.parser.Developer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeveloperProjectsDao {

    void save(Developer developer);

    Optional<StaffMember> getStaffMemberByName(String name);

    List<Project> getStaffMemberProjects(StaffMember staffMember, int numberOfMonths, int numberOfHours);

    boolean isEmptyDatabase();

    Optional<Project> getProject(StaffMember staffMember, int projectId);

    List<WorkgroupMember> getWorkgroup(StaffMember staffMember, Project project, LocalDate start, LocalDate end);

    Optional<StaffMember> getStaffMemberById(int staffMemberId);

    void clearDatabase();
}
