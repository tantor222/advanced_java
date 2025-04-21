package com.khamitov.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ECallbackPrefixes {

    MOCK("404"),
    START("/start"),
    MAIN_MENU("main"),
    CREATE_CAT("cat_c"),
    TOUCH_PHOTO("photo_t"),
    SAVE_PHOTO("photo_s"),
    SAVE_NAME("name_s"),
    SAVE_CAT("cat_s"),
    ACCEPT_CAT("cat_a"),
    SHOW_CAT("cat_ch"),
    LIKE_CAT("cat_l"),
    DISLIKE_CAT("cat_dl"),
    CAT_LIST("cat_li");

    private final String pref;
}
