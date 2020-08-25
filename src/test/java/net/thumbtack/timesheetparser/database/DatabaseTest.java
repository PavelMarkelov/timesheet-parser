package net.thumbtack.timesheetparser.database;

import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;

import java.time.LocalDate;
import java.util.List;
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
                "Project without name #1", startDate, endDate, 25.0);
        var project1 = new Project(43,
                "Unknown name #1", startDate, endDate, 42.0);
        var project2 = new Project(72,
                "Project without name #2", startDate, endDate, 98.0);
        var project3 = new Project(109,
                "Unknown name #2", startDate, endDate, 125);
        database.addToDeveloperProjectsCollection(developer, new Project(project));
        database.addToDeveloperProjectsCollection(developer1, new Project(project1));
        database.addToDeveloperProjectsCollection(developer2, new Project(project2));
        database.addToDeveloperProjectsCollection(developer, new Project(project3));
        database.addToDeveloperProjectsCollection(developer, new Project(project1));
        database.addToDeveloperProjectsCollection(developer2, new Project(project3));
        var collection = database.getDeveloperProjects();
        var staffMember = new StaffMember(0, developer);
        var developerFromDbAsList = collection.keySet().stream()
                .filter(value -> value.equals(staffMember))
                .collect(Collectors.toList());
        assertEquals(1, developerFromDbAsList.size());
        assertEquals(1, developerFromDbAsList.get(0).getId());
        var projectsOfDeveloper = (List<Project>) collection.get(staffMember);
        assertEquals(3, projectsOfDeveloper.size());
        assertEquals(1, projectsOfDeveloper.get(0).getId());
        assertEquals(project.getName(), projectsOfDeveloper.get(0).getName());
        assertEquals(2, projectsOfDeveloper.get(1).getId());
        assertEquals(3, projectsOfDeveloper.get(2).getId());

        staffMember.setName(developer1);
        var developerFromDbOpt = collection.keySet().stream()
                .filter(value -> value.equals(staffMember))
                .findFirst();
        assertTrue(developerFromDbOpt.isPresent());
        var developerFromDb = developerFromDbOpt.get();
        assertEquals(2, developerFromDb.getId());
        projectsOfDeveloper = (List<Project>) collection.get(developerFromDb);
        assertEquals(1, projectsOfDeveloper.size());
        assertEquals(project1.getName(), projectsOfDeveloper.get(0).getName());
        assertEquals(1, projectsOfDeveloper.get(0).getId());

        staffMember.setName(developer2);
        developerFromDbOpt = collection.keySet().stream()
                .filter(value -> value.equals(staffMember))
                .findFirst();
        assertTrue(developerFromDbOpt.isPresent());
        developerFromDb = developerFromDbOpt.get();
        assertEquals(3, developerFromDb.getId());
        projectsOfDeveloper = (List<Project>) collection.get(developerFromDb);
        assertEquals(2, projectsOfDeveloper.size());
        assertEquals(project2.getName(), projectsOfDeveloper.get(0).getName());
        assertEquals(1, projectsOfDeveloper.get(0).getId());
        assertEquals(project3.getName(), projectsOfDeveloper.get(1).getName());
        assertEquals(2, projectsOfDeveloper.get(1).getId());
    }
}