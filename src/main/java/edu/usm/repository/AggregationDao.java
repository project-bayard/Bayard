package edu.usm.repository;

import edu.usm.domain.Aggregation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Aggregation}
 */
@Repository
public interface AggregationDao extends CrudRepository<Aggregation, String>{


}
