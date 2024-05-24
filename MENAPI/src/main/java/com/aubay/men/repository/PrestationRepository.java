package com.aubay.men.repository;

import com.aubay.men.domain.Prestation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Prestation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Long> {}
