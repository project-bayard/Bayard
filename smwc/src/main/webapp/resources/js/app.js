(function () {
    'use strict';

    var app = angular.module('app', ['ngRoute', 'controllers','services']);

    app.config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'resources/partials/main.html',
                controller: 'MainCtrl'
            })
            .when('/contacts', {
                templateUrl: 'resources/partials/contactList.html',
                controller: 'ContactsCtrl'
            })
            .when('/newContact', {
                templateUrl: 'resources/partials/newContact.html',
                controller: 'CreateContactCtrl'
            })
            .when('/contacts/contact/:id', {
                templateUrl: 'resources/partials/details.html',
                controller: 'DetailsCtrl'
            })
            .when('/organizations' , {
                templateUrl:'resources/partials/organizationList.html',
                controller: 'OrganizationsCtrl'
            })
            .otherwise({redirectTo: 'resources/partials/main.html'});
    });

}());