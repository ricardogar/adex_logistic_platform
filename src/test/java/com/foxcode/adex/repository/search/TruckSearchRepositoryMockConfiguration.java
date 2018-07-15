package com.foxcode.adex.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of TruckSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TruckSearchRepositoryMockConfiguration {

    @MockBean
    private TruckSearchRepository mockTruckSearchRepository;

}
