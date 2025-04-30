package com.khamitov.server.service.component;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainMenuComponent extends AbstractMessageComponent {

    @Override
    public String getMessageText() {
        return """
                Это главное меню бота с котиками,
                здесь ты можешь посмотреть чужих
                котиков и добавить своих""";
    }

    @Override
    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard() {
        return List.of(
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.CAT_LIST, "",
                                "Мои котики"),
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.SHOW_CAT, "",
                                "Смотреть кот"),
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.CREATE_CAT, "",
                                "Добавить кот")
                )
        );
    }
}
