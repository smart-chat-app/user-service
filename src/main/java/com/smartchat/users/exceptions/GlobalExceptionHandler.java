package com.smartchat.users.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${messages.user-not-found}")
    private String userNotFound;
    @Value("${messages.username-not-found}")
    private String usernameNotFound;
    @Value("${messages.existing-user}")
    private String existingUser;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUsernNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(userNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        return new ResponseEntity<>(usernameNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<String> handleExistingUser(ExistingUserException ex) {
        return new ResponseEntity<>(existingUser, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException() {
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
