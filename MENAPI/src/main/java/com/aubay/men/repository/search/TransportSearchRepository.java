package com.aubay.men.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.aubay.men.domain.Transport;
import com.aubay.men.repository.TransportRepository;
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
 * Spring Data Elasticsearch repository for the {@link Transport} entity.
 */
public interface TransportSearchRepository extends ElasticsearchRepository<Transport, Long>, TransportSearchRepositoryInternal {}

interface TransportSearchRepositoryInternal {
    Page<Transport> search(String query, Pageable pageable);

    Page<Transport> search(Query query);

    @Async
    void index(Transport entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TransportSearchRepositoryInternalImpl implements TransportSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TransportRepository repository;

    TransportSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TransportRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Transport> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Transport> search(Query query) {
        SearchHits<Transport> searchHits = elasticsearchTemplate.search(query, Transport.class);
        List<Transport> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Transport entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Transport.class);
    }
}
