package com.foxcode.adex.repository.search;

import com.foxcode.adex.domain.CompanyMain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanyMain entity.
 */
public interface CompanyMainSearchRepository extends ElasticsearchRepository<CompanyMain, Long> {
}
