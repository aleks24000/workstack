package io.workstack.repository.search;

import io.workstack.domain.Deliverable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Deliverable entity.
 */
public interface DeliverableSearchRepository extends ElasticsearchRepository<Deliverable, Long> {
}
