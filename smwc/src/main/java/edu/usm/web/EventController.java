package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Event;
import edu.usm.domain.Views;
import edu.usm.dto.EventDto;
import edu.usm.mapper.EventMapper;
import edu.usm.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andrew on 5/30/2015.
 */
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    EventMapper eventMapper;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Set<Event> getAllEvents() {
        Set<Event> allEvents;
        System.out.println("Filler");
        allEvents = eventService.findAll();
        return allEvents;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody EventDto eventDto) {
        Event event = eventMapper.fromDto(eventDto);
        eventService.create(event);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/event/{id}", produces={"application/json"})
    public Event getContactById(@PathVariable("id") String id) {
        return eventService.findById(id);
    }

}
