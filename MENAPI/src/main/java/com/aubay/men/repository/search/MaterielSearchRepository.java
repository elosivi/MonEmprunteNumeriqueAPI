package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.Materiel;
import com.aubay.men.repository.MaterielRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Materiel} entity.
 */
public interface MaterielSearchRepository extends ElasticsearchRepository<Materiel, Long>, MaterielSearchRepositoryInternal {}

interface MaterielSearchRepositoryInternal {
    Page<Materiel> search(String query, Pageable pageable);

    Page<Materiel> search(Query query);

    @Async
    void index(Materiel entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MaterielSearchRepositoryInternalImpl implements MaterielSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MaterielRepository repository;

    MaterielSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MaterielRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Materiel> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Materiel> search(Query query) {
        SearchHits<Materiel> searchHits = elasticsearchTemplate.search(query, Materiel.class);
        List<Materiel> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Materiel entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Materiel.class);
    }
}
