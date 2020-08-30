package net.thumbtack.timesheetparser.controller;

import net.thumbtack.timesheetparser.database.Database;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(9, database.getDeveloperProjects().keySet().size());
    }
}