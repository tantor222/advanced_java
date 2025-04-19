package com.khamitov.server.service.component;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SavePhotoComponent extends AbstractMessageComponent {
    @Override
    public String getMessageText() {
        return "Как зовут твоего котика?";
    }

    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard(Long catId) {
        return List.of(
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.TOUCH_PHOTO, String.valueOf(catId),
                                "Назад")
                )
        );
    }

}
