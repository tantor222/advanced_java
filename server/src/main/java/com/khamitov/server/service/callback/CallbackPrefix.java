package com.khamitov.server.service.callback;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.server.constant.ECallbackPrefixes;

import java.util.Optional;

public class CallbackPrefix {

    public static String delimiter = ":";
    public static String defaultPath = "%";

    public static TelegramMessageKeyboardDto createInlineButton(ECallbackPrefixes pref, String path, String text) {
        return TelegramMessageKeyboardDto.builder()
                .text(text)
                .callback(createPrefix(pref, path))
                .build();
    }

    public static String createPrefix(ECallbackPrefixes prefix, String path) {
        return prefix.getPref() + delimiter + Optional.ofNullable(path)
                .filter(p -> !path.isEmpty())
                .orElse(defaultPath);
    }

    public static String createPrefix(ECallbackPrefixes prefix) {
        return createPrefix(prefix, null);
    }

    public static String getPrefix(String path) {
        var splits = path.split(delimiter);
        if (splits.length < 2) {
            return null;
        }
        return splits[0];
    }

    public static String getPath(String path) {
        var splits = path.split(delimiter);
        if (splits.length < 2) {
            return null;
        }
        return splits[1];
    }
}
