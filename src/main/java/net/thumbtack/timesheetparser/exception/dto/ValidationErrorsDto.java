package net.thumbtack.timesheetparser.exception.dto;

import net.thumbtack.timesheetparser.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsDto {

    private final List<ErrorDto> errors = new ArrayList<>();

    public ValidationErrorsDto() {
    }

    public void addFieldError(ErrorCode errorCode, String message) {
        ErrorDto error = new ErrorDto(errorCode, message);
        errors.add(error);
    }

    public List<ErrorDto> getErrors() {
        return errors;
    }
}
