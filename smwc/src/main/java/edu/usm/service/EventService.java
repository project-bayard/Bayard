package edu.usm.service;

import edu.usm.domain.Committee;
import edu.usm.domain.Event;
import edu.usm.dto.EventDto;

import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */
public interface EventService {

    Event findById(String id);
    String create (Event event);
    String create (EventDto dto, Committee committee);
    Set<Event> findAll();
    void delete(Event event);
    void deleteAll();
    void update(Event event);
    void update(Event event, EventDto eventDto);
}
