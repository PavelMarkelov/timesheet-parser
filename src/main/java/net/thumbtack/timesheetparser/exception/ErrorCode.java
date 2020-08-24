package net.thumbtack.timesheetparser.exception;

public enum ErrorCode {

    UNSUPPORTED_FORMAT("This file format if not supported"),
    EMPTY_FILE("XLS file is not selected"),
    INVALID_HEAD_ROW("Missing columns with \"Staff Member\" or \"Date\" or \"Project\" name"),
    IO_ERROR("Something went wrong. Try uploading the file again"),
    FIELD_ERROR("Field error"),
    STAFF_MEMBER_NOT_FOUND("Staff member with specified name is not found"),
    FILE_N_LOAD("Excel file not loaded"),
    PROJECT_NOT_FOUND("Project not found");

    private String errorString;

    ErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
