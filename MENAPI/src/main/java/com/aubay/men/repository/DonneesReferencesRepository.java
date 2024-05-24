package com.aubay.men.repository;

import com.aubay.men.domain.DonneesReferences;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DonneesReferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonneesReferencesRepository extends JpaRepository<DonneesReferences, Long> {}
