package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.TransportProfil;
import com.aubay.men.repository.TransportProfilRepository;
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
 * Spring Data Elasticsearch repository for the {@link TransportProfil} entity.
 */
public interface TransportProfilSearchRepository
    extends ElasticsearchRepository<TransportProfil, Long>, TransportProfilSearchRepositoryInternal {}

interface TransportProfilSearchRepositoryInternal {
    Page<TransportProfil> search(String query, Pageable pageable);

    Page<TransportProfil> search(Query query);

    @Async
    void index(TransportProfil entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TransportProfilSearchRepositoryInternalImpl implements TransportProfilSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TransportProfilRepository repository;

    TransportProfilSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TransportProfilRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TransportProfil> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TransportProfil> search(Query query) {
        SearchHits<TransportProfil> searchHits = elasticsearchTemplate.search(query, TransportProfil.class);
        List<TransportProfil> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TransportProfil entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TransportProfil.class);
    }
}
