package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.BudgetItem;
import edu.usm.domain.Donation;
import edu.usm.domain.Views;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DonationDto;
import edu.usm.dto.Response;
import edu.usm.service.DonationService;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by andrew on 2/25/16.
 */
@RestController
@RequestMapping("/donations")
@ConditionalOnExpression("${bayard.implementation.enableDevelopmentFeatures:true}")
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
    public Response updateDonation(@PathVariable("id")String id, @RequestBody DonationDto dto) {
        Donation existing = donationService.findById(id);
        if (null == existing) {
            //TODO: refactor 404s
        }
        donationService.update(existing, dto);
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
    public Response createDonation(@RequestBody DonationDto dto) {
        String donationId = donationService.create(dto);
        return new Response(donationId, Response.SUCCESS);
    }

    @RequestMapping(value= "/budgetitems", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Set<BudgetItem> getBudgetItems() {
        return donationService.findAllBudgetItems();
    }

    @RequestMapping(value= "/budgetitems", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Response createBudgetItem(@RequestBody BudgetItem budgetItem) {
        donationService.createBudgetItem(budgetItem);
        return new Response(budgetItem.getId(), Response.SUCCESS);
    }

    @RequestMapping(value= "/budgetitems/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBudgetItem(@PathVariable("id") String id) {
        BudgetItem budgetItem = donationService.findBudgetItem(id);
        if (null == budgetItem) {
            //TODO 404 refactor
        }
        donationService.deleteBudgetItem(budgetItem);
        return Response.successGeneric();
    }

    @RequestMapping(value= "/budgetitems/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response changeBudgetItemName(@PathVariable("id") String id, @RequestBody BudgetItem updated) {
        BudgetItem budgetItem = donationService.findBudgetItem(id);
        if (null == budgetItem) {
            //TODO 404 refactor
        }
        donationService.updateBudgetItemName(budgetItem, updated.getName());
        return Response.successGeneric();
    }

    @RequestMapping(value= "/bydepositdate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Donation> getDonationsDepositedWithinDateRange(@PageableDefault(sort= "dateOfDeposit", direction = Sort.Direction.DESC)Pageable pageable,
                                                  @RequestParam("from")@DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from,
                                                  @RequestParam("to")@DateTimeFormat(pattern="yyyy-MM-dd")LocalDate to) {
        return donationService.findDonationsDepositedBetween(from, to, pageable);

    }

    @RequestMapping(value= "/byreceiptdate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Donation> getDonationsReceivedWithinDateRange(@PageableDefault(sort= "dateOfReceipt", direction = Sort.Direction.DESC)Pageable pageable,
                                                  @RequestParam("from")@DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from,
                                                  @RequestParam("to")@DateTimeFormat(pattern="yyyy-MM-dd")LocalDate to) {
        return donationService.findDonationsReceivedBetween(from, to, pageable);
    }

    @RequestMapping(value= "/bybudgetitem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Donation> getDonationsReceivedWithinDateRange(@PageableDefault(sort= "dateOfReceipt", direction = Sort.Direction.DESC)Pageable pageable,
                                                              @RequestParam("item")String item) {
        return donationService.findDonationsByBudgetItem(item, pageable);
    }


}
