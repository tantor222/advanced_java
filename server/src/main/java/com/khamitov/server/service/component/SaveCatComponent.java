package com.khamitov.server.service.component;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SaveCatComponent extends AbstractMessageComponent {

    public String getMessageText(String catName, String author) {
        return catName + "\n" + "Автор: " + author;
    }

    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard(UUID catId) {
        return List.of(
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.ACCEPT_CAT, String.valueOf(catId),
                                "Подтвердить"),
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.CREATE_CAT, "",
                                "Повторить")
                ),
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.MAIN_MENU, "", "Назад")
                )
        );
    }
}
