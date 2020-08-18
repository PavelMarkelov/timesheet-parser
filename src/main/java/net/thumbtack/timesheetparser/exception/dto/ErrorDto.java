package net.thumbtack.timesheetparser.exception.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import net.thumbtack.timesheetparser.exception.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private String errorCode;
    private String message;

    public ErrorDto() {
    }

    public ErrorDto(ErrorCode errorCode, String message) {
        this.errorCode = String.format("%s", errorCode);
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
