package com.stackroute.kafkaproducer.domain;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
}
