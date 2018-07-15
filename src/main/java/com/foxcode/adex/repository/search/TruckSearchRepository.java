package com.foxcode.adex.repository.search;

import com.foxcode.adex.domain.Truck;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Truck entity.
 */
public interface TruckSearchRepository extends ElasticsearchRepository<Truck, Long> {
}
