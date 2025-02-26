package com.khamitov.tgproject.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InlineButtonCallbacks {

    SHOW_IMAGES("show_images", "Посмотреть мои картинки"),
    NEXT_IMAGE("next-image", "Далее"),
    GO_BACK("go-back", "Назад");

    private final String callback;
    private final String text;
}
