package com.khamitov.cat_server.repository;

import com.khamitov.cat_server.entity.Cat;

import java.util.List;
import java.util.UUID;

public interface CatRepository {

    void saveCat(Cat cat);

    Cat getCat(UUID catId);

    List<Cat> getAllCats();
}
