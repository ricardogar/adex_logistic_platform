package com.foxcode.adex.repository.search;

import com.foxcode.adex.domain.Distance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Distance entity.
 */
public interface DistanceSearchRepository extends ElasticsearchRepository<Distance, Long> {
}
