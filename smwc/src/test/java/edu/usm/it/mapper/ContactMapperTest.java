package edu.usm.it.mapper;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.EncounterType;
import edu.usm.dto.*;
import edu.usm.mapper.ContactMapper;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
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

    @Autowired
    ContactService contactService;

    @Autowired
    private CommitteeService committeeService;

    private Contact initiator;
    private Contact original;
    private Committee committee;


    @Before
    public void setup() {
        initiator = new Contact();
        initiator.setFirstName("first");
        initiator.setLastName("last");
        contactService.create(initiator);

        original = new Contact();
        original.setFirstName("firstName");
        original.setLastName("lastName");
        contactService.create(original);

        committee = new Committee();
        committee.setName("committeeName");

        committeeService.create(committee);
    }

    @After
    public void teardown() {
        organizationService.deleteAll();
        committeeService.deleteAll();
        contactService.deleteAll();

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
        contactDto.setAssessment(0);
        contactDto.setOccupation("occupation");
        contactDto.setId(original.getId());
        contactDto.setCreated(original.getCreated());
        contactDto.setLastModified(original.getLastModified());

        /*collections and objects*/
        CommitteeDto committeeDto = new CommitteeDto();
        committeeDto.setId(committee.getId());
        committeeDto.setName(committee.getName());
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
        encounterDto.setInitiator(initiator.getId());
        encounterDto.setAssessment(0);
        encounterDto.setNotes("notes");
        encounterDto.setEncounterDate(LocalDate.now().toString());
        encounterDto.setType(EncounterType.EVENT);
        encounterDto.setId("encounterID");
        encounterDto.setCreated(LocalDate.now().toString());
        encounterDto.setLastModified(encounterDto.getCreated());
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

        /*Committees*/
        assertNotNull(contact.getCommittees());
        assertEquals(contact.getCommittees().size(), 1);
        assertEquals(contact.getCommittees().iterator().next().getName(), committeeDto.getName());

        /*Organizations*/
        assertNotNull(contact.getOrganizations());
        assertEquals(contact.getOrganizations().size(), 1);
        assertEquals(contact.getOrganizations().iterator().next().getName(), organizationDto.getName());

        /*MemberInfo*/
        assertNotNull(contact.getMemberInfo());
        assertNotNull(contact.getMemberInfo().getId(),memberInfoDto.getId());
        assertEquals(contact.getMemberInfo().getStatus(),memberInfoDto.getStatus());
        assertEquals(contact.getMemberInfo().hasPaidDues(), contactDto.getMemberInfo().hasPaidDues());
        assertEquals(contact.getMemberInfo().hasSignedAgreement(),contactDto.getMemberInfo().hasSignedAgreement());
        assertEquals(contact.getMemberInfo().getLastModified(),contactDto.getMemberInfo().getLastModified());
        assertEquals(contact.getMemberInfo().getCreated(), contactDto.getMemberInfo().getCreated());


        /*Encounters*/
        assertEquals(contact.getEncounters().get(0).getContact().getId(),contactDto.getId());
        assertEquals(contact.getEncounters().get(0).getInitiator().getId(), initiator.getId());
        assertEquals(contact.getEncounters().get(0).getAssessment(),encounterDto.getAssessment());
        assertEquals(contact.getEncounters().get(0).getEncounterDate(),encounterDto.getEncounterDate());
        assertEquals(contact.getEncounters().get(0).getNotes(),encounterDto.getNotes());
        assertEquals(contact.getEncounters().get(0).getType(),encounterDto.getType());
        assertEquals(contact.getEncounters().get(0).getLastModified(), contactDto.getEncounters().get(0).getLastModified());
        assertEquals(contact.getEncounters().get(0).getCreated(), contactDto.getEncounters().get(0).getCreated());

    }
}
