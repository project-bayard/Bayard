package edu.usm.service.impl;

import edu.usm.domain.Donation;
import edu.usm.repository.DonationDao;
import edu.usm.service.BasicService;
import edu.usm.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by andrew on 2/25/16.
 */
@Service
public class DonationServiceImpl extends BasicService implements DonationService {

    @Autowired
    private DonationDao donationDao;

    @Override
    public Donation findById(String id) {
        return donationDao.findOne(id);
    }

    @Override
    public Set<Donation> findAll() {
        return donationDao.findAll();
    }

    @Override
    public String create(Donation donation) {
        updateLastModified(donation);
        donationDao.save(donation);
        return donation.getId();
    }

    @Override
    public void update(Donation donation) {
        updateLastModified(donation);
        donationDao.save(donation);
    }

    @Override
    public void update(Donation existing, Donation update) {
        existing.setAmount(update.getAmount());
        existing.setStandalone(update.isStandalone());
        existing.setAnonymous(update.isAnonymous());
        existing.setRestrictedToCategory(update.getRestrictedToCategory());
        existing.setMethod(update.getMethod());
        existing.setBudgetItem(update.getBudgetItem());
        existing.setDateOfDeposit(update.getDateOfDeposit());
        existing.setDateOfReceipt(update.getDateOfReceipt());

        update(existing);
    }

    @Override
    public void delete(Donation donation) {
        donationDao.delete(donation);
    }

    @Override
    public void deleteAll() {
        findAll().stream().forEach(this::delete);
    }
}
