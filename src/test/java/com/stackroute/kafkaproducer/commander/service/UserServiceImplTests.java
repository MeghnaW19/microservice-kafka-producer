package com.stackroute.kafkaproducer.commander.service;

import com.stackroute.kafkaproducer.domain.User;
import com.stackroute.kafkaproducer.exception.UserAlreadyExistsException;
import com.stackroute.kafkaproducer.exception.UserNotFoundException;
import com.stackroute.kafkaproducer.repository.UserRepository;
import com.stackroute.kafkaproducer.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DirtiesContext
class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User("nick@email.com", "1234567");
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    public void givenExistingUserEmailReturnUserDetails() throws UserNotFoundException {
        when(userRepository.findByEmail("nick@email.com")).thenReturn(Optional.of(user));
        User userFound = userService.getUserByEmail("nick@email.com");
        assertThat(userFound).isEqualTo(user);
    }

    @Test
    public void givenNonExistingUserEmailThenThrowException() throws UserNotFoundException {
        when(userRepository.findByEmail("jonas@email.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserByEmail("jonas@email.com")).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void givenNonExistingUserThenCreateNewUser() throws UserNotFoundException, UserAlreadyExistsException {
        when(userRepository.findByEmail("nick@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        User userCreated = userService.saveNewUser(user);
        assertThat(userCreated).isEqualTo(user);
    }

    @Test
    public void givenExistingUserThenThrowException() throws UserNotFoundException {
        when(userRepository.findByEmail("nick@email.com")).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.saveNewUser(user)).isInstanceOf(UserAlreadyExistsException.class);

    }

    @Test
    public void givenNonExistingUserEmailToDeleteThenThrowException() throws UserNotFoundException {
        when(userRepository.findByEmail("nick@email.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.deleteUserByEmail("nick@email.com")).isInstanceOf(UserNotFoundException.class);
    }


}