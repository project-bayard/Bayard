package edu.usm.domain.exception;

import edu.usm.domain.BasicEntity;

/**
 * Created by andrew on 8/24/15.
 */
public class ConstraintViolation extends Exception {

    public ConstraintViolation(String message) {
        super(message);
    }

    public ConstraintViolation(ConstraintMessage message) {
        super(message.toString());
    }

    public static class NonUniqueDomainEntity extends ConstraintViolation {

        public NonUniqueDomainEntity(ConstraintMessage message, BasicEntity existingEntity) {
            super(messageConstructor(message, existingEntity));
        }

        private static String messageConstructor(ConstraintMessage message, BasicEntity entity) {
            return message.toString() + " -- ID: "+entity.getId();
        }

    }

}
