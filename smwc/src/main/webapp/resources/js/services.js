(function () {
    'use strict';

    /* Services */

    var services = angular.module('services', ['ngResource']);

    services.factory('ContactService',[ '$resource', function ($resource) {
        return $resource('../contacts/contact/:id', {id : '@id'}, {
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
                url : "../contacts",
                isArray : true
            },
            create : {
                method: 'POST',
                url : "../contacts"
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

}());