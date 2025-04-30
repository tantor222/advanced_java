package com.khamitov.server.service.component;

import org.springframework.stereotype.Component;

@Component
public class AcceptCatComponent extends AbstractMessageComponent {

    @Override
    public String getMessageText() {
        return "Ура! Котик добавлен";
    }
}
