package edu.usm.service;

import edu.usm.domain.Grant;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.GrantDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by andrew on 2/16/16.
 */
public interface GrantService {

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Grant findById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Grant> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Grant grant) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Grant grant) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateGrantDetails(Grant grant, GrantDto newDetails) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void delete(Grant grant) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();

}
