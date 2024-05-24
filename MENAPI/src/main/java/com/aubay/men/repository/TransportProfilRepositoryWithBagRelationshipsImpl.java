package com.aubay.men.repository;

import com.aubay.men.domain.TransportProfil;
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
public class TransportProfilRepositoryWithBagRelationshipsImpl implements TransportProfilRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TRANSPORTPROFILS_PARAMETER = "transportProfils";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TransportProfil> fetchBagRelationships(Optional<TransportProfil> transportProfil) {
        return transportProfil.map(this::fetchProfils).map(this::fetchPrestations).map(this::fetchTransports);
    }

    @Override
    public Page<TransportProfil> fetchBagRelationships(Page<TransportProfil> transportProfils) {
        return new PageImpl<>(
            fetchBagRelationships(transportProfils.getContent()),
            transportProfils.getPageable(),
            transportProfils.getTotalElements()
        );
    }

    @Override
    public List<TransportProfil> fetchBagRelationships(List<TransportProfil> transportProfils) {
        return Optional.of(transportProfils)
            .map(this::fetchProfils)
            .map(this::fetchPrestations)
            .map(this::fetchTransports)
            .orElse(Collections.emptyList());
    }

    TransportProfil fetchProfils(TransportProfil result) {
        return entityManager
            .createQuery(
                "select transportProfil from TransportProfil transportProfil left join fetch transportProfil.profils where transportProfil.id = :id",
                TransportProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<TransportProfil> fetchProfils(List<TransportProfil> transportProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, transportProfils.size()).forEach(index -> order.put(transportProfils.get(index).getId(), index));
        List<TransportProfil> result = entityManager
            .createQuery(
                "select transportProfil from TransportProfil transportProfil left join fetch transportProfil.profils where transportProfil in :transportProfils",
                TransportProfil.class
            )
            .setParameter(TRANSPORTPROFILS_PARAMETER, transportProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    TransportProfil fetchPrestations(TransportProfil result) {
        return entityManager
            .createQuery(
                "select transportProfil from TransportProfil transportProfil left join fetch transportProfil.prestations where transportProfil.id = :id",
                TransportProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<TransportProfil> fetchPrestations(List<TransportProfil> transportProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, transportProfils.size()).forEach(index -> order.put(transportProfils.get(index).getId(), index));
        List<TransportProfil> result = entityManager
            .createQuery(
                "select transportProfil from TransportProfil transportProfil left join fetch transportProfil.prestations where transportProfil in :transportProfils",
                TransportProfil.class
            )
            .setParameter(TRANSPORTPROFILS_PARAMETER, transportProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    TransportProfil fetchTransports(TransportProfil result) {
        return entityManager
            .createQuery(
                "select transportProfil from TransportProfil transportProfil left join fetch transportProfil.transports where transportProfil.id = :id",
                TransportProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<TransportProfil> fetchTransports(List<TransportProfil> transportProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, transportProfils.size()).forEach(index -> order.put(transportProfils.get(index).getId(), index));
        List<TransportProfil> result = entityManager
            .createQuery(
                "select transportProfil from TransportProfil transportProfil left join fetch transportProfil.transports where transportProfil in :transportProfils",
                TransportProfil.class
            )
            .setParameter(TRANSPORTPROFILS_PARAMETER, transportProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
