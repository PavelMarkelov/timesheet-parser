package net.thumbtack.timesheetparser.servise_impl;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.service.XlsParserService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class XlsParserServiceImpl implements XlsParserService {

    private static String PROJECT = "Project";
    private static String STAFF_MEMBER = "Staff Member";
    private static String DATE = "Date";
    private static String INPUT = "Input";

    private final DeveloperProjectsDao developerProjectsDao;

    public XlsParserServiceImpl(DeveloperProjectsDao developerProjectsDao) {
        this.developerProjectsDao = developerProjectsDao;
    }

    @Override
    public void parseXls(MultipartFile file) throws IOException {
        var workbook = WorkbookFactory.create(file.getInputStream());
        var sheet = workbook.getSheetAt(0);
        var firstRowNum = sheet.getFirstRowNum();
        var columnsIndexes = getColumnsIndexes(sheet.getRow(firstRowNum));
        developerProjectsDao.clearDatabase();
        var projectNameColumnInd = columnsIndexes[0];
        var staffMemberColumnInd = columnsIndexes[1];
        var dateColumnInd = columnsIndexes[2];
        var numberOfHoursColumnInd = columnsIndexes[3];
        var previousDeveloperName = "";
        var projectName = "";
        var previousProjectName = "";
        var developerName = "";
        var startDate = LocalDate.of(1, 1, 1);
        var endDate = LocalDate.of(1, 1, 1);
        var numberRowsForCurrentDeveloperInCurrentProject = 0;
        var numberOfHours = 0.0;
        for (int i = firstRowNum + 2; sheet.getRow(i) != null; i++) {
            var row = sheet.getRow(i);
            var projectNameCellOpt = Optional.ofNullable(row.getCell(projectNameColumnInd));
            var developerNameCellOpt = Optional.ofNullable(row.getCell(staffMemberColumnInd));
            var dateCellOpt = Optional.ofNullable(row.getCell(dateColumnInd));
            var numberOfHoursOpt = Optional.ofNullable(row.getCell(numberOfHoursColumnInd));
            var isPresentData = projectNameCellOpt.isPresent() && developerNameCellOpt.isPresent() &&
                    dateCellOpt.isPresent() && numberOfHoursOpt.isPresent();
            if (isPresentData && projectNameCellOpt.get().getCellType().equals(CellType.STRING) &&
                    developerNameCellOpt.get().getCellType().equals(CellType.STRING) &&
                    DateUtil.isCellDateFormatted(dateCellOpt.get()) &&
                    numberOfHoursOpt.get().getCellType().equals(CellType.NUMERIC)) {
                projectName = projectNameCellOpt.get().getRichStringCellValue().getString();
                developerName = developerNameCellOpt.get().getRichStringCellValue().getString();
                if (!developerName.equals(previousDeveloperName)) {
                    if (!StringUtils.isEmpty(previousDeveloperName)) {
                        if (numberRowsForCurrentDeveloperInCurrentProject == 1) {
                            endDate = startDate;
                        }
                        Project projectModel = new Project(numberRowsForCurrentDeveloperInCurrentProject,
                                previousProjectName, startDate, endDate, numberOfHours);
                        developerProjectsDao.save(previousDeveloperName, projectModel);
                        numberRowsForCurrentDeveloperInCurrentProject = 0;
                        numberOfHours = 0;
                    }
                    previousProjectName = projectName;
                    previousDeveloperName = developerName;
                    startDate = dateCellOpt.get().getLocalDateTimeCellValue().toLocalDate();
                }
                endDate = dateCellOpt.get().getLocalDateTimeCellValue().toLocalDate();
                numberRowsForCurrentDeveloperInCurrentProject++;
                numberOfHours += numberOfHoursOpt.get().getNumericCellValue();
            } else if (sheet.getRow(i + 1) == null) {
                if (numberRowsForCurrentDeveloperInCurrentProject == 1) {
                    endDate = startDate;
                }
                Project projectModel = new Project(numberRowsForCurrentDeveloperInCurrentProject,
                        projectName, startDate, endDate, numberOfHours);
                developerProjectsDao.save(developerName, projectModel);
                break;
            }
        }
    }

    private int[] getColumnsIndexes(Row headRow) {
        var projectNameColumnInd = -1;
        var staffMemberColumnInd = -1;
        var dateColumnInd = -1;
        var numberOfHoursColumnInd = -1;
        for (Cell cell : headRow) {
            if (cell.getCellType().equals(CellType.STRING)) {
                String cellValue = cell.getRichStringCellValue().getString();
                if (cellValue.equalsIgnoreCase(PROJECT)) {
                    projectNameColumnInd = cell.getColumnIndex();
                }
                if (cellValue.equalsIgnoreCase(STAFF_MEMBER)) {
                    staffMemberColumnInd = cell.getColumnIndex();
                }
                if (cellValue.equalsIgnoreCase(DATE)) {
                    dateColumnInd = cell.getColumnIndex();
                }
                if (cellValue.equalsIgnoreCase(INPUT)) {
                    numberOfHoursColumnInd = cell.getColumnIndex();
                }
            }
        }
        if (projectNameColumnInd == -1 || staffMemberColumnInd == -1 || dateColumnInd == -1 || numberOfHoursColumnInd == -1) {
            throw new InvalidHeadRowException(ErrorCode.INVALID_HEAD_ROW.getErrorString());
        }
        return new int[]{projectNameColumnInd, staffMemberColumnInd, dateColumnInd, numberOfHoursColumnInd};
    }
}
