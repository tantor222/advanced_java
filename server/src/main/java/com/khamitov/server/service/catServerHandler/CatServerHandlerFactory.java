package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.dto.CatServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatServerHandlerFactory {

    private final List<CatServerHandler> serverHandlers;

    public CatServerHandler getHandler(CatServerDto messageDto) {
        for (CatServerHandler serverHandler : serverHandlers) {
            if (serverHandler.getAction().equals(messageDto.getAction())) {
                return serverHandler;
            }
        }
        return null;
    }
}
