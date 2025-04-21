package com.khamitov.server.repository;

import com.khamitov.server.model.entity.Cat;

import java.util.UUID;

public interface CatRepository {

    void saveCat(Cat cat);

    Cat getCat(UUID catId);
}
