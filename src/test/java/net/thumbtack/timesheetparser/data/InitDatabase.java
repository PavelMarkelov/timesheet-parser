package net.thumbtack.timesheetparser.data;

import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.time.LocalDate;

public class InitDatabase {
    private final MultiValuedMap<StaffMember, Project> database = new ArrayListValuedHashMap<>();

    public static final StaffMember staffMember = new StaffMember(1, "Anna Frolova");
    public static final StaffMember staffMember1 = new StaffMember(2, "Denis Petrov");
    public static final StaffMember staffMember2 = new StaffMember(3, "Ivan Ivanov");
    public static final StaffMember staffMember3 = new StaffMember(4, "John Tailor");

    public static final LocalDate startDate = LocalDate.now().minusMonths(4);
    public static final LocalDate endDate = LocalDate.now();

    public static final Project project = new Project(10,
            "Project without name #1", startDate, endDate, 25.0);
    public static final Project project1 = new Project(43,
            "Unknown name #1", startDate, endDate, 42.0);
    public static final Project project2 = new Project(72,
            "Project without name #2", startDate, endDate, 98.0);
    public static final Project project3 = new Project(3,
            "None", startDate, endDate.minusMonths(3), 1.5);


    public MultiValuedMap<StaffMember, Project> getData() {
        return database;
    }

    public void init() {

        database.put(staffMember, new Project(1, project3));
        database.put(staffMember, new Project(2, project));
        database.put(staffMember3, new Project(1, project));
        database.put(staffMember2, new Project(1, project));

        database.put(staffMember1, new Project(1, project1));
        database.put(staffMember2, new Project(2, project1));
        database.put(staffMember3, new Project(2, project1));

        database.put(staffMember3, new Project(3, project2));
        database.put(staffMember2, new Project(3, project2));
        Project project = new Project(3, project2);
        project.setStart(LocalDate.now().minusMonths(6));
        project.setEnd(LocalDate.now().minusMonths(5));
        database.put(staffMember, new Project(3, project));
    }
}
