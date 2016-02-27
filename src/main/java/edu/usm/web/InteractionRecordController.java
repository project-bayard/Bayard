package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.InteractionRecordDto;
import edu.usm.dto.Response;
import edu.usm.service.InteractionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by andrew on 2/19/16.
 */
@RestController
@RequestMapping("/interactions")
public class InteractionRecordController {

    @Autowired
    private InteractionRecordService interactionService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.InteractionRecordList.class)
    public Set<InteractionRecord> getAllInteractions() {
        return interactionService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.InteractionRecordDetails.class)
    public InteractionRecord getInteraction(@PathVariable("id")String id) throws NullDomainReference {
        return retrieveInteractionRecord(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response updateInteractionDetails(@PathVariable("id")String id, @RequestBody InteractionRecordDto dto) throws ConstraintViolation, NullDomainReference{
        InteractionRecord record = retrieveInteractionRecord(id);
        interactionService.update(record, dto);
        return Response.successGeneric();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response deleteInteractionRecord(@PathVariable("id")String id) throws ConstraintViolation, NullDomainReference {
        InteractionRecord record = retrieveInteractionRecord(id);
        interactionService.delete(record);
        return Response.successGeneric();
    }

    private InteractionRecord retrieveInteractionRecord(String id) throws NullDomainReference{
        InteractionRecord record = interactionService.findById(id);
        if (null == record) {
            //TODO: replace with our new approach to handling 404s
            throw new NullDomainReference.NullInteractionRecord(id);
        }
        return record;
    }

}
