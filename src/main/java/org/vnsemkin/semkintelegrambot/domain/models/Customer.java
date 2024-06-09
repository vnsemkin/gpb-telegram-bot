package org.vnsemkin.semkintelegrambot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public final class Customer {
    private final long tgId;
    private final String firstName;
    private final String username;
    private String email;
    private String password;
}