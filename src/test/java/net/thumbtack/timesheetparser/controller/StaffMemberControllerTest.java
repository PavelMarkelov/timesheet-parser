package net.thumbtack.timesheetparser.controller;

import net.thumbtack.timesheetparser.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StaffMemberControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void buildMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void uploadFile() throws Exception {
        URL res = StaffMemberControllerTest.class.getClassLoader().getResource("test_data.xls");
        File xslFile = Paths.get(res.toURI()).toFile();
        FileInputStream fis = new FileInputStream(xslFile);
        MockMultipartFile file = new MockMultipartFile("file", xslFile.getName(),
                "application/vnd.ms-excel", fis);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/uploadXlsFile")
                .file(file))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getProjectsForFirstStaffMember() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .get("/staffMember?staffMemberName=Anna Belova&numberOfMonths=6&numberOfHours=0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        assertEquals("{\"id\":1,\"projects\":" +
                "[{\"id\":1,\"name\":\"Project #1 | Project#1:none\",\"start\":\"2020-07-15\"," +
                "\"end\":\"2020-08-21\",\"countOfTrackingRows\":24,\"numberOfHours\":36.25}]}", content);
    }

    @Test
    void getProjectsForNextStaffMember() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .get("/staffMember?staffMemberName=Inna Kins&numberOfMonths=6&numberOfHours=0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        assertEquals("{\"id\":4,\"projects\":" +
                "[{\"id\":1,\"name\":\"Project #1 | Project#1:none\",\"start\":\"2020-07-01\",\"end\":\"2020-08-24\",\"countOfTrackingRows\":36,\"numberOfHours\":216.75}," +
                "{\"id\":2,\"name\":\"Project #2 | Project #2:invisible\",\"start\":\"2020-07-16\",\"end\":\"2020-08-24\",\"countOfTrackingRows\":59,\"numberOfHours\":38.25}]}", content);

        response = mockMvc.perform(MockMvcRequestBuilders
                .get("/staffMember?staffMemberName=Inna Kins&numberOfMonths=6&numberOfHours=50")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        content = response.getContentAsString();
        assertEquals("{\"id\":4,\"projects\":" +
                "[{\"id\":1,\"name\":\"Project #1 | Project#1:none\"," +
                "\"start\":\"2020-07-01\",\"end\":\"2020-08-24\",\"countOfTrackingRows\":36," +
                "\"numberOfHours\":216.75}]}", content);
    }

    @Test
    void getProjectsForLastStaffMember() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .get("/staffMember?staffMemberName=Maxim Numanov&numberOfMonths=6&numberOfHours=0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        assertEquals("{\"id\":9,\"projects\":[{\"id\":1,\"name\":" +
                "\"Project #3 | Project #3:hidden\",\"start\":\"2020-07-01\"," +
                "\"end\":\"2020-08-21\",\"countOfTrackingRows\":24," +
                "\"numberOfHours\":97.0}]}", content);
    }

    @Test
    void failGetProjectsBecauseStaffMemberNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/staffMember?staffMemberName=Chuck Norris&numberOfMonths=6&numberOfHours=0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].errorCode").value(ErrorCode.STAFF_MEMBER_NOT_FOUND.name()));
    }


    @Test
    void getWorkgroupForFirstStaffMember() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .get("/staffMember/workgroup?staffMemberId=1&projectId=1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        assertEquals("[{\"workgroupMember\":" +
                "{\"id\":4,\"name\":\"Inna Kins\"},\"start\":\"2020-07-01\",\"end\":\"2020-08-24\",\"numberOfHours\":216.75}," +
                "{\"workgroupMember\":" +
                "{\"id\":3,\"name\":\"John Tailor\"},\"start\":\"2020-07-01\",\"end\":\"2020-08-21\",\"numberOfHours\":150.0}]", content);
    }

    @Test
    void getWorkgroupForSecondStaffMember() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .get("/staffMember/workgroup?staffMemberId=4&projectId=2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        assertEquals("[{\"workgroupMember\":" +
                "{\"id\":6,\"name\":\"Jessica Liskov\"},\"start\":\"2020-07-01\",\"end\":\"2020-08-24\",\"numberOfHours\":241.25}," +
                "{\"workgroupMember\":" +
                "{\"id\":5,\"name\":\"Petr Petrov\"},\"start\":\"2020-07-01\",\"end\":\"2020-08-20\",\"numberOfHours\":32.0}]", content);
    }
}