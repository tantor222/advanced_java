package com.khamitov.cat_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cat {

    private UUID id;
    private String name;
    private Long authorChatId;
    private String authorName;
    private Integer likes;
    private Integer dislikes;
    private String filePath;
    private Boolean accepted;
}
