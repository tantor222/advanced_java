package com.khamitov.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cat {

    private UUID id;
    private String name;
    private Long authorChatId;
    private String authorName;
    private Integer likes;
    private Integer dislikes;
}
