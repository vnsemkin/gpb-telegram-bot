package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.transfer;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.dtos.CustomerInfoResponse;
import org.vnsemkin.semkintelegrambot.application.dtos.TransferMoneyRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.TransferMoneyResponse;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;
import org.vnsemkin.semkintelegrambot.domain.utils.AppValidator;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferMoneyService implements MessageHandler {
    private final static String INVALID_TRANSFER_REQUEST = "Неправильный формат!";
    private final static String USER_OR_ACCOUNT_NOT_EXIST = "Не найдет пользователь или аккаунт!";
    private final static String UNKNOWN_ERROR = "Неизвестная ошибка";
    private final static String TRANSFER_SUCCESSFUL = "Деньги успешно переведены!";
    private final static String DEFAULT_ACCOUNT_NAME = "Дебетовый";
    private final static String NEW_LINE = "\n";
    private final Map<Long, String> messageHandlerServiceMap;
    private final TgSenderInterface sender;
    private final AppValidator validator;
    private final AppWebClient appWebClient;

    public void startTransferMoneyProcess(Message message) {
        Long chatId = message.getChatId();
        if (!checkUserExistAndHasAccount(chatId)) {
            sender.sendText(chatId, USER_OR_ACCOUNT_NOT_EXIST);
        } else {
            sender.sendText(chatId, inputTransferCommand());
            messageHandlerServiceMap.put(chatId, getHandlerName());
        }
    }

    @Override
    public void handle(@NonNull Message message) {
        Long chatId = message.getChatId();
        if (parseAndValidateAnswer(message).isPresent()) {
            handleTransferMoneyResponse(chatId, transferMoney(parseAndValidateAnswer(message).get()));
        }
    }

    private void handleTransferMoneyResponse(long chatId,
                                             Result<TransferMoneyResponse, String> transferMoneyResponse) {
        transferMoneyResponse.getError().ifPresentOrElse(error ->
                sender.sendText(chatId, error)
            , () -> sender.sendText(chatId, TRANSFER_SUCCESSFUL));
        messageHandlerServiceMap.remove(chatId);
    }

    private Optional<TransferMoneyRequest> parseAndValidateAnswer(Message message) {
        long chatId = message.getChatId();
        User fromUser = message.getFrom();
        String[] parts = message.getText().split(" ");
        if (parts.length != 2) {
            sender.sendText(message.getChatId(),
                INVALID_TRANSFER_REQUEST);
            return Optional.empty();
        }
        String toUser = parts[0];
        String amount = parts[1];
        if (validateTransferData(chatId, toUser, amount)) {
            return Optional
                .of(new TransferMoneyRequest(fromUser.getUserName(),
                    toUser,
                    amount));
        }
        return Optional.empty();
    }

    private boolean checkUserExistAndHasAccount(long userId) {
        Result<CustomerInfoResponse, String> customerInfo =
            appWebClient.getCustomerInfo(userId);
        return customerInfo.isSuccess() && checkAccountExist(customerInfo);
    }

    private Result<TransferMoneyResponse, String> transferMoney(TransferMoneyRequest request) {
        return appWebClient.transferMoney(request);
    }

    private boolean checkAccountExist(Result<CustomerInfoResponse, String> customerInfo) {
        String accountName = customerInfo.getData()
            .map(CustomerInfoResponse::accountName).orElse(UNKNOWN_ERROR);
        return accountName.equals(DEFAULT_ACCOUNT_NAME);
    }

    private boolean validateTransferData(long chatId,
                                         String toUser,
                                         String amount) {
        Result<Boolean, String> result = validator.validateName(toUser);
        if (result.isError()) {
            sender.sendText(chatId, result.getError().orElse(UNKNOWN_ERROR));
            return false;
        }
        result = validator.validateAmount(amount);
        if (result.isError()) {
            sender.sendText(chatId, result.getError().orElse(UNKNOWN_ERROR));
            return false;
        }
        return true;
    }


    private String inputTransferCommand() {
        return "Введите запрос на перевод в формате :" + NEW_LINE +
            "[TelegramUsername] [amount]" + NEW_LINE +
            "Пример: " + NEW_LINE +
            "ivanov 500";
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.TRANSFER.value;
    }
}