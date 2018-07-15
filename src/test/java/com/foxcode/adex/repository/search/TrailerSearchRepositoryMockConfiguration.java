package com.foxcode.adex.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of TrailerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TrailerSearchRepositoryMockConfiguration {

    @MockBean
    private TrailerSearchRepository mockTrailerSearchRepository;

}
