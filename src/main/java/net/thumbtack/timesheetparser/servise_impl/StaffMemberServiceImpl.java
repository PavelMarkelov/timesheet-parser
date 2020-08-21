package net.thumbtack.timesheetparser.servise_impl;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.dto.response.StaffMemberResponse;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.FileNotLoadedException;
import net.thumbtack.timesheetparser.exception.ProjectNotFoundException;
import net.thumbtack.timesheetparser.exception.StaffMemberNotFoundException;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import net.thumbtack.timesheetparser.models.WorkgroupMember;
import net.thumbtack.timesheetparser.service.StaffMemberService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class StaffMemberServiceImpl implements StaffMemberService {

    private final DeveloperProjectsDao developerProjectsDao;


    public StaffMemberServiceImpl(DeveloperProjectsDao developerProjectsDao) {
        this.developerProjectsDao = developerProjectsDao;
    }

    @Override
    public StaffMemberResponse getProjects(String staffMember, int numberOfMonths, int numberOfHours) {
        if(developerProjectsDao.isEmptyDatabase()) {
            throw new FileNotLoadedException(ErrorCode.FILE_N_LOAD.getErrorString());
        }
        Optional<StaffMember> staffMemberByNameOpt = developerProjectsDao.getStaffMemberByName(staffMember);
        if (staffMemberByNameOpt.isEmpty()) {
            throw new StaffMemberNotFoundException(ErrorCode.STAFF_MEM_NOT_F.getErrorString());
        }
        StaffMember staffMemberModel = staffMemberByNameOpt.get();
        List<Project> staffMemberProjects = developerProjectsDao.getStaffMemberProjects(staffMemberModel, numberOfMonths, numberOfHours);
        return new StaffMemberResponse(staffMemberModel.getId(), staffMemberProjects);
    }

    @Override
    public List<WorkgroupMember> getWorkgroup(int staffMemberId, int projectId) {
        if(developerProjectsDao.isEmptyDatabase()) {
            throw new FileNotLoadedException(ErrorCode.FILE_N_LOAD.getErrorString());
        }
        Optional<StaffMember> staffMemberOpt = developerProjectsDao.getStaffMemberById(staffMemberId);
        if (staffMemberOpt.isEmpty()) {
            throw new StaffMemberNotFoundException(ErrorCode.STAFF_MEM_NOT_F.getErrorString());
        }
        StaffMember staffMember = staffMemberOpt.get();
        Optional<Project> projectOpt = developerProjectsDao.getProject(staffMember, projectId);
        if (projectOpt.isEmpty()) {
            throw new ProjectNotFoundException(ErrorCode.PR_NOT_F.getErrorString());
        }
        Project project = projectOpt.get();
        LocalDate startDateForRequest = project.getStart();
        LocalDate endDateForRequest = project.getEnd();
        Period period = Period.between(startDateForRequest, endDateForRequest);
        if (period.getDays() < 3) {
            startDateForRequest = startDateForRequest.minusDays(5);
            endDateForRequest = endDateForRequest.plusDays(5);
        }
        return developerProjectsDao.getWorkgroup(staffMember, project, startDateForRequest, endDateForRequest);
    }
}
