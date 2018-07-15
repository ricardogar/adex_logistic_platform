package com.foxcode.adex.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of TransportSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TransportSearchRepositoryMockConfiguration {

    @MockBean
    private TransportSearchRepository mockTransportSearchRepository;

}
