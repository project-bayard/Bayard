package edu.usm.service.impl;

import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.domain.Organization;
import edu.usm.repository.EventDao;
import edu.usm.service.BasicService;
import edu.usm.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by scottkimball on 5/7/15.
 */

@Service
public class EventServiceImpl extends BasicService implements EventService {

    @Autowired
    private EventDao eventDao;

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
    public void create(Event event) {
        logger.debug("creating event with ID: " + event.getId());
        eventDao.save(event);
    }

    @Override
    public void delete(Event event) {
        eventDao.delete(event);
    }

    @Override
    public void deleteAll() {
        logger.debug("Deleting all events!");
        Set<Event> events = findAll();
        events.stream().forEach(this::delete);
    }
}
