package org.vnsemkin.semkintelegrambot.domain.constants;

public enum CommandToServiceMap {
    REGISTER("register", "register" ),
    START("start", "start"),
    PING("ping", "ping");
    public final String command;
    public final String service;

    CommandToServiceMap(String command, String service) {
        this.command = command;
        this.service = service;
    }
}