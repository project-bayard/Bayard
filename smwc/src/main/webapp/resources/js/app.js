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
        .when('/contacts/contact/:id', {
            templateUrl : 'resources/partials/details.html',
            controller : 'DetailsCtrl'
        })
        .otherwise({redirectTo : 'resources/partials/main.html'});
});