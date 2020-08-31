package net.thumbtack.timesheetparser.controller;

import net.thumbtack.timesheetparser.database.Database;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.models.StaffMember;
import org.apache.commons.collections4.MultiValuedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class XlsFileControllerTest {

    @Autowired
    private Database database;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void buildMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void uploadFile() throws Exception {
        URL res = getClass().getClassLoader().getResource("test_data.xls");
        File xslFile = Paths.get(res.toURI()).toFile();
        FileInputStream fis = new FileInputStream(xslFile);
        MockMultipartFile file = new MockMultipartFile("file", xslFile.getName(),
                "application/vnd.ms-excel", fis);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/uploadXlsFile")
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value(xslFile.getName()))
                .andExpect(jsonPath("$.fileType").value("application/vnd.ms-excel"))
                .andExpect(jsonPath("$.size").value(xslFile.length()));

        mockMvc.perform(multipart("/uploadXlsFile")
                .file(file))
                .andDo(print())
                .andExpect(status().isOk());


        MultiValuedMap<StaffMember, Project> developerProjects = database.getDeveloperProjects();
        assertEquals(9, developerProjects.keySet().size());
        var projects = (List<Project>) developerProjects.get(new StaffMember(0, "Anna Belova"));
        assertEquals(1, projects.size());
        assertEquals(1, projects.get(0).getId());
        assertEquals("Project #1 | Project#1:none", projects.get(0).getName());
        assertEquals(LocalDate.of(2020, 7, 15), projects.get(0).getStart());
        assertEquals(LocalDate.of(2020, 8, 21), projects.get(0).getEnd());
        assertEquals(24, projects.get(0).getCountOfTrackingRows());
        assertEquals(36.25, projects.get(0).getNumberOfHours());


        projects = (List<Project>) developerProjects.get(new StaffMember(0, "Inna Kins"));
        assertEquals(2, projects.size());
        assertEquals(1, projects.get(0).getId());
        assertEquals("Project #1 | Project#1:none", projects.get(0).getName());
        assertEquals(LocalDate.of(2020, 7, 1), projects.get(0).getStart());
        assertEquals(LocalDate.of(2020, 8, 24), projects.get(0).getEnd());
        assertEquals(216.75, projects.get(0).getNumberOfHours());

        assertEquals(2, projects.get(1).getId());
        assertEquals("Project #2 | Project #2:invisible", projects.get(1).getName());
        assertEquals(LocalDate.of(2020, 7, 16), projects.get(1).getStart());
        assertEquals(LocalDate.of(2020, 8, 24), projects.get(1).getEnd());
        assertEquals(38.25, projects.get(1).getNumberOfHours());


        projects = (List<Project>) developerProjects.get(new StaffMember(0, "Maxim Numanov"));
        assertEquals(1, projects.size());
        assertEquals(1, projects.get(0).getId());
        assertEquals("Project #3 | Project #3:hidden", projects.get(0).getName());
        assertEquals(LocalDate.of(2020, 7, 1), projects.get(0).getStart());
        assertEquals(LocalDate.of(2020, 8, 21), projects.get(0).getEnd());
        assertEquals(97.0, projects.get(0).getNumberOfHours());
    }

    @Test
    void failUploadFileBecauseInvalidContentType() throws Exception {
        URL res = getClass().getClassLoader().getResource("test_data.xls");
        File xslFile = Paths.get(res.toURI()).toFile();
        FileInputStream fis = new FileInputStream(xslFile);
        MockMultipartFile file = new MockMultipartFile("file", xslFile.getName(),
                "text-plain", fis);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/uploadXlsFile")
                .file(file))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].errorCode").value(ErrorCode.UNSUPPORTED_FORMAT.name()));
    }
}