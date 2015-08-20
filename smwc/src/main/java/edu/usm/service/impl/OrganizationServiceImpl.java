package edu.usm.service.impl;

import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.repository.OrganizationDao;
import edu.usm.service.BasicService;
import edu.usm.service.ContactService;
import edu.usm.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by scottkimball on 4/11/15.
 */

@Service
public class OrganizationServiceImpl extends BasicService implements OrganizationService {

    @Autowired
    private ContactService contactService;
    @Autowired
    private OrganizationDao organizationDao;

    private Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);


    @Override
    public Organization findById(String id) {
        if (null == id) {
            return null;
        }
        return organizationDao.findOne(id);
    }

    @Override
    public Set<Organization> findAll() {
        logger.debug("Finding all organizations");
        return  (Set<Organization> )organizationDao.findAll();
    }

    @Override
    public void delete(Organization organization) throws NullDomainReference.NullOrganization, NullDomainReference.NullContact {

        if (null == organization) {
            throw new NullDomainReference.NullOrganization();
        }

        updateLastModified(organization);

        /*Remove references to */

        if (organization.getMembers() != null) {
            CopyOnWriteArrayList<Contact> contacts = new CopyOnWriteArrayList<>(organization.getMembers());
            for(Contact contact : contacts) {
                contactService.removeContactFromOrganization(contact,organization);
            }
        }
        organizationDao.delete(organization);
    }

    @Override
    public void update(Organization organization) {
        logger.debug("Updating organization with ID: " + organization.getId());
        logger.debug("Time: " + LocalDateTime.now());
        updateLastModified(organization);
        organizationDao.save(organization);
    }


    @Override
    public String create(Organization organization) {
        logger.debug("Creating organization with ID: " + organization.getId());
        logger.debug("Time: " + LocalDateTime.now());
        organizationDao.save(organization);
        return organization.getId();
    }

    private void uncheckedDelete(Organization organization) {
        try {
            delete(organization);
        } catch (NullDomainReference e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {

        logger.debug("Deleting all Organizations");
        logger.debug("Time: " + LocalDateTime.now());
        Set<Organization> organizations = findAll();
        organizations.stream().forEach(this::uncheckedDelete);
    }
}
