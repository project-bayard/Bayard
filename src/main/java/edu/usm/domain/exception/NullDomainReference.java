package edu.usm.domain.exception;

import edu.usm.domain.*;

/**
 * Created by andrew on 8/20/15.
 */
public abstract class NullDomainReference extends Exception {

    public NullDomainReference(String message) {
        super(message);
    }

    public NullDomainReference(String message, Throwable throwable) {
        super(message, throwable);
    }

    public static class NullContact extends NullDomainReference {

        public NullContact(String id) {
            super(messageConstructor(Contact.class.getSimpleName(), id));
        }

        public NullContact() {
            super(messageConstructor(Contact.class.getSimpleName()));
        }

        public NullContact(String id, Throwable throwable) {
            super(messageConstructor(Contact.class.getSimpleName(), id), throwable);
        }
    }

    public static class NullOrganization extends NullDomainReference {

        public NullOrganization(String id) {
            super(messageConstructor(Organization.class.getSimpleName(), id));
        }

        public NullOrganization() {
            super(messageConstructor(Organization.class.getSimpleName()));
        }

        public NullOrganization(String id, Throwable throwable) {
            super(messageConstructor(Organization.class.getSimpleName(), id), throwable);
        }

    }

    public static class NullGroup extends NullDomainReference {

        public NullGroup(String id) {
            super(messageConstructor(Group.class.getSimpleName(), id));
        }

        public NullGroup() {
            super(messageConstructor(Group.class.getSimpleName()));
        }

        public NullGroup(String id, Throwable throwable) {
            super(messageConstructor(Group.class.getSimpleName(), id), throwable);
        }

    }

    public static class NullCommittee extends NullDomainReference {

        public NullCommittee(String id) {
            super(messageConstructor(Committee.class.getSimpleName(), id));
        }

        public NullCommittee() {
            super(messageConstructor(Committee.class.getSimpleName()));
        }

        public NullCommittee(String id, Throwable throwable) {
            super(messageConstructor(Committee.class.getSimpleName(), id), throwable);
        }

    }

    public static class NullEvent extends NullDomainReference {

        public NullEvent(String id) {
            super(messageConstructor(Event.class.getSimpleName(), id));
        }

        public NullEvent() {
            super(messageConstructor(Event.class.getSimpleName()));
        }

        public NullEvent(String id, Throwable throwable) {
            super(messageConstructor(Event.class.getSimpleName(), id), throwable);
        }

    }

    public static class NullEncounter extends NullDomainReference {

        public NullEncounter(String id) {
            super(messageConstructor(Encounter.class.getSimpleName(), id));
        }

        public NullEncounter() {
            super(messageConstructor(Encounter.class.getSimpleName()));
        }

        public NullEncounter(String id, Throwable throwable) {
            super(messageConstructor(Encounter.class.getSimpleName(), id), throwable);
        }
    }

    public static class NullEncounterType extends NullDomainReference {

        public NullEncounterType(String id) {
            super(messageConstructor(EncounterType.class.getSimpleName(),id));
        }

        public NullEncounterType() {
            super(messageConstructor(EncounterType.class.getSimpleName()));
        }

        public NullEncounterType(String id, Throwable throwable) {
            super(messageConstructor(EncounterType.class.getSimpleName(),id),throwable);
        }
    }

    public static class NullFoundation extends NullDomainReference {

        public NullFoundation(String id) {
            super(messageConstructor(Foundation.class.getSimpleName(),id));
        }

        public NullFoundation() {
            super(messageConstructor(Foundation.class.getSimpleName()));
        }

        public NullFoundation(String id, Throwable throwable) {
            super(messageConstructor(Foundation.class.getSimpleName(),id),throwable);
        }
    }

    public static class NullGrant extends NullDomainReference {

        public NullGrant(String id) {
            super(messageConstructor(Grant.class.getSimpleName(),id));
        }

    }

    public static class NullInteractionRecord extends NullDomainReference {

        public NullInteractionRecord(String id) {
            super(messageConstructor(NullInteractionRecord.class.getSimpleName(),id));
        }

    }

    protected static String messageConstructor(String domainClass, String id) {
        return domainClass+" with id : "+id+" does not exist";
    }

    protected static String messageConstructor(String domainClass) {
        return domainClass+" reference was null.";
    }

}
