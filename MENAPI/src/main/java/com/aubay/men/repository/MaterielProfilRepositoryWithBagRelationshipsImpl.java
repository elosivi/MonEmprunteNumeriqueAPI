package com.aubay.men.repository;

import com.aubay.men.domain.MaterielProfil;
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
public class MaterielProfilRepositoryWithBagRelationshipsImpl implements MaterielProfilRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MATERIELPROFILS_PARAMETER = "materielProfils";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MaterielProfil> fetchBagRelationships(Optional<MaterielProfil> materielProfil) {
        return materielProfil.map(this::fetchProfils).map(this::fetchPrestations).map(this::fetchMateriels);
    }

    @Override
    public Page<MaterielProfil> fetchBagRelationships(Page<MaterielProfil> materielProfils) {
        return new PageImpl<>(
            fetchBagRelationships(materielProfils.getContent()),
            materielProfils.getPageable(),
            materielProfils.getTotalElements()
        );
    }

    @Override
    public List<MaterielProfil> fetchBagRelationships(List<MaterielProfil> materielProfils) {
        return Optional.of(materielProfils)
            .map(this::fetchProfils)
            .map(this::fetchPrestations)
            .map(this::fetchMateriels)
            .orElse(Collections.emptyList());
    }

    MaterielProfil fetchProfils(MaterielProfil result) {
        return entityManager
            .createQuery(
                "select materielProfil from MaterielProfil materielProfil left join fetch materielProfil.profils where materielProfil.id = :id",
                MaterielProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<MaterielProfil> fetchProfils(List<MaterielProfil> materielProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, materielProfils.size()).forEach(index -> order.put(materielProfils.get(index).getId(), index));
        List<MaterielProfil> result = entityManager
            .createQuery(
                "select materielProfil from MaterielProfil materielProfil left join fetch materielProfil.profils where materielProfil in :materielProfils",
                MaterielProfil.class
            )
            .setParameter(MATERIELPROFILS_PARAMETER, materielProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    MaterielProfil fetchPrestations(MaterielProfil result) {
        return entityManager
            .createQuery(
                "select materielProfil from MaterielProfil materielProfil left join fetch materielProfil.prestations where materielProfil.id = :id",
                MaterielProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<MaterielProfil> fetchPrestations(List<MaterielProfil> materielProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, materielProfils.size()).forEach(index -> order.put(materielProfils.get(index).getId(), index));
        List<MaterielProfil> result = entityManager
            .createQuery(
                "select materielProfil from MaterielProfil materielProfil left join fetch materielProfil.prestations where materielProfil in :materielProfils",
                MaterielProfil.class
            )
            .setParameter(MATERIELPROFILS_PARAMETER, materielProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    MaterielProfil fetchMateriels(MaterielProfil result) {
        return entityManager
            .createQuery(
                "select materielProfil from MaterielProfil materielProfil left join fetch materielProfil.materiels where materielProfil.id = :id",
                MaterielProfil.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<MaterielProfil> fetchMateriels(List<MaterielProfil> materielProfils) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, materielProfils.size()).forEach(index -> order.put(materielProfils.get(index).getId(), index));
        List<MaterielProfil> result = entityManager
            .createQuery(
                "select materielProfil from MaterielProfil materielProfil left join fetch materielProfil.materiels where materielProfil in :materielProfils",
                MaterielProfil.class
            )
            .setParameter(MATERIELPROFILS_PARAMETER, materielProfils)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
