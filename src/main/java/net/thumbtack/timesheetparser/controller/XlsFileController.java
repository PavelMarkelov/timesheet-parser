package net.thumbtack.timesheetparser.controller;

import net.thumbtack.timesheetparser.dto.response.UploadFileResponse;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import net.thumbtack.timesheetparser.exception.dto.ValidationErrorsDto;
import net.thumbtack.timesheetparser.service.XlsParserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class XlsFileController {

    private final XlsParserService parserService;

    public XlsFileController(XlsParserService parserService) {
        this.parserService = parserService;
    }

    @PostMapping("/uploadXlsFile")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.EMPTY_FILE, ErrorCode.EMPTY_FILE.toString(), ErrorCode.EMPTY_FILE.getErrorString());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (!file.getContentType().equals("application/vnd.ms-excel")) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.UNSUPPORTED_FORMAT, ErrorCode.UNSUPPORTED_FORMAT.toString(), ErrorCode.UNSUPPORTED_FORMAT.getErrorString());
            return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        try {
            parserService.parseXls(file);
        } catch (InvalidHeadRowException ex) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.INVALID_HEAD_ROW, ErrorCode.INVALID_HEAD_ROW.toString(), ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (IOException ex) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.IO_ERROR, ErrorCode.IO_ERROR.toString(), ErrorCode.IO_ERROR.getErrorString());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        UploadFileResponse response = new UploadFileResponse(fileName, file.getContentType(), file.getSize());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
