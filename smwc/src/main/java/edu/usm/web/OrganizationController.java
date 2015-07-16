package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Organization;
import edu.usm.domain.Views;
import edu.usm.dto.Response;
import edu.usm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by scottkimball on 5/28/15.
 */

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteOrganization(@PathVariable("id") String id) {

        Organization organization = organizationService.findById(id);
        if (null == organization) {
            return new Response(null, Response.FAILURE, "Organization with ID "+id+" does not exist.");
        }

        try {
            organizationService.delete(organization);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error deleting Organization with ID "+id+".");
        }

    }

    @RequestMapping(method = RequestMethod.PUT, value="/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response updateOrganizationDetails(@PathVariable("id") String id, @RequestBody Organization organization) {
        Organization fromDb = organizationService.findById(id);
        if (null == fromDb) {
            return new Response(null, Response.FAILURE, "Organization with ID "+id+" does not exist.");
        }

        //assumption that a RequestBody without members should be interpreted as an omission of the complete object graph
        if (null == organization.getMembers()) {
            organization.setMembers(fromDb.getMembers());
        }

        try {
            organizationService.update(organization);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error updating Organization with ID "+id+".");
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.OrganizationList.class)
    public Set<Organization> getAllOrganizations() {
        return organizationService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public Response createOrganization(@RequestBody Organization organization) {
        String id;
        try {
            id = organizationService.create(organization);
            return new Response(id,Response.SUCCESS,null);
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Unable to create organization");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces={"application/json"})
    @JsonView(Views.OrganizationList.class)
    public Organization getOrganizationById(@PathVariable("id") String id) {
        return organizationService.findById(id);
    }


}
