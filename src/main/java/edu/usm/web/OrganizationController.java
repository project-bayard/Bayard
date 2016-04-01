package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.Response;
import edu.usm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST Controller for {@link Organization} domain objects.
 */

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    /**
     * Deletes an organization with the id if it exists.
     * @param id
     * @return {@link Response}
     * @throws ConstraintViolation
     * @throws NullDomainReference
     */
    @RequestMapping(method = RequestMethod.DELETE, value="/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteOrganization(@PathVariable("id") String id) throws ConstraintViolation, NullDomainReference{
        organizationService.delete(id);
        return Response.successGeneric();
    }

    /**
     * Updates an organization's details
     * @param id
     * @param organization
     * @return {@link Response}
     * @throws ConstraintViolation
     * @throws NullDomainReference
     */
    @RequestMapping(method = RequestMethod.PUT, value="/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response updateOrganizationDetails(@PathVariable("id") String id, @RequestBody Organization organization) throws ConstraintViolation, NullDomainReference{
        organizationService.updateOrganizationDetails(id, organization);
        return Response.successGeneric();

    }

    /**
     * Returns all organizations
     * @return A Set of {@link Organization}
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.OrganizationList.class)
    public Set<Organization> getAllOrganizations() {
        return organizationService.findAll();
    }

    /**
     * Creates a new Organization
     * @param organization
     * @return {@link Response}
     * @throws NullDomainReference
     * @throws ConstraintViolation
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public Response createOrganization(@RequestBody Organization organization) throws NullDomainReference, ConstraintViolation{
        String id = organizationService.create(organization);
        return new Response(id,Response.SUCCESS);
    }

    /**
     * Gets an organization by it's ID if it exists.
     * @param id
     * @return {@link Organization}
     * @throws NullDomainReference.NullOrganization
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView(Views.OrganizationList.class)
    public Organization getOrganizationById(@PathVariable("id") String id) throws NullDomainReference.NullOrganization {
        return organizationService.findById(id);
    }

    /**
     * Associates a donation with an Organization
     * @param id
     * @param donation
     * @return {@link Response}
     * @throws NullDomainReference
     * @throws ConstraintViolation
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/donations", produces={"application/json"})
    public Response addDonation(@PathVariable("id")String id, @RequestBody Donation donation) throws NullDomainReference, ConstraintViolation{
        organizationService.addDonation(id, donation);
        return Response.successGeneric();
    }

    /**
     * Disassociates a donation with an organization.
     * @param id
     * @param donationId
     * @return {@link Response}
     * @throws NullDomainReference
     * @throws ConstraintViolation
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/donations/{donationId}", produces={"application/json"})
    public Response removeDonation(@PathVariable("id")String id, @PathVariable("donationId")String donationId) throws NullDomainReference, ConstraintViolation {
        organizationService.removeDonation(id, donationId);
        return Response.successGeneric();
    }

    /**
     * Gets all donations associated with an organization.
     * @param id
     * @return A Set of {@link Donation}
     * @throws NullDomainReference
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/donations", produces={"application/json"})
    @JsonView(Views.DonationDetails.class)
    public Set<Donation> getDonations(@PathVariable("id")String id) throws NullDomainReference {
       return organizationService.getDonations(id);
    }

}
