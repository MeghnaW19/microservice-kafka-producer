package com.stackroute.kafkaproducer.service;

import com.stackroute.kafkaproducer.domain.User;
import com.stackroute.kafkaproducer.exception.UserAlreadyExistsException;
import com.stackroute.kafkaproducer.exception.UserNotFoundException;

public interface UserService {

    public User getUserByEmail(String email) throws UserNotFoundException;

    public User saveNewUser(User user) throws UserAlreadyExistsException;

    public void deleteUserByEmail(String email) throws UserNotFoundException;

    public User updateUser(User user);
}
