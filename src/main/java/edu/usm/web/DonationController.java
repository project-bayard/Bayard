package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.Donation;
import edu.usm.domain.Views;
import edu.usm.dto.Response;
import edu.usm.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by andrew on 2/25/16.
 */
@RestController
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;


    @RequestMapping(value= "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.DonationDetails.class)
    public Donation getDonation(@PathVariable("id")String id) {
        Donation d = donationService.findById(id);
        if (null == d) {
            //TODO: refactor 404s
        }
        return d;
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response updateDonation(@PathVariable("id")String id, @RequestBody Donation updated) {
        Donation existing = donationService.findById(id);
        if (null == existing) {
            //TODO: refactor 404s
        }
        donationService.update(existing, updated);
        return Response.successGeneric();
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response deleteDonation(@PathVariable("id")String id) {
        Donation d = donationService.findById(id);
        if (null == d) {
            //TODO: refactor 404s
        }
        donationService.delete(d);
        return Response.successGeneric();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Response createDonation(@RequestBody Donation donation) {
        String donationId = donationService.create(donation);
        return new Response(donationId, Response.SUCCESS);
    }

}
