package edu.usm.service.impl;

import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.repository.OrganizationDao;
import edu.usm.service.BasicService;
import edu.usm.service.ContactService;
import edu.usm.service.DonationService;
import edu.usm.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of {@link OrganizationService}
 */

@Service
public class OrganizationServiceImpl extends BasicService implements OrganizationService {

    @Autowired
    private ContactService contactService;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private DonationService donationService;

    private Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);


    @Override
    @Transactional
    public Organization findById(String id) throws NullDomainReference.NullOrganization {
        if (id == null) {
            return null;
        }
        Organization organization = findOrganization(id);

        if (organization.getMembers() == null) {
            organization.setMembers(new HashSet<>());
        }

        if (organization.getGroups() == null) {
            organization.setGroups(new HashSet<>());
        }

        organization.getMembers().size();
        organization.getGroups().size();
        return organization;
    }

    @Override
    public Set<Organization> findAll() {
        logger.debug("Finding all organizations");
        return  (Set<Organization> )organizationDao.findAll();
    }

    @Override
    @Transactional
    public void delete(String id) throws NullDomainReference.NullOrganization, NullDomainReference.NullContact {
        if (id == null) {
            throw new NullDomainReference.NullOrganization();
        }

        Organization organization = organizationDao.findOne(id);
        if (organization == null) {
            throw new NullDomainReference.NullOrganization(id);
        }

        /*Remove references to */
        Set<Contact> members = organization.getMembers();
        if (members != null) {
            CopyOnWriteArrayList<Contact> contacts = new CopyOnWriteArrayList<>(members);
            for(Contact contact : contacts) {
                contactService.removeContactFromOrganization(contact.getId(),organization.getId());
            }
        }
        updateLastModified(organization);
        organizationDao.delete(organization);
    }

    @Override
    @Transactional
    public Set<Donation> getDonations(String id) throws NullDomainReference.NullOrganization {
        Organization organization = findOrganization(id);
        Set<Donation> donations = organization.getDonations();
        if (donations != null) {
            donations.size();
            return donations;
        } else {
            return new HashSet<Donation>();
        }
    }

    @Override
    @Transactional
    public void addDonation(String id, Donation donation) throws NullDomainReference.NullOrganization, ConstraintViolation {
        Organization organization = findOrganization(id);
        organization.addDonation(donation);
        updateLastModified(donation);
        update(organization);
    }

    @Override
    @Transactional
    public void removeDonation(String id, String donationId) throws NullDomainReference.NullOrganization, ConstraintViolation {
        Organization organization = findOrganization(id);
        Donation donation = donationService.findById(donationId);
        if (null != organization.getDonations()) {
            organization.getDonations().remove(donation);
            updateLastModified(donation);
            update(organization);
        }
    }

    @Override
    public String create(Organization organization) throws ConstraintViolation, NullDomainReference.NullOrganization{
        validateOnCreate(organization);
        organizationDao.save(organization);
        return organization.getId();
    }

    @Override
    @Transactional
    public void deleteAll() {
        logger.debug("Deleting all Organizations");
        logger.debug("Time: " + LocalDateTime.now());
        Set<Organization> organizations = findAll();
        organizations.stream().forEach(this::uncheckedDelete);
    }

    @Override
    @Transactional
    public void updateOrganizationDetails(String id, Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation {
        Organization fromDb = findById(id);

        if (null == fromDb) {
            throw new NullDomainReference.NullOrganization(id);
        }

        if (null == organization.getMembers()) {
            organization.setMembers(fromDb.getMembers());
        }
        update(organization);
    }

    private void update(Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation{
        validateOnUpdate(organization);
        updateLastModified(organization);
        organizationDao.save(organization);
    }

    /*Assures that the organization is not null and that it has a name*/
    private void validateOrganization(Organization organization) throws ConstraintViolation, NullDomainReference.NullOrganization{
        if (null == organization) {
            throw new NullDomainReference.NullOrganization();
        }

        if (null == organization.getName()) {
            throw new ConstraintViolation(ConstraintMessage.ORGANIZATION_REQUIRED_NAME);
        }
    }

    /*Assures that if the organization name is changed that it doesn't conflict with another organization's name*/
    private void validateOnUpdate(Organization organization) throws ConstraintViolation, NullDomainReference.NullOrganization{
        validateOrganization(organization);
        Organization fromDb = organizationDao.findOneByName(organization.getName());
        if (fromDb != null && (organization.getId() == null || !organization.getId().equalsIgnoreCase(fromDb.getId()))) {
            throw new ConstraintViolation.NonUniqueDomainEntity(ConstraintMessage.ORGANIZATION_NON_UNIQUE, fromDb);
        }
    }

    /*Assures that a new organization doesn't have a name that conflicts with an existing organization*/
    private void validateOnCreate (Organization organization) throws ConstraintViolation, NullDomainReference.NullOrganization {
        validateOrganization(organization);
        Organization fromDb = organizationDao.findOneByName(organization.getName());
        if (fromDb != null) {
            throw new ConstraintViolation.NonUniqueDomainEntity(ConstraintMessage.ORGANIZATION_NON_UNIQUE, fromDb);
        }
    }

    private void uncheckedDelete(Organization organization) {
        try {
            delete(organization.getId());
        } catch (NullDomainReference e) {
            throw new RuntimeException(e);
        }
    }

    private Organization findOrganization(String id) throws NullDomainReference.NullOrganization {
        Organization organization = organizationDao.findOne(id);
        if (null == organization) {
            //TODO: 404 refactor
            throw new NullDomainReference.NullOrganization(id);
        } else {
            return organization;
        }
    }
}
