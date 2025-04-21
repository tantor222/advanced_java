package com.khamitov.server.service.component;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.service.callback.CallbackPrefix;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowCatComponent extends AbstractMessageComponent {

    public String getMessageText(Cat cat) {
        return cat.getName() + "\n" + "Автор: " + cat.getAuthorName();
    }

    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard(Cat cat) {
        String likes = "(" + cat.getLikes() + ")";
        String dislikes = "(" + cat.getDislikes() + ")";
        return List.of(
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.LIKE_CAT,
                                String.valueOf(cat.getId()), "👍" + likes),
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.DISLIKE_CAT,
                                String.valueOf(cat.getId()), "👎" + dislikes),
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.MAIN_MENU, "", "Назад")
                )
        );
    }
}
