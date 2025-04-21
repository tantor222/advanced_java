package com.khamitov.server.repository;

import com.khamitov.server.model.entity.Cat;

import java.util.ArrayList;
import java.util.UUID;

public class CatRepositoryImpl implements CatRepository {

    private final ArrayList<Cat> cats = new ArrayList<>();

    @Override
    public void saveCat(Cat cat) {
        cats.add(cat);
    }

    @Override
    public Cat getCat(UUID catId) {
        return cats.stream()
                .filter(c -> c.getId().equals(catId))
                .findFirst()
                .orElse(null);
    }
}
