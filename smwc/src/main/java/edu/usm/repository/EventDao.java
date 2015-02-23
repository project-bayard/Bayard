package edu.usm.repository;

import edu.usm.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface EventDao extends CrudRepository<Event, Long> {
}
