package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.DemographicCategory;
import edu.usm.domain.DemographicOption;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.Response;
import edu.usm.service.DemographicCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by andrew on 10/16/15.
 */
@RestController
@RequestMapping("/demographics")
public class DemographicCategoryController {

    @Autowired
    private DemographicCategoryService service;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(Views.DemographicDetails.class)
    public Set<DemographicCategory> getAll() {
        return service.findAll();
    }

    @RequestMapping(value = "{categoryName}", method = RequestMethod.GET, produces = "application/json")
    @JsonView(Views.DemographicDetails.class)
    public DemographicCategory getCategory(@PathVariable("categoryName")String categoryName) {
        return service.findByName(categoryName);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{categoryName}/options", method = RequestMethod.POST, consumes = "application/json")
    public Response createCategoryOption(@PathVariable("categoryName")String categoryName, @RequestBody DemographicOption option) throws ConstraintViolation {
        DemographicCategory c = service.findByName(categoryName);
        if (c == null) {
            c = new DemographicCategory();
            c.setName(categoryName);
            service.create(c);
        }
        service.addOption(option, c);
        return Response.successGeneric();
    }

}
