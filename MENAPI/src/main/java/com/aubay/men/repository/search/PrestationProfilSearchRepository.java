package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.PrestationProfil;
import com.aubay.men.repository.PrestationProfilRepository;
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
 * Spring Data Elasticsearch repository for the {@link PrestationProfil} entity.
 */
public interface PrestationProfilSearchRepository
    extends ElasticsearchRepository<PrestationProfil, Long>, PrestationProfilSearchRepositoryInternal {}

interface PrestationProfilSearchRepositoryInternal {
    Page<PrestationProfil> search(String query, Pageable pageable);

    Page<PrestationProfil> search(Query query);

    @Async
    void index(PrestationProfil entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PrestationProfilSearchRepositoryInternalImpl implements PrestationProfilSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PrestationProfilRepository repository;

    PrestationProfilSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PrestationProfilRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PrestationProfil> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<PrestationProfil> search(Query query) {
        SearchHits<PrestationProfil> searchHits = elasticsearchTemplate.search(query, PrestationProfil.class);
        List<PrestationProfil> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PrestationProfil entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PrestationProfil.class);
    }
}
