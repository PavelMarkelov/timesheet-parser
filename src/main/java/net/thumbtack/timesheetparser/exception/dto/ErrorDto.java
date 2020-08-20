package net.thumbtack.timesheetparser.exception.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import net.thumbtack.timesheetparser.exception.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private String errorCode;
    private String field;
    private String message;

    public ErrorDto() {
    }

    public ErrorDto(ErrorCode errorCode, String field, String message) {
        this.errorCode = String.format("%s", errorCode);
        this.field = field;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
