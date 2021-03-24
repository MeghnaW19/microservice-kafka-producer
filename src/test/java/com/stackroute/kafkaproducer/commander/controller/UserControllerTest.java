package com.stackroute.kafkaproducer.commander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.kafkaproducer.controller.UserController;
import com.stackroute.kafkaproducer.domain.RegisterDTO;
import com.stackroute.kafkaproducer.domain.User;
import com.stackroute.kafkaproducer.service.Producer;
import com.stackroute.kafkaproducer.service.UserService;
import kafka.utils.Json;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private Producer producer;

    @Autowired
    private ObjectMapper mapper;

    private User user;
    private RegisterDTO registerDto;

    @BeforeEach
    void setUp() {
        user = new User("nick@email.com", "1234567");
        registerDto = new RegisterDTO("Nick", "Jonas", "Male", "nick@email.com", "1234567");
    }

    @AfterEach
    void tearDown() {
        user = null;
        registerDto = null;
    }

    @Test
    public void givenRegisterDetailsThenCreateNewUser() throws Exception {
        when(userService.saveNewUser(any(User.class)))
                .thenReturn(user);
        MvcResult mvcResult = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.encodeAsString(registerDto)))
                .andExpect(status().isCreated())
                .andReturn();
        User receivedUser = toObjectFromJson(mvcResult, User.class);
        assertThat(receivedUser.getEmail()).isEqualTo(user.getEmail());
        verify(userService, times(1))
                .saveNewUser(any(User.class));
    }

    @Test
    public void givenLoginDetailsThenReturnMessage() throws Exception {
        when(userService.getUserByEmail(any()))
                .thenReturn(user);
        MvcResult mvcResult = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.encodeAsString(user)))
                .andExpect(status().isAccepted())
                .andReturn();
        String message = mvcResult.getResponse().getContentAsString();
        assertThat(message).isEqualTo("Successful LogIn");
        verify(userService, times(1))
                .getUserByEmail(any());
    }

    @Test
    public void givenLoginDetailsWithWrongPasswordThenReturnErrorMessage() throws Exception {
        when(userService.getUserByEmail(any()))
                .thenReturn(user);
        User wrongUser = new User("nick@email.com", "1234");
        MvcResult mvcResult = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.encodeAsString(wrongUser)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String message = mvcResult.getResponse().getContentAsString();
        assertThat(message).isEqualTo("Unsuccessful Login");
        verify(userService, times(1))
                .getUserByEmail(any());
    }

    private <T> T toObjectFromJson(MvcResult result, Class<T> toClass) throws Exception {
        return this.mapper.readValue(result.getResponse().getContentAsString(), toClass);
    }
}