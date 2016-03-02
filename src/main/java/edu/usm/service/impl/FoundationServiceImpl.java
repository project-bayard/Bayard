package edu.usm.service.impl;

import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.FoundationDto;
import edu.usm.dto.GrantDto;
import edu.usm.repository.FoundationDao;
import edu.usm.service.BasicService;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import edu.usm.service.InteractionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.Set;

/**
 * Created by andrew on 2/12/16.
 */
@Service
public class FoundationServiceImpl extends BasicService implements FoundationService {

    @Autowired
    private FoundationDao foundationDao;

    @Autowired
    private GrantService grantService;

    @Autowired
    private InteractionRecordService interactionService;

    @Override
    public Foundation findById(String id) {
        return foundationDao.findOne(id);
    }

    @Override
    public Foundation findByName(String name) {
        return foundationDao.findByName(name);
    }

    @Override
    public Set<Foundation> findAll() {
        return (Set<Foundation>) foundationDao.findAll();
    }

    @Override
    public String create(Foundation foundation) throws ConstraintViolation {
        try {
            updateLastModified(foundation);
            foundationDao.save(foundation);
        } catch (DataAccessException | TransactionSystemException e) {
            //TODO: revisit when working on the new validation approach
            handlePersistenceException(foundation);
        }
        return foundation.getId();
    }

    private void handlePersistenceException(Foundation foundation) throws ConstraintViolation{
        if (null == foundation.getName()) {
            throw new ConstraintViolation(ConstraintMessage.FOUNDATION_REQUIRED_NAME);
        }
        Foundation existing = findByName(foundation.getName());
        if (null != existing) {
            throw new ConstraintViolation.NonUniqueDomainEntity(ConstraintMessage.FOUNDATION_DUPLICATE_NAME, existing);
        }
        throw new ConstraintViolation();
    }

    @Override
    public void update(Foundation foundation) throws ConstraintViolation{
        try {
            updateLastModified(foundation);
            foundationDao.save(foundation);
        } catch (DataAccessException e) {
            //TODO: revisit when working on the new validation approach
            handlePersistenceException(foundation);
        }
    }

    @Override
    public void update(Foundation foundation, FoundationDto dto) throws ConstraintViolation{
        foundation.setName(dto.getName());
        foundation.setAddress(dto.getAddress());
        foundation.setWebsite(dto.getWebsite());
        foundation.setPhoneNumber(dto.getPhoneNumber());
        foundation.setCurrentGrantor(dto.isCurrentGrantor());
        foundation.setPrimaryContactName(dto.getPrimaryContactName());
        foundation.setPrimaryContactTitle(dto.getPrimaryContactTitle());
        foundation.setPrimaryContactPhone(dto.getPrimaryContactPhone());
        foundation.setPrimaryContactEmail(dto.getPrimaryContactEmail());
        foundation.setSecondaryContactName(dto.getSecondaryContactName());
        foundation.setSecondaryContactTitle(dto.getSecondaryContactTitle());
        foundation.setSecondaryContactPhone(dto.getSecondaryContactPhone());
        foundation.setSecondaryContactEmail(dto.getSecondaryContactEmail());

        update(foundation);
    }

    @Override
    public void delete(Foundation foundation) {
        updateLastModified(foundation);
        foundationDao.delete(foundation);
    }

    @Override
    public void deleteAll() {
        Set<Foundation> foundations = findAll();
        foundations.stream().forEach(this::delete);
    }

    @Override
    public void createInteractionRecord(Foundation foundation, InteractionRecord interactionRecord) throws ConstraintViolation {
        updateLastModified(foundation);
        updateLastModified(interactionRecord);
        interactionRecord.setFoundation(foundation);
        foundation.addInteractionRecord(interactionRecord);
        interactionService.create(interactionRecord);
    }

    @Override
    public void createGrant(Foundation foundation, Grant grant) throws ConstraintViolation {
        updateLastModified(foundation);
        foundation.addGrant(grant);
        grant.setFoundation(foundation);
        grantService.create(grant);
    }

    @Override
    public void createGrant(Foundation foundation, GrantDto dto) throws ConstraintViolation {
        Grant grant = new Grant();
        grant = DtoTransformer.fromDto(dto, grant);
        createGrant(foundation, grant);
    }

    @Override
    public void deleteGrant(Foundation foundation, Grant grant) throws ConstraintViolation {
        foundation.getGrants().remove(grant);
        updateLastModified(grant);
        update(foundation);
    }


}
