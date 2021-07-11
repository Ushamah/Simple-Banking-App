package com.ushwamala.simplebankingapp.dto;


import com.ushwamala.simplebankingapp.model.Applicant;
import com.ushwamala.simplebankingapp.model.Application;
import com.ushwamala.simplebankingapp.repository.ApplicantRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationDto {
    private Application application;
    private Applicant applicant;

}
