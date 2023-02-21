package com.techverito.Students.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techverito.Students.entity.Student;
import com.techverito.Students.responses.DeleteResponse;
import com.techverito.Students.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class StudentsControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    StudentService studentService;

    @Test
    void shouldReturnOkStatusAndListOfTwoStudentsWhenRoutedToGetStudents() throws Exception {
        List<Student> listOfStudents = new ArrayList<>();
        listOfStudents.add(createASampleStudent("Lorem", "Ipsum"));
        listOfStudents.add(createASampleStudent("Fizz", "Buzz"));
        Mockito.when(studentService.findAll()).thenReturn(listOfStudents);

        ResultActions result = mvc.perform(MockMvcRequestBuilders.get("/students")
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))).andDo(print());
    }

    @Test
    void shouldReturnCreatedStatusWhenRoutedToPostStudents() throws Exception {
        Student loremIpsumStudent = createASampleStudent("Lorem", "Ipsum");
        Mockito.when(studentService.save(any(Student.class))).thenReturn(loremIpsumStudent);
        ObjectMapper objectMapper = new ObjectMapper();
        String loremIpsumStudentJSON = objectMapper.writeValueAsString(loremIpsumStudent);

        ResultActions result = mvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loremIpsumStudentJSON)
        );

        result.andExpect(status().isCreated());
    }

    @Test
    void shouldReturnErrorMessagesWithBadRequestStatusWhenRoutedToPostStudentsWithInvalidRequestBody() throws Exception {
        Student loremIpsumStudent = new Student();
        ObjectMapper objectMapper = new ObjectMapper();
        String loremIpsumStudentJSON = objectMapper.writeValueAsString(loremIpsumStudent);

        ResultActions result = mvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loremIpsumStudentJSON)
        );

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName", is("First Name can not be blank.")))
                .andExpect(jsonPath("$.lastName", is("Last Name can not be blank.")))
                .andExpect(jsonPath("$.dateOfBirth", is("Date of birth can not be blank.")));
    }

    @Test
    void shouldReturnOkStatusWhenRoutedToPatchExistingStudents() throws Exception {
        Student loremIpsumStudent = createASampleStudent("Lorem", "Ipsum");
        Mockito.when(studentService.update(anyMap(), anyLong())).thenReturn(loremIpsumStudent);
        HashMap<String, Object> studentPatch = new HashMap<>();
        studentPatch.put("firstName", "Lorem");
        studentPatch.put("lastName", "Ipsum");
        ObjectMapper objectMapper = new ObjectMapper();
        String studentPatchJSON = objectMapper.writeValueAsString(studentPatch);

        ResultActions result = mvc.perform(MockMvcRequestBuilders.patch("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPatchJSON)
        );

        Mockito.verify(studentService, Mockito.times(1)).update(anyMap(), anyLong());
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotOkStatusWhenRoutedToPatchNonExistingStudent() throws Exception {
        Mockito.when(studentService.update(anyMap(), anyLong())).thenReturn(null);
        HashMap<String, Object> studentPatch = new HashMap<>();
        studentPatch.put("firstName", "Lorem");
        studentPatch.put("lastName", "Ipsum");
        ObjectMapper objectMapper = new ObjectMapper();
        String studentPatchJSON = objectMapper.writeValueAsString(studentPatch);

        ResultActions result = mvc.perform(MockMvcRequestBuilders.patch("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPatchJSON)
        );

        Mockito.verify(studentService, Mockito.times(1)).update(anyMap(), anyLong());
        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkStatusWhenRoutedToDeleteExistingStudent() throws Exception {
        Mockito.when(studentService.delete(anyLong())).thenReturn(new DeleteResponse(1L, true));

        ResultActions result = mvc.perform(MockMvcRequestBuilders.delete("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        Mockito.verify(studentService, Mockito.times(1)).delete(anyLong());
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(DeleteResponse.successMessage, 1L))));
    }

    @Test
    void shouldReturnNotFoundStatusWhenRoutedToDeleteNonExistingStudent() throws Exception {
        Mockito.when(studentService.delete(anyLong())).thenReturn(new DeleteResponse(1L, false));

        ResultActions result = mvc.perform(MockMvcRequestBuilders.delete("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        Mockito.verify(studentService, Mockito.times(1)).delete(anyLong());
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(String.format(DeleteResponse.failureMessage, 1L))));
    }

    private static Student createASampleStudent(String firstName, String lastName) {
        return new Student(firstName, lastName, new Date(System.currentTimeMillis()));
    }
}
