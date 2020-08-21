package net.thumbtack.timesheetparser.controller;

import net.thumbtack.timesheetparser.dto.request.StaffMemberRequest;
import net.thumbtack.timesheetparser.dto.request.WorkgroupRequest;
import net.thumbtack.timesheetparser.dto.response.StaffMemberResponse;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.FileNotLoadedException;
import net.thumbtack.timesheetparser.exception.ProjectNotFoundException;
import net.thumbtack.timesheetparser.exception.StaffMemberNotFoundException;
import net.thumbtack.timesheetparser.exception.dto.ValidationErrorsDto;
import net.thumbtack.timesheetparser.models.WorkgroupMember;
import net.thumbtack.timesheetparser.service.StaffMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController()
@RequestMapping("/staffMember")
public class StaffMemberController {

    private final StaffMemberService staffMemberService;

    public StaffMemberController(StaffMemberService staffMemberService) {
        this.staffMemberService = staffMemberService;
    }

    @GetMapping()
    public StaffMemberResponse getProjects(@Valid StaffMemberRequest request) {
        return staffMemberService.getProjects(request.getStaffMemberName(),
                request.getNumberOfMonths(), request.getNumberOfHours());
    }

    @GetMapping("/workgroup")
    public List<WorkgroupMember> getWorkgroup(@Valid WorkgroupRequest request) {
        return staffMemberService.getWorkgroup(request.getStaffMemberId(), request.getProjectId());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationErrorsDto> handleBindException(BindException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ValidationErrorsDto veDto = new ValidationErrorsDto();
        fieldErrors.forEach(fieldError -> {
            String field = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            veDto.addFieldError(ErrorCode.FIELD, field, errorMessage);
        });
        return new ResponseEntity<>(veDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotLoadedException.class)
    public ResponseEntity<ValidationErrorsDto> handleFileNotLoadedException(FileNotLoadedException ex) {
        ValidationErrorsDto error = new ValidationErrorsDto();
        error.addFieldError(ErrorCode.FILE_N_LOAD, ErrorCode.FILE_N_LOAD.toString(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StaffMemberNotFoundException.class)
    public ResponseEntity<ValidationErrorsDto> handleStaffMemberNotFoundException(StaffMemberNotFoundException ex) {
        ValidationErrorsDto error = new ValidationErrorsDto();
        error.addFieldError(ErrorCode.STAFF_MEM_NOT_F, ErrorCode.STAFF_MEM_NOT_F.toString(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ValidationErrorsDto> handleProjectNotFoundException(ProjectNotFoundException ex) {
        ValidationErrorsDto error = new ValidationErrorsDto();
        error.addFieldError(ErrorCode.PR_NOT_F, ErrorCode.PR_NOT_F.toString(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
