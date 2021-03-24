package com.stackroute.kafkaproducer.service;


import com.stackroute.kafkaproducer.domain.User;
import com.stackroute.kafkaproducer.exception.UserAlreadyExistsException;
import com.stackroute.kafkaproducer.exception.UserNotFoundException;
import com.stackroute.kafkaproducer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class to access database repository
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    /**
     * method to get user details of given email
     */
    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            return user.get();
        else
            throw new UserNotFoundException("User with this email " + email + " is not found");
    }


    /**
     * method to save new user detail
     */
    @Override
    public User saveNewUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        else
            return userRepository.save(user);
    }


    /**
     * method to delete a user of given email
     */
    @Override
    public void deleteUserByEmail(String email) throws UserNotFoundException {
        Optional<User> existsByEmail = userRepository.findByEmail(email);
        if (existsByEmail.isPresent())
            userRepository.deleteById(existsByEmail.get().getEmail());
        else
            throw new UserNotFoundException("User with this email " + email + " is not found");
    }

    /**
     * method to update details of a user
     */
    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
