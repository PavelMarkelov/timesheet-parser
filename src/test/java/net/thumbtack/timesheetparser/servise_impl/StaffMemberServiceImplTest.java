package net.thumbtack.timesheetparser.servise_impl;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.dao_impl.DeveloperProjectsDaoImpl;
import net.thumbtack.timesheetparser.data.InitDatabase;
import net.thumbtack.timesheetparser.dto.response.StaffMemberResponse;
import net.thumbtack.timesheetparser.exception.FileNotLoadedException;
import net.thumbtack.timesheetparser.exception.ProjectNotFoundException;
import net.thumbtack.timesheetparser.exception.StaffMemberNotFoundException;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.WorkgroupMember;
import net.thumbtack.timesheetparser.service.StaffMemberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.timesheetparser.data.InitDatabase.project;
import static net.thumbtack.timesheetparser.data.InitDatabase.project1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class StaffMemberServiceImplTest {

    private static final DeveloperProjectsDao daoImpl = mock(DeveloperProjectsDaoImpl.class);
    private static final StaffMemberService service = new StaffMemberServiceImpl(daoImpl);
    private static final InitDatabase initDatabase = new InitDatabase();
    private static final String staffMember = initDatabase.staffMember.getName();
    private int numberOfMonths = 6;
    private int numberOfHours = 10;



    @BeforeAll
    static void initDb() {
        initDatabase.init();
    }

    @Test
    void getProjects() throws StaffMemberNotFoundException, FileNotLoadedException {
        when(daoImpl.getStaffMemberByName(staffMember))
                .thenReturn(Optional.of(initDatabase.staffMember));
        when((daoImpl.getStaffMemberProjects(initDatabase.staffMember, numberOfMonths, numberOfHours)))
                .thenAnswer(invocation -> {
                    var projects = (List<Project>) initDatabase.getData().get(invocation.getArgument(0));
                    projects.remove(2);
                    return projects;
                });
        when(daoImpl.isEmptyDatabase()).thenReturn(false);
        StaffMemberResponse response = service.getProjects(staffMember, numberOfMonths, numberOfHours);
        assertEquals(1, response.getId());
        assertEquals(2, response.getProjects().size());
    }

    @Test
    void failGetProjectsBecauseDbIsEmpty() {
        when(daoImpl.isEmptyDatabase()).thenReturn(true);
        assertThrows(FileNotLoadedException.class,
                () -> service.getProjects(staffMember, numberOfMonths, numberOfHours));
    }

    @Test
    void failGetProjectsBecauseStaffMemberNotFound() {
        when(daoImpl.isEmptyDatabase()).thenReturn(false);
        when(daoImpl.getStaffMemberByName(staffMember)).thenReturn(Optional.empty());
        assertThrows(StaffMemberNotFoundException.class,
                () -> service.getProjects(staffMember, numberOfMonths, numberOfHours));
    }

    @Test
    void getWorkgroup() throws ProjectNotFoundException, FileNotLoadedException, StaffMemberNotFoundException {
        Project project = new Project(1, project1);
        LocalDate start = project.getStart();
        LocalDate end = project.getEnd();
        when(daoImpl.isEmptyDatabase()).thenReturn(false);
        when(daoImpl.getStaffMemberById(2))
                .thenReturn(Optional.of(InitDatabase.staffMember1));
        when((daoImpl.getProject(InitDatabase.staffMember1, project.getId())))
                .thenReturn(Optional.of(project));
        List<WorkgroupMember> workGroup = new ArrayList<>();
        workGroup.add(new WorkgroupMember(InitDatabase.staffMember2, project));
        workGroup.add(new WorkgroupMember(InitDatabase.staffMember3, project));
        when(daoImpl.getWorkgroup(InitDatabase.staffMember1, project, start, end))
                .thenReturn(workGroup);
        List<WorkgroupMember> workgroup = service.getWorkgroup(
                InitDatabase.staffMember1.getId(), project.getId());
        assertEquals(2, workgroup.size());
        assertEquals(3, workgroup.get(0).getWorkgroupMember().getId());
        assertEquals(4, workgroup.get(1).getWorkgroupMember().getId());
    }

    @Test
    void failGetWorkgroupBecauseProjectNotFound() {
        when(daoImpl.isEmptyDatabase()).thenReturn(false);
        when(daoImpl.getStaffMemberById(InitDatabase.staffMember.getId())).thenReturn(Optional.of(InitDatabase.staffMember));
        when(daoImpl.getProject(InitDatabase.staffMember, project.getId())).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class,
                () -> service.getWorkgroup(InitDatabase.staffMember.getId(), project.getId()));
    }
}