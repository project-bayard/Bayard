package edu.usm.service;

import edu.usm.domain.Encounter;
import edu.usm.dto.EncounterDto;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by Andrew on 7/16/2015.
 */
public interface EncounterService {

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    Encounter findById(String id);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void updateEncounter(Encounter existingEncounter, EncounterDto dto);

    @PreAuthorize(value = "hasAuthority('ROLE_ELEVATED')")
    void deleteEncounter(Encounter encounter);

}
