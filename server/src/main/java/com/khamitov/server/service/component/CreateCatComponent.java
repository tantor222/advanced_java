package com.khamitov.server.service.component;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateCatComponent extends AbstractMessageComponent {

    @Override
    public String getMessageText() {
        return "Покажи мне фото своего котика";
    }

    @Override
    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard() {
        return List.of(
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.MAIN_MENU, "", "Назад")
                )
        );
    }
}
