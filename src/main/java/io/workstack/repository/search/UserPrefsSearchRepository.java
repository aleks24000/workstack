package io.workstack.repository.search;

import io.workstack.domain.UserPrefs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserPrefs entity.
 */
public interface UserPrefsSearchRepository extends ElasticsearchRepository<UserPrefs, Long> {
}
