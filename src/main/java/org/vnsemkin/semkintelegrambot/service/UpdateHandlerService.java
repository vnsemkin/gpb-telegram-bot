package org.vnsemkin.semkintelegrambot.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public final class UpdateHandlerService {
    private final List<UpdateHandler> updateHandlers;

    public void handle(Update update) {
        for (UpdateHandler handler : updateHandlers) {
            handler.handle(update);
        }
    }
}
