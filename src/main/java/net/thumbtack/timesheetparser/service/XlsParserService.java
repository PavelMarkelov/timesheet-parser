package net.thumbtack.timesheetparser.service;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class XlsParserService {

    private final DeveloperProjectsDao developerProjectsDao;

    public XlsParserService(DeveloperProjectsDao developerProjectsDao) {
        this.developerProjectsDao = developerProjectsDao;
    }

    public void parseXls(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        AtomicInteger numRow = new AtomicInteger();
        Row headRow = sheet.getRow(sheet.getFirstRowNum());
        for (Cell cell : headRow) {
            if (!cell.getCellType().equals(CellType.STRING)) {

            }
        }
        for (Row row : sheet) {

        }
    }
}
