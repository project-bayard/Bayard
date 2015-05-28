package edu.usm.service;

import edu.usm.domain.Event;

/**
 * Created by scottkimball on 5/7/15.
 */
public interface EventService {

    Event findById(String id);
    void create (Event event);
}
