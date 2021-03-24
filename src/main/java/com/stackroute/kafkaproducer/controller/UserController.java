package com.stackroute.kafkaproducer.controller;


import com.stackroute.kafkaproducer.domain.Customer;
import com.stackroute.kafkaproducer.domain.RegisterDTO;
import com.stackroute.kafkaproducer.domain.User;
import com.stackroute.kafkaproducer.exception.UserAlreadyExistsException;
import com.stackroute.kafkaproducer.exception.UserNotFoundException;
import com.stackroute.kafkaproducer.service.Producer;
import com.stackroute.kafkaproducer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to access, modify data in database using REST
 */
@RestController
public class UserController {


    private Producer producer;
    private UserService userService;

    @Autowired
    public UserController(Producer producer, UserService userService) {
        this.producer = producer;
        this.userService = userService;
    }

    /**
     * REST Endpoint for creating new User
     * URI: /register  METHOD: POST
     * Response status: success: 201(created) , User already exists : 409(conflict)
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterDTO newUser) throws UserAlreadyExistsException {
        producer.sendMessage(new Customer(newUser.getFirstName(), newUser.getLastName(), newUser.getGender(), newUser.getEmail()));
        return new ResponseEntity<>(userService.saveNewUser(new User(newUser.getEmail(), newUser.getPassword())), HttpStatus.CREATED);
    }

    /**
     * REST Endpoint for login in user
     * URI: /login  METHOD: POST
     * Response status: success: 202(accepted) , Unsuccessful : 401(Unauthorized)
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) throws UserNotFoundException {
        if (userService.getUserByEmail(user.getEmail()).getPassword().equals(user.getPassword()))
            return new ResponseEntity<>("Successful LogIn", HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>("Unsuccessful Login", HttpStatus.UNAUTHORIZED);
    }

    /**
     * REST Endpoint for update user
     * URI: /update  METHOD: POST
     * Response status: success: 201(created)
     */
    @PostMapping("/update")
    public ResponseEntity<String> updatePassword(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>("Updated User", HttpStatus.CREATED);
    }

    /**
     * REST Endpoint for deleting user
     * URI: /delete/user METHOD: DELETE
     * Response status: success: 202(accepted)
     */
    @DeleteMapping("/delete/user")
    public ResponseEntity<String> deleteUser(@RequestParam("email") String email) throws UserNotFoundException {
        userService.deleteUserByEmail(email);
        return new ResponseEntity<>("User deleted", HttpStatus.ACCEPTED);
    }
}
