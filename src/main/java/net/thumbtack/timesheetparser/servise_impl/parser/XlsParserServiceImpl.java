package net.thumbtack.timesheetparser.servise_impl.parser;

import java.util.InvalidPropertiesFormatException;
import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import net.thumbtack.timesheetparser.service.XlsParserService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

        Developer developer = null;

        for (int i = firstRowNum + 2; sheet.getRow(i) != null; i++) {
            try {
                final RowData data = dataReader.readData(sheet.getRow(i));
                final String developerName = data.getStaffMember();
                final boolean isNewDeveloper = developer == null || !developerName.equals(developer.getName());

                if (isNewDeveloper) {
                    saveDeveloper(developer);
                    developer = new Developer(developerName, data.getProjectName(), data.getDate());
                }

                // Update current developer
                developer.setEndDate(data.getDate());
                developer.addHours(data.getNumberOfHours());

            } catch (final InvalidPropertiesFormatException e) {
                saveDeveloper(developer);
            }
        }
    }

    private void saveDeveloper(final Developer developer) {
        // TODO: Optional.ofNullable(developer).ifPresent(developerProjectsDao::save);
    }
}
