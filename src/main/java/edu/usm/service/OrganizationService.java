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
     * Updates the details of an {@link Organization}. Assumes that a null set of members is an omission
     * of that set.
     * @param id
     * @param organization
     * @throws NullDomainReference.NullOrganization
     * @throws ConstraintViolation
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateOrganizationDetails (String id, Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation;

    /**
     * Associates a donation with an organization.
     * @param id
     * @param donation
     * @throws NullDomainReference.NullOrganization
     * @throws ConstraintViolation
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addDonation (String id, Donation donation) throws NullDomainReference.NullOrganization, ConstraintViolation;

    /**
     * Disassociates a donation with an organization.
     * @param id
     * @param donationId
     * @throws NullDomainReference.NullOrganization
     * @throws ConstraintViolation
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeDonation (String id, String donationId) throws NullDomainReference.NullOrganization, ConstraintViolation;

    /**
     * Returns a set of donations associated with an organization.
     * @param id
     * @return
     * @throws NullDomainReference.NullOrganization
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Donation> getDonations(String id) throws NullDomainReference.NullOrganization;

    /**
     * Creates a new Organization.
     * @param organization
     * @return The UUID of the new organization.
     * @throws NullDomainReference.NullOrganization
     * @throws ConstraintViolation
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Organization organization) throws NullDomainReference.NullOrganization, ConstraintViolation;

    /**
     * Deletes all existing organizations.
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();
}
