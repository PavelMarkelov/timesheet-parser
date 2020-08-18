package net.thumbtack.timesheetparser.controller;

import net.thumbtack.timesheetparser.dto.response.UploadFileResponse;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.dto.ValidationErrorsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class XlsFileController {

    @PostMapping("/uploadXlsFile")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.EMPTY, ErrorCode.EMPTY.getErrorString());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (!file.getContentType().equals("application/vnd.ms-excel")) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.UNS_FORMAT, ErrorCode.UNS_FORMAT.getErrorString());
            return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        UploadFileResponse response = new UploadFileResponse(fileName, file.getContentType(), file.getSize());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
