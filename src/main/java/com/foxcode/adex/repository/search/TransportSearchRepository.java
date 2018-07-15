package com.foxcode.adex.repository.search;

import com.foxcode.adex.domain.Transport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Transport entity.
 */
public interface TransportSearchRepository extends ElasticsearchRepository<Transport, Long> {
}
