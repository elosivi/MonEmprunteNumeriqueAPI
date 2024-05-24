package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.Prestation;
import com.aubay.men.repository.PrestationRepository;
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
 * Spring Data Elasticsearch repository for the {@link Prestation} entity.
 */
public interface PrestationSearchRepository extends ElasticsearchRepository<Prestation, Long>, PrestationSearchRepositoryInternal {}

interface PrestationSearchRepositoryInternal {
    Page<Prestation> search(String query, Pageable pageable);

    Page<Prestation> search(Query query);

    @Async
    void index(Prestation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PrestationSearchRepositoryInternalImpl implements PrestationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PrestationRepository repository;

    PrestationSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PrestationRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Prestation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Prestation> search(Query query) {
        SearchHits<Prestation> searchHits = elasticsearchTemplate.search(query, Prestation.class);
        List<Prestation> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Prestation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Prestation.class);
    }
}
