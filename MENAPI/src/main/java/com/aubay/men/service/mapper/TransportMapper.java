package com.aubay.men.service.mapper;

import com.aubay.men.domain.DeplacementProfil;
import com.aubay.men.domain.Transport;
import com.aubay.men.domain.TransportProfil;
import com.aubay.men.service.dto.DeplacementProfilDTO;
import com.aubay.men.service.dto.TransportDTO;
import com.aubay.men.service.dto.TransportProfilDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transport} and its DTO {@link TransportDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransportMapper extends EntityMapper<TransportDTO, Transport> {
    @Mapping(target = "transportProfils", source = "transportProfils", qualifiedByName = "transportProfilIdSet")
    @Mapping(target = "deplacementProfils", source = "deplacementProfils", qualifiedByName = "deplacementProfilIdSet")
    TransportDTO toDto(Transport s);

    @Mapping(target = "transportProfils", ignore = true)
    @Mapping(target = "removeTransportProfil", ignore = true)
    @Mapping(target = "deplacementProfils", ignore = true)
    @Mapping(target = "removeDeplacementProfil", ignore = true)
    Transport toEntity(TransportDTO transportDTO);

    @Named("transportProfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportProfilDTO toDtoTransportProfilId(TransportProfil transportProfil);

    @Named("transportProfilIdSet")
    default Set<TransportProfilDTO> toDtoTransportProfilIdSet(Set<TransportProfil> transportProfil) {
        return transportProfil.stream().map(this::toDtoTransportProfilId).collect(Collectors.toSet());
    }

    @Named("deplacementProfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeplacementProfilDTO toDtoDeplacementProfilId(DeplacementProfil deplacementProfil);

    @Named("deplacementProfilIdSet")
    default Set<DeplacementProfilDTO> toDtoDeplacementProfilIdSet(Set<DeplacementProfil> deplacementProfil) {
        return deplacementProfil.stream().map(this::toDtoDeplacementProfilId).collect(Collectors.toSet());
    }
}
