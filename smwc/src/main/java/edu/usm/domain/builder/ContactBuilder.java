package edu.usm.domain.builder;

import edu.usm.domain.*;
import edu.usm.domain.dto.*;
import edu.usm.repository.OrganizationDao;
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
public class ContactBuilder {

    @Autowired
    private OrganizationDao organizationDao;
    private Logger logger = LoggerFactory.getLogger(ContactBuilder.class);



    public Contact buildContact(ContactDto contactDto) {

        /*Basic Fields*/
        Contact contact = convertToContact(contactDto);

        /*Organization*/
        updateOrganizations(contactDto.getOrganizations(), contact);

        return contact;
    }

    private void updateOrganizations (Set<OrganizationDto> organizationDtos, Contact contact) {

        Set<Organization> organizations = new HashSet<>();
        contact.setOrganizations(organizations);

        for (OrganizationDto organizationDto : organizationDtos) {

            /*organization already exists*/
            if (organizationDto.getId() != null) {

                Organization organization = organizationDao.findOne(organizationDto.getId());
                if (organization == null) {
                    logger.error("Could not find organization with ID: " + organizationDto.getId());

                } else {
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

        if (contactDto.getId() != null)
            contact = new Contact(contactDto.getId());
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


        contact.setCommittees(convertCommittees(contactDto));

        contact.setAttendedEvents(convertEvents(contactDto));
        contact.setEncounters(convertEncounters(contactDto));

        if (contactDto.getDonorInfo() != null)
            contact.setDonorInfo(contactDto.getDonorInfo().convertToDonorInfo());
        if (contactDto.getMemberInfo() != null)
            contact.setMemberInfo(contactDto.getMemberInfo().convertToMemberInfo());

        return contact;
    }


    private Set<Committee> convertCommittees (ContactDto contactDto) {

        if (contactDto.getCommittees() != null) {
            Set<Committee> committeeSet = new HashSet<>();

            for (CommitteeDto committeeDto : contactDto.getCommittees()) {
                committeeSet.add(committeeDto.convertToCommittee());
            }

            return committeeSet;
        }

        return null;

    }

    private List<Event> convertEvents(ContactDto contactDto) {
        if (contactDto.getAttendedEvents() != null) {
            List<Event> eventList = new ArrayList<>();
            for (EventDto eventDto : contactDto.getAttendedEvents()) {
                eventList.add(eventDto.convertToEvent());
            }

            return eventList;
        }

        return null;

    }


    private List<Encounter> convertEncounters (ContactDto contactDto) {

        if (contactDto.getEncounters() != null) {
            List<Encounter> encounterList = new ArrayList<>();
            for (EncounterDto encounterDto : contactDto.getEncounters()) {
                encounterList.add(encounterDto.convertToEncounter());
            }
            return encounterList;
        }

        return null;

    }
}
