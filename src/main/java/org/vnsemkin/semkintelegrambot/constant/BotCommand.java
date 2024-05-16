package org.vnsemkin.semkintelegrambot.constant;

public enum BotCommand {
    START("start"),
    PING("ping");
    public final String command;

    BotCommand(String command) {
        this.command = command;
    }
}
