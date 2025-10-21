package com.example.ws.email;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    
    JavaMailSenderImpl mailSender;

    public EmailService (){
        this.initilaze();
    }


  public void initilaze() {
    this.mailSender = new JavaMailSenderImpl();
    mailSender.setHost("stmp.ethereal.email");
    mailSender.setPort(587);
    mailSender.setUsername("isaac.goyette73@ethereal.email");
    mailSender.setPassword("BFfKYVxxHH2QcT71P");
    Properties properties = mailSender.getJavaMailProperties();
    properties.put("mail.stmp.starttls.enable", "true");
  }


    public void sendActivationEmail(String email , String activationToken) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("noreply@my-app.com");
    message.setTo(email);
    message.setSubject("Account Activation");
    message.setText("http://localhost:5173/activation/" + activationToken);
    this.mailSender.send(message);
  }
}
