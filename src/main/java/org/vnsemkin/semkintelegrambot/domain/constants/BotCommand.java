package org.vnsemkin.semkintelegrambot.domain.constants;

public enum BotCommand {
    REGISTER("register"),
    CREATE_ACCOUNT("create_account"),
    CURRENT_BALANCE("current_balance"),
    TRANSFER("transfer"),
    START("start"),
    PING("ping");
    public final String command;

    BotCommand(String command) {
        this.command = command;
    }
}
