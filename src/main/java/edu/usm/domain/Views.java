package edu.usm.domain;

/**
 * Defines the interfaces used with JSONView
 */
public interface Views {
    interface ContactList{}
    interface EventList {}
    interface OrganizationList {}
    interface CommitteeList {}
    interface CommitteeDetails{}
    interface ContactDetails {}
    interface ContactEncounterDetails {}
    interface DemographicDetails {}
    interface MemberInfo {}

    /*For the contact details view. Doesn't contain membership information.*/
    interface ContactOrganizationDetails {}
    interface ContactCommitteeDetails {}

    interface UserDetails {}

    interface GroupList {}
    interface GroupDetails {}
    interface GroupPanel {}

    interface FoundationList extends Views{}
    interface FoundationDetails extends Views{}

    interface GrantList extends Views{}
    interface GrantDetails extends Views{}

    interface InteractionRecordDetails extends Views{}
    interface InteractionRecordList extends Views{}

    interface DonationDetails extends Views{}

    interface SustainerPeriodDetails extends Views{}


}
