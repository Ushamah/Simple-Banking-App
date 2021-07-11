package com.ushwamala.simplebankingapp.repository;

import com.ushwamala.simplebankingapp.model.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
}
