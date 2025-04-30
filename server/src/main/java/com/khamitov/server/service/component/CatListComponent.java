package com.khamitov.server.service.component;

import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CatListComponent extends AbstractMessageComponent {

    @Override
    public String getMessageText() {
        return """
                Вот список ваших котиков, Для,
                детальной информации по котику
                воспользуйтесь кнопкой""";
    }

    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard(Integer page, List<CatDto> cats) {
        List<List<CatDto>> subSets = new ArrayList<>();
        int size = cats.size();
        for (int i = 0; i < size; i += 9) {
            int end = Math.min(i + 9, size);
            List<CatDto> sublist = new ArrayList<>(cats.subList(i, end));
            subSets.add(sublist);
        }
        Integer nextPage = page == subSets.size() ? 0 : page + 1;
        Integer prevPage = page > 0 ? page - 1 : 0;
        List<CatDto> buttons = subSets.isEmpty() ? Collections.emptyList() : subSets.get(page);
        List<List<TelegramMessageKeyboardDto>> keyboard = new ArrayList<>();
        if (!buttons.isEmpty()) {
            var keyboardRowSize = 2;
            for (var i = 0; i < buttons.size(); i += keyboardRowSize) {
                var chunk = new ArrayList<>(buttons.subList(i, Math.min(buttons.size(), i + keyboardRowSize)));
                keyboard.add(chunk
                        .stream().map(j -> getCatButton(j, page))
                        .collect(Collectors.toCollection(ArrayList::new))
                );
            }
        }
        keyboard.addAll(List.of(
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.MAIN_MENU,
                                String.valueOf(nextPage), "Далее")
                ),
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.MAIN_MENU,
                                String.valueOf(prevPage), "Назад")
                ),
                List.of(
                        CallbackPrefix.createInlineButton(ECallbackPrefixes.MAIN_MENU, "", "В меню")
                )
            )

        );

        return keyboard;
    }

    private TelegramMessageKeyboardDto getCatButton(CatDto cat, Integer page) {
        return CallbackPrefix.createInlineButton(ECallbackPrefixes.CAT_LIST,
                String.valueOf(page), cat.getName());
    }
}
