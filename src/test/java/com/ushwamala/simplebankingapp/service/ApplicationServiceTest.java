package com.ushwamala.simplebankingapp.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock private ApplicantRepository mockApplicantRepository;
    @Mock private ApplicationRepository mockApplicationRepository;

    @InjectMocks
    private ApplicationService applicationServiceUnderTest;


    @Test
    void testFindApplicationById() {
        // Setup
        final ApplicationDto expectedResult = new ApplicationDto();
        final CreditCardApplicationRequestBody body =
                getRequestBody(18, true);

        expectedResult.setStatus(Status.SUBMITTED.value);
        final OffsetDateTime createdAt = OffsetDateTime.of(
                1992, 12, 26, 12, 32, 21, 12345, ZoneOffset.UTC);
        expectedResult.setCreatedAt(createdAt);

        // Configure ApplicationRepository.findById(...).
        final Optional<Application> applicationOptional = Optional.of(
                Application.builder()
                        .status(Status.SUBMITTED.value)
                        .createdAt(createdAt)
                        .applicant(getApplicant(body))
                        .build()
        );
        when(mockApplicationRepository.findById(0)).thenReturn(applicationOptional);

        // Run the test
        final ApplicationDto result = applicationServiceUnderTest.findApplicationById(0);

        // Verify the results
        assertEquals(expectedResult, result);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockApplicationRepository).findById(captor.capture());

        assertEquals(0, captor.getValue());
    }

    @Test
    void testFindApplicationById_ApplicationRepositoryReturnsAbsent() {

        when(mockApplicationRepository.findById(0)).thenReturn(Optional.empty());

        // Run the test
        final ApplicationDto result = applicationServiceUnderTest.findApplicationById(0);

        // Verify the results
        assertNull(result);
    }

    @Test
    void testSubmitApplication() {
        // Setup
        final CreditCardApplicationRequestBody body =
                getRequestBody(17, true);

        final CreditCardApplicationResponse expectedResult =
                getExpectedResult(0, Status.REJECTED.value);

        // Run the test
        final CreditCardApplicationResponse actualResult =
                applicationServiceUnderTest.submitApplication(body);

        // Verify the results
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testSubmitApplication_ApplicantRepositoryFindByPanNumberReturnsAbsent() {
        // Setup
        final CreditCardApplicationRequestBody body = getRequestBody(18, false);

        final CreditCardApplicationResponse expectedResult =
                getExpectedResult(0, Status.REJECTED.value);

        // Run the test
        final CreditCardApplicationResponse result =
                applicationServiceUnderTest.submitApplication(body);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @NotNull
    private CreditCardApplicationResponse getExpectedResult(int applicationId, String status) {
        final CreditCardApplicationResponse expectedResult = new CreditCardApplicationResponse();
        expectedResult.setApplicationId(applicationId);
        expectedResult.setStatus(new CreditCardApplicationStatusResponse().status(status));
        return expectedResult;
    }

    private Applicant getApplicant(CreditCardApplicationRequestBody body) {
        return Applicant.builder()
                .firstName(body.getFirstName())
                .lastName(body.getLastName())
                .age(body.getAge())
                .salary(body.getSalary())
                .panNumber(body.getPanNumber())
                .isEmployed(body.getIsEmployed())
                .applications(List.of())
                .build();
    }

    @NotNull
    private CreditCardApplicationRequestBody getRequestBody(int age, boolean isEmployed) {
        final CreditCardApplicationRequestBody body = new CreditCardApplicationRequestBody();
        body.setFirstName("firstName");
        body.setLastName("lastName");
        body.setAge(age);
        body.setIsEmployed(isEmployed);
        body.setSalary(0);
        body.setPanNumber(26121992);
        return body;
    }
}
