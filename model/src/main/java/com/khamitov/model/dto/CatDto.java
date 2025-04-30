package com.khamitov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatDto {

    private UUID id;
    private String name;
    private Long authorChatId;
    private String authorName;
    private Integer likes;
    private Integer dislikes;
    private String filePath;
    private Boolean accepted;
}
