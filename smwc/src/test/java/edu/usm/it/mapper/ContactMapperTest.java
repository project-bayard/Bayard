package edu.usm.it.mapper;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.EncounterType;
import edu.usm.dto.*;
import edu.usm.mapper.ContactMapper;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by scottkimball on 5/10/15.
 */
public class ContactMapperTest extends WebAppConfigurationAware {

    @Autowired
    ContactMapper contactMapper;

    @Autowired
    OrganizationService organizationService;

    @After
    public void teardown() {
        organizationService.deleteAll();
    }

    @Test
    @Transactional
    public void testConvertToContact () throws Exception {
         /*basic fields*/
        ContactDto contactDto = new ContactDto();
        contactDto.setFirstName("firstName");
        contactDto.setMiddleName("middleName");
        contactDto.setLastName("lastName");
        contactDto.setStreetAddress("address");
        contactDto.setAptNumber("apt");
        contactDto.setCity("city");
        contactDto.setZipCode("zipcode");
        contactDto.setLanguage("language");
        contactDto.setEmail("email");
        contactDto.setPhoneNumber1("phone1");
        contactDto.setPhoneNumber2("phone2");
        contactDto.setId("id");
        contactDto.setAssessment(0);
        contactDto.setOccupation("occupation");
        contactDto.setCreated(LocalDateTime.now().toString());
        contactDto.setLastModified(LocalDateTime.now().toString());

        /*collections and objects*/
        CommitteeDto committeeDto = new CommitteeDto();
        committeeDto.setId("committeeID");
        committeeDto.setName("committee");
        Set<CommitteeDto> committeeDtoSet = new HashSet<>();
        committeeDtoSet.add(committeeDto);
        contactDto.setCommittees(committeeDtoSet);

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setName("organizationName");
        Set<OrganizationDto> organizationDtoSet = new HashSet<>();
        organizationDtoSet.add(organizationDto);
        contactDto.setOrganizations(organizationDtoSet);

        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setId("memberInfoID");
        memberInfoDto.setSignedAgreement(true);
        memberInfoDto.setPaidDues(true);
        memberInfoDto.setStatus(1);
        memberInfoDto.setLastModified(LocalDateTime.now().toString());
        memberInfoDto.setCreated(LocalDateTime.now().toString());

        contactDto.setMemberInfo(memberInfoDto);

        /*Encounters*/
        EncounterDto encounterDto = new EncounterDto();
        encounterDto.setAssessment(0);
        encounterDto.setNotes("notes");
        encounterDto.setEncounterDate(LocalDate.now().toString());
        encounterDto.setType(EncounterType.EVENT);
        encounterDto.setCreated(LocalDateTime.now().toString());
        encounterDto.setLastModified(LocalDateTime.now().toString());
        List<EncounterDto> encounterList = new ArrayList<>();
        encounterList.add(encounterDto);
        contactDto.setEncounters(encounterList);


        Contact contact = contactMapper.convertDtoToContact(contactDto);

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

        /*collections and objects*/
        assertNotNull(contact.getCommittees());
        assertEquals(contact.getCommittees().size(), 1);
        assertEquals(contact.getCommittees().iterator().next().getName(), committeeDto.getName());
        assertNotNull(contact.getOrganizations());
        assertEquals(contact.getOrganizations().size(), 1);
        assertEquals(contact.getOrganizations().iterator().next().getName(), organizationDto.getName());
        assertNotNull(contact.getMemberInfo());
        assertNotNull(contact.getMemberInfo().getId(),memberInfoDto.getId());
        assertEquals(contact.getMemberInfo().getStatus(),memberInfoDto.getStatus());
        assertEquals(contact.getMemberInfo().hasPaidDues(), contactDto.getMemberInfo().hasPaidDues());
        assertEquals(contact.getMemberInfo().hasSignedAgreement(),contactDto.getMemberInfo().hasSignedAgreement());
        assertEquals(contact.getMemberInfo().getLastModified(),contactDto.getMemberInfo().getLastModified());
        assertEquals(contact.getMemberInfo().getCreated(), contactDto.getMemberInfo().getCreated());


        /*Encounters*/
        assertEquals(contact.getEncounters().get(0).getContact().getId(),contact.getId());
        assertEquals(contact.getEncounters().get(0).getAssessment(),encounterDto.getAssessment());
        assertEquals(contact.getEncounters().get(0).getEncounterDate(),encounterDto.getEncounterDate());
        assertEquals(contact.getEncounters().get(0).getNotes(),encounterDto.getNotes());
        assertEquals(contact.getEncounters().get(0).getType(),encounterDto.getType());
        assertEquals(contact.getEncounters().get(0).getLastModified(), contactDto.getEncounters().get(0).getLastModified());
        assertEquals(contact.getEncounters().get(0).getCreated(), contactDto.getEncounters().get(0).getCreated());

    }
}
