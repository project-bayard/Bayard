package edu.usm.it.mapper;

import edu.usm.domain.*;
import edu.usm.dto.ContactDto;
import edu.usm.mapper.ContactDtoMapper;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by scottkimball on 5/14/15.
 */
public class ContactDtoMapperTest {

    Contact initiator;

    @Before
    public void setup() {
        initiator = new Contact();
        initiator.setFirstName("first");
        initiator.setLastName("last");

    }

    @Test
    public void testConvertToContactDto() throws Exception {

        Contact contact = new Contact();
        contact.setFirstName("firstName");
        contact.setMiddleName("middleName");
        contact.setLastName("lastName");
        contact.setStreetAddress("address");
        contact.setAptNumber("apt");
        contact.setCity("city");
        contact.setZipCode("zipcode");
        contact.setLanguage("language");
        contact.setEmail("email");
        contact.setPhoneNumber1("phone1");
        contact.setPhoneNumber2("phone2");
        contact.setId("contactID");
        contact.setAssessment(0);
        contact.setOccupation("occupation");
        contact.setCreated(LocalDateTime.now().toString());
        contact.setLastModified(LocalDateTime.now().toString());

        /*collections and objects*/
        Committee committee = new Committee();
        committee.setId("committeeID");
        committee.setName("committee");
        Set<Contact> members = new HashSet<>();
        members.add(contact);
        committee.setMembers(members);
        Set<Committee> committeeSet = new HashSet<>();
        committeeSet.add(committee);
        contact.setCommittees(committeeSet);

        Organization organization = new Organization();
        organization.setName("organizationName");
        organization.setId("organizationID");
        organization.setMembers(members);
        Set<Organization> organizationSet = new HashSet<>();
        organizationSet.add(organization);
        contact.setOrganizations(organizationSet);

        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setId("memberInfoID");
        memberInfo.setSignedAgreement(true);
        memberInfo.setPaidDues(true);
        memberInfo.setStatus(1);
        memberInfo.setLastModified(LocalDateTime.now().toString());
        memberInfo.setCreated(LocalDateTime.now().toString());

        contact.setMemberInfo(memberInfo);

        /*Encounters*/
        Encounter encounter = new Encounter();
        encounter.setAssessment(0);
        encounter.setId("encounterID");
        encounter.setNotes("notes");
        encounter.setEncounterDate(LocalDate.now().toString());
        encounter.setType(EncounterType.EVENT);
        encounter.setCreated(LocalDateTime.now().toString());
        encounter.setLastModified(LocalDateTime.now().toString());
        encounter.setContact(contact);
        encounter.setInitiator(initiator);
        List<Encounter> encounterList = new ArrayList<>();
        encounterList.add(encounter);
        contact.setEncounters(encounterList);

        ContactDtoMapper mapper = new ContactDtoMapper();

        ContactDto contactDto = mapper.convertToContactDto(contact);

          /*basic fields*/
        assertNotNull(contact);
        assertEquals(contact.getId(), contactDto.getId());
        assertEquals(contact.getFirstName(), contactDto.getFirstName());
        assertEquals(contact.getMiddleName(), contactDto.getMiddleName());
        assertEquals(contact.getLastName(), contactDto.getLastName());
        assertEquals(contact.getStreetAddress(), contactDto.getStreetAddress());
        assertEquals(contact.getAptNumber(), contactDto.getAptNumber());
        assertEquals(contact.getCity(), contactDto.getCity());
        assertEquals(contact.getZipCode(), contactDto.getZipCode());
        assertEquals(contact.getLanguage(), contactDto.getLanguage());
        assertEquals(contact.getEmail(), contactDto.getEmail());
        assertEquals(contact.getPhoneNumber1(), contactDto.getPhoneNumber1());
        assertEquals(contact.getPhoneNumber2(), contactDto.getPhoneNumber2());
        assertEquals(contact.getAssessment(), contactDto.getAssessment());
        assertEquals(contact.getOccupation(), contactDto.getOccupation());
        assertEquals(contact.getLastModified(),contactDto.getLastModified());
        assertEquals(contact.getCreated(), contactDto.getCreated());

        /*Committees*/
        assertNotNull(contactDto.getCommittees());
        assertEquals(contactDto.getCommittees().size(), 1);
        assertEquals(contact.getCommittees().iterator().next().getName(), contactDto.getCommittees().iterator().next().getName());

        /*Organizations*/
        assertNotNull(contact.getOrganizations());
        assertEquals(contact.getOrganizations().size(), 1);
        assertEquals(contact.getOrganizations().iterator().next().getName(), contactDto.getOrganizations().iterator().next().getName());

           /*Encounters*/
        assertEquals(contactDto.getEncounters().get(0).getContact(), contact.getId());
        assertEquals(contactDto.getEncounters().get(0).getInitiator(), initiator.getId());
        assertEquals(contact.getEncounters().get(0).getAssessment(), contactDto.getEncounters().get(0).getAssessment());
        assertEquals(contact.getEncounters().get(0).getEncounterDate(), contactDto.getEncounters().get(0).getEncounterDate());
        assertEquals(contact.getEncounters().get(0).getNotes(), contactDto.getEncounters().get(0).getNotes());
        assertEquals(contact.getEncounters().get(0).getType(), contactDto.getEncounters().get(0).getType());
        assertEquals(contact.getEncounters().get(0).getLastModified(), contactDto.getEncounters().get(0).getLastModified());
        assertEquals(contact.getEncounters().get(0).getCreated(), contactDto.getEncounters().get(0).getCreated());

        /*MemberInfo*/
        assertNotNull(contactDto.getMemberInfo());
        assertNotNull(contact.getMemberInfo().getId(), contactDto.getMemberInfo().getId());
        assertEquals(contact.getMemberInfo().getStatus(), contactDto.getMemberInfo().getStatus());
        assertEquals(contact.getMemberInfo().hasPaidDues(), contactDto.getMemberInfo().hasPaidDues());
        assertEquals(contact.getMemberInfo().hasSignedAgreement(),contactDto.getMemberInfo().hasSignedAgreement());
        assertEquals(contact.getMemberInfo().getLastModified(),contactDto.getMemberInfo().getLastModified());
        assertEquals(contact.getMemberInfo().getCreated(), contactDto.getMemberInfo().getCreated());

    }
}
