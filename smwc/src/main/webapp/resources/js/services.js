(function () {
    'use strict';

    /* Services */

    var services = angular.module('services', ['ngResource']);

    services.factory('ContactService', function ($resource) {
        return $resource('../contacts/:id');
    });

}());