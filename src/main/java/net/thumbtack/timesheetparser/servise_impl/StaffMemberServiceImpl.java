package net.thumbtack.timesheetparser.servise_impl;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.FileNotLoadedException;
import net.thumbtack.timesheetparser.exception.StaffMemberNotFoundException;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.service.StaffMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffMemberServiceImpl implements StaffMemberService {

    private final DeveloperProjectsDao developerProjectsDao;


    public StaffMemberServiceImpl(DeveloperProjectsDao developerProjectsDao) {
        this.developerProjectsDao = developerProjectsDao;
    }

    @Override
    public List<Project> getProjects(String staffMember, int numberOfMonths) {
        if(developerProjectsDao.isEmptyDatabase()) {
            throw new FileNotLoadedException(ErrorCode.FILE_N_LOAD.getErrorString());
        }
        if (!developerProjectsDao.isStaffMemberExist(staffMember)) {
            throw new StaffMemberNotFoundException(ErrorCode.STAFF_MEM_NOT_F.getErrorString());
        }
        List<Project> projects = developerProjectsDao.getStaffMemberProjects(staffMember, numberOfMonths);
        return projects;
    }
}
