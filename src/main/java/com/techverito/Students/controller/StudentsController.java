package com.techverito.Students.controller;

import com.techverito.Students.entity.Student;
import com.techverito.Students.responses.DeleteResponse;
import com.techverito.Students.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentsController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentService.save(student), HttpStatus.CREATED);
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@RequestBody Map<String, Object> patchMap, @PathVariable("id") Long id) {
        Student student = studentService.update(patchMap, id);

        if (student == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable("id") Long id) {
        DeleteResponse response = studentService.delete(id);

        if (!response.getIsSuccess())
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}

