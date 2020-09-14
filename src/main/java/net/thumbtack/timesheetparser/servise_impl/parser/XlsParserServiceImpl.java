package net.thumbtack.timesheetparser.servise_impl.parser;

import java.util.InvalidPropertiesFormatException;
import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.service.XlsParserService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class XlsParserServiceImpl implements XlsParserService {

    private final DeveloperProjectsDao developerProjectsDao;

    public XlsParserServiceImpl(DeveloperProjectsDao developerProjectsDao) {
        this.developerProjectsDao = developerProjectsDao;
    }

    @Override
    public void parseXls(MultipartFile file) throws IOException, InvalidHeadRowException {
        developerProjectsDao.clearDatabase();

        var workbook = WorkbookFactory.create(file.getInputStream());
        var sheet = workbook.getSheetAt(0);
        var firstRowNum = sheet.getFirstRowNum();

        final Row header = sheet.getRow(firstRowNum);
        final RowDataReader dataReader = RowDataReader.createFromHeader(header);

        // Current project / current developer
        var previousDeveloperName = "";
        var projectName = "";
        var previousProjectName = "";
        var developerName = "";
        var startDate = LocalDate.of(1, 1, 1);
        var endDate = LocalDate.of(1, 1, 1);
        var numberRowsForCurrentDeveloperInCurrentProject = 0;
        var numberOfHours = 0.0;

        for (int i = firstRowNum + 2; sheet.getRow(i) != null; i++) {

            try {
                // If validation failed then this method will throw Exception
                final RowData data = dataReader.readData(sheet.getRow(i));

                projectName = data.getProjectName();
                developerName = data.getStaffMember();

                // if this a new developer
                if (!developerName.equals(previousDeveloperName)) {
                    // if there was a developer
                    if (!StringUtils.isEmpty(previousDeveloperName)) {
                        // TODO: Save current developer to database
                        if (numberRowsForCurrentDeveloperInCurrentProject == 1) {
                            endDate = startDate;
                        }
                        Project projectModel = new Project(numberRowsForCurrentDeveloperInCurrentProject,
                            previousProjectName, startDate, endDate, numberOfHours);
                        developerProjectsDao.save(previousDeveloperName, projectModel);
                        numberRowsForCurrentDeveloperInCurrentProject = 0;
                        numberOfHours = 0;
                    }
                    // TODO: Create a new developer
                    previousProjectName = projectName;
                    previousDeveloperName = developerName;
                    startDate = data.getDate();
                }

                // TODO: Write data for the current developer
                endDate = data.getDate();
                numberRowsForCurrentDeveloperInCurrentProject++;
                numberOfHours += data.getNumberOfHours();
            } catch (final InvalidPropertiesFormatException e) {
                if (numberRowsForCurrentDeveloperInCurrentProject == 1) {
                    endDate = startDate;
                }
                Project projectModel = new Project(numberRowsForCurrentDeveloperInCurrentProject,
                    projectName, startDate, endDate, numberOfHours);
                developerProjectsDao.save(developerName, projectModel);
            }
        }
    }
}
