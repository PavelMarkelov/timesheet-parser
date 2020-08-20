package net.thumbtack.timesheetparser.dao_impl;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.dataBase.DataBase;
import net.thumbtack.timesheetparser.models.Project;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DeveloperProjectsDaoImpl implements DeveloperProjectsDao {

    private final DataBase database;

    public DeveloperProjectsDaoImpl(DataBase database) {
        this.database = database;
    }

    @Override
    public void save(String developerName, Project project) {
        database.addToDeveloperProjectsCollection(developerName, project);
    }

    @Override
    public boolean isStaffMemberExist(String staffMember) {
        return database.getDeveloperProjects().containsKey(staffMember);
    }

    @Override
    public List<Project> getStaffMemberProjects(String staffMember, int numberOfMonths) {
        LocalDate startTime = LocalDate.now().minusMonths(numberOfMonths);
        return (List<Project>) database.getDeveloperProjects().get(staffMember);
    }

    @Override
    public boolean isEmptyDatabase() {
        return database.getDeveloperProjects().isEmpty();
    }
}
