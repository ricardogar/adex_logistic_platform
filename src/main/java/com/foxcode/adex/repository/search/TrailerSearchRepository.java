package com.foxcode.adex.repository.search;

import com.foxcode.adex.domain.Trailer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Trailer entity.
 */
public interface TrailerSearchRepository extends ElasticsearchRepository<Trailer, Long> {
}
