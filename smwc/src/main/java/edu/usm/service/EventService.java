package edu.usm.service;

import edu.usm.domain.Committee;
import edu.usm.domain.Event;
import edu.usm.dto.EventDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */
public interface EventService {

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    Event findById(String id);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    String create (Event event);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    String create (EventDto dto, Committee committee);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    Set<Event> findAll();

    @PreAuthorize(value = "hasRole('ROLE_ELEVATED')")
    void delete(Event event);

    @PreAuthorize(value = "hasRole('ROLE_SUPERUSER')")
    void deleteAll();

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    void update(Event event);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    void update(Event event, EventDto eventDto);
}
