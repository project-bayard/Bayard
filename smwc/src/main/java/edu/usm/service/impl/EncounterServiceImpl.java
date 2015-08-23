package edu.usm.service.impl;

import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.domain.EncounterType;
import edu.usm.dto.EncounterDto;
import edu.usm.repository.EncounterDao;
import edu.usm.service.BasicService;
import edu.usm.service.ContactService;
import edu.usm.service.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void deleteEncounter(Encounter encounter) throws NullDomainReference.NullEncounter{

        if (null == encounter) {
            throw new NullDomainReference.NullEncounter();
        }

        if (null != encounter.getContact()) {
            contactService.removeEncounter(encounter.getContact(), encounter);
        }

        if (null != encounter.getInitiator()) {
            contactService.removeInitiator(encounter.getInitiator(), encounter);
        }

        encounterDao.delete(encounter);
    }

    /*
    * Java 8 streams require called methods to throw only subclasses of RuntimeException.
     * This is a utility wrapper method deleteEncounter that converts the NullDomainReference
     * To a RuntimeException
    */
    private void uncheckedDeleteEncounter(Encounter encounter) {
        try {
            deleteEncounter(encounter);
        } catch (NullDomainReference.NullEncounter e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * Updates the core fields of the Encounter. If this is the Contact's most recent encounter,
    * will also update that Contact's needsFollowUp and assessment fields
    */
    @Override
    public void updateEncounter(Encounter existingEncounter,EncounterType encounterType,EncounterDto dto)
            throws NullDomainReference.NullEncounter, NullDomainReference.NullContact, NullDomainReference.NullEncounterType{

        if (null == existingEncounter) {
            throw new NullDomainReference.NullEncounter();
        }

        if (null == dto) {
            encounterDao.save(existingEncounter);
            return;
        }


        Contact initiator = contactService.findById(dto.getInitiatorId());
        if (null == initiator) {
            throw new NullDomainReference.NullContact();
        }

        existingEncounter.setAssessment(dto.getAssessment());
        existingEncounter.setNotes(dto.getNotes());
        existingEncounter.setEncounterDate(dto.getEncounterDate());
        existingEncounter.setInitiator(initiator);
        existingEncounter.setRequiresFollowUp(dto.requiresFollowUp());

         if (encounterType != null) {
            existingEncounter.setType(encounterType.getName());
        }


        encounterDao.save(existingEncounter);

        Contact contact = existingEncounter.getContact();
        if (existingEncounter == contact.getEncounters().first()) {
            contactService.updateNeedsFollowUp(contact, existingEncounter.requiresFollowUp());
        }

        contactService.updateAssessment(contact, contactService.getUpdatedAssessment(contact));

    }

    @Override
    public void deleteAll() {
        Set<Encounter> encounters = findAll();
        encounters.stream().forEach(this::uncheckedDeleteEncounter);
    }
}
