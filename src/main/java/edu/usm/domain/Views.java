package edu.usm.domain;

/**
 * Defines the interfaces used with JSONView
 */
public class Views {
    public interface ContactList {}
    public interface EventList {}
    public interface OrganizationList {}
    public interface CommitteeList {}
    public interface CommitteeDetails{}
    public interface ContactDetails {}
    public interface ContactEncounterDetails {}
    public interface DemographicDetails {}
    public interface MemberInfo {}

    /*For the contact details view. Doesn't contain membership information.*/
    public interface ContactOrganizationDetails {}
    public interface ContactCommitteeDetails {}

    public interface UserDetails {}

    public interface GroupList {}
    public interface GroupDetails {}
    public interface GroupPanel {}

}
