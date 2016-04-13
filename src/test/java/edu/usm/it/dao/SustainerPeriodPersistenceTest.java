package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.DonorInfo;
import edu.usm.domain.SustainerPeriod;
import edu.usm.repository.ContactDao;
import edu.usm.repository.DonorInfoDao;
import edu.usm.repository.SustainerPeriodDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;

/**
 * Created by andrew on 1/23/16.
 */
public class SustainerPeriodPersistenceTest extends WebAppConfigurationAware{


    @Autowired
    DonorInfoDao donorInfoDao;

    @Autowired
    ContactDao contactDao;

    @Autowired
    SustainerPeriodDao sustainerPeriodDao;

    private DonorInfo donorInfo;
    private Contact contact;

    private SustainerPeriod sustainerPeriod;

    @After
    public void tearDown() {
        contactDao.deleteAll();
        donorInfoDao.deleteAll();
    }

    @Before
    public void setup() {
        donorInfo = new DonorInfo();
        donorInfo.setCurrentSustainer(true);

        contact = new Contact();
        contact.setFirstName("Test");
        contact.setEmail("Test Email");

        sustainerPeriod = new SustainerPeriod();
        sustainerPeriod.setPeriodStartDate(LocalDate.of(2015, Month.JANUARY, 1));
        sustainerPeriod.setMonthlyAmount(50);

        sustainerPeriod.setDonorInfo(donorInfo);
        donorInfo.addSustainerPeriod(sustainerPeriod);
        contact.setDonorInfo(donorInfo);

    }

    @Test
    @Transactional
    public void testCreateSustainerPeriod() {
        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());

        assertNotNull(contact.getDonorInfo());
        assertNotNull(contact.getDonorInfo().getSustainerPeriods());
        assertNotNull(contact.getDonorInfo().getSustainerPeriods().iterator().next());
    }

    @Test
    @Transactional
    public void testUpdateSustainerPeriod() {
        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());
        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();
        int newAmount = sustainerPeriod.getMonthlyAmount() + 1;
        sustainerPeriod.setMonthlyAmount(newAmount);
        contactDao.save(contact);

        contact = contactDao.findOne(contact.getId());
        assertEquals(newAmount, contact.getDonorInfo().getSustainerPeriods().iterator().next().getMonthlyAmount());

    }

    @Test
    public void testDeleteSustainerPeriod() {
        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());
        assertNotNull(contact);

        contactDao.delete(contact);

        contact = contactDao.findOne(contact.getId());
        assertNull(contact);

        donorInfo = donorInfoDao.findOne(donorInfo.getId());
        assertNull(donorInfo);

        sustainerPeriod = sustainerPeriodDao.findOne(sustainerPeriod.getId());
        assertNull(sustainerPeriod);
    }

    @Ignore
    @Transactional
    public void testCreateMultipleSustainerPeriods() {
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.setCurrentSustainer(true);

        Contact contact = new Contact();
        contact.setFirstName("Test");
        contact.setEmail("Test Email");

        SustainerPeriod sustainerPeriod = new SustainerPeriod();
        sustainerPeriod.setPeriodStartDate(LocalDate.of(2015, Month.JANUARY, 1));
        sustainerPeriod.setMonthlyAmount(50);

        sustainerPeriod.setDonorInfo(donorInfo);
        donorInfo.addSustainerPeriod(sustainerPeriod);
        contact.setDonorInfo(donorInfo);
        contactDao.save(contact);

        contact = contactDao.findOne(contact.getId());

        SustainerPeriod secondPeriod = new SustainerPeriod();
        secondPeriod.setMonthlyAmount(sustainerPeriod.getMonthlyAmount());
        secondPeriod.setPeriodStartDate(LocalDate.now());
        secondPeriod.setDonorInfo(contact.getDonorInfo());
        contact.getDonorInfo().addSustainerPeriod(secondPeriod);

        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());

        assertTrue(contact.getDonorInfo().getSustainerPeriods().contains(sustainerPeriod));
        assertEquals(contact.getDonorInfo().getSustainerPeriods().size(), 2);

    }



    @Test
    @Transactional
    public void testCalculateYearToDate() {
        sustainerPeriod.setPeriodStartDate(LocalDate.of(2015, 1, 1));
        sustainerPeriod.setCancelDate(LocalDate.of(2015, 3, 15));
        assertEquals(sustainerPeriod.getMonthlyAmount() * 3, sustainerPeriod.getTotalYearToDate());

        contactDao.save(contact);
        contact = contactDao.findOne(contact.getId());

        assertEquals(sustainerPeriod.getMonthlyAmount() * 3, contact.getDonorInfo().getSustainerPeriods().iterator().next().getTotalYearToDate());
    }

    @Test
    public void testCalculateTotalFencepostDates() {

        /*cancel = null*/
        sustainerPeriod.setCancelDate(null);
        sustainerPeriod.setPeriodStartDate(LocalDate.now().minusMonths(6));
        assertEquals(sustainerPeriod.getMonthlyAmount() * 6, sustainerPeriod.getTotalYearToDate());

        /*cancel = start + 6*/
        sustainerPeriod.setPeriodStartDate(LocalDate.of(2016, 2, 15).minusMonths(6));
        sustainerPeriod.setCancelDate(sustainerPeriod.getPeriodStartDate().plusMonths(6));
        assertEquals(sustainerPeriod.getMonthlyAmount() * 6, sustainerPeriod.getTotalYearToDate());

        /*cancel = start + 6 + 1 day*/
        sustainerPeriod.setCancelDate(sustainerPeriod.getPeriodStartDate().plusDays(1).plusMonths(6));
        assertEquals(sustainerPeriod.getMonthlyAmount() * 7, sustainerPeriod.getTotalYearToDate());

        /*cancel = start + 6 - 1 day*/
        sustainerPeriod.setCancelDate(sustainerPeriod.getPeriodStartDate().minusDays(1).plusMonths(6));
        assertEquals(sustainerPeriod.getMonthlyAmount() * 6, sustainerPeriod.getTotalYearToDate());

        /*cancel = before the start*/
        sustainerPeriod.setCancelDate(sustainerPeriod.getPeriodStartDate().minusMonths(6));
        assertEquals(0, sustainerPeriod.getTotalYearToDate());

        /*cancel = start*/
        sustainerPeriod.setCancelDate(sustainerPeriod.getPeriodStartDate());
        assertEquals(0, sustainerPeriod.getTotalYearToDate());

        /*cancel = start + 1 day*/
        sustainerPeriod.setCancelDate(LocalDate.of(sustainerPeriod.getPeriodStartDate().getYear(),
                sustainerPeriod.getPeriodStartDate().getMonth(),
                sustainerPeriod.getPeriodStartDate().getDayOfMonth() + 1));
        assertEquals(sustainerPeriod.getMonthlyAmount(), sustainerPeriod.getTotalYearToDate());

    }

}
