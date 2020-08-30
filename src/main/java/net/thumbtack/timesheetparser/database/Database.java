package net.thumbtack.timesheetparser.database;

import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import org.apache.commons.collections4.MultiValuedMap;

public interface Database {
    void addToDeveloperProjectsCollection(String developer, Project project);

    MultiValuedMap<StaffMember, Project> getDeveloperProjects();

    void clearData();
}
