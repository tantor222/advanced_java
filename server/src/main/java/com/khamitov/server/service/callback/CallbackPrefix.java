package com.khamitov.server.service.callback;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;

public class CallbackPrefix {

    public static String delimiter = ":";

    public static TelegramMessageKeyboardDto createInlineButton(String pref, String path, String text) {
        return TelegramMessageKeyboardDto.builder()
                .text(text)
                .callback(setPrefix(pref, path))
                .build();
    }

    public static String setPrefix(String prefix, String path) {
        return prefix + delimiter + path;
    }

    public static String getPrefix(String path) {
        var splits = path.split(delimiter);
        if (splits.length < 2) {
            return null;
        }
        return splits[0];
    }

    public static String getTile(String path) {
        var splits = path.split(delimiter);
        if (splits.length < 2) {
            return null;
        }
        return splits[1];
    }
}
