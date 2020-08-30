package net.thumbtack.timesheetparser;

import net.thumbtack.timesheetparser.database.Database;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;

@Component
@Profile("test")
public class DatabaseImplForE2eTests implements Database {

    private final MultiValuedMap<StaffMember, Project> developerProjects = new ArrayListValuedHashMap<>();
    private int idForDeveloperProjects = 0;

    @Override
    public void addToDeveloperProjectsCollection(String developer, Project project) {
        var staffMember = new StaffMember(idForDeveloperProjects, developer);
        if (!developerProjects.containsKey(staffMember)) {
            idForDeveloperProjects++;
            staffMember.setId(idForDeveloperProjects);
        }
        var projects = new ArrayList<>(developerProjects.get(staffMember));
        var projectId = projects.isEmpty() ? 1 : projects.size() + 1;
        project.setId(projectId);
        developerProjects.put(staffMember, project);
    }

    @Override
    public MultiValuedMap<StaffMember, Project> getDeveloperProjects() {
        return developerProjects;
    }

    @Override
    public void clearData() {
        idForDeveloperProjects = 0;
        developerProjects.clear();
    }
}
