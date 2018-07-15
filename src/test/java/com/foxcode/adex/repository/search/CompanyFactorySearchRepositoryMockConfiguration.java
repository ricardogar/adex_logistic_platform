package com.foxcode.adex.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CompanyFactorySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CompanyFactorySearchRepositoryMockConfiguration {

    @MockBean
    private CompanyFactorySearchRepository mockCompanyFactorySearchRepository;

}
