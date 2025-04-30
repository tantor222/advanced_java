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
public class GetOrCreateHandler implements MessageHandler {

    private final CatRepository catRepository;
    private final ServerProducer serverProducer;
    private final CatMapping catMapping;

    @Override
    public List<CatAction> getActions() {
        return List.of(
                CatAction.GET_OR_CREATE_CAT,
                CatAction.GET_CAT_SAVE_PHOTO,
                CatAction.GET_CAT_SAVE,
                CatAction.SHOW_CAT
        );
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats()
                .stream()
                .findFirst()
                .orElse(null);

        if (catDto != null) {
            Cat catExists = catRepository.getCat(catDto.getId());
            if (catExists != null) {
                CatServerDto response = CatServerDto.builder()
                        .contextId(messageDto.getContextId())
                        .action(messageDto.getAction())
                        .cats(List.of(catMapping.toDto(catExists)))
                        .build();

                serverProducer.sendMessage(response);
            } else {
                Cat cat = catMapping.fromDto(catDto);
                catRepository.saveCat(cat);
                CatServerDto response = CatServerDto.builder()
                        .contextId(messageDto.getContextId())
                        .action(messageDto.getAction())
                        .cats(List.of(catMapping.toDto(cat)))
                        .build();

                serverProducer.sendMessage(response);
            }
        }
    }
}
