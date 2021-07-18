package com.ushwamala.simplebankingapp.controller;

import com.ushwamala.simplebankingapp.api.ApplicationsApiDelegate;
import com.ushwamala.simplebankingapp.model.ApplicationDto;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationRequestBody;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationResponse;
import com.ushwamala.simplebankingapp.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController implements ApplicationsApiDelegate {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public ResponseEntity<CreditCardApplicationResponse> apply(
            CreditCardApplicationRequestBody body) {
        CreditCardApplicationResponse response = applicationService.submitApplication(body);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApplicationDto> getApplicationById(Integer id) {
        ApplicationDto response = applicationService.findApplicationById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

}
