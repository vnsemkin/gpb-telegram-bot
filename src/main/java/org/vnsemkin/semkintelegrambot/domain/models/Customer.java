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
    private String name;
    private String email;
    private String password;
}
