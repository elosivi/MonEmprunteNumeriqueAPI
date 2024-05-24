package com.aubay.men.repository;

import com.aubay.men.domain.DeplacementProfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeplacementProfil entity.
 *
 * When extending this class, extend DeplacementProfilRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DeplacementProfilRepository
    extends DeplacementProfilRepositoryWithBagRelationships, JpaRepository<DeplacementProfil, Long> {
    default Optional<DeplacementProfil> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<DeplacementProfil> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<DeplacementProfil> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
