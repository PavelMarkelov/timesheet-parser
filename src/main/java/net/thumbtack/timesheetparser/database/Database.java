package net.thumbtack.timesheetparser.database;

import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@SessionScope
public class Database {

    private final MultiValuedMap<StaffMember, Project> developerProjects = new ArrayListValuedHashMap<>();
    private int idForDeveloperProjects = 0;

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

    public MultiValuedMap<StaffMember, Project> getDeveloperProjects() {
        return developerProjects;
    }
}
