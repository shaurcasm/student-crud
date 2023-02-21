package com.techverito.Students.service;

import com.techverito.Students.entity.Student;
import com.techverito.Students.exceptions.StudentNotFoundException;
import com.techverito.Students.repository.StudentRepository;
import com.techverito.Students.responses.DeleteResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student update(Map<String, Object> patchMap, Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        patchMap.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Student.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, student, value);
            }
        });

        return studentRepository.save(student);
    }

    @Override
    public DeleteResponse delete(Long id) {
        Optional<Student> student = studentRepository.findById(id);

        studentRepository.deleteById(id);

        return new DeleteResponse(id, student.isPresent());
    }
}
