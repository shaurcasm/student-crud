package com.techverito.Students.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.sql.Date;

@Entity
@Getter
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "First Name can not be blank.")
    private final String firstName;
    @NotBlank(message = "Last Name can not be blank.")
    private final String lastName;
    @NotNull(message = "Date of birth can not be blank.")
    private final Date dateOfBirth;

    public Student() {
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = null;
    }

    public Student(String firstName, String lastName, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
}
