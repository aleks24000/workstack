package io.workstack.repository;

import io.workstack.domain.UserPrefs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserPrefs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPrefsRepository extends JpaRepository<UserPrefs, Long> {

}
