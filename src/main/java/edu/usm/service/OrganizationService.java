package edu.usm.service;

import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Service Interface for {@link Organization} domain objects.
 */
public interface OrganizationService {

    /**
     * Finds an Organization by its UUID
     * @param id
     * @return {@link Organization}
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Organization findById (String id) throws NullDomainReference.NullOrganization;

    /**
     * Returns all Organizations.
     * @return a List of {@link Organization}
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Organization> findAll();

    /**
     * Deletes the organization with the id if it exists.
     * @param id
     * @throws NullDomainReference.NullOrganization
     * @throws NullDomainReference.NullContact
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete (String id) throws NullDomainReference.NullOrganization, NullDomainReference.NullContact;

    /**
     * Updates the details of an organization.
     * @param id
     * @param organization
     * @throws NullDomainReference.NullOrganization
     * @throws ConstraintViolation
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateOrganizationDetails (String id, Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addDonation (String id, Donation donation) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeDonation (String id, String donationId) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();
}
