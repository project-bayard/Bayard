package edu.usm.service;

import edu.usm.domain.Event;

import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */
public interface EventService {

    Event findById(String id);
    String create (Event event);
    Set<Event> findAll();
    void delete(Event event);
    void deleteAll();
    void update(Event event);
}
