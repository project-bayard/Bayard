package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Committee;
import edu.usm.domain.Views;
import edu.usm.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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


}
