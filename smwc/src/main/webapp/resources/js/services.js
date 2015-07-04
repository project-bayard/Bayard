(function () {
    'use strict';

    /* Services */

    var services = angular.module('services', ['ngResource']);

    services.factory('ContactService',[ '$resource', function ($resource) {
        return $resource('../contacts/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            attend: {
                method: 'PUT',
                url: "../contacts/:id/events"
            },
            getEncounters : {
                method: 'GET',
                isArray : true,
                url: "../contacts/:id/encounters"
            },
            getInitiators : {
                method: 'GET',
                isArray : true,
                url: "../contacts/initiators"
            },
            getEvents : {
                method: 'GET',
                isArray : true,
                url: "../contacts/:id/events"
            },
            getOrganizations : {
                method: 'GET',
                isArray : true,
                url : "/contacts/:id/organizations"
            },
            addToOrganization : {
                method: 'PUT',
                url : "/contacts/:id/organizations"
            },
            createEncounter : {
                method : 'PUT',
                url: "/contacts/:id/encounters"
            },
            addToCommittee : {
                method : 'PUT',
                url : "/contacts/:id/committees"
            },
            getCommittees : {
                method : 'GET',
                isArray : true,
                url : "/contacts/:id/committees"
            }
        });
    }]);

    services.factory('OrganizationService',[ '$resource', function ($resource) {
        return $resource('../organizations/organization/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                url : "../organizations",
                isArray : true
            },
            create : {
                method: 'POST',
                url : "../organizations"
            }
        });
    }]);

    services.factory('EventService',[ '$resource', function ($resource) {
        return $resource('../events/event/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                url : "../events",
                isArray : true
            },
            create : {
                method: 'POST',
                url : "../events"
            }
        });
    }]);

    services.factory('CommitteeService',[ '$resource', function ($resource) {
        return $resource('../committees/committee/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                url : "../committees",
                isArray : true
            },
            create : {
                method: 'POST',
                url : "../committees"
            }
        });
    }]);

}());