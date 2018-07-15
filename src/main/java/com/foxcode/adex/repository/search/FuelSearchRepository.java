package com.foxcode.adex.repository.search;

import com.foxcode.adex.domain.Fuel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Fuel entity.
 */
public interface FuelSearchRepository extends ElasticsearchRepository<Fuel, Long> {
}
