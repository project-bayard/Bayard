package edu.usm.service.impl;

import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.dto.EventDto;
import edu.usm.repository.EventDao;
import edu.usm.service.BasicService;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */

@Service
public class EventServiceImpl extends BasicService implements EventService {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CommitteeService committeeService;

    private Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Override
    public Set<Event> findAll() {
        return (Set<Event>) eventDao.findAll();
    }

    @Override
    public Event findById(String id) {
        return eventDao.findOne(id);
    }

    @Override
    public String create(EventDto dto, Committee committee) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setNotes(dto.getNotes());
        event.setLocation(dto.getLocation());
        event.setDateHeld(dto.getDateHeld());
        event.setCommittee(committee);
        return create(event);
    }

    @Override
    public String create(Event event) {
        logger.debug("creating event with ID: " + event.getId());
        eventDao.save(event);
        return event.getId();
    }

    @Override
    public void delete(Event event) {

        for (Contact contact : event.getAttendees()) {
            contactService.unattendEvent(contact,event);
        }
        eventDao.delete(event);
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all events!");
        Set<Event> events = findAll();
        events.stream().forEach(this::delete);
    }

    @Override
    public void update(Event event) {
        logger.debug("Updating contact with ID: " + event.getId());
        logger.debug("Time: " + LocalDateTime.now());
        updateLastModified(event);
        eventDao.save(event);
    }

    @Override
    public void update(Event event, EventDto eventDto) {
        if (eventDto.getCommitteeId() != null && !eventDto.getCommitteeId().isEmpty()) {
            event.setCommittee(committeeService.findById(eventDto.getCommitteeId()));
        }
        event.setNotes(eventDto.getNotes());
        event.setLocation(eventDto.getLocation());
        event.setDateHeld(eventDto.getDateHeld());
        event.setName(eventDto.getName());
        update(event);
    }
}
