package com.ushwamala.simplebankingapp.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ushwamala.simplebankingapp.model.Applicant;
import com.ushwamala.simplebankingapp.model.Application;
import com.ushwamala.simplebankingapp.model.ApplicationDto;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationRequestBody;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationResponse;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationStatusResponse;
import com.ushwamala.simplebankingapp.model.Status;
import com.ushwamala.simplebankingapp.repository.ApplicantRepository;
import com.ushwamala.simplebankingapp.repository.ApplicationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    private final ApplicantRepository applicantRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicantRepository applicantRepository,
            ApplicationRepository applicationRepository) {
        this.applicantRepository = applicantRepository;
        this.applicationRepository = applicationRepository;
    }

    public ApplicationDto findApplicationById(Integer applicationId) {
        Optional<Application> application =
                applicationRepository.findById(applicationId);
        ApplicationDto response = new ApplicationDto();
        if (application.isPresent()) {
            response.setStatus(application.get().getStatus());
            response.setCreatedAt(application.get().getCreatedAt());
            return response;
        }
        return null;
    }

    public CreditCardApplicationResponse submitApplication(
            CreditCardApplicationRequestBody body) {

        boolean applicantIsAdult = body.getAge() >= 18;
        if (!applicantIsAdult || !body.getIsEmployed()) {
            return new CreditCardApplicationResponse()
                    .applicationId(0)
                    .status(new CreditCardApplicationStatusResponse().status(Status.REJECTED.value));
        }

        Applicant newApplicant = Applicant.builder()
                .firstName(body.getFirstName())
                .lastName(body.getLastName())
                .age(body.getAge())
                .salary(body.getSalary())
                .panNumber(body.getPanNumber())
                .isEmployed(body.getIsEmployed())
                .applications(List.of())
                .build();

        //if the applicant already exists, get all their applications
        List<Application> existingApplications;
        Optional<Applicant> oldApplicant = applicantRepository.findByPanNumber(body.getPanNumber());
        if (oldApplicant.isPresent()) {
            newApplicant = oldApplicant.get();
            existingApplications = new ArrayList<>(newApplicant.getApplications());

            //if their applications list is not empty
            if (!existingApplications.isEmpty()) {
                //check if there are no previous applications that were approved or rejected in the last 6 months
                List<Application> rejectedOrApprovedApplications =
                        getRejectedOrApprovedApplications(existingApplications);

                boolean noRejectedOrApprovedApplications = rejectedOrApprovedApplications.isEmpty();

                //if there are rejected or approved applications in the last 6 months, set the application status to
                // rejected
                if (!noRejectedOrApprovedApplications) {
                    return new CreditCardApplicationResponse()
                            .applicationId(0)
                            .status(new CreditCardApplicationStatusResponse().status(Status.REJECTED.value));
                }

                Application newApplication = getApplication(newApplicant, existingApplications);

                return new CreditCardApplicationResponse()
                        .applicationId(newApplication.getId())
                        .status(new CreditCardApplicationStatusResponse().status(Status.SUBMITTED.value));
            }
        }


        //someDTO.getImmutableList().stream().collect(toCollection(ArrayList::new));
        List<Application> applications = new ArrayList<>(newApplicant.getApplications());
        Application newApplication = getApplication(newApplicant, applications);

        return new CreditCardApplicationResponse()
                .applicationId(newApplication.getId())
                .status(new CreditCardApplicationStatusResponse().status(Status.SUBMITTED.value));
    }

    private Application getApplication(Applicant newApplicant, List<Application> existingApplications) {
        Application newApplication = Application.builder()
                .status(Status.SUBMITTED.value)
                .createdAt(OffsetDateTime.now())
                .applicant(newApplicant)
                .build();
        existingApplications.add(newApplication);
        newApplicant.setApplications(existingApplications);
        applicantRepository.save(newApplicant);
        return newApplication;
    }

    @NotNull
    private List<Application> getRejectedOrApprovedApplications(List<Application> existingApplications) {
        return existingApplications.stream()
                .filter(application ->
                        (Status.APPROVED.value.equals(application.getStatus())
                                || Status.REJECTED.value.equals(application.getStatus()))
                                && (application.getCreatedAt().isBefore(application.getCreatedAt().minusMonths(6))))
                .collect(Collectors.toList());
    }
}
