package com.khamitov.cat_server.service.handler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.CatServerDto;

import java.util.List;

public interface MessageHandler {

    List<CatAction> getActions();

    void execute(CatServerDto messageDto);
}
