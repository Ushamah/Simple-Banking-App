package com.ushwamala.simplebankingapp.repository;

import java.util.Optional;

import com.ushwamala.simplebankingapp.model.Applicant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Integer> {
    Optional<Applicant> findByPanNumber(int panNumber);
}
