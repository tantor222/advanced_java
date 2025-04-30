package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.CatServerDto;

public interface CatServerHandler {

    CatAction getAction();

    void execute(CatServerDto messageDto);
}
