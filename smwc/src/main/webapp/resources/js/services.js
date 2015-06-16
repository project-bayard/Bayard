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
                method: 'POST',
                url: "../contacts/:id/attend"
            },
            getEncounters : {
                method: 'GET',
                isArray : true,
                url: "../contacts/:id/encounters"
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