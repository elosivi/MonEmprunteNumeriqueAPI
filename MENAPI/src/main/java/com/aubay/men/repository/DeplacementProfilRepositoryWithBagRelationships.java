package com.aubay.men.repository;

import com.aubay.men.domain.DeplacementProfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DeplacementProfilRepositoryWithBagRelationships {
    Optional<DeplacementProfil> fetchBagRelationships(Optional<DeplacementProfil> deplacementProfil);

    List<DeplacementProfil> fetchBagRelationships(List<DeplacementProfil> deplacementProfils);

    Page<DeplacementProfil> fetchBagRelationships(Page<DeplacementProfil> deplacementProfils);
}
