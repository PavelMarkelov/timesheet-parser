package net.thumbtack.timesheetparser.dao_impl;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.dataBase.DataBase;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import net.thumbtack.timesheetparser.models.WorkgroupMember;
import org.apache.commons.collections4.MapIterator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<StaffMember> getStaffMemberByName(String name) {
        return database.getDeveloperProjects().keySet().stream()
                .filter(staffMember -> staffMember.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Project> getStaffMemberProjects(StaffMember staffMember, int numberOfMonths, int numberOfHours) {
        LocalDate startTime = LocalDate.now().minusMonths(numberOfMonths);
        List<Project> projects = (List<Project>) database.getDeveloperProjects().get(staffMember);
        return projects.stream()
                .filter(project -> project.getEnd().compareTo(startTime) > 0 &&
                        project.getNumberOfHours() >= numberOfHours)
                .sorted(Comparator.comparing(Project::getStart).thenComparing(Project::getName))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmptyDatabase() {
        return database.getDeveloperProjects().isEmpty();
    }

    @Override
    public Optional<Project> getProject(StaffMember staffMember, int projectId) {
        return database.getDeveloperProjects().get(staffMember).stream()
                .filter(project -> project.getId() == projectId)
                .findFirst();
    }

    @Override
    public List<WorkgroupMember> getWorkgroup(StaffMember staffMemberModel, Project project, LocalDate start, LocalDate end) {
        List<WorkgroupMember> workgroupMembers = new ArrayList<>();
        MapIterator<StaffMember, Project> iterator = database.getDeveloperProjects().mapIterator();
        while (iterator.hasNext()) {
            StaffMember staffMember = iterator.next();
            Project projectFromDb = iterator.getValue();
            if (projectFromDb.getName().equalsIgnoreCase(project.getName())) {
                LocalDate startDate = projectFromDb.getStart();
                LocalDate endDate = projectFromDb.getEnd();
                int firstCompare = start.compareTo(endDate);
                int secondCompare = startDate.compareTo(end);
                if (firstCompare < 0 && secondCompare < 0) {
                    workgroupMembers.add(new WorkgroupMember(staffMember, startDate,
                            endDate, projectFromDb.getNumberOfHours()));
                }
            }
        }
        return workgroupMembers;
    }

    @Override
    public Optional<StaffMember> getStaffMemberById(int staffMemberId) {
        return database.getDeveloperProjects().keySet().stream()
                .filter(staffMember -> staffMember.getId() == staffMemberId)
                .findFirst();
    }
}
