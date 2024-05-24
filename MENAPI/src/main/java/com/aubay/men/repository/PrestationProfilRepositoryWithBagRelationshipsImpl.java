package com.aubay.men.repository;

import com.aubay.men.domain.PrestationProfil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PrestationProfilRepositoryWithBagRelationshipsImpl implements PrestationProfilRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PRESTATIONPROFILS_PARAMETER = "prestationProfils";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PrestationProfil> fetchBagRelationships(Optional<PrestationProfil> prestationProfil) {
        return prestationProfil.map(this::fetchPrestations);
    }

    @Override
    public Page<PrestationProfil> fetchBagRelationships(Page<PrestationProfil> prestationProfils) {
        return new PageImpl<>(
            fetchBagRelationships(prestationProfils.getContent()),
            prestationProfils.getPageable(),
            prestationProfils.getTotalElements()
        );
    }

    @Override
    public List<PrestationProfil> fetchBagRelationships(List<PrestationProfil> prestationProfils) {
        return Optional.of(prestationProfils).map(this::fetchPrestations).orElse(Collections.emptyList());
    }

    PrestationProfil fetchPrestations(PrestationProfil result) {
        return entityManager
            .createQuery(
                "select prestationProfil from PrestationProfil prestationProfil left join fetch prestationProfil.prestations where prestationProfil.id = :id",
                PrestationProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<PrestationProfil> fetchPrestations(List<PrestationProfil> prestationProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, prestationProfils.size()).forEach(index -> order.put(prestationProfils.get(index).getId(), index));
        List<PrestationProfil> result = entityManager
            .createQuery(
                "select prestationProfil from PrestationProfil prestationProfil left join fetch prestationProfil.prestations where prestationProfil in :prestationProfils",
                PrestationProfil.class
            )
            .setParameter(PRESTATIONPROFILS_PARAMETER, prestationProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
