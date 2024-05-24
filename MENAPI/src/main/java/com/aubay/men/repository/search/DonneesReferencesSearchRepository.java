package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.DonneesReferences;
import com.aubay.men.repository.DonneesReferencesRepository;
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
 * Spring Data Elasticsearch repository for the {@link DonneesReferences} entity.
 */
public interface DonneesReferencesSearchRepository
    extends ElasticsearchRepository<DonneesReferences, Long>, DonneesReferencesSearchRepositoryInternal {}

interface DonneesReferencesSearchRepositoryInternal {
    Page<DonneesReferences> search(String query, Pageable pageable);

    Page<DonneesReferences> search(Query query);

    @Async
    void index(DonneesReferences entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DonneesReferencesSearchRepositoryInternalImpl implements DonneesReferencesSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DonneesReferencesRepository repository;

    DonneesReferencesSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DonneesReferencesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DonneesReferences> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DonneesReferences> search(Query query) {
        SearchHits<DonneesReferences> searchHits = elasticsearchTemplate.search(query, DonneesReferences.class);
        List<DonneesReferences> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DonneesReferences entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DonneesReferences.class);
    }
}
