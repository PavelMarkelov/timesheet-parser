package net.thumbtack.timesheetparser.database;

import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;

import java.time.LocalDate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    Database database = new Database();

    @Test
    void addToDeveloperProjectsCollection() {
        var developer = "Denis Petrov";
        var developer1 = "Ivan Ivanov";
        var developer2 = "John Tailor";
        var startDate = LocalDate.of(2020, 6, 20);
        var endDate = LocalDate.of(2020, 10, 5);
        var project = new Project(10,
                "Project without name", startDate, endDate, 25.0);
        var project1 = new Project(43,
                "Unknown name", startDate, endDate, 42.0);
        var project2 = new Project(72,
                "Project without name", startDate, endDate, 98.0);
        var project3 = new Project(109,
                "Unknown name", startDate, endDate, 125);
        database.addToDeveloperProjectsCollection(developer, project);
        database.addToDeveloperProjectsCollection(developer1, project1);
        database.addToDeveloperProjectsCollection(developer2, project2);
        database.addToDeveloperProjectsCollection(developer, project3);
        database.addToDeveloperProjectsCollection(developer, project1);
        database.addToDeveloperProjectsCollection(developer2, project3);
        var collection = database.getDeveloperProjects();
        var staffMember = new StaffMember(0, developer);
        assertTrue(collection.containsValue(staffMember));
        var developerFromDb = collection.keySet().stream()
                .filter(value -> value.equals(staffMember))
                .count();
        assertEquals(1, developerFromDb);
        assertEquals(1, staffMember.getId());
        var projectsOfDeveloper = collection.get(staffMember);
        assertEquals(3, projectsOfDeveloper.size());
        assertEquals(1, project.getId());
        assertEquals(2, project3.getId());
        assertEquals(3, project1.getId());
    }
}