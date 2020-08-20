package net.thumbtack.timesheetparser.dataBase;

import net.thumbtack.timesheetparser.models.Project;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataBase {

    MultiValuedMap<String, Project> developerProjects = new ArrayListValuedHashMap<>();

    public void addToDeveloperProjectsCollection(String developer, Project project) {
        List<Project> projects = new ArrayList<>(developerProjects.get(developer));
        int id = projects.isEmpty() ? 1 : projects.size() + 1;
        project.setId(id);
        developerProjects.put(developer, project);
    }

    public MultiValuedMap<String, Project> getDeveloperProjects() {
        return developerProjects;
    }

    public void setDeveloperProjects(MultiValuedMap<String, Project> developerProjects) {
        this.developerProjects = developerProjects;
    }
}
