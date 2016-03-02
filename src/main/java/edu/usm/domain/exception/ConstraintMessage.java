package edu.usm.domain.exception;

/**
 * Created by andrew on 8/24/15.
 */
public enum ConstraintMessage {

    GENERIC_PERSISTENCE_ERROR("Error persisting entity."),
    CONTACT_DUPLICATE_NAME_EMAIL("A contact with this first name and email already exists"),
    CONTACT_DUPLICATE_NAME_PHONE_NUMBER("A contact with this first name and primary phone number already exists"),
    CONTACT_NO_EMAIL_OR_PHONE_NUMBER("A contact must have either an email or primary phone number associated with them"),
    CONTACT_NO_FIRST_NAME("A contact must have a first name associated with them"),
    ENCOUNTER_REQUIRED_TYPE("An encounter must have a type associated with it"),
    ENCOUNTER_CONTACT_NOT_INITIATOR("The initiator associated with this encounter is not qualified as an initiator"),
    ENCOUNTER_REQUIRED_DATE("An encounter must have an encounter date associated with it"),
    EVENT_REQUIRED_NAME("An event must have a name associated with it"),
    EVENT_REQUIRED_DATE("An event must have a date associated with it"),
    EVENT_NON_UNIQUE("An event with this name and date already exists"),
    COMMITTEE_REQUIRED_NAME("A committee must have a name associated with it"),
    COMMITTEE_NON_UNIQUE("A committee with this name already exists"),
    ORGANIZATION_NON_UNIQUE("An organization with this name already exists"),
    ORGANIZATION_REQUIRED_NAME("An organization must have a name associated with it"),
    GROUP_NON_UNIQUE("A group with this name already exists."),
    GROUP_REQUIRED_NAME("A group must have a name associated with it."),
    DEMOGRAPHIC_OPTION_REQUIRED_NAME("A demographic option must have a name associated with it."),
    DEMOGRAPHIC_CATEGORY_REQUIRED_NAME("A demographic category must have a name associated with it."),
    USER_DUPLICATE_EMAIL("A user registered with this email already exists."),
    USER_NO_PASSWORD("A user must have a password associated with it."),
    USER_PASSWORD_TOO_SHORT("The password provided was not long enough."),
    FOUNDATION_REQUIRED_NAME("A foundation must have a name associated with it"),
    FOUNDATION_DUPLICATE_NAME("A foundation with that name already exists."),
    INTERACTION_RECORD_NO_CONTACT_PERSON("An interaction record must reference the person contacted."),
    INTERACTION_RECORD_NO_DATE("An interaction must have an interaction date."),
    INTERACTION_RECORD_NO_FOUNDATION("An interaction must ne associated with a foundation."),
    GRANT_REQUIRED_NAME("A grant must have a name associated with it."),
    GRANT_NO_FOUNDATION("A grant must be associated with a foundation."),
    SUSTAINER_PERIOD_NO_START_DATE("A sustainer period must have a start date associated with it.");

    private final String enumText;

    ConstraintMessage(final String enumText) {
        this.enumText = enumText;
    }

    @Override
    public String toString() {
        return enumText;
    }


}
