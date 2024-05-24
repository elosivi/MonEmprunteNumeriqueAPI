package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.Fonction;
import com.aubay.men.repository.FonctionRepository;
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
 * Spring Data Elasticsearch repository for the {@link Fonction} entity.
 */
public interface FonctionSearchRepository extends ElasticsearchRepository<Fonction, Long>, FonctionSearchRepositoryInternal {}

interface FonctionSearchRepositoryInternal {
    Page<Fonction> search(String query, Pageable pageable);

    Page<Fonction> search(Query query);

    @Async
    void index(Fonction entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FonctionSearchRepositoryInternalImpl implements FonctionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FonctionRepository repository;

    FonctionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FonctionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Fonction> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Fonction> search(Query query) {
        SearchHits<Fonction> searchHits = elasticsearchTemplate.search(query, Fonction.class);
        List<Fonction> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Fonction entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Fonction.class);
    }
}
