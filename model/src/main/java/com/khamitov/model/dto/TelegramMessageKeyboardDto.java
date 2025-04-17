package com.khamitov.model.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TelegramMessageKeyboardDto {

    private String text;
    private String callback;
}
