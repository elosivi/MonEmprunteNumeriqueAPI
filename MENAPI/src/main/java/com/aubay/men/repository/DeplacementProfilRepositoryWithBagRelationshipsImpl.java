package com.aubay.men.repository;

import com.aubay.men.domain.DeplacementProfil;
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
public class DeplacementProfilRepositoryWithBagRelationshipsImpl implements DeplacementProfilRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String DEPLACEMENTPROFILS_PARAMETER = "deplacementProfils";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DeplacementProfil> fetchBagRelationships(Optional<DeplacementProfil> deplacementProfil) {
        return deplacementProfil.map(this::fetchProfils).map(this::fetchPrestations).map(this::fetchTransports);
    }

    @Override
    public Page<DeplacementProfil> fetchBagRelationships(Page<DeplacementProfil> deplacementProfils) {
        return new PageImpl<>(
            fetchBagRelationships(deplacementProfils.getContent()),
            deplacementProfils.getPageable(),
            deplacementProfils.getTotalElements()
        );
    }

    @Override
    public List<DeplacementProfil> fetchBagRelationships(List<DeplacementProfil> deplacementProfils) {
        return Optional.of(deplacementProfils)
            .map(this::fetchProfils)
            .map(this::fetchPrestations)
            .map(this::fetchTransports)
            .orElse(Collections.emptyList());
    }

    DeplacementProfil fetchProfils(DeplacementProfil result) {
        return entityManager
            .createQuery(
                "select deplacementProfil from DeplacementProfil deplacementProfil left join fetch deplacementProfil.profils where deplacementProfil.id = :id",
                DeplacementProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<DeplacementProfil> fetchProfils(List<DeplacementProfil> deplacementProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, deplacementProfils.size()).forEach(index -> order.put(deplacementProfils.get(index).getId(), index));
        List<DeplacementProfil> result = entityManager
            .createQuery(
                "select deplacementProfil from DeplacementProfil deplacementProfil left join fetch deplacementProfil.profils where deplacementProfil in :deplacementProfils",
                DeplacementProfil.class
            )
            .setParameter(DEPLACEMENTPROFILS_PARAMETER, deplacementProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    DeplacementProfil fetchPrestations(DeplacementProfil result) {
        return entityManager
            .createQuery(
                "select deplacementProfil from DeplacementProfil deplacementProfil left join fetch deplacementProfil.prestations where deplacementProfil.id = :id",
                DeplacementProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<DeplacementProfil> fetchPrestations(List<DeplacementProfil> deplacementProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, deplacementProfils.size()).forEach(index -> order.put(deplacementProfils.get(index).getId(), index));
        List<DeplacementProfil> result = entityManager
            .createQuery(
                "select deplacementProfil from DeplacementProfil deplacementProfil left join fetch deplacementProfil.prestations where deplacementProfil in :deplacementProfils",
                DeplacementProfil.class
            )
            .setParameter(DEPLACEMENTPROFILS_PARAMETER, deplacementProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    DeplacementProfil fetchTransports(DeplacementProfil result) {
        return entityManager
            .createQuery(
                "select deplacementProfil from DeplacementProfil deplacementProfil left join fetch deplacementProfil.transports where deplacementProfil.id = :id",
                DeplacementProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<DeplacementProfil> fetchTransports(List<DeplacementProfil> deplacementProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, deplacementProfils.size()).forEach(index -> order.put(deplacementProfils.get(index).getId(), index));
        List<DeplacementProfil> result = entityManager
            .createQuery(
                "select deplacementProfil from DeplacementProfil deplacementProfil left join fetch deplacementProfil.transports where deplacementProfil in :deplacementProfils",
                DeplacementProfil.class
            )
            .setParameter(DEPLACEMENTPROFILS_PARAMETER, deplacementProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
