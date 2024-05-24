package com.aubay.men.repository;

import com.aubay.men.domain.PrestationProfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrestationProfil entity.
 *
 * When extending this class, extend PrestationProfilRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PrestationProfilRepository extends PrestationProfilRepositoryWithBagRelationships, JpaRepository<PrestationProfil, Long> {
    default Optional<PrestationProfil> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<PrestationProfil> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<PrestationProfil> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
