package com.techverito.Students.exceptions;

public class StudentNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Student with ID %d Not Found.";

    public StudentNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
