package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.InvalidApiRequestException;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.*;
import edu.usm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by scottkimball on 3/12/15.
 */

@RestController
@RequestMapping(value = "/contacts" )
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EventService eventService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private EncounterService encounterService;

    @Autowired
    private EncounterTypeService encounterTypeService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DonationService donationService;

    private Logger logger = LoggerFactory.getLogger(ContactController.class);


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getContacts() {
        return contactService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"}, produces = {"application/json"})
    public Response createContact(@RequestBody Contact contact) throws ConstraintViolation {
        String id = contactService.create(contact);
        return new Response(id, Response.SUCCESS);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView(Views.ContactDetails.class)
    public Contact getContactById(@PathVariable("id") String id) {
        return contactService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes={"application/json"})
    public Response updateContactById(@PathVariable("id") String id, @RequestBody Contact details) throws  ConstraintViolation,  NullDomainReference {
        contactService.updateBasicDetails(contactService.findById(id), details);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/events")
    @JsonView(Views.EventList.class)
    public Set<Event> getAttendedEvents(@PathVariable("id") String id) throws NullDomainReference{
        Contact contact = contactService.findById(id);
        if (null == contact) {
            throw new NullDomainReference.NullContact(id);
        }
        return contact.getAttendedEvents();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/events")
    public Response attendEvent(@PathVariable("id") String id, @RequestBody IdDto eventIdDto) throws ConstraintViolation,  NullDomainReference{
        Contact contact = contactService.findById(id);
        Event event = eventService.findById(eventIdDto.getId());

        try {
            contactService.attendEvent(contact, event);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullEvent e) {
            throw new NullDomainReference.NullEvent(eventIdDto.getId(), e);
        }

    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/events/{entityId}")
    public Response removeFromEvent(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws  ConstraintViolation, NullDomainReference {

        Contact contact =  contactService.findById(id);
        Event event = eventService.findById(entityId);

        try {
            contactService.unattendEvent(contact, event);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullEvent e) {
            throw new NullDomainReference.NullEvent(entityId, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/encounters")
    @JsonView(Views.ContactEncounterDetails.class)
    public SortedSet<Encounter> getAllEncountersForContact(@PathVariable("id") String id) throws NullDomainReference {
        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }
        return c.getEncounters();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/encounters/{entityId}")
    public Response deleteEncounter(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws  ConstraintViolation, NullDomainReference {

        Encounter encounter = encounterService.findById(entityId);

        try {
            encounterService.deleteEncounter(encounter);
            return Response.successGeneric();
        } catch (NullDomainReference.NullEncounter e) {
            throw new NullDomainReference.NullEncounter(entityId, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters/{encounterId}")
    public Response updateEncounter(@PathVariable("id") String id, @PathVariable("encounterId") String encounterId, @RequestBody EncounterDto encounterDto)
            throws ConstraintViolation,  NullDomainReference, InvalidApiRequestException{
        Contact contact = contactService.findById(id);
        Encounter encounter = encounterService.findById(encounterId);
        EncounterType encounterType = encounterTypeService.findById(encounterDto.getType());

        if (null == contact) {
            throw new NullDomainReference.NullContact(id);
        }

        if (!contact.getId().equals(encounter.getContact().getId())) {
            throw new InvalidApiRequestException("The encounter with id "+encounterId+" does not belong to contact with id "+id);
        }


        try {
            encounterService.updateEncounter(encounter, encounterType, encounterDto);
            return Response.successGeneric();
        } catch (NullDomainReference.NullEncounter e) {
            throw new NullDomainReference.NullEncounter(encounterId, e);
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(encounterDto.getInitiatorId(), e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/encounters")
    public Response createEncounter(@PathVariable("id") String id, @RequestBody EncounterDto encounterDto) throws  ConstraintViolation, NullDomainReference{
        Contact contact = contactService.findById(id);
        EncounterType encounterType = encounterTypeService.findById(encounterDto.getType());
        Contact initiator = contactService.findById(encounterDto.getInitiatorId());

        try {
            contactService.addEncounter(contact, initiator, encounterType, encounterDto);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id+" or "+encounterDto.getInitiatorId());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/initiators")
    @JsonView(Views.ContactList.class)
    public Set<Contact> getAllInitiators() {
        return contactService.findAllInitiators();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/organizations")
    @JsonView(Views.ContactOrganizationDetails.class)
    public Set<Organization> getAllOrganizationsForContact(@PathVariable("id") String id) throws NullDomainReference{

        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }

        return c.getOrganizations();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/organizations", consumes= {"application/json"})
    public Response addContactToOrganization(@PathVariable("id") String id, @RequestBody IdDto idDto) throws ConstraintViolation,  NullDomainReference {

        Organization organization = organizationService.findById(idDto.getId());
        Contact contact = contactService.findById(id);

        try {
            contactService.addContactToOrganization(contact,organization);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullOrganization e) {
            throw new NullDomainReference.NullOrganization(idDto.getId(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/organizations/{entityId}")
    public Response removeContactFromOrganization(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws ConstraintViolation,  NullDomainReference{

        Organization organization = organizationService.findById(entityId);
        Contact contact = contactService.findById(id);

        try {
            contactService.removeContactFromOrganization(contact, organization);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullOrganization e) {
            throw new NullDomainReference.NullOrganization(entityId, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/memberinfo")
    @JsonView(Views.MemberInfo.class)
    public MemberInfo getMemberInfo(@PathVariable("id") String id) throws NullDomainReference{

        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }

        return c.getMemberInfo();

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/memberinfo")
    public Response updateMemberInfo(@PathVariable("id") String id, @RequestBody MemberInfo memberInfo) throws  ConstraintViolation, NullDomainReference{
        Contact contact = contactService.findById(id);
        try {
            contactService.updateMemberInfo(contact, memberInfo);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/demographics")
    @JsonView(Views.DemographicDetails.class)
    public Contact getDemographicDetails(@PathVariable("id") String id) {
        return contactService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/demographics")
    public Response updateDemographicDetails(@PathVariable("id") String id, @RequestBody Contact details) throws  ConstraintViolation, NullDomainReference{
        Contact contact = contactService.findById(id);

        try {
            contactService.updateDemographicDetails(contact, details);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/committees", consumes= {"application/json"})
    public Response addContactToCommittee(@PathVariable("id") String id, @RequestBody IdDto idDto) throws  ConstraintViolation, NullDomainReference{

        Committee committee = committeeService.findById(idDto.getId());
        Contact contact = contactService.findById(id);

        try {
            contactService.addContactToCommittee(contact, committee);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullCommittee e) {
            throw new NullDomainReference.NullCommittee(idDto.getId(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/committees/{entityId}")
    public Response removeContactFromCommittee(@PathVariable("id") String id, @PathVariable("entityId") String entityId) throws  ConstraintViolation, NullDomainReference {

        Committee committee = committeeService.findById(entityId);
        Contact contact = contactService.findById(id);

        try {
            contactService.removeContactFromCommittee(contact, committee);
            return Response.successGeneric();
        } catch (NullDomainReference.NullContact e) {
            throw new NullDomainReference.NullContact(id, e);
        } catch (NullDomainReference.NullCommittee e) {
            throw new NullDomainReference.NullCommittee(id, e);
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/committees")
    @JsonView(Views.ContactCommitteeDetails.class)
    public Set<Committee> getAllCommitteesForContact(@PathVariable("id") String id) throws NullDomainReference{
        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }
        return c.getCommittees();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/groups")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.GroupPanel.class)
    public Set<Group> getContactGroups(@PathVariable("id") String id) throws NullDomainReference{
        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }
        return c.getGroups();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/groups", consumes= {"application/json"})
    public Response addContactToGroup(@PathVariable("id") String id, @RequestBody IdDto idDto) throws NullDomainReference{
        Contact c = contactService.findById(id);
        if (null == c) {
            throw new NullDomainReference.NullContact(id);
        }
        Group g = groupService.findById(idDto.getId());
        if (null == g) {
            throw new NullDomainReference.NullGroup(idDto.getId());
        }
        contactService.addToGroup(c, g);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/groups/{entityId}")
    public Response removeContactFromGroup(@PathVariable("id") String id, @PathVariable("entityId") String entityId) {

        Group group = groupService.findById(entityId);
        Contact contact = contactService.findById(id);

        contactService.removeFromGroup(contact, group);
        return Response.successGeneric();

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/donations", method = RequestMethod.POST, consumes={"application/json"}, produces = {"application/json"})
    public Response addDonation(@PathVariable("id")String id, @RequestBody Donation donation) throws NullDomainReference, ConstraintViolation {
        Contact c = contactService.findById(id);
        if (null == c) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullContact(id);
        }
        contactService.addDonation(c, donation);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/donations/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response removeDonation(@PathVariable("id")String id, @PathVariable("entityId")String donationId) throws NullDomainReference, ConstraintViolation {
        Contact c = contactService.findById(id);
        Donation d = donationService.findById(donationId);
        if (null == c) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullContact(id);
        }
        if (null == d || null == c.getDonorInfo() || !c.getDonorInfo().getDonations().contains(d)) {
            //TODO 404 refactor
        }
        contactService.removeDonation(c, d);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/sustainer/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.SustainerPeriodDetails.class)
    public SustainerPeriod getSustainerPeriod(@PathVariable("id")String id, @PathVariable("entityId")String sustainerPeriodId) throws NullDomainReference {
        Contact c = contactService.findById(id);
        SustainerPeriod s = contactService.findSustainerPeriodById(sustainerPeriodId);
        if (null == c) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullContact(id);
        }
        if (null == s || !c.getDonorInfo().getSustainerPeriods().contains(s)) {
            //TODO: 404 refactor
        }
        return s;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/sustainer", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.SustainerPeriodDetails.class)
    public Set<SustainerPeriod> getSustainerPeriodsForContact(@PathVariable("id")String id) throws NullDomainReference {
        Contact c = contactService.findById(id);
        if (null == c) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullContact(id);
        }
        if (null == c.getDonorInfo()) {
            return new HashSet<>();
        }
        return c.getDonorInfo().getSustainerPeriods();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/sustainer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createSustainerPeriod(@PathVariable("id")String id, @RequestBody SustainerPeriodDto dto) throws NullDomainReference, ConstraintViolation {
        Contact c = contactService.findById(id);
        if (null == c) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullContact(id);
        }
        contactService.createSustainerPeriod(c, dto);
        return Response.successGeneric();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/sustainer/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response updateSustainerPeriod (@PathVariable("id")String id, @PathVariable("entityId")String sustainerPeriodId, @RequestBody SustainerPeriodDto dto)
            throws NullDomainReference, ConstraintViolation {
        Contact c = contactService.findById(id);
        SustainerPeriod s = contactService.findSustainerPeriodById(sustainerPeriodId);
        if (null == c) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullContact(id);
        }
        if (null == s || !c.getDonorInfo().getSustainerPeriods().contains(s)) {
            //TODO: 404 refactor
        }
        contactService.updateSustainerPeriod(c, s, dto);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/sustainer/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteSustainerPeriod(@PathVariable("id")String id, @PathVariable("entityId")String sustainerPeriodId) throws NullDomainReference, ConstraintViolation {
        Contact c = contactService.findById(id);
        SustainerPeriod s = contactService.findSustainerPeriodById(sustainerPeriodId);
        if (null == c) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullContact(id);
        }
        if (null == s || !c.getDonorInfo().getSustainerPeriods().contains(s)) {
            //TODO: 404 refactor
        }
        contactService.deleteSustainerPeriod(c, s);
        return Response.successGeneric();

    }


}

