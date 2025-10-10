package com.example.ws.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController 
{

   @Autowired // DI
   UserRepository userRepository;


    @PostMapping("/api/v1/users")
    void creatUser(@RequestBody User user)
    {
        userRepository.save(user);
    }

}
