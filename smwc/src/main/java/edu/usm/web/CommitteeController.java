package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Committee;
import edu.usm.domain.Views;
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
    public void createCommittee(@RequestBody Committee committee) {
        committeeService.create(committee);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/committee/{id}", method = RequestMethod.PUT, produces={"application/json"})
    public void updateCommittee(@PathVariable("id") String id, @RequestBody Committee committee) {
        committeeService.update(committee);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/committee/{id}", method = RequestMethod.GET, produces={"application/json"})
    @JsonView(Views.CommitteeList.class)
    public Committee updateCommittee(@PathVariable("id") String id) {
        return committeeService.findById(id);
    }


}
