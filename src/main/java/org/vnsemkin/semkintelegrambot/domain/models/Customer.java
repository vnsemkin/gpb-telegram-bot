package org.vnsemkin.semkintelegrambot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Customer {
    private long tgId;
    private String firstName;
    private String username;
    private String email;
    private String password;
}