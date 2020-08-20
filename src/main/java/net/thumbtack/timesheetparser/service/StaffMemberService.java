package net.thumbtack.timesheetparser.service;

import net.thumbtack.timesheetparser.models.Project;

import java.util.List;

public interface StaffMemberService {

    List<Project> getProjects(String staffMember, int numberOfMonths);
}
