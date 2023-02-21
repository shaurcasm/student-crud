package com.techverito.Students.service;

import com.techverito.Students.entity.Student;
import com.techverito.Students.responses.DeleteResponse;

import java.util.List;
import java.util.Map;

public interface StudentService {
    List<Student> findAll();

    Student save(Student student);

    Student update(Map<String, Object> studentPatch, Long id);

    DeleteResponse delete(Long id);
}
