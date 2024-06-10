package org.vnsemkin.semkintelegrambot.application.constants;

public enum CommandToServiceMap {
    REGISTER("register" ),
    CREATE_ACCOUNT("create_account" ),
    CURRENT_BALANCE("current_balance" ),
    TRANSFER("transfer" );
    public final String value;

    CommandToServiceMap(String value) {
        this.value = value;
    }
}