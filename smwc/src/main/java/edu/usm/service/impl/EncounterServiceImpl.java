package edu.usm.service.impl;

import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
import edu.usm.dto.EncounterDto;
import edu.usm.repository.EncounterDao;
import edu.usm.service.BasicService;
import edu.usm.service.ContactService;
import edu.usm.service.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void deleteEncounter(Encounter encounter) {
        contactService.removeEncounter(encounter.getContact(), encounter);
        contactService.removeInitiator(encounter.getInitiator(), encounter);
        encounterDao.delete(encounter);
    }

    @Override
    public void updateEncounter(Encounter existingEncounter, EncounterDto dto) {
        Contact initiator = contactService.findById(dto.getInitiatorId());
        existingEncounter.setAssessment(dto.getAssessment());
        existingEncounter.setNotes(dto.getNotes());
        existingEncounter.setEncounterDate(dto.getEncounterDate());
        existingEncounter.setType(dto.getType());
        existingEncounter.setInitiator(initiator);
        encounterDao.save(existingEncounter);
    }

    @Override
    public void updateEncounter(Encounter encounter) {
        encounterDao.save(encounter);
    }
}
