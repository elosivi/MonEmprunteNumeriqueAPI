package com.aubay.men.service.mapper;

import com.aubay.men.domain.Fonction;
import com.aubay.men.service.dto.FonctionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fonction} and its DTO {@link FonctionDTO}.
 */
@Mapper(componentModel = "spring")
public interface FonctionMapper extends EntityMapper<FonctionDTO, Fonction> {}
