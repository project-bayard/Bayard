package edu.usm.service;

import edu.usm.domain.Donation;
import edu.usm.dto.DonationDto;
import edu.usm.dto.DtoTransformer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by andrew on 3/18/16.
 */
public abstract class DonationAssigningService extends BasicService {

    @Autowired
    protected DonationService donationService;

    protected Donation convertToDonation(DonationDto dto) {
        Donation donation = new Donation();
        donation = DtoTransformer.fromDto(dto, donation);
        if (null != dto.getBudgetItemId()) {
            donation.setBudgetItem(donationService.findBudgetItem(dto.getBudgetItemId()));
        } else {
            donation.setBudgetItem(null);
        }
        return donation;
    }

}
