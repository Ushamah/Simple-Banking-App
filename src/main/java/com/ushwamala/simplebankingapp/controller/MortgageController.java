package com.ushwamala.simplebankingapp.controller;

import com.ushwamala.simplebankingapp.api.MortgagesApiDelegate;
import com.ushwamala.simplebankingapp.model.MortgageApplicationOkResponse;
import com.ushwamala.simplebankingapp.model.MortgageApplicationRequest;
import com.ushwamala.simplebankingapp.service.UserService;
import com.ushwamala.simplebankingapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MortgageController implements MortgagesApiDelegate {

    private final ValidationService validationService;

    @Autowired
    public MortgageController(ValidationService validationService,
            UserService userService) {
        this.validationService = validationService;
    }


    @Override
    public ResponseEntity<MortgageApplicationOkResponse> apply(MortgageApplicationRequest application) {
        return validationService.validateApplication(application);
    }

}

