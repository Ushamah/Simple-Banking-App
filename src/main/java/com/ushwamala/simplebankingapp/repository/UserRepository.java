package com.ushwamala.simplebankingapp.repository;

import java.util.Optional;

import com.ushwamala.simplebankingapp.model.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDto, Integer> {
    Optional<UserDto> findByUserId(String userId);

    Optional<UserDto> findByEmail(String email);
}
