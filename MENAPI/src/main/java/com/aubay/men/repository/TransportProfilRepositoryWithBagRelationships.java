package com.aubay.men.repository;

import com.aubay.men.domain.TransportProfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TransportProfilRepositoryWithBagRelationships {
    Optional<TransportProfil> fetchBagRelationships(Optional<TransportProfil> transportProfil);

    List<TransportProfil> fetchBagRelationships(List<TransportProfil> transportProfils);

    Page<TransportProfil> fetchBagRelationships(Page<TransportProfil> transportProfils);
}
