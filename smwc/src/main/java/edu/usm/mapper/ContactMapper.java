package edu.usm.mapper;

import edu.usm.domain.*;
import edu.usm.dto.*;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import edu.usm.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by scottkimball on 5/9/15.
 */

@Component
@Scope("singleton")
public class ContactMapper {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private EventService eventService;


    private Logger logger = LoggerFactory.getLogger(ContactMapper.class);



    public Contact convertDtoToContact(ContactDto contactDto) {

        Contact contact = convertToContact(contactDto);

        if (contactDto.getOrganizations() != null)
            convertOrganizations(contactDto.getOrganizations(), contact);

        if (contactDto.getCommittees() != null)
            convertCommittees(contactDto.getCommittees(), contact);

        if (contactDto.getAttendedEvents() != null) {
            convertEvents(contactDto, contact);
        }

        //TODO events, etc

        return contact;
    }

    private void convertEvents(ContactDto dto, Contact contact) {

        Set<Event> events = new HashSet<>();
        contact.setAttendedEvents(events);

        for (EventDto eventDto : dto.getAttendedEvents()) {

            /*Event already exists*/
            if (eventDto.getId() != null) {
                Event event = eventService.findById(eventDto.getId());
                if (event == null) {
                    logger.error("Could not find event with ID: "+eventDto.getId());
                } else {
                    events.add(event);
                }
            }
        }

    }

    private void convertOrganizations(Set<OrganizationDto> organizationDtos, Contact contact) {

        Set<Organization> organizations = new HashSet<>();
        contact.setOrganizations(organizations);

        for (OrganizationDto organizationDto : organizationDtos) {

            /*organization already exists*/
            if (organizationDto.getId() != null) {

                Organization organization = organizationService.findById(organizationDto.getId());
                if (organization == null) {
                    logger.error("Could not find organization with ID: " + organizationDto.getId());

                } else {
                    if (organization.getMembers() == null)
                        organization.setMembers(new HashSet<>());

                    organization.getMembers().add(contact);
                    organizations.add(organization);
                }

            /*organization was created when contact was created*/
            } else {
                Organization organization = new Organization();
                organization.setName(organizationDto.getName());
                Set<Contact> members = new HashSet<>();
                members.add(contact);
                organization.setMembers(members);
                organizations.add(organization);

            }
        }

    }

    private Contact convertToContact (ContactDto contactDto) {
        Contact contact;

        if (contactDto.getId() != null) {
            contact = new Contact(contactDto.getId());
            contact.setLastModified(contactDto.getLastModified());
            contact.setCreated(contactDto.getCreated());
        }

        else
            contact = new Contact();

        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setMiddleName(contactDto.getMiddleName());
        contact.setStreetAddress(contactDto.getStreetAddress());
        contact.setAptNumber(contactDto.getAptNumber());
        contact.setCity(contactDto.getCity());
        contact.setZipCode(contactDto.getZipCode());
        contact.setLanguage(contactDto.getLanguage());
        contact.setOccupation(contactDto.getOccupation());

        contact.setAssessment(contactDto.getAssessment());
        contact.setEmail(contactDto.getEmail());
        contact.setInterests(contactDto.getInterests());
        contact.setPhoneNumber1(contactDto.getPhoneNumber1());
        contact.setPhoneNumber2(contactDto.getPhoneNumber2());
        
        if (contactDto.getDonorInfo() != null)
            contact.setDonorInfo(contactDto.getDonorInfo().convertToDonorInfo());
        if (contactDto.getMemberInfo() != null)
            contact.setMemberInfo(contactDto.getMemberInfo().convertToMemberInfo());

        return contact;
    }


    private void convertCommittees(Set<CommitteeDto> committeeDtos, Contact contact) {

        Set<Committee> committees = new HashSet<>();
        contact.setCommittees(committees);

        for (CommitteeDto committeeDto : committeeDtos) {

            /*organization already exists*/
            if (committeeDto.getId() != null) {

                Committee committee = committeeService.findById(committeeDto.getId());
                if (committee == null) {
                    logger.error("Could not find committe with ID: " + committeeDto.getId());

                } else {
                    if (committee.getMembers() == null)
                        committee.setMembers(new HashSet<>());
                    committee.getMembers().add(contact);
                    committees.add(committee);
                }

            } else {
                logger.error("Committee '%s' has no ID", committeeDto.getName());
            }
        }

    }
}
