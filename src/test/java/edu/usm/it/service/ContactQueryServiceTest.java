package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.dto.PredicateDto;
import edu.usm.dto.QueryDto;
import edu.usm.query.QueryBuilderException;
import edu.usm.service.ContactService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class ContactQueryServiceTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Before
    public void setup() throws Exception {
        Contact contact = new Contact("first", "last", "email@email.com", "1234567890");
        contact.setAssessment(4);
        contact.setCity("Gorham");
        contact.setStreetAddress("100 Grant St.");
        contactService.create(contact);

        Contact contact1 = new Contact("first1", "last1", "email@gmail.com", "1234567891");
        contact1.setCity("Portland");
        contact1.setAssessment(5);
        contactService.create(contact1);

        Contact contact2 = new Contact("first", "last", "email@yahoo.com", "1234567890");
        contact2.setAssessment(6);
        contact2.setCity("Bangor");
        contactService.create(contact2);
    }

    @After
    public void teardown() {
        contactService.deleteAll();
    }


    @Test
    public void testStringValueEquals() throws Exception {
        QueryDto dto = new QueryDto();
        List<PredicateDto> predicates = new ArrayList<>();
        predicates.add(new PredicateDto("firstName", "eq", "first"));
        predicates.add(new PredicateDto("lastName", "eq", "last"));
        dto.setPredicateDtos(predicates);
        Set<Contact> contactList = contactService.findAllByQuery(dto);
        assertEquals(contactList.size(), 2);
    }

    @Test
    public void testStringValueNotEquals() throws Exception {
        QueryDto dto = new QueryDto();
        List<PredicateDto> predicates = new ArrayList<>();
        predicates.add(new PredicateDto("firstName", "neq", "first"));
        predicates.add(new PredicateDto("lastName", "neq", "last"));
        dto.setPredicateDtos(predicates);
        Set<Contact> contactList = contactService.findAllByQuery(dto);
        assertEquals(contactList.size(), 1);
    }

    @Test
    public void testStringValueContains() throws Exception {
        QueryDto dto = new QueryDto();
        List<PredicateDto> predicates = new ArrayList<>();
        predicates.add(new PredicateDto("streetAddress", "co", "Grant"));
        dto.setPredicateDtos(predicates);
        Set<Contact> contactList = contactService.findAllByQuery(dto);
        assertEquals(contactList.size(), 1);
    }


    @Test
    public void testIntegerValueLessThan() throws Exception {
        QueryDto dto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "lt", "5"));
        dto.setPredicateDtos(predicateDtos);
        Set<Contact> contactList = contactService.findAllByQuery(dto);
        assertEquals(contactList.size(), 1);
    }

    @Test
    public void testIntegerValueEquals() throws Exception {
        QueryDto dto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "eq", "5"));
        dto.setPredicateDtos(predicateDtos);
        Set<Contact> contactList = contactService.findAllByQuery(dto);
        assertEquals(contactList.size(), 1);
    }

    @Test
    public void testIntegerValueGreaterThan() throws Exception {
        QueryDto dto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "gt", "0"));
        dto.setPredicateDtos(predicateDtos);
        Set<Contact> contactList = contactService.findAllByQuery(dto);
        assertEquals(contactList.size(), 3);
    }

    @Test(expected = QueryBuilderException.class)
    public void testIntegerValueBadFormat() throws Exception {
        QueryDto dto = new QueryDto();
        List<PredicateDto> predicateDtos = new ArrayList<>();
        predicateDtos.add(new PredicateDto("assessment", "lt", "foo"));
        dto.setPredicateDtos(predicateDtos);
        Set<Contact> contactList = contactService.findAllByQuery(dto);
    }
}
