package com.aubay.men.service.mapper;

import com.aubay.men.domain.Prestation;
import com.aubay.men.domain.PrestationProfil;
import com.aubay.men.domain.Profil;
import com.aubay.men.service.dto.PrestationDTO;
import com.aubay.men.service.dto.PrestationProfilDTO;
import com.aubay.men.service.dto.ProfilDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrestationProfil} and its DTO {@link PrestationProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrestationProfilMapper extends EntityMapper<PrestationProfilDTO, PrestationProfil> {
    @Mapping(target = "profil", source = "profil", qualifiedByName = "profilId")
    @Mapping(target = "prestations", source = "prestations", qualifiedByName = "prestationIdSet")
    PrestationProfilDTO toDto(PrestationProfil s);

    @Mapping(target = "removePrestation", ignore = true)
    PrestationProfil toEntity(PrestationProfilDTO prestationProfilDTO);

    @Named("profilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfilDTO toDtoProfilId(Profil profil);

    @Named("prestationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrestationDTO toDtoPrestationId(Prestation prestation);

    @Named("prestationIdSet")
    default Set<PrestationDTO> toDtoPrestationIdSet(Set<Prestation> prestation) {
        return prestation.stream().map(this::toDtoPrestationId).collect(Collectors.toSet());
    }
}
