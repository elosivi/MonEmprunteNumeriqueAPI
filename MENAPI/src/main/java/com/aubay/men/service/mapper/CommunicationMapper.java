package com.aubay.men.service.mapper;

import com.aubay.men.domain.Communication;
import com.aubay.men.domain.Unite;
import com.aubay.men.service.dto.CommunicationDTO;
import com.aubay.men.service.dto.UniteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Communication} and its DTO {@link CommunicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommunicationMapper extends EntityMapper<CommunicationDTO, Communication> {
    @Mapping(target = "unite", source = "unite", qualifiedByName = "uniteId")
    CommunicationDTO toDto(Communication s);

    @Named("uniteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UniteDTO toDtoUniteId(Unite unite);
}
