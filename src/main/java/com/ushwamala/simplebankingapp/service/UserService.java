package com.ushwamala.simplebankingapp.service;

import java.text.MessageFormat;
import java.util.Optional;

import com.ushwamala.simplebankingapp.model.Applicant;
import com.ushwamala.simplebankingapp.model.MortgageApplicationRequest;
import com.ushwamala.simplebankingapp.model.UserDto;
import com.ushwamala.simplebankingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userService, EmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto loadUserByEmail(String email) {
        final Optional<UserDto> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseGet(UserDto::new);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        final Optional<UserDto> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else {
            throw new UsernameNotFoundException(
                    MessageFormat.format("User with userId {0} cannot be found.", userId));
        }
    }

    void sign(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }


    public void sendConfirmationMail(MortgageApplicationRequest request, UserDto user) {

        for (Applicant applicant : request.getOperation().getApplicants()) {

            String userMail = applicant.getEmail();
            String userId = user.getUserId();
            String userAccountNumber = user.getUserAccount().getAccountNumber();
            String userMortgageNumber = user.getMortgageNumber();

            final SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userMail);
            mailMessage.setSubject("Mortgage application!");
            mailMessage.setFrom("<MAIL>");
            mailMessage.setText(
                    "Congratulations, your mortgage has been\n"
                            + "granted:\n."
                            + "-\t" + "Your customer login id: " + userId + "\n"
                            + "-\t" + "Your current account number: " + userAccountNumber + "\n"
                            + "-\t" + "Your current account number: " + userMortgageNumber
            );

            emailService.sendEmail(mailMessage);
        }

    }

    public void sendRequestDeniedMail(MortgageApplicationRequest request, UserDto user) {

        for (Applicant applicant : request.getOperation().getApplicants()) {

            String userMail = applicant.getEmail();

            final SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userMail);
            mailMessage.setSubject("Mortgage application!");
            mailMessage.setFrom("<MAIL>");
            mailMessage.setText("Sorry we are unable to grant you the mortgage at this moment");

            emailService.sendEmail(mailMessage);
        }

    }

    public void sendPasswordMail(MortgageApplicationRequest request, UserDto user) {

        for (Applicant applicant : request.getOperation().getApplicants()) {

            String userMail = applicant.getEmail();
            String userId = user.getUserId();
            String userPassword = user.getPassword();

            final SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userMail);
            mailMessage.setSubject("Mortgage application!");
            mailMessage.setFrom("<MAIL>");
            mailMessage.setText(
                    "Congratulations, your mortgage has been\n"
                            + "granted:\n."
                            + "-\t" + "Your customer login id: " + userId + "\n"
                            + "-\t" + "Your password: " + userPassword
            );

            emailService.sendEmail(mailMessage);
        }

    }

}