package edu.usm.service.impl;

import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
import edu.usm.domain.EncounterType;
import edu.usm.domain.exception.InvalidApiRequestException;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EncounterDto;
import edu.usm.repository.EncounterDao;
import edu.usm.service.BasicService;
import edu.usm.service.ContactService;
import edu.usm.service.EncounterService;
import edu.usm.service.EncounterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by Andrew on 7/16/2015.
 */
@Service
public class EncounterServiceImpl extends BasicService implements EncounterService {

    @Autowired
    private EncounterDao encounterDao;

    @Autowired
    private ContactService contactService;

    @Autowired
    private EncounterTypeService encounterTypeService;

    @Override
    public Encounter findById(String id) {
        if (null == id) {
            return null;
        }
        return encounterDao.findOne(id);
    }

    private Set<Encounter> findAll() {
        return encounterDao.findAll();
    }



    /*
    * Updates the core fields of the Encounter. If this is the Contact's most recent encounter,
    * will also update that Contact's needsFollowUp and assessment fields
    */
    @Override
    @Transactional
    public void updateEncounter(String contactId, String encounterId, EncounterDto dto) throws NullDomainReference, InvalidApiRequestException {
        Encounter encounter = findById(encounterId);
        if (encounter == null) {
            throw new NullDomainReference.NullEncounter(encounterId);

        }
        if (!contactId.equals(encounter.getContact().getId())) {
            throw new InvalidApiRequestException("The encounter with id "+encounterId+" does not belong to contact with id "+contactId);
        }

        if (null == dto) {
            encounterDao.save(encounter);
            return;
        }

        EncounterType type = encounterTypeService.findById(dto.getType());

        if (type == null) {
            throw new NullDomainReference.NullEncounterType(dto.getType());

        }

        boolean sameInitiator = null != encounter.getInitiator() && encounter.getInitiator().getId().equalsIgnoreCase(dto.getInitiatorId());
        if (!sameInitiator) {
            contactService.removeInitiator(encounter.getInitiator().getId(), encounter.getId());
        }

        Contact initiator = contactService.findById(dto.getInitiatorId());
        if (null == initiator) {
            throw new NullDomainReference.NullContact();
        }

        Contact contact = encounter.getContact();
        contact.getEncounters().remove(encounter);

        encounter.setAssessment(dto.getAssessment());
        encounter.setNotes(dto.getNotes());
        encounter.setEncounterDate(dto.getEncounterDate());
        encounter.setInitiator(initiator);
        encounter.setRequiresFollowUp(dto.requiresFollowUp());

        contact.getEncounters().add(encounter);

        if (encounter == contact.getEncounters().first()) {
            contactService.updateNeedsFollowUp(contact.getId(), encounter.requiresFollowUp());
        }
        contactService.updateAssessment(contact.getId(), contactService.getUpdatedAssessment(contact.getId()));
    }


    @Override
    public void deleteAll() {
        Set<Encounter> encounters = findAll();
        encounters.stream().forEach(this::uncheckedDelete);
    }

    protected void delete(String id) throws NullDomainReference{
        Encounter encounter = encounterDao.findOne(id);
        if (null == encounter) {
            throw new NullDomainReference.NullEncounter();
        }

        if (null != encounter.getContact()) {
            contactService.removeEncounter(encounter.getContact().getId(), encounter.getId());
        }
    }
}
