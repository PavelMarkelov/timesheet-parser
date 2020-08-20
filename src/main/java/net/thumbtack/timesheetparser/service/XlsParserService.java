package net.thumbtack.timesheetparser.service;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import net.thumbtack.timesheetparser.models.Project;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class XlsParserService {

    private static String PROJECT = "Project";
    private static String STAFF_MEMBER = "Staff Member";
    private static String DATE = "Date";
    private static String END_DATA_MARKER = "OVERALL TOTALS";

    private final DeveloperProjectsDao developerProjectsDao;

    public XlsParserService(DeveloperProjectsDao developerProjectsDao) {
        this.developerProjectsDao = developerProjectsDao;
    }

    public void parseXls(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int[] columnsIndexes = getColumnsIndexes(sheet.getRow(firstRowNum));
        int projectNameColumnInd = columnsIndexes[0];
        int staffMemberColumnInd = columnsIndexes[1];
        int dateColumnInd = columnsIndexes[2];
        String previousDeveloperName = "";
        String projectName = "";
        String previousProjectName = "";
        String developerName = "";
        LocalDate startDate = LocalDate.of(1, 1, 1);
        LocalDate endDate = LocalDate.of(1, 1, 1);
        int numberRowsForCurrentDeveloperInCurrentProject = 0;
        for (int i = firstRowNum + 2; sheet.getRow(i) != null; i++) {
            Row row = sheet.getRow(i);
            Optional<Cell> projectNameCellOpt = Optional.ofNullable(row.getCell(projectNameColumnInd));
            Optional<Cell> developerNameCellOpt = Optional.ofNullable(row.getCell(staffMemberColumnInd));
            Optional<Cell> dateCellOpt = Optional.ofNullable(row.getCell(dateColumnInd));
            boolean isPresentData = projectNameCellOpt.isPresent() && developerNameCellOpt.isPresent() &&
                    dateCellOpt.isPresent();
            if (isPresentData && projectNameCellOpt.get().getCellType().equals(CellType.STRING) &&
                    developerNameCellOpt.get().getCellType().equals(CellType.STRING) &&
                    DateUtil.isCellDateFormatted(dateCellOpt.get())) {
                numberRowsForCurrentDeveloperInCurrentProject++;
                projectName = projectNameCellOpt.get().getRichStringCellValue().getString();
                developerName = developerNameCellOpt.get().getRichStringCellValue().getString();
                if (!developerName.equals(previousDeveloperName)) {
                    if (!StringUtils.isEmpty(previousDeveloperName)) {
                        if (numberRowsForCurrentDeveloperInCurrentProject == 1) {
                            endDate = startDate;
                        }
                        Project projectModel = new Project(numberRowsForCurrentDeveloperInCurrentProject,
                                previousProjectName, startDate, endDate);
                        developerProjectsDao.save(previousDeveloperName, projectModel);
                        numberRowsForCurrentDeveloperInCurrentProject = 0;
                    }
                    previousProjectName = projectName;
                    previousDeveloperName = developerName;
                    startDate = dateCellOpt.get().getLocalDateTimeCellValue().toLocalDate();
                } else {
                    endDate = dateCellOpt.get().getLocalDateTimeCellValue().toLocalDate();
                }
            } else if (sheet.getRow(i).getCell(0).getRichStringCellValue().getString()
                    .equalsIgnoreCase(END_DATA_MARKER)) {
                if (numberRowsForCurrentDeveloperInCurrentProject == 1) {
                    endDate = startDate;
                }
                Project projectModel = new Project(numberRowsForCurrentDeveloperInCurrentProject,
                        projectName, startDate, endDate);
                developerProjectsDao.save(developerName, projectModel);
                break;
            }
        }
        System.out.println("test");
    }

    private int[] getColumnsIndexes(Row headRow) {
        int projectNameColumnInd = -1;
        int staffMemberColumnInd = -1;
        int dateColumnInd = -1;
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
            }
        }
        if (projectNameColumnInd == -1 || staffMemberColumnInd == -1 || dateColumnInd == -1) {
            throw new InvalidHeadRowException(ErrorCode.INV_HD_ROW.getErrorString());
        }
        return new int[]{projectNameColumnInd, staffMemberColumnInd, dateColumnInd};
    }
}
