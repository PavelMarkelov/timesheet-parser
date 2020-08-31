package net.thumbtack.timesheetparser.service;

import net.thumbtack.timesheetparser.dto.response.StaffMemberResponse;
import net.thumbtack.timesheetparser.exception.FileNotLoadedException;
import net.thumbtack.timesheetparser.exception.ProjectNotFoundException;
import net.thumbtack.timesheetparser.exception.StaffMemberNotFoundException;
import net.thumbtack.timesheetparser.models.WorkgroupMember;

import java.util.List;

public interface StaffMemberService {

    StaffMemberResponse getProjects(String staffMember, int numberOfMonths, int numberOfHours) throws StaffMemberNotFoundException, FileNotLoadedException;

    List<WorkgroupMember> getWorkgroup(int staffMemberId, int projectId) throws ProjectNotFoundException, StaffMemberNotFoundException, FileNotLoadedException;
}
