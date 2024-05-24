package com.aubay.men.service.mapper;

import com.aubay.men.domain.DonneesReferences;
import com.aubay.men.domain.Unite;
import com.aubay.men.service.dto.DonneesReferencesDTO;
import com.aubay.men.service.dto.UniteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DonneesReferences} and its DTO {@link DonneesReferencesDTO}.
 */
@Mapper(componentModel = "spring")
public interface DonneesReferencesMapper extends EntityMapper<DonneesReferencesDTO, DonneesReferences> {
    @Mapping(target = "unite", source = "unite", qualifiedByName = "uniteId")
    @Mapping(target = "temporalite", source = "temporalite", qualifiedByName = "uniteId")
    DonneesReferencesDTO toDto(DonneesReferences s);

    @Named("uniteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UniteDTO toDtoUniteId(Unite unite);
}
