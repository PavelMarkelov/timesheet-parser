package net.thumbtack.timesheetparser.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface XlsParserService {
    void parseXls(MultipartFile file) throws IOException;
}
