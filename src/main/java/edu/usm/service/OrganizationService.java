package edu.usm.service;

import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by scottkimball on 4/11/15.
 */
public interface OrganizationService {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Organization findById (String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Organization> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete (Organization organization) throws NullDomainReference.NullOrganization, NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update (Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addDonation (Organization organization, Donation donation) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeDonation (Organization organization, Donation donation) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();
}
