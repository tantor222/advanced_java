package com.khamitov.cat_server.mapping;

import com.khamitov.cat_server.entity.Cat;
import com.khamitov.model.dto.CatDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatMapping {

    @Mapping(target = "likes", source = "likes", defaultValue = "0")
    @Mapping(target = "dislikes", source = "dislikes", defaultValue = "0")
    @Mapping(target = "accepted", source = "accepted", defaultValue = "false")
    Cat fromDto(CatDto catDto);

    CatDto toDto(Cat cat);

    List<CatDto> toDto(List<Cat> cats);
}
