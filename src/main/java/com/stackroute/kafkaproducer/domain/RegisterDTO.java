package com.stackroute.kafkaproducer.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegisterDTO {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
}
