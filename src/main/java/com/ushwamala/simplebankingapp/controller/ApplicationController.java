package com.ushwamala.simplebankingapp.controller;

import com.ushwamala.simplebankingapp.api.ApplicationsApi;
import com.ushwamala.simplebankingapp.dto.ApplicationDto;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationRequestBody;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationResponse;
import com.ushwamala.simplebankingapp.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController implements ApplicationsApi {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public ResponseEntity<CreditCardApplicationResponse> apply(
            CreditCardApplicationRequestBody body) {
        ApplicationDto applicationDto = applicationService.submitApplication(body);
        CreditCardApplicationResponse response = new CreditCardApplicationResponse();
        response.setApplicationId(applicationDto.getApplication().getId());
        return ResponseEntity.ok(response);
    }
}
