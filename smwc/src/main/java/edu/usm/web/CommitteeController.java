package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Committee;
import edu.usm.domain.Views;
import edu.usm.dto.Response;
import edu.usm.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by scottkimball on 6/5/15.
 */

@RestController
@RequestMapping(value = "/committees" )
public class CommitteeController {

    @Autowired
    CommitteeService committeeService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @JsonView(Views.CommitteeList.class)
    public Set<Committee> getAllCommittees() {
        return committeeService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes={"application/json"})
    public Response createCommittee(@RequestBody Committee committee) {
        try {
            String id = committeeService.create(committee);
            return new Response(id, Response.SUCCESS, null);
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Unable to create Committee.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces={"application/json"})
    public Response updateCommitteeDetails(@PathVariable("id") String id, @RequestBody Committee committee) {
        Committee fromDb = committeeService.findById(id);
        if (null == fromDb) {
            return new Response(null, Response.FAILURE, "Committee with ID "+id+" does not exist.");
        }

        if (null == committee.getMembers()) {
            committee.setMembers(fromDb.getMembers());
        }

        try {
            committeeService.update(committee);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error updating Committee with ID "+id+".");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces={"application/json"})
    public Response updateCommittee(@PathVariable("id") String id) {
        Committee committee = committeeService.findById(id);
        if (null == committee) {
            return new Response(null, Response.FAILURE, "Committee with ID "+id+" does not exist.");
        }

        try {
            committeeService.delete(committee);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, Response.FAILURE, "Error deleting Committee with ID "+id+".");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces={"application/json"})
    @JsonView(Views.CommitteeList.class)
    public Committee getCommittee(@PathVariable("id") String id) {
        return committeeService.findById(id);
    }

}
