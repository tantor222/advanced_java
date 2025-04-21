package com.khamitov.server.service.component;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TouchPhotoComponent extends AbstractMessageComponent {

    @Override
    public String getMessageText() {
        return "Какой красивый котик! Хочешь\n" +
                "оставить это фото?";
    }

    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard(UUID catId) {
        return List.of(
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.SAVE_PHOTO, String.valueOf(catId),
                                "Подтвердить"),
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.CREATE_CAT, String.valueOf(catId),
                                "Повторить")
                ),
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.MAIN_MENU, "", "Назад")
                )
        );
    }
}
