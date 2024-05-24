package com.aubay.men.service.mapper;

import com.aubay.men.domain.Unite;
import com.aubay.men.service.dto.UniteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Unite} and its DTO {@link UniteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniteMapper extends EntityMapper<UniteDTO, Unite> {}
