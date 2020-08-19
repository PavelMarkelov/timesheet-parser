package net.thumbtack.timesheetparser.service;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import net.thumbtack.timesheetparser.models.Project;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

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
        Row headRow = sheet.getRow(firstRowNum);
        int projectNameColumnInd = -1;
        int staffMemberColumnInd = -1;
        int dateRowNumColumnInd = -1;
        for (Cell cell : headRow) {
            if (cell.getCellType().equals(CellType.STRING)) {
                String cellValue = cell.getRichStringCellValue().getString();
                if (cellValue.equals(PROJECT)) {
                    projectNameColumnInd = cell.getColumnIndex();
                }
                if (cellValue.equals(STAFF_MEMBER)) {
                    staffMemberColumnInd = cell.getColumnIndex();
                }
                if (cellValue.equals(DATE)) {
                    dateRowNumColumnInd = cell.getColumnIndex();
                }
            }
        }
        if (projectNameColumnInd == -1 || staffMemberColumnInd == -1 || dateRowNumColumnInd == -1) {
            throw new InvalidHeadRowException(ErrorCode.INV_HD_ROW.getErrorString());
        }
        String previousDeveloperName = "";
        LocalDate startDate = LocalDate.of(1, 1, 1);
        LocalDate endDate = LocalDate.of(1, 1, 1);
        for (int i = firstRowNum + 2; !sheet.getRow(i).getCell(0).getRichStringCellValue().getString().equals(END_DATA_MARKER); i++) {
            Row row = sheet.getRow(i);
            if (row.getLastCellNum() > 2) {
                String projectName = row.getCell(projectNameColumnInd).getCellType().equals(CellType.STRING) ?
                        row.getCell(projectNameColumnInd).getRichStringCellValue().getString() : "none";
                String developerName = row.getCell(staffMemberColumnInd).getCellType().equals(CellType.STRING) ?
                        row.getCell(staffMemberColumnInd).getRichStringCellValue().getString() : "none";
                if (!developerName.equals(previousDeveloperName)) {
                    if (!StringUtils.isEmpty(previousDeveloperName)) {
                        Project projectModel = new Project(projectName, startDate, endDate);
                        developerProjectsDao.save(previousDeveloperName, projectModel);
                    }
                    previousDeveloperName = developerName;
                    startDate = DateUtil.isCellDateFormatted(row.getCell(dateRowNumColumnInd)) ?
                            row.getCell(dateRowNumColumnInd).getLocalDateTimeCellValue().toLocalDate() :
                            LocalDate.of(0, 1, 1);
                } else {
                    endDate = DateUtil.isCellDateFormatted(row.getCell(dateRowNumColumnInd)) ?
                            row.getCell(dateRowNumColumnInd).getLocalDateTimeCellValue().toLocalDate() :
                            LocalDate.of(0, 1, 1);
                }
            }
        }
        System.out.println("Test");
    }
}
