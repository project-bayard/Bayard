package edu.usm.service.impl;

import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.repository.CommitteeDao;
import edu.usm.service.BasicService;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by scottkimball on 4/15/15.
 */

@Service
public class CommitteeServiceImpl extends BasicService implements CommitteeService {

    @Autowired
    private CommitteeDao committeeDao;

    @Autowired
    private ContactService contactService;

    private Logger logger = LoggerFactory.getLogger(CommitteeServiceImpl.class);

    @Override
    public Committee findById(String id) {
        if (null == id) {
            return null;
        }
        return committeeDao.findOne(id);
    }

    @Override
    public Set<Committee> findAll() {
        logger.debug("Finding all Committees");
        return (Set<Committee>) committeeDao.findAll();
    }

    @Override
    public void delete(Committee committee) throws NullDomainReference {

        if (null == committee) {
            throw new NullDomainReference.NullCommittee();
        }

        logger.debug("Deleting committe with ID: " + committee.getId());
        updateLastModified(committee);
        if (committee.getMembers() != null) {
            for(Contact contact : committee.getMembers()) {
                contactService.removeContactFromCommittee(contact,committee);
            }
        }
        committeeDao.delete(committee);
    }

    @Override
    public void update(Committee committee) {
        logger.debug("Updating Committee with ID: " + committee.getId());
        updateLastModified(committee);
        committeeDao.save(committee);
    }

    @Override
    public String create(Committee committee) {
        committeeDao.save(committee);
        return committee.getId();
    }

    private void uncheckedDelete(Committee c) {
        try {
            delete(c);
        } catch (NullDomainReference e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        Set<Committee> committees = findAll();
        committees.stream().forEach(this::uncheckedDelete);
    }
}
