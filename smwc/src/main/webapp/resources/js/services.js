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
            }
        });
    }]);

}());