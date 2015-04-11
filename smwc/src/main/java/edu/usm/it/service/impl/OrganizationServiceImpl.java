package edu.usm.it.service.impl;

import com.google.common.collect.Lists;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.repository.ContactDao;
import edu.usm.repository.OrganizationDao;
import edu.usm.it.service.BasicService;
import edu.usm.it.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by scottkimball on 4/11/15.
 */

@Service
public class OrganizationServiceImpl extends BasicService implements OrganizationService {

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private OrganizationDao organizationDao;

    private Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);


    @Override
    public Organization findById(String id) {
        logger.debug("Finding contact with ID: " + id);
        return organizationDao.findOne(id);
    }

    @Override
    public List<Organization> findAll() {
        logger.debug("Finding all Contacts");
        return  Lists.newArrayList(organizationDao.findAll());
    }

    @Override
    public void delete(Organization organization) {
        logger.debug("Deleting contact with ID: " + organization.getId());
        logger.debug("Time: " + LocalDateTime.now());
        updateLastModified(organization);

        /*Remove references to */

        if (organization.getMembers() != null) {
            for(Contact contact : organization.getMembers()) {

                contact.getOrganizations().remove(organization);
                contactDao.save(contact);
            }
        }



        organizationDao.delete(organization);
    }

    @Override
    public void update(Organization organization) {
        logger.debug("Updating contact with ID: " + organization.getId());
        logger.debug("Time: " + LocalDateTime.now());
        updateLastModified(organization);
        organizationDao.save(organization);
    }


    @Override
    public void create(Organization organization) {
        update(organization);
        logger.debug("Creating contact with ID: " + organization.getId());
        logger.debug("Time: " + LocalDateTime.now());

    }

    @Override
    public void deleteAll() {

        logger.debug("Deleting all contacts.");
        logger.debug("Time: " + LocalDateTime.now());
        List<Organization> organizations = findAll();
        organizations.stream().forEach(this::delete);
    }
}
