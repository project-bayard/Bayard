package edu.usm.mapper;

import edu.usm.domain.*;
import edu.usm.dto.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by scottkimball on 5/13/15.
 */

@Component
public class ContactDtoMapper {

    private ContactDto contactDto;

    public ContactDto convertToContactDto (Contact contact) {

        if (null == contact) {
            return null;
        }

        convertContactToDto(contact);

        if (contact.getOrganizations() != null)
            convertOrganizations(contact);

        if (contact.getCommittees() != null)
            convertCommittees(contact);

        if (contact.getEncounters() != null)
            convertEncounters(contact);

        if (contact.getMemberInfo() != null)
            convertMemberInfo(contact);

        if (contact.getAttendedEvents() != null) {
            convertAttendedEvents(contact);
        }

        //TODO the rest of the contact

        return contactDto;
    }

    private void convertContactToDto(Contact contact) {
        contactDto = new ContactDto();
        contactDto.setId(contact.getId());
        contactDto.setLastModified(contact.getLastModified());
        contactDto.setCreated(contact.getCreated());
        contactDto.setFirstName(contact.getFirstName());
        contactDto.setMiddleName(contact.getMiddleName());
        contactDto.setLastName(contact.getLastName());
        contactDto.setStreetAddress(contact.getStreetAddress());
        contactDto.setAptNumber(contact.getAptNumber());
        contactDto.setCity(contact.getCity());
        contactDto.setZipCode(contact.getZipCode());
        contactDto.setAssessment(contact.getAssessment());
        contactDto.setLanguage(contact.getLanguage());
        contactDto.setEmail(contact.getEmail());
        contactDto.setInterests(contact.getInterests());
        contactDto.setOccupation(contact.getOccupation());
        contactDto.setPhoneNumber2(contact.getPhoneNumber2());
        contactDto.setPhoneNumber1(contact.getPhoneNumber1());
    }

    private void convertOrganizations(Contact contact) {

        Set<OrganizationDto> organizationDtoList = new HashSet<>();
        for (Organization organization : contact.getOrganizations()) {
            OrganizationDto organizationDto = new OrganizationDto();
            organizationDto.setId(organization.getId());
            organizationDto.setCreated(organization.getCreated());
            organizationDto.setLastModified(organization.getLastModified());
            organizationDto.setName(organization.getName());

            Set<String> members = new HashSet<>();
            for (Contact member : organization.getMembers()) {
                members.add(member.getId());
            }
            organizationDto.setMembers(members);
            organizationDtoList.add(organizationDto);
        }

        contactDto.setOrganizations(organizationDtoList);
    }

    private void convertCommittees(Contact contact) {
        Set<CommitteeDto> committeeDtos = new HashSet<>();
        for (Committee committee : contact.getCommittees()) {
            CommitteeDto committeeDto = new CommitteeDto();
            committeeDto.setName(committee.getName());
            committeeDto.setId(committee.getId());
            committeeDto.setCreated(committee.getCreated());
            committeeDto.setLastModified(committee.getLastModified());

            Set<String> members = new HashSet<>();

            for (Contact member : committee.getMembers()) {
                members.add(member.getId());
            }

            committeeDto.setMembers(members);
            committeeDtos.add(committeeDto);
        }

        contactDto.setCommittees(committeeDtos);
    }

    private void convertEncounters (Contact contact) {
        List<EncounterDto> encounterDtos = new ArrayList<>();

        for (Encounter encounter : contact.getEncounters()) {
            EncounterDto encounterDto = new EncounterDto();
            encounterDto.setId(encounter.getId());
            encounterDto.setCreated(encounter.getCreated());
            encounterDto.setLastModified(encounter.getLastModified());

            encounterDto.setNotes(encounter.getNotes());
            encounterDto.setType(encounter.getType());
            encounterDto.setAssessment(encounter.getAssessment());
            encounterDto.setEncounterDate(encounter.getEncounterDate());
            encounterDto.setContact(encounter.getContact().getId());
            encounterDto.setInitiator(encounter.getInitiator().getId());
            encounterDtos.add(encounterDto);
        }

        contactDto.setEncounters(encounterDtos);
    }

    private void convertMemberInfo(Contact contact) {
        MemberInfoDto memberInfoDto = new MemberInfoDto();

        memberInfoDto.setId(contact.getMemberInfo().getId());
        memberInfoDto.setCreated(contact.getMemberInfo().getCreated());
        memberInfoDto.setLastModified(contact.getMemberInfo().getLastModified());
        memberInfoDto.setPaidDues(contact.getMemberInfo().hasPaidDues());
        memberInfoDto.setSignedAgreement(contact.getMemberInfo().hasSignedAgreement());
        memberInfoDto.setStatus(contact.getMemberInfo().getStatus());

        contactDto.setMemberInfo(memberInfoDto);

    }

    private void convertAttendedEvents(Contact contact) {
        List<EventDto> attendedEvents = new ArrayList<>();

        for (Event event : contact.getAttendedEvents()) {
            EventDto dto = new EventDto();
            dto.setId(event.getId());
            dto.setDate(event.getDateHeld());
            dto.setLocation(event.getLocation());
            dto.setName(event.getName());
            dto.setNotes(event.getNotes());

            Set<String> attendees = new HashSet<>();
            for (Contact c : event.getAttendees()) {
                attendees.add(c.getId());
            }
            dto.setAttendees(attendees);

            attendedEvents.add(dto);
        }

        contactDto.setAttendedEvents(attendedEvents);

    }




}
