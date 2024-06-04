package org.vnsemkin.semkintelegrambot.application.constants;

public enum CommandToServiceMap {
    REGISTER("register" ),
    START("start"),
    PING("ping");
    public final String value;

    CommandToServiceMap(String value) {
        this.value = value;
    }
}