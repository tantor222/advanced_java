package com.khamitov.model.dto;

import com.khamitov.model.constant.CatAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatServerDto {

    private Long contextId;
    private CatAction action;
    private Integer page;
    private List<CatDto> cats;
}
