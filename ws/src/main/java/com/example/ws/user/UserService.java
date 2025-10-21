package com.example.ws.user;

import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ws.user.exception.ActivationNotificationException;
import com.example.ws.user.exception.NotUniqueEmailException;

import jakarta.transaction.Transactional;

@Service
public class UserService {

  @Autowired // DI
  UserRepository userRepository;

  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();// password hashing

  @Transactional(rollbackOn = MailException.class)
  public void save(User user) {
    try {
      String encodedPassword = passwordEncoder.encode(user.getPassword());
      user.setActivationToken(UUID.randomUUID().toString());
      user.setPassword(encodedPassword);
      userRepository.saveAndFlush(user);
      sendActivationEmail(user);
    } catch (DataIntegrityViolationException ex) {
      throw new NotUniqueEmailException();
    } catch (MailException ex) {
      throw new ActivationNotificationException();
    }

  }

  private void sendActivationEmail(User user) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("noreply@my-app.com");
    message.setTo(user.getEmail());
    message.setSubject("Account Activation");
    message.setText("http://localhost:5173/activation/" + user.getActivationToken());
    getJavaMailSender().send(message);
  }

  public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("stmp.ethereal.email");
    mailSender.setPort(587);
    mailSender.setUsername("isaac.goyette73@ethereal.email");
    mailSender.setPassword("BFfKYVxxHH2QcT71P");

    Properties properties = mailSender.getJavaMailProperties();
    properties.put("mail.stmp.starttls.enable", "true");
    return mailSender;
  }

}
