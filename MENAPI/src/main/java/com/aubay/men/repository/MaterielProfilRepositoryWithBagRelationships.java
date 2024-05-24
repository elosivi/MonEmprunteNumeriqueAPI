package com.aubay.men.repository;

import com.aubay.men.domain.MaterielProfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MaterielProfilRepositoryWithBagRelationships {
    Optional<MaterielProfil> fetchBagRelationships(Optional<MaterielProfil> materielProfil);

    List<MaterielProfil> fetchBagRelationships(List<MaterielProfil> materielProfils);

    Page<MaterielProfil> fetchBagRelationships(Page<MaterielProfil> materielProfils);
}
