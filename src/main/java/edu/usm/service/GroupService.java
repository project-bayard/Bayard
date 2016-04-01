package edu.usm.service;

import edu.usm.domain.Aggregation;
import edu.usm.domain.Contact;
import edu.usm.domain.Group;
import edu.usm.domain.exception.ConstraintViolation;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by andrew on 10/8/15.
 */
public interface GroupService {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Group group) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Group group) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete(Group group);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Group findById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Group findByName(String groupName);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Group> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addAggregation(String aggregationId, Group group) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addAggregation(Aggregation aggregation, Group group) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeAggregation(String aggregationId, Group group) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeAggregation(Aggregation aggregation, Group group) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Contact> getAllMembers(String aggId);

}
