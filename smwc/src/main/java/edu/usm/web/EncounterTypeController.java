package edu.usm.web;

import edu.usm.domain.EncounterType;
import edu.usm.dto.Response;
import edu.usm.service.EncounterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by scottkimball on 8/17/15.
 */
@RestController
@RequestMapping(value = "/encounterTypes")
public class EncounterTypeController {

    @Autowired
    EncounterTypeService encounterTypeService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    public Set<EncounterType> getEncounterTypes() {
        return encounterTypeService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes="application/json")
    public Response createEncounterType(@RequestBody EncounterType encounterType) {
        String id = encounterTypeService.create(encounterType);
        return Response.successGeneric();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public Response deleteEncounterType(@PathVariable("id") String id) {
        encounterTypeService.delete(id);
        return Response.successGeneric();
    }





}