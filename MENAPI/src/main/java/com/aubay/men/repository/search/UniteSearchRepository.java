package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.Unite;
import com.aubay.men.repository.UniteRepository;
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
 * Spring Data Elasticsearch repository for the {@link Unite} entity.
 */
public interface UniteSearchRepository extends ElasticsearchRepository<Unite, Long>, UniteSearchRepositoryInternal {}

interface UniteSearchRepositoryInternal {
    Page<Unite> search(String query, Pageable pageable);

    Page<Unite> search(Query query);

    @Async
    void index(Unite entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UniteSearchRepositoryInternalImpl implements UniteSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UniteRepository repository;

    UniteSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UniteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Unite> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Unite> search(Query query) {
        SearchHits<Unite> searchHits = elasticsearchTemplate.search(query, Unite.class);
        List<Unite> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Unite entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Unite.class);
    }
}
