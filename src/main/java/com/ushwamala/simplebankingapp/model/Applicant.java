package com.ushwamala.simplebankingapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applicants")
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

    @Column(name = "is_employed")
    private Boolean isEmployed;

    @Column(name = "salary")
    private int salary;

    @Column(name = "pan_number")
    private int panNumber;

    //https://www.baeldung.com/jpa-joincolumn-vs-mappedby
    @OneToMany(mappedBy = "applicant",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();

    public void addApplication(Application tempApplication){
        if(applications == null){
            applications = new ArrayList<>();
        }
        applications.add(tempApplication);
    }
}
