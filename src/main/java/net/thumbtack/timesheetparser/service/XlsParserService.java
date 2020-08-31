package net.thumbtack.timesheetparser.service;

import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface XlsParserService {
    void parseXls(MultipartFile file) throws IOException, InvalidHeadRowException;
}
