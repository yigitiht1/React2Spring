package com.example.ws.user;


import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ws.email.EmailService;
import com.example.ws.user.exception.ActivationNotificationException;
import com.example.ws.user.exception.NotUniqueEmailException;

import jakarta.transaction.Transactional;


@Service
public class UserService {

  @Autowired // DI
  UserRepository userRepository;

  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();// password hashing

  @Autowired
  EmailService emailService;

  @Transactional(rollbackOn = MailException.class)
  public void save(User user) {
    try {
      String encodedPassword = passwordEncoder.encode(user.getPassword());
      user.setActivationToken(UUID.randomUUID().toString());
      user.setPassword(encodedPassword);
      userRepository.saveAndFlush(user);
      emailService.sendActivationEmail(user.getEmail(),user.activationToken);
    } catch (DataIntegrityViolationException ex) {
      throw new NotUniqueEmailException();
    } catch (MailException ex) {
      throw new ActivationNotificationException();
    }

  }



}
