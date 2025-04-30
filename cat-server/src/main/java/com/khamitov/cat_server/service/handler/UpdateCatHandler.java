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
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UpdateCatHandler implements MessageHandler {

    private final CatRepository catRepository;
    private final CatMapping catMapping;
    private final ServerProducer serverProducer;

    @Override
    public List<CatAction> getActions() {
        return List.of(
                CatAction.GET_CAT_SAVE_NAME,
                CatAction.GET_CAT_LIKE,
                CatAction.GET_CAT_DISLIKE,
                CatAction.GET_CAT_TOUCH_PHOTO,
                CatAction.GET_CAT_ACCEPT
        );
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats().stream().findFirst().orElseThrow();
        Cat cat = catRepository.getCat(catDto.getId());
        if (cat == null) {
            throw new NoSuchElementException();
        }

        if (catDto.getFilePath() != null) {
            cat.setFilePath(catDto.getFilePath());
        }
        if (catDto.getAuthorChatId() != null) {
            cat.setAuthorChatId(catDto.getAuthorChatId());
        }
        if (catDto.getAuthorName() != null) {
            cat.setAuthorName(catDto.getAuthorName());
        }
        if (catDto.getName() != null) {
            cat.setName(catDto.getName());
        }
        if (catDto.getLikes() != null) {
            cat.setLikes(cat.getLikes());
        }
        if (catDto.getDislikes() != null) {
            cat.setDislikes(catDto.getDislikes());
        }
        if (catDto.getAccepted() != null) {
            cat.setAccepted(catDto.getAccepted());
        }

        CatServerDto response = CatServerDto.builder()
                .action(messageDto.getAction())
                .contextId(messageDto.getContextId())
                .cats(List.of(catMapping.toDto(cat)))
                .build();

        serverProducer.sendMessage(response);
    }
}
