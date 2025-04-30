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
public class LikesCatHandler implements MessageHandler {

    private final CatRepository catRepository;
    private final CatMapping catMapping;
    private final ServerProducer serverProducer;

    @Override
    public List<CatAction> getActions() {
        return List.of(
                CatAction.GET_CAT_LIKE,
                CatAction.GET_CAT_DISLIKE
        );
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats().stream().findFirst().orElseThrow();
        Cat cat = catRepository.getCat(catDto.getId());
        if (cat == null) {
            throw new NoSuchElementException();
        }
        if (messageDto.getAction().equals(CatAction.GET_CAT_LIKE)) {
            cat.setLikes(likeCat(cat));
        }
        if (messageDto.getAction().equals(CatAction.GET_CAT_DISLIKE)) {
            cat.setDislikes(dislikeCat(cat));
        }
        List<Cat> cats = catRepository.getAllCats()
                .stream()
                .filter(c -> !c.getId().equals(cat.getId()))
                .filter(Cat::getAccepted)
                .toList();

        if (cats.isEmpty()) {
            cats = catRepository.getAllCats();
        }
        List<CatDto> catDtos = cats.stream()
                .filter(Cat::getAccepted)
                .map(catMapping::toDto)
                .toList();

        CatServerDto response = CatServerDto.builder()
                .action(CatAction.SHOW_CAT)
                .contextId(messageDto.getContextId())
                .cats(catDtos)
                .build();

        serverProducer.sendMessage(response);
    }

    private Integer likeCat(Cat cat) {
        return cat.getLikes() + 1;
    }

    private Integer dislikeCat(Cat cat) {
        return cat.getDislikes() + 1;
    }
}
