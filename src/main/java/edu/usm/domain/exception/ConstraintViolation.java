package edu.usm.domain.exception;

import edu.usm.domain.BasicEntity;

/**
 * Created by andrew on 8/24/15.
 */
public class ConstraintViolation extends Exception {

    private String clashingDomainId;

    public ConstraintViolation() {
        super(ConstraintMessage.GENERIC_PERSISTENCE_ERROR.toString());

    }

    public ConstraintViolation(String message, String clashingDomainId) {
        super(message);
        this.clashingDomainId = clashingDomainId;
    }

    public ConstraintViolation(ConstraintMessage message) {
        super(message.toString());
    }

    public static class NonUniqueDomainEntity extends ConstraintViolation {

        public NonUniqueDomainEntity(ConstraintMessage message, BasicEntity existingEntity) {
            super(message.toString(), existingEntity.getId());
        }

    }

    public String getClashingDomainId() {
        return clashingDomainId;
    }

    public void setClashingDomainId(String clashingDomainId) {
        this.clashingDomainId = clashingDomainId;
    }
}
