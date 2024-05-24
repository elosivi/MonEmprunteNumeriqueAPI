package com.aubay.men.service.mapper;

import com.aubay.men.domain.DeplacementProfil;
import com.aubay.men.domain.Fonction;
import com.aubay.men.domain.MaterielProfil;
import com.aubay.men.domain.Profil;
import com.aubay.men.domain.TransportProfil;
import com.aubay.men.service.dto.DeplacementProfilDTO;
import com.aubay.men.service.dto.FonctionDTO;
import com.aubay.men.service.dto.MaterielProfilDTO;
import com.aubay.men.service.dto.ProfilDTO;
import com.aubay.men.service.dto.TransportProfilDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {
    @Mapping(target = "fonction", source = "fonction", qualifiedByName = "fonctionId")
    @Mapping(target = "transportProfils", source = "transportProfils", qualifiedByName = "transportProfilIdSet")
    @Mapping(target = "deplacementProfils", source = "deplacementProfils", qualifiedByName = "deplacementProfilIdSet")
    @Mapping(target = "materielProfils", source = "materielProfils", qualifiedByName = "materielProfilIdSet")
    ProfilDTO toDto(Profil s);

    @Mapping(target = "transportProfils", ignore = true)
    @Mapping(target = "removeTransportProfil", ignore = true)
    @Mapping(target = "deplacementProfils", ignore = true)
    @Mapping(target = "removeDeplacementProfil", ignore = true)
    @Mapping(target = "materielProfils", ignore = true)
    @Mapping(target = "removeMaterielProfil", ignore = true)
    Profil toEntity(ProfilDTO profilDTO);

    @Named("fonctionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FonctionDTO toDtoFonctionId(Fonction fonction);

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

    @Named("materielProfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterielProfilDTO toDtoMaterielProfilId(MaterielProfil materielProfil);

    @Named("materielProfilIdSet")
    default Set<MaterielProfilDTO> toDtoMaterielProfilIdSet(Set<MaterielProfil> materielProfil) {
        return materielProfil.stream().map(this::toDtoMaterielProfilId).collect(Collectors.toSet());
    }
}
