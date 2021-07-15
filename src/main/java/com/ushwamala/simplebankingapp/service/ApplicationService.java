package com.ushwamala.simplebankingapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ushwamala.simplebankingapp.dto.ApplicationDto;
import com.ushwamala.simplebankingapp.model.Applicant;
import com.ushwamala.simplebankingapp.model.Application;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationRequestBody;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationResponse;
import com.ushwamala.simplebankingapp.model.Status;
import com.ushwamala.simplebankingapp.repository.ApplicantRepository;
import com.ushwamala.simplebankingapp.repository.ApplicationRepository;
import org.jetbrains.annotations.NotNull;
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

    public CreditCardApplicationResponse getResponse(
            CreditCardApplicationRequestBody body){
        Applicant newApplicant = searchForApplicantInDB(body);
        boolean applicantIsAdult = body.getAge() >= 18;
        if(!applicantIsAdult){

        }
        return null;
    }

    public ApplicationDto submitApplication(CreditCardApplicationRequestBody body) {
        Application newApplication = Application.builder()
                .status(Status.SUBMITTED.value)
                .createdAt(LocalDateTime.now())
                .build();

        Applicant applicant = searchForApplicantInDB(body);

        //check if the applicant is 18 or older
        if (applicant.getAge() < 18) {
            newApplication.setStatus(Status.REJECTED.value);
        }
        else {

            //if the applicant already exists, get all their applications
            List<Application> applicantsApplications = new ArrayList<>(applicant.getApplications());

            //if their applications list is not empty
            if (!applicantsApplications.isEmpty()) {
                //check if there are no previous applications that were approved or rejected in the last 6 months
                List<Application> rejectedOrApprovedApplications =
                        getRejectedOrApprovedApplications(applicantsApplications);

                boolean noRejectedOrApprovedApplications = rejectedOrApprovedApplications.isEmpty();

                //if there are rejected or approved applications in the last 6 months, set the application status to
                // rejected
                if (!noRejectedOrApprovedApplications) {
                    newApplication.setStatus(Status.REJECTED.value);
                }
                else {

                    //someDTO.getImmutableList().stream().collect(toCollection(ArrayList::new));
                    addNewApplicationAndSaveInDB(newApplication, applicant, applicantsApplications);
                }

            }
            else {
                addNewApplicationAndSaveInDB(newApplication, applicant, applicantsApplications);
            }

            //Get all existing applications from the DB and
            //Iterable<Application> applicationsInDB = applicationRepository.findAll();

            //Store them in a list
           /* List<Application> applications = new ArrayList<>();
            while (applicationsInDB.iterator().hasNext()) {
                applications.add(applicationsInDB.iterator().next());
            }*/
        }


       /* //Search for the application which has the same applicant based on the PAN-number
        Optional<Applicant> existingApplicantOpt = applications
                .stream()
                .map(Application::getApplicant)
                .filter(applicationApplicant -> applicationApplicant.getPanNumber() == body.getPanNumber())
                .findFirst();

        boolean applicantExists = existingApplicantOpt.isPresent();




        //someDTO.getImmutableList().stream().collect(toCollection(ArrayList::new));
        List<Application> newApplications = new ArrayList<>(applicant.getApplications());
        newApplications.add(newApplication);
        applicant.setApplications(newApplications);*/

        return ApplicationDto.builder()
                .application(newApplication)
                .applicant(applicant)
                .build();
    }

    @NotNull
    private List<Application> getRejectedOrApprovedApplications(List<Application> existingApplications) {
        return existingApplications.stream()
               .filter(a -> (Status.APPROVED.value.equals(a.getStatus()) ||
                        Status.REJECTED.value.equals(a.getStatus())) &&
                        (a.getCreatedAt().isBefore(a.getCreatedAt().minusMonths(6))))
                .collect(Collectors.toList());
    }

    private void addNewApplicationAndSaveInDB(Application newApplication, Applicant applicant,
            List<Application> existingApplications) {
        existingApplications.add(newApplication);
        applicant.setApplications(existingApplications);
        applicantRepository.save(applicant);
    }


    private Applicant searchForApplicantInDB(CreditCardApplicationRequestBody body) {

        Applicant newApplicant = Applicant.builder()
                .firstName(body.getFirstName())
                .lastName(body.getLastName())
                .age(body.getAge())
                .salary(body.getSalary())
                .panNumber(body.getPanNumber())
                .isEmployed(body.getIsEmployed())
                .applications(List.of())
                .build();

        //Retrieve all existing applicants from the DB
        Iterable<Applicant> applicantsInDB = applicantRepository.findAll();
        //convert the iterable into a list
        List<Applicant> applicants = new ArrayList<>();
        for (Applicant applicant : applicantsInDB) {
            applicants.add(applicant);
        }

        //If there is applications in the DB, check the retrieved applications,
        // if there is an application with an applicant whose PAN is as same as the PAN in the body
        Optional<Applicant> applicantInDB;
        if (!applicants.isEmpty()) {
            applicantInDB = applicants.stream()
                    .filter(a -> a.getPanNumber() == body.getPanNumber())
                    .findFirst();
            //if there is an applicant present, return that applicant
            if (applicantInDB.isPresent()) {
                newApplicant = applicantInDB.get();
            }
        }

        return newApplicant;

    }

}
