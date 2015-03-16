var smwcAngular = angular.module('smwcAngular', ['ngRoute', 'smwcControllers']);

smwcAngular.config(function($routeProvider) {
    $routeProvider
        .when('/', {
        templateUrl : 'resources/partials/main.html',
        controller : 'MainController'
        })
        .when('/contacts', {
            templateUrl : 'resources/partials/contactList.html',
            controller : 'ContactsController'
        })
        .when('/newContact', {
            templateUrl : 'resources/partials/newContact.html',
            controller : 'CreateContactController'
        })
        .otherwise({redirectTo : 'resources/partials/main.html'});
});