package edu.usm.repository;

import edu.usm.domain.Aggregation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by andrew on 10/10/15.
 */
@Repository
public interface AggregationDao extends CrudRepository<Aggregation, String>{


}
