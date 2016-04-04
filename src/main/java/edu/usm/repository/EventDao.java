package edu.usm.repository;

import edu.usm.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Repository for {@link Event}
 */

@Repository
public interface EventDao extends CrudRepository<Event,String> {

    /**
     * Returns all existing events.
     * @return {@link java.util.Set} of {@link Event}
     */
    @Override
    HashSet<Event> findAll();

    /**
     * Finds an event by its name, if it exists.
     * @param name The name of the event.
     * @return {@link java.util.Set} of {@link Event}
     */
    HashSet<Event> findByName(String name);

}
