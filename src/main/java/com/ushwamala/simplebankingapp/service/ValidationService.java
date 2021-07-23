package com.ushwamala.simplebankingapp.service;

import java.time.LocalDate;
import java.time.Period;

import com.ushwamala.simplebankingapp.model.Applicant;
import com.ushwamala.simplebankingapp.model.ContractType;
import com.ushwamala.simplebankingapp.model.MortgageApplicationOkResponse;
import com.ushwamala.simplebankingapp.model.MortgageApplicationRequest;
import com.ushwamala.simplebankingapp.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final UserService userService;

    @Autowired
    public ValidationService(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<MortgageApplicationOkResponse> validateApplication(MortgageApplicationRequest application) {
        MortgageApplicationOkResponse response = new MortgageApplicationOkResponse();
        for (Applicant applicant : application.getOperation().getApplicants()) {

            LocalDate today = LocalDate.now();
            LocalDate dob = applicant.getDob();
            Period period = Period.between(dob, today);

            boolean isAdult = period.getYears() >= 18;

            //made up validations
            boolean isEmployed = applicant.getIsEmployed();
            boolean hasPermanentContact = ContractType.PERMANENT.value.equals(applicant.getContractType());
            boolean earnsEnough = (application.getOperation().getObjectCost() * 0.1) >= applicant.getSalary();
            UserDto user = userService.loadUserByEmail(applicant.getEmail());


            if (!isAdult) {
                userService.sendRequestDeniedMail(application, user);
                return ResponseEntity.ok(response);
            }

            if (!isEmployed || !hasPermanentContact || !earnsEnough) {
                userService.sendConfirmationMail(application, user);
                userService.sendRequestDeniedMail(application, user);
                return ResponseEntity.ok(response);
            }

            userService.sendConfirmationMail(application, user);
            userService.sendPasswordMail(application, user);
            setResponseVariables(user, response);
        }
        return ResponseEntity.ok(response);
    }

    private void setResponseVariables(UserDto user, MortgageApplicationOkResponse response) {
        response.setLoginId(user.getUsername());
        response.setMortgageNumber(user.getMortgageNumber());
        response.setAccountNumber(user.getUserAccount().getAccountNumber());
    }
}

