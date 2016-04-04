package edu.usm.repository;

import edu.usm.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface EventDao extends CrudRepository<Event,String> {

    @Override
    HashSet<Event> findAll();

    HashSet<Event> findByName(String name);

    Event findByDonations_id(String id);

}
