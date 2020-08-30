package net.thumbtack.timesheetparser.dao_impl;

import net.thumbtack.timesheetparser.data.InitDatabase;
import net.thumbtack.timesheetparser.database.DatabaseImpl;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.WorkgroupMember;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


class DeveloperProjectsDaoImplTest {

    private static final InitDatabase initDatabase = new InitDatabase();
    private static final DatabaseImpl database = mock(DatabaseImpl.class);
    private static final DeveloperProjectsDaoImpl daoImpl = new DeveloperProjectsDaoImpl(database);

    @BeforeAll
    static void initDb() {
        initDatabase.init();
    }


    @Test
    void getStaffMemberProjects() {
        when(database.getDeveloperProjects())
                .thenReturn(initDatabase.getData());
        List<Project> staffMemberProjects = daoImpl.getStaffMemberProjects(InitDatabase.staffMember, 6, 10);
        assertEquals(2, staffMemberProjects.size());
        assertEquals(3, staffMemberProjects.get(0).getId());
        assertEquals(2, staffMemberProjects.get(1).getId());

        staffMemberProjects = daoImpl.getStaffMemberProjects(InitDatabase.staffMember, 6, 0);
        assertEquals(3, staffMemberProjects.size());
        assertEquals(3, staffMemberProjects.get(0).getId());
        assertEquals(1, staffMemberProjects.get(1).getId());
        assertEquals(2, staffMemberProjects.get(2).getId());

        staffMemberProjects = daoImpl.getStaffMemberProjects(InitDatabase.staffMember, 3, 10);
        assertEquals(1, staffMemberProjects.size());
        assertEquals(2, staffMemberProjects.get(0).getId());
    }

    @Test
    void getWorkgroup() {
        when(database.getDeveloperProjects())
                .thenReturn(initDatabase.getData());
        List<WorkgroupMember> workgroup = daoImpl.getWorkgroup(InitDatabase.staffMember2,
                InitDatabase.project1, InitDatabase.startDate, InitDatabase.endDate);
        assertEquals(2, workgroup.size());
        assertEquals(2, workgroup.get(0).getWorkgroupMember().getId());
        assertEquals(4, workgroup.get(1).getWorkgroupMember().getId());

        workgroup = daoImpl.getWorkgroup(InitDatabase.staffMember3,
                InitDatabase.project2, InitDatabase.startDate, InitDatabase.endDate);
        assertEquals(1, workgroup.size());
        assertEquals(3, workgroup.get(0).getWorkgroupMember().getId());
    }
}