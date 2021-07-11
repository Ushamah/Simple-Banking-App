package com.ushwamala.simplebankingapp;

import javax.validation.Valid;

import com.ushwamala.simplebankingapp.api.ApplicationsApiDelegate;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationRequestBody;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationResponse;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationStatusResponse;
import com.ushwamala.simplebankingapp.model.CreditCardApplicationsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService implements ApplicationsApiDelegate {

    @Override
    public ResponseEntity<CreditCardApplicationResponse> apply(
            @Valid CreditCardApplicationRequestBody creditCardApplicationRequestBody) {
        return ApplicationsApiDelegate.super.apply(creditCardApplicationRequestBody);
    }

    @Override
    public ResponseEntity<CreditCardApplicationsResponse> getApplications() {
        return ApplicationsApiDelegate.super.getApplications();
    }

    @Override
    public ResponseEntity<CreditCardApplicationRequestBody> getApplicationById(Integer applicationId) {
        return ApplicationsApiDelegate.super.getApplicationById(applicationId);
    }

    @Override
    public ResponseEntity<CreditCardApplicationStatusResponse> getApplicationStatus(Integer applicationId) {
        return ApplicationsApiDelegate.super.getApplicationStatus(applicationId);
    }
}
