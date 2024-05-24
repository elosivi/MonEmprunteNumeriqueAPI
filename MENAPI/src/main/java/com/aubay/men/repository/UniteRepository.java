package com.aubay.men.repository;

import com.aubay.men.domain.Unite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Unite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniteRepository extends JpaRepository<Unite, Long> {}
