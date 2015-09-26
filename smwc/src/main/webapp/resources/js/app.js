(function () {
    'use strict';

    var app = angular.module('app', ['ui.mask', 'ngRoute', 'controllers','services','filters']);

    app.config(function ($routeProvider, $httpProvider) {

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
            .when('/organizations/organization/:id', {
                templateUrl: 'resources/partials/organizationDetails.html',
                controller: 'OrganizationDetailsCtrl'
            })
            .when('/events' , {
                templateUrl:'resources/partials/eventList.html',
                controller: 'EventsCtrl'
            })
            .when('/committees' , {
                templateUrl:'resources/partials/committeeList.html',
                controller: 'CommitteesCtrl'
            })
            .when('/events/event/:id', {
                templateUrl: 'resources/partials/eventDetails.html',
                controller: 'EventDetailsCtrl'
            })
            .when('/login', {
                templateUrl: 'resources/partials/login.html',
                controller: 'LoginCtrl'
            })
            .when('/encountertypes', {
                templateUrl: 'resources/partials/encounterTypes.html',
                controller: 'EncounterTypeCtrl'
            })
            .otherwise({redirectTo: '/'});


        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    });

    app.run(function($rootScope, $location) {
        $rootScope.user = sessionStorage.getItem('user');

        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            $rootScope.authenticated = sessionStorage.getItem('authenticated');
            if ($rootScope.authenticated == null || $rootScope.authenticated == 'false') {
                event.preventDefault();
                $rootScope.$evalAsync(function() {
                    $location.path('/login');
                });
            }
        });
    })



}());