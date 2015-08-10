package edu.usm.service.impl;

import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
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
        return encounterDao.findOne(id);
    }

    private Set<Encounter> findAll() {
        return encounterDao.findAll();
    }

    @Override
    public void deleteEncounter(Encounter encounter) {
        if (null != encounter.getContact()) {
            contactService.removeEncounter(encounter.getContact(), encounter);
        }

        if (null != encounter.getInitiator()) {
            contactService.removeInitiator(encounter.getInitiator(), encounter);
        }

        encounterDao.delete(encounter);
    }

    /*
    * Updates the core fields of the Encounter. If this is the Contact's most recent encounter,
    * will also update that Contact's needsFollowUp and assessment fields
    */
    @Override
    public void updateEncounter(Encounter existingEncounter, EncounterDto dto) {

        if (null == dto) {
            encounterDao.save(existingEncounter);
            return;
        }

        Contact initiator = contactService.findById(dto.getInitiatorId());
        existingEncounter.setAssessment(dto.getAssessment());
        existingEncounter.setNotes(dto.getNotes());
        existingEncounter.setEncounterDate(dto.getEncounterDate());
        existingEncounter.setType(dto.getType());
        existingEncounter.setInitiator(initiator);
        existingEncounter.setRequiresFollowUp(dto.requiresFollowUp());
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
        encounters.stream().forEach(this::deleteEncounter);
    }
}
