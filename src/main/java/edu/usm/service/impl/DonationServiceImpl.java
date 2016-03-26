package edu.usm.service.impl;

import edu.usm.domain.BudgetItem;
import edu.usm.domain.Donation;
import edu.usm.dto.DonationDto;
import edu.usm.dto.DtoTransformer;
import edu.usm.repository.BudgetItemDao;
import edu.usm.repository.DonationDao;
import edu.usm.service.BasicService;
import edu.usm.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by andrew on 2/25/16.
 */
@Service
public class DonationServiceImpl extends BasicService implements DonationService {

    @Autowired
    private DonationDao donationDao;

    @Autowired
    private BudgetItemDao budgetItemDao;

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
    public String create(DonationDto dto) {
        Donation donation = new Donation();
        donation = DtoTransformer.fromDto(dto, donation);
        if (null != dto.getBudgetItemId()) {
            donation.setBudgetItem(findBudgetItem(dto.getBudgetItemId()));
        }
        return create(donation);
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
    public void update(Donation existing, DonationDto dto) {
        existing = DtoTransformer.fromDto(dto, existing);
        if (null != dto.getBudgetItemId()) {
            existing.setBudgetItem(findBudgetItem(dto.getBudgetItemId()));
        } else {
            existing.setBudgetItem(null);
        }
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

    @Override
    public void deleteAllBudgetItems() {
        findAllBudgetItems().stream().forEach(this::deleteBudgetItem);
    }

    @Override
    public Set<BudgetItem> findAllBudgetItems() {
        return budgetItemDao.findAll();
    }

    @Override
    public BudgetItem findBudgetItem(String id) {
        return budgetItemDao.findOne(id);
    }

    @Override
    public void createBudgetItem(BudgetItem budgetItem) {
        budgetItemDao.save(budgetItem);
    }

    @Override
    public void updateBudgetItemName(BudgetItem budgetItem, String name) {
        budgetItem.setName(name);
        budgetItemDao.save(budgetItem);
    }

    @Override
    public void deleteBudgetItem(BudgetItem budgetItem) {
        Set<Donation> donationsWithBudgetItem = findByBudgetItem(budgetItem);
        for (Donation donation: donationsWithBudgetItem) {
            donation.setBudgetItem(null);
            update(donation);
        }
        budgetItemDao.delete(budgetItem);
    }

    @Override
    public Set<Donation> findByBudgetItem(BudgetItem budgetItem) {
        return donationDao.findByBudgetItem(budgetItem);
    }

    @Override
    public Page<Donation> findDonationsDepositedBetween(LocalDate from, LocalDate to, Pageable pageable) {
        return donationDao.findByDateOfDepositBetween(from, to, pageable);
    }

    @Override
    public Page<Donation> findDonationsReceivedBetween(LocalDate from, LocalDate to, Pageable pageable) {
        return donationDao.findByDateOfReceiptBetween(from, to, pageable);
    }

    @Override
    public Page<Donation> findDonationsByBudgetItem(String budgetItemId, Pageable pageable) {
        BudgetItem item = budgetItemDao.findOne(budgetItemId);
        return donationDao.findByBudgetItem(item, pageable);
    }
}