package com.ushwamala.simplebankingapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ushwamala.simplebankingapp.dto.ApplicationDto;
import com.ushwamala.simplebankingapp.model.Applicant;
import com.ushwamala.simplebankingapp.model.Application;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationRequestBody;
import com.ushwamala.simplebankingapp.model.Status;
import com.ushwamala.simplebankingapp.repository.ApplicantRepository;
import com.ushwamala.simplebankingapp.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicantRepository applicantRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository,
            ApplicantRepository applicantRepository) {
        this.applicationRepository = applicationRepository;
        this.applicantRepository = applicantRepository;
    }

    public ApplicationDto submitApplication(CreditCardApplicationRequestBody body) {

        Applicant newApplicant = new Applicant();
        newApplicant.setFirstName(body.getFirstName());
        newApplicant.setLastName(body.getLastName());
        newApplicant.setAge(body.getAge());
        newApplicant.setSalary(body.getSalary());
        newApplicant.setIsEmployed(body.getIsEmployed());
        newApplicant.setApplications(List.of());

        /*Applicant.builder()
                        .firstName(body.getFirstName())
                        .lastName(body.getLastName())
                        .age(body.getAge())
                        .salary(body.getSalary())
                        .panNumber(body.getPanNumber())
                        .isEmployed(body.getIsEmployed())
                        .applications(List.of())
                        .build()*/
        Applicant applicant = applicantRepository.findByPanNumber(body.getPanNumber())
                .orElse(newApplicant);

        Application application = Application.builder()
                .status(Status.SUBMITTED.value)
                .createdAt(LocalDateTime.now())
                .build();

        applicant.getApplications().add(application);

        return ApplicationDto.builder()
                .application(application)
                .applicant(applicant)
                .build();
    }

}
