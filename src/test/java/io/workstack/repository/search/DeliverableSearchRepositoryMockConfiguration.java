package io.workstack.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DeliverableSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DeliverableSearchRepositoryMockConfiguration {

    @MockBean
    private DeliverableSearchRepository mockDeliverableSearchRepository;

}
