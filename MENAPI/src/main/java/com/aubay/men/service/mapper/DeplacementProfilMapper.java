package com.aubay.men.service.mapper;

import com.aubay.men.domain.DeplacementProfil;
import com.aubay.men.domain.Prestation;
import com.aubay.men.domain.Profil;
import com.aubay.men.domain.Transport;
import com.aubay.men.service.dto.DeplacementProfilDTO;
import com.aubay.men.service.dto.PrestationDTO;
import com.aubay.men.service.dto.ProfilDTO;
import com.aubay.men.service.dto.TransportDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeplacementProfil} and its DTO {@link DeplacementProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeplacementProfilMapper extends EntityMapper<DeplacementProfilDTO, DeplacementProfil> {
    @Mapping(target = "profils", source = "profils", qualifiedByName = "profilIdSet")
    @Mapping(target = "prestations", source = "prestations", qualifiedByName = "prestationIdSet")
    @Mapping(target = "transports", source = "transports", qualifiedByName = "transportIdSet")
    DeplacementProfilDTO toDto(DeplacementProfil s);

    @Mapping(target = "removeProfil", ignore = true)
    @Mapping(target = "removePrestation", ignore = true)
    @Mapping(target = "removeTransport", ignore = true)
    DeplacementProfil toEntity(DeplacementProfilDTO deplacementProfilDTO);

    @Named("profilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfilDTO toDtoProfilId(Profil profil);

    @Named("profilIdSet")
    default Set<ProfilDTO> toDtoProfilIdSet(Set<Profil> profil) {
        return profil.stream().map(this::toDtoProfilId).collect(Collectors.toSet());
    }

    @Named("prestationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrestationDTO toDtoPrestationId(Prestation prestation);

    @Named("prestationIdSet")
    default Set<PrestationDTO> toDtoPrestationIdSet(Set<Prestation> prestation) {
        return prestation.stream().map(this::toDtoPrestationId).collect(Collectors.toSet());
    }

    @Named("transportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportDTO toDtoTransportId(Transport transport);

    @Named("transportIdSet")
    default Set<TransportDTO> toDtoTransportIdSet(Set<Transport> transport) {
        return transport.stream().map(this::toDtoTransportId).collect(Collectors.toSet());
    }
}
