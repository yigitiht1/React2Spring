package com.example.ws.user;


import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.ws.error.ApiError;
import com.example.ws.shared.GenericMessage;
import com.example.ws.shared.Messages;

import jakarta.validation.Valid;



@RestController
public class UserController 
{
   @Autowired
   UserService userService;

   //@Autowired
   //MessageSource messageSource;

    @PostMapping("/api/v1/users")
    GenericMessage creatUser(@Valid @RequestBody User user)
    {
        userService.save(user);
        String message = Messages.getMessageForLocale("hoaxify.create.user.success.message", LocaleContextHolder.getLocale());
        return new GenericMessage(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidEx(MethodArgumentNotValidException exception)
    {
        ApiError apiError = new ApiError();
        String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
        apiError.setPath("/api/v1/users");
        apiError.setMessage(message);
        apiError.setStatus(400);
        
        var validationErrors = exception.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap
            (FieldError::getField,FieldError::getDefaultMessage, (existing,replacing) -> existing));
        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.badRequest().body(apiError);

    }

    @ExceptionHandler(NotUniqueEmailException.class)
    ResponseEntity<ApiError> handleNotUniqueEmailEx(NotUniqueEmailException exception)
    {
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(400);
        apiError.setValidationErrors(exception.getValidationErrors());
        return ResponseEntity.badRequest().body(apiError);

    }

}
