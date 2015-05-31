package edu.usm.service;

import edu.usm.domain.Event;

import javax.persistence.Entity;
import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */
public interface EventService {

    Event findById(String id);
    void create (Event event);
    Set<Event> findAll();
    void delete(Event event);
    void deleteAll();
}
