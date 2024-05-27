package org.vnsemkin.semkintelegrambot.domain.constants;

public enum CommandToServiceMap {
    REGISTER("register", "register" ),
    CREATE_ACCOUNT("create_account", "create_account"),
    CURRENT_BALANCE("current_balance", "current_balance"),
    TRANSFER("transfer", "transfer"),
    START("start", "start"),
    PING("ping", "ping");
    public final String command;
    public final String service;

    CommandToServiceMap(String command, String service) {
        this.command = command;
        this.service = service;
    }
}
