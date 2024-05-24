package com.aubay.men.service.mapper;

import com.aubay.men.domain.Materiel;
import com.aubay.men.domain.MaterielProfil;
import com.aubay.men.domain.Prestation;
import com.aubay.men.domain.Profil;
import com.aubay.men.service.dto.MaterielDTO;
import com.aubay.men.service.dto.MaterielProfilDTO;
import com.aubay.men.service.dto.PrestationDTO;
import com.aubay.men.service.dto.ProfilDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterielProfil} and its DTO {@link MaterielProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterielProfilMapper extends EntityMapper<MaterielProfilDTO, MaterielProfil> {
    @Mapping(target = "profils", source = "profils", qualifiedByName = "profilIdSet")
    @Mapping(target = "prestations", source = "prestations", qualifiedByName = "prestationIdSet")
    @Mapping(target = "materiels", source = "materiels", qualifiedByName = "materielIdSet")
    MaterielProfilDTO toDto(MaterielProfil s);

    @Mapping(target = "removeProfil", ignore = true)
    @Mapping(target = "removePrestation", ignore = true)
    @Mapping(target = "removeMateriel", ignore = true)
    MaterielProfil toEntity(MaterielProfilDTO materielProfilDTO);

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

    @Named("materielId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterielDTO toDtoMaterielId(Materiel materiel);

    @Named("materielIdSet")
    default Set<MaterielDTO> toDtoMaterielIdSet(Set<Materiel> materiel) {
        return materiel.stream().map(this::toDtoMaterielId).collect(Collectors.toSet());
    }
}
