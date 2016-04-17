package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Donation;
import edu.usm.domain.Event;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DonationDto;
import edu.usm.dto.EventDto;
import edu.usm.dto.Response;
import edu.usm.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST Controller for {@link Event}.
 */
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteEvent(@PathVariable("id") String id) throws ConstraintViolation, NullDomainReference{
        eventService.delete(id);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response updateEventDetails(@PathVariable("id") String id, @RequestBody EventDto eventDto) throws ConstraintViolation, NullDomainReference{
        eventService.update(id, eventDto);
        return Response.successGeneric();
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView({Views.EventList.class})
    public Set<Event> getAllEvents() {
        return eventService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createEvent(@RequestBody EventDto eventDto) throws ConstraintViolation, NullDomainReference{
        String eventId = eventService.create(eventDto, eventDto.getCommitteeId());
        return new Response(eventId,Response.SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView({Views.EventList.class})
    public Event getEventById(@PathVariable("id") String id) throws NullDomainReference {
        return eventService.findById(id);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/donations", produces={"application/json"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response addDonation(@PathVariable("id")String id, @RequestBody DonationDto dto) throws ConstraintViolation, NullDomainReference {
        eventService.addDonation(id, dto);
        return Response.successGeneric();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/donations/{donationId}", produces={"application/json"})
    public Response removeDonation(@PathVariable("id")String id, @PathVariable("donationId")String donationId) throws ConstraintViolation, NullDomainReference {
        Event event = eventService.findById(id);
        eventService.removeDonation(event.getId(), donationId);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/donations", produces={"application/json"})
    @JsonView(Views.DonationDetails.class)
    public Set<Donation> getDonations(@PathVariable("id")String id) throws NullDomainReference {
        return eventService.getAllEventDonations(id);
    }

}
