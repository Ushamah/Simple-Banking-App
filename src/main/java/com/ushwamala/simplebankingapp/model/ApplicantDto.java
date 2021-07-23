package com.ushwamala.simplebankingapp.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApplicantDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    private String title;

    private String firstName;

    private String middleName;

    private String surName;

    private Date dob;

    private Boolean isEmployed;

    private String occupation;

    private String contractType;

    private Integer salary;

    private Date jobStartDate;

    private String phoneNumber;

    private String email;

    private String confirmEmail;

}
