package com.foxcode.adex.repository.search;

import com.foxcode.adex.domain.CompanyFactory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanyFactory entity.
 */
public interface CompanyFactorySearchRepository extends ElasticsearchRepository<CompanyFactory, Long> {
}
