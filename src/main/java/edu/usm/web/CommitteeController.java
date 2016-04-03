package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Committee;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.Response;
import edu.usm.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST Controller for {@link Committee}
 */

@RestController
@RequestMapping(value = "/committees" )
public class CommitteeController {

    @Autowired
    CommitteeService committeeService;

    /**
     * Returns a list of Committee's
     * @return A list of{@link Committee}
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @JsonView(Views.CommitteeList.class)
    public Set<Committee> getAllCommittees() {
        return committeeService.findAll();
    }

    /**
     * Creates a new Committee
     * @param committee
     * @return The UUID of the Committee
     * @throws ConstraintViolation
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"})
    public Response createCommittee(@RequestBody Committee committee) throws ConstraintViolation{
        String id = committeeService.create(committee);
        return new Response(id, Response.SUCCESS);
    }

    /**
     * Updates the details of a Committee
     * @param id
     * @param committee
     * @return
     * @throws ConstraintViolation
     * @throws NullDomainReference
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces={"application/json"})
    public Response updateCommitteeDetails(@PathVariable("id") String id, @RequestBody Committee committee) throws ConstraintViolation, NullDomainReference{
        committeeService.update(id, committee);
        return Response.successGeneric();
    }

    /**
     * Deletes the Committee with the ID if it exists.
     * @param id
     * @return
     * @throws ConstraintViolation
     * @throws NullDomainReference
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces={"application/json"})
    public Response deleteCommittee(@PathVariable("id") String id) throws ConstraintViolation, NullDomainReference {
        committeeService.delete(id);
        return Response.successGeneric();
    }

    /**
     * Gets a Committee by its UUID
     * @param id
     * @return
     * @throws NullDomainReference.NullCommittee
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces={"application/json"})
    @JsonView(Views.CommitteeDetails.class)
    public Committee getCommittee(@PathVariable("id") String id) throws NullDomainReference.NullCommittee {
        return committeeService.findById(id);
    }

}
