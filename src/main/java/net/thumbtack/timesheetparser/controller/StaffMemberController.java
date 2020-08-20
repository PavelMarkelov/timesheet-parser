package net.thumbtack.timesheetparser.controller;

import net.thumbtack.timesheetparser.dto.request.StaffMemberRequest;
import net.thumbtack.timesheetparser.exception.ErrorCode;
import net.thumbtack.timesheetparser.exception.FileNotLoadedException;
import net.thumbtack.timesheetparser.exception.StaffMemberNotFoundException;
import net.thumbtack.timesheetparser.exception.dto.ValidationErrorsDto;
import net.thumbtack.timesheetparser.models.Project;
import net.thumbtack.timesheetparser.service.StaffMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsDto> handleException(MethodArgumentNotValidException ex) {
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


    @GetMapping()
    public ResponseEntity getProjects(@Valid @RequestBody StaffMemberRequest request) {
        List<Project> projects;
        try {
            projects = staffMemberService.getProjects(request.getStaffMemberName(), request.getNumberOfMonths());
        } catch (StaffMemberNotFoundException ex) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.STAFF_MEM_NOT_F, ErrorCode.STAFF_MEM_NOT_F.toString(), ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (FileNotLoadedException ex) {
            ValidationErrorsDto error = new ValidationErrorsDto();
            error.addFieldError(ErrorCode.FILE_N_LOAD, ErrorCode.FILE_N_LOAD.toString(), ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
