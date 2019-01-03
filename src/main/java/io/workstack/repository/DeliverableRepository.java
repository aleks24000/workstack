package io.workstack.repository;

import io.workstack.domain.Deliverable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Deliverable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliverableRepository extends JpaRepository<Deliverable, Long> {

}
