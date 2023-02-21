package com.techverito.Students.service;

import com.techverito.Students.entity.Student;
import com.techverito.Students.exceptions.StudentNotFoundException;
import com.techverito.Students.repository.StudentRepository;
import com.techverito.Students.responses.DeleteResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentServiceImplTest {
    @MockBean
    private StudentRepository studentRepository;

    @Test
    void shouldGetAListOfStudentsWithDetailsAsCreatedWhenFetchingAllStudentsFromRepository() {
        Student sampleStudent = createSampleStudent("Lorem", "Ipsum");
        when(studentRepository.findAll()).thenReturn(new ArrayList<>(List.of(sampleStudent)));
        // studentRepository.save(sampleStudent);
        StudentService studentService = new StudentServiceImpl(studentRepository);

        List<Student> studentList = studentService.findAll();
        Student lastStudent = studentList.get(studentList.size() - 1);

        Assertions.assertEquals(sampleStudent.getId(), lastStudent.getId());
        Assertions.assertEquals(sampleStudent.getFirstName(), lastStudent.getFirstName());
        Assertions.assertEquals(sampleStudent.getLastName(), lastStudent.getLastName());
        Assertions.assertEquals(sampleStudent.getDateOfBirth().toLocalDate(), lastStudent.getDateOfBirth().toLocalDate());
    }

    @Test
    void shouldGetTheSameStudentDetailsWhenTheStudentIsSavedToRepository() {
        Student expectedStudent = createSampleStudent("Fizz", "Buzz");
        when(studentRepository.save(any(Student.class))).thenReturn(expectedStudent);
        StudentService studentService = new StudentServiceImpl(studentRepository);

        Student actualStudent = studentService.save(expectedStudent);

        Assertions.assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
        Assertions.assertEquals(expectedStudent.getLastName(), actualStudent.getLastName());
        Assertions.assertEquals(expectedStudent.getDateOfBirth().toLocalDate(), actualStudent.getDateOfBirth().toLocalDate());
    }

    @Test
    void verifyStudentServiceCallsMockRepositoryWhenAStudentIsSavedToRepository() {
        Student expectedStudent = createSampleStudent("Fizz", "Buzz");
        when(studentRepository.save(any(Student.class))).thenReturn(expectedStudent);
        StudentService studentService = new StudentServiceImpl(studentRepository);

        studentService.save(expectedStudent);

        verify(studentRepository, times(1)).save(expectedStudent);
    }

    @Test
    void specificStudentDetailsShouldBeUpdatedWhenUpdateServiceIsCalled() {
        Student original = createSampleStudent("Lorem", "Ipsum");
        HashMap<String, Object> studentPatch = new HashMap<>();
        studentPatch.put("firstName", "Fizz");
        studentPatch.put("lastName", "Buzz");
        when(studentRepository.save(any(Student.class))).thenReturn(original);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(original));
        StudentService studentService = new StudentServiceImpl(studentRepository);

        Student result = studentService.update(studentPatch, 1L);

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(original);
        Assertions.assertEquals(studentPatch.get("firstName"), result.getFirstName());  // Names change
        Assertions.assertEquals(studentPatch.get("lastName"), result.getLastName());
        Assertions.assertEquals(original.getDateOfBirth(), result.getDateOfBirth());    // But dateOfBirth should not change
    }

    // TODO: Delete service test and implementation
    @Test
    void shouldReturnSuccessMessageWithIdWhenDeleteServiceIsCalledForValidId() {
        doNothing().when(studentRepository).deleteById(anyLong());
        Student sampleStudent = createSampleStudent("Lorem", "Ipsum");
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(sampleStudent));
        StudentService studentService = new StudentServiceImpl(studentRepository);
        DeleteResponse expectedResponse = new DeleteResponse(1L, true);

        DeleteResponse result = studentService.delete(1L);

        verify(studentRepository, times(1)).findById(anyLong());
        verify(studentRepository, times(1)).deleteById(anyLong());
        Assertions.assertEquals(expectedResponse.getMessage(), result.getMessage());
    }

    @Test
    void shouldReturnFailureMessageWithIdWhenDeleteServiceIsCalledForIdThatCannotBeDeleted() {
        doNothing().when(studentRepository).deleteById(anyLong());
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        StudentService studentService = new StudentServiceImpl(studentRepository);
        DeleteResponse expectedResponse = new DeleteResponse(1L, false);

        DeleteResponse result = studentService.delete(1L);

        verify(studentRepository, times(1)).findById(anyLong());
        verify(studentRepository, times(1)).deleteById(anyLong());
        Assertions.assertEquals(expectedResponse.getMessage(), result.getMessage());
    }

    private static Student createSampleStudent(String firstName, String lastName) {
        return new Student(firstName, lastName, new Date(System.currentTimeMillis()));
    }
}
