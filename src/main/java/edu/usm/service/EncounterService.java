package edu.usm.service;

import edu.usm.domain.Encounter;
import edu.usm.domain.exception.InvalidApiRequestException;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EncounterDto;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by Andrew on 7/16/2015.
 */
public interface EncounterService {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Encounter findById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateEncounter(String contactId, String encounterId, EncounterDto dto) throws NullDomainReference, InvalidApiRequestException;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll() throws NullDomainReference.NullEncounter;

}
