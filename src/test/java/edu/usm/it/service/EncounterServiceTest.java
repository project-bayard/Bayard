package edu.usm.it.service;

import edu.usm.config.DateFormatConfig;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.domain.EncounterType;

import edu.usm.dto.EncounterDto;
import edu.usm.service.ContactService;
import edu.usm.service.EncounterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by andrew on 8/5/15.
 */
public class EncounterServiceTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Autowired
    EncounterService encounterService;

    private Contact contact;
    private Contact contact2;

    @Before
    public void setup() {
        contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");
        contact.setNeedsFollowUp(false);

        contact2 = new Contact();
        contact2.setFirstName("FirstName");
        contact2.setLastName("LastNAme");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");

    }

    @After
    public void teardown() throws NullDomainReference{
        contactService.deleteAll();
        encounterService.deleteAll();
    }

    @Test
    public void placeholder() {

    }

//    @Test
//    public void testUpdateEncounterFollowUp() throws NullDomainReference, ConstraintViolation{
//        String id1 = contactService.create(contact);
//        String id2 = contactService.create(contact2);
//        EncounterDto dto = new EncounterDto();
//        dto.setRequiresFollowUp(true);
//        dto.setNotes("notes");
//        dto.setAssessment(2);
//        dto.setEncounterDate(DateFormatConfig.formatDomainDate(LocalDate.now()));
//        dto.setInitiatorId(id2);
//        dto.setType("Call");
//        contact = contactService.findById(id1);
//        EncounterType encounterType = new EncounterType("CALL");
//
//        contactService.addEncounter(contact, contactService.findById(id2),encounterType, dto);
//        contact = contactService.findById(id1);
//        assertNotNull(contact.getEncounters());
//        assertTrue(contact.needsFollowUp());
//
//        Encounter fromDb = contact.getEncounters().first();
//        dto.setRequiresFollowUp(false);
//        encounterService.updateEncounter(fromDb,null, dto);
//        contact = contactService.findById(id1);
//        assertFalse(contact.needsFollowUp());
//
//    }


}
