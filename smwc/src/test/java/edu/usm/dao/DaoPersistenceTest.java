package edu.usm.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.DonorInfo;
import edu.usm.domain.Event;
import edu.usm.repository.ContactDao;
import edu.usm.repository.DonorInfoDao;
import edu.usm.repository.EventDao;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by scottkimball on 2/22/15.
 */


public class DaoPersistenceTest extends WebAppConfigurationAware{



    @Autowired
    ContactDao contactDao;

    @Autowired
    EventDao eventDao;

    @Autowired
    DonorInfoDao donorInfoDao;



    @Test
    public void testDaoSave () throws Exception {


        /*Basic info*/
        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setAddress("123 Fake St");
        contact.setEmail("email@gmail.com");

        /*Event*/
        Event event = new Event();
        event.setDate(LocalDate.of(2015, 01, 01));
        event.setLocation("location");
        event.setNotes("notes");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        event.setAttendees(contacts);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        contact.setAttendedEvents(eventList);

        /*Donations*/
        Donation donation = new Donation();
        donation.setDate(LocalDate.of(2015, 01, 01));
        donation.setAmount(100);
        donation.setComment("comment");
        donation.setIrsLetterSent(true);


        /*DonorInfo*/
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.setContact(contact);
        donorInfo.setDate(LocalDate.of(2015, 01, 01));

        List<Donation> donations = new ArrayList<>();
        donations.add(donation);
        donorInfo.setDonations(donations);
        contact.setDonorInfo(donorInfo);

        contactDao.save(contact);




        Contact fromDb = contactDao.findOne(contact.getId());

        /*Basic contact info*/
        assertNotNull(fromDb);
        assertEquals(fromDb.getId(),contact.getId());
        assertEquals(fromDb.getLastName(),contact.getLastName());
        assertEquals(fromDb.getFirstName(),contact.getFirstName());
        assertEquals(fromDb.getEmail(),contact.getEmail());


        /*Event info*/
        assertNotNull(fromDb.getAttendedEvents());
        Event fromEventDao = eventDao.findOne(event.getId());
        assertEquals(fromEventDao.getId(),event.getId());
        List<Contact> attendees = fromEventDao.getAttendees();
        assertNotNull(attendees);


        /*Donor Info*/
        assertEquals(donorInfoDao.findOne(contact.getDonorInfo().getId()).getId(),donorInfo.getId());


    }





}
