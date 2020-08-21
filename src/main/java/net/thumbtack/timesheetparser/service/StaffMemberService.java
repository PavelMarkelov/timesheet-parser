package net.thumbtack.timesheetparser.service;

import net.thumbtack.timesheetparser.dto.response.StaffMemberResponse;
import net.thumbtack.timesheetparser.models.WorkgroupMember;

import java.time.LocalDate;
import java.util.List;

public interface StaffMemberService {

    StaffMemberResponse getProjects(String staffMember, int numberOfMonths, int numberOfHours);

    List<WorkgroupMember> getWorkgroup(int staffMemberId, int projectId);
}
