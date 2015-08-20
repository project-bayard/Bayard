package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Committee;
import edu.usm.domain.Views;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.Response;
import edu.usm.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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
        String id = committeeService.create(committee);
        return new Response(id, Response.SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces={"application/json"})
    public Response updateCommitteeDetails(@PathVariable("id") String id, @RequestBody Committee committee) throws NullDomainReference{
        Committee fromDb = committeeService.findById(id);
        if (null == fromDb) {
            throw new NullDomainReference.NullCommittee(id);
        }

        if (null == committee.getMembers()) {
            committee.setMembers(fromDb.getMembers());
        }

        committeeService.update(committee);
        return Response.successGeneric();

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces={"application/json"})
    public Response deleteCommittee(@PathVariable("id") String id) throws NullDomainReference {
        Committee committee = committeeService.findById(id);
        try {
            committeeService.delete(committee);
            return Response.successGeneric();
        } catch (NullDomainReference e) {
            throw new NullDomainReference.NullCommittee(id, e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces={"application/json"})
    @JsonView(Views.CommitteeList.class)
    public Committee getCommittee(@PathVariable("id") String id) {
        return committeeService.findById(id);
    }

}
