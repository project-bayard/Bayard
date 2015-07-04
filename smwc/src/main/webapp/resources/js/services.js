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
        return $resource('../organizations/:id', {id : '@id'}, {
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
            }
        });
    }]);

    services.factory('EventService',[ '$resource', function ($resource) {
        return $resource('../events/:id', {id : '@id'}, {
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
            }
        });
    }]);

    services.factory('CommitteeService',[ '$resource', function ($resource) {
        return $resource('../committees/:id', {id : '@id'}, {
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
            }
        });
    }]);

}());