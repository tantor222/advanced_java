package com.khamitov.cat_server.repository;

import com.khamitov.cat_server.entity.Cat;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CatRepositoryImpl implements CatRepository {

    private final ArrayList<Cat> cats = new ArrayList<>();

    @Override
    public void saveCat(Cat cat) {
        if (cat.getId() == null) {
            cat.setId(UUID.randomUUID());
        }
        cats.add(cat);
    }

    @Override
    public Cat getCat(UUID catId) {
        return cats.stream()
                .filter(c -> c.getId().equals(catId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cat> getAllCats() {
        return List.copyOf(cats);
    }
}
