package com.aubay.men.service.mapper;

import com.aubay.men.domain.Materiel;
import com.aubay.men.domain.MaterielProfil;
import com.aubay.men.service.dto.MaterielDTO;
import com.aubay.men.service.dto.MaterielProfilDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Materiel} and its DTO {@link MaterielDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterielMapper extends EntityMapper<MaterielDTO, Materiel> {
    @Mapping(target = "materielProfils", source = "materielProfils", qualifiedByName = "materielProfilIdSet")
    MaterielDTO toDto(Materiel s);

    @Mapping(target = "materielProfils", ignore = true)
    @Mapping(target = "removeMaterielProfil", ignore = true)
    Materiel toEntity(MaterielDTO materielDTO);

    @Named("materielProfilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterielProfilDTO toDtoMaterielProfilId(MaterielProfil materielProfil);

    @Named("materielProfilIdSet")
    default Set<MaterielProfilDTO> toDtoMaterielProfilIdSet(Set<MaterielProfil> materielProfil) {
        return materielProfil.stream().map(this::toDtoMaterielProfilId).collect(Collectors.toSet());
    }
}
