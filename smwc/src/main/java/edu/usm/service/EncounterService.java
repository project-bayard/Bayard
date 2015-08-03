package edu.usm.service;

import edu.usm.domain.Encounter;
import edu.usm.dto.EncounterDto;

/**
 * Created by Andrew on 7/16/2015.
 */
public interface EncounterService {

    Encounter findById(String id);
    void updateEncounter(Encounter existingEncounter, EncounterDto dto);
    void updateEncounter(Encounter encounter);
    void deleteEncounter(Encounter encounter);

}
