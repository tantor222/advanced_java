package com.khamitov.cat_server.service.handler;

import com.khamitov.cat_server.entity.Cat;
import com.khamitov.cat_server.mapping.CatMapping;
import com.khamitov.cat_server.repository.CatRepository;
import com.khamitov.cat_server.service.server.ServerProducer;
import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCatsHandler implements MessageHandler {

    private final CatRepository catRepository;
    private final ServerProducer serverProducer;
    private final CatMapping catMapping;

    @Override
    public List<CatAction> getActions() {
        return List.of(
                CatAction.GET_ALL_CAT_LIST,
                CatAction.SHOW_CAT
        );
    }

    @Override
    public void execute(CatServerDto messageDto) {
        List<Cat> cats = catRepository.getAllCats()
                .stream()
                .filter(Cat::getAccepted)
                .toList();

        List<CatDto> catDtos = catMapping.toDto(cats);
        CatServerDto response = CatServerDto.builder()
                .contextId(messageDto.getContextId())
                .action(messageDto.getAction())
                .cats(catDtos)
                .page(messageDto.getPage())
                .build();

        serverProducer.sendMessage(response);
    }
}
