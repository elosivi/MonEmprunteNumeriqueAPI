package com.aubay.men.repository;

import com.aubay.men.domain.PrestationProfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PrestationProfilRepositoryWithBagRelationships {
    Optional<PrestationProfil> fetchBagRelationships(Optional<PrestationProfil> prestationProfil);

    List<PrestationProfil> fetchBagRelationships(List<PrestationProfil> prestationProfils);

    Page<PrestationProfil> fetchBagRelationships(Page<PrestationProfil> prestationProfils);
}
