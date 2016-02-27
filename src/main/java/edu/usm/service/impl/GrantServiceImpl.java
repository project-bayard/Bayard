package edu.usm.service.impl;

import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.GrantDto;
import edu.usm.repository.GrantDao;
import edu.usm.service.BasicService;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.Set;

/**
 * Created by andrew on 2/16/16.
 */
@Service
public class GrantServiceImpl extends BasicService implements GrantService {

    @Autowired
    private GrantDao grantDao;

    @Autowired
    private FoundationService foundationService;

    @Override
    public void deleteAll() {
        findAll().stream().forEach(this::uncheckedDelete);
    }

    private void uncheckedDelete(Grant grant) {
        try {
            delete(grant);
        } catch (ConstraintViolation e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Grant grant) throws ConstraintViolation {
        updateLastModified(grant);
        Foundation foundation = grant.getFoundation();
        foundation.getGrants().remove(grant);
        foundationService.update(foundation);
    }

    @Override
    public void update(Grant grant) throws ConstraintViolation {
        try {
            updateLastModified(grant);
            grantDao.save(grant);
        } catch (DataAccessException | TransactionSystemException e) {
            //TODO: revisit when working on the new validation approach
            handlePersistenceException(grant);
        }
    }

    @Override
    public void updateGrantDetails(Grant grant, GrantDto newDetails) throws ConstraintViolation {
        grant = DtoTransformer.fromDto(newDetails, grant);
        update(grant);
    }

    @Override
    public String create(Grant grant) throws ConstraintViolation {
        try {
            updateLastModified(grant);
            grantDao.save(grant);
        } catch (DataAccessException | TransactionSystemException e) {
            //TODO: revisit when working on the new validation approach
            handlePersistenceException(grant);
        }
        return grant.getId();
    }

    private void handlePersistenceException(Grant grant) throws ConstraintViolation{
        if (null == grant.getName()) {
            throw new ConstraintViolation(ConstraintMessage.GRANT_REQUIRED_NAME);
        }
        if (null == grant.getFoundation()) {
            throw new ConstraintViolation(ConstraintMessage.GRANT_NO_FOUNDATION);
        }
        throw new ConstraintViolation();
    }

    @Override
    public Set<Grant> findAll() {
        return grantDao.findAll();
    }

    @Override
    public Grant findById(String id) {
        return grantDao.findOne(id);
    }
}
