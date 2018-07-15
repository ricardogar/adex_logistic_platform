package com.foxcode.adex.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DistanceSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DistanceSearchRepositoryMockConfiguration {

    @MockBean
    private DistanceSearchRepository mockDistanceSearchRepository;

}
