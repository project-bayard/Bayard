package edu.usm.service;

import edu.usm.domain.InteractionRecord;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.InteractionRecordDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by andrew on 2/18/16.
 */
public interface InteractionRecordService {

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<InteractionRecord> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    InteractionRecord findById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(InteractionRecord record) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(InteractionRecord record) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(InteractionRecord record, InteractionRecordDto details) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete(InteractionRecord record) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();

}
