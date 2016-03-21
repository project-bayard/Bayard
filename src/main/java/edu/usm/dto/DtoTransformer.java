package edu.usm.dto;

import edu.usm.domain.Donation;
import edu.usm.domain.Grant;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.SustainerPeriod;

/**
 * Transforms various domain entities to and from their DTO representations. fromDto will construct and return
 * fully-formed DTOs; fromEntity will set all primitives on the provided entity but will avoid setting object references
 * unknown to the DTO.
 */
public class DtoTransformer {

    public static Grant fromDto(GrantDto dto, Grant grant) {
        grant.setName(dto.getName());
        grant.setAmountAppliedFor(dto.getAmountAppliedFor());
        grant.setAmountReceived(dto.getAmountReceived());
        grant.setStartPeriod(dto.getStartPeriod());
        grant.setEndPeriod(dto.getEndPeriod());
        grant.setApplicationDeadline(dto.getApplicationDeadline());
        grant.setIntentDeadline(dto.getIntentDeadline());
        grant.setReportDeadline(dto.getReportDeadline());
        grant.setDescription(dto.getDescription());
        grant.setRestriction(dto.getRestriction());
        return grant;
    }

    public static GrantDto fromEntity(Grant grant) {
        GrantDto dto = new GrantDto();
        dto.setName(grant.getName());
        dto.setAmountAppliedFor(grant.getAmountAppliedFor());
        dto.setAmountReceived(grant.getAmountReceived());
        dto.setStartPeriod(grant.getStartPeriod());
        dto.setEndPeriod(grant.getEndPeriod());
        dto.setApplicationDeadline(grant.getApplicationDeadline());
        dto.setIntentDeadline(grant.getIntentDeadline());
        dto.setReportDeadline(grant.getReportDeadline());
        dto.setDescription(grant.getDescription());
        dto.setRestriction(grant.getRestriction());
        if (null != grant.getFoundation()) {
            dto.setFoundationId(grant.getFoundation().getId());
        }
        return dto;
    }

    public static InteractionRecord fromDto(InteractionRecordDto dto, InteractionRecord record) {
        record.setPersonContacted(dto.getPersonContacted());
        record.setDateOfInteraction(dto.getDateOfInteraction());
        record.setNotes(dto.getNotes());
        record.setRequiresFollowUp(dto.isRequiresFollowUp());
        return record;
    }

    public static InteractionRecordDto fromEntity(InteractionRecord record) {
        InteractionRecordDto dto = new InteractionRecordDto();
        dto.setPersonContacted(record.getPersonContacted());
        dto.setDateOfInteraction(record.getDateOfInteraction());
        dto.setInteractionTypeId(record.getInteractionType().getId());
        dto.setNotes(record.getNotes());
        dto.setRequiresFollowUp(record.isRequiresFollowUp());

        if (null != record.getFoundation()) {
            dto.setFoundationId(record.getFoundation().getId());
        }
        return dto;
    }

    public static SustainerPeriodDto fromEntity(SustainerPeriod sustainerPeriod) {
        SustainerPeriodDto dto = new SustainerPeriodDto();
        dto.setMonthlyAmount(sustainerPeriod.getMonthlyAmount());
        dto.setPeriodStartDate(sustainerPeriod.getPeriodStartDate());
        dto.setCancelDate(sustainerPeriod.getCancelDate());
        dto.setSentIRSLetter(sustainerPeriod.isSentIRSLetter());
        return dto;
    }

    public static SustainerPeriod fromDto(SustainerPeriodDto dto, SustainerPeriod sustainerPeriod) {
        sustainerPeriod.setMonthlyAmount(dto.getMonthlyAmount());
        sustainerPeriod.setSentIRSLetter(dto.isSentIRSLetter());
        sustainerPeriod.setCancelDate(dto.getCancelDate());
        sustainerPeriod.setPeriodStartDate(dto.getPeriodStartDate());
        return sustainerPeriod;
    }

    public static DonationDto fromEntity(Donation donation) {
        DonationDto dto = new DonationDto();
        dto.setAmount(donation.getAmount());
        dto.setMethod(donation.getMethod());
        dto.setDateOfReceipt(donation.getDateOfReceipt());
        dto.setDateOfDeposit(donation.getDateOfDeposit());
        dto.setRestrictedToCategory(donation.getRestrictedToCategory());
        dto.setAnonymous(donation.isAnonymous());
        dto.setStandalone(donation.isStandalone());
        if (null != donation.getBudgetItem()) {
            dto.setBudgetItemId(donation.getBudgetItem().getId());
        }
        return dto;
    }

    public static Donation fromDto(DonationDto dto, Donation donation) {
        donation.setAmount(dto.getAmount());
        donation.setMethod(dto.getMethod());
        donation.setDateOfReceipt(dto.getDateOfReceipt());
        donation.setDateOfDeposit(dto.getDateOfDeposit());
        donation.setRestrictedToCategory(dto.getRestrictedToCategory());
        donation.setAnonymous(dto.isAnonymous());
        donation.setStandalone(dto.isStandalone());
        return donation;
    }

}
