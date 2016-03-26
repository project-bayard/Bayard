(function () {
    'use strict';

    var app = angular.module('app', ['ui.mask', 'ngRoute', 'controllers','services','filters', 'interceptors', 'angularUtils.directives.dirPagination']);

    app.config(["$httpProvider", function ($httpProvider) {
        $httpProvider.interceptors.push("baseInterceptor");
    }]);

    app.config(function ($routeProvider, $httpProvider) {

        var resolveDevelopmentEnabled = function($q, $rootScope, ConfigService) {

            if ($rootScope.bayardConfig != null) {
                return ($rootScope.bayardConfig.developmentEnabled == true) ? $q.defer().resolve() : $q.reject("development is disabled");
            } else {
                ConfigService.getImplementationConfig({}, function(config) {
                    $rootScope.bayardConfig = config;
                    return ($rootScope.bayardConfig.developmentEnabled == true) ? $q.defer().resolve() : $q.reject("development is disabled");
                }, function(err) {
                    console.log(err);
                    return ($q.reject("development is disabled"));
                });
            }
        };

        $routeProvider
            .when('/', {
                templateUrl: 'resources/partials/main.html',
                controller: 'MainCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
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
            .when('/committees/committee/:id', {
                templateUrl: 'resources/partials/committeeDetails.html',
                controller: 'CommitteeDetailsCtrl'
            })
            .when('/committees/committee/:id/events', {
                templateUrl: 'resources/partials/committeeEvents.html',
                controller: 'CommitteeEventsCtrl'
            })
            .when('/events/event/:id', {
                templateUrl: 'resources/partials/eventDetails.html',
                controller: 'EventDetailsCtrl'
            })
            .when('/events/event/:id/sign-in', {
                templateUrl: 'resources/partials/eventSignIn.html',
                controller: 'EventSignInCtrl'
            })
            .when('/login', {
                templateUrl: 'resources/partials/login.html',
                controller: 'LoginCtrl'
            })
            .when('/configuration', {
                templateUrl: 'resources/partials/configurationOptions.html',
                controller: 'ConfigurationCtrl'
            })
            .when('/groups', {
                templateUrl: 'resources/partials/groupList.html',
                controller: 'GroupsCtrl'
            })
            .when('/groups/group/:id', {
                templateUrl: 'resources/partials/groupDetails.html',
                controller: 'GroupDetailsCtrl'
            })
            .when('/users', {
                templateUrl: 'resources/partials/users.html',
                controller: 'UserCtrl'
            })
            .when('/foundations', {
                templateUrl: 'resources/partials/foundationList.html',
                controller: 'FoundationListCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/foundations/:id', {
                templateUrl: 'resources/partials/foundationDetails.html',
                controller: 'FoundationDetailsCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/grants', {
                templateUrl: 'resources/partials/grantList.html',
                controller: 'GrantListCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/grants/create/:foundationId', {
                templateUrl: 'resources/partials/grantList.html',
                controller: 'GrantListCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/grants/:id', {
                templateUrl: 'resources/partials/grantDetails.html',
                controller: 'GrantDetailsCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/interactions/:id', {
                templateUrl: 'resources/partials/interactionDetails.html',
                controller: 'InteractionDetailsCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/donations', {
                templateUrl: 'resources/partials/donationList.html',
                controller: 'DonationListCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/donations/:id', {
                templateUrl: 'resources/partials/donationDetails.html',
                controller: 'DonationDetailsCtrl',
                resolve: {
                    enabled: resolveDevelopmentEnabled
                }
            })
            .when('/logout', {
                templateUrl: 'resources/partials/login.html',
                controller: 'LogoutCtrl'
            })
            .otherwise({redirectTo: '/'});


        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    });

    app.run(function($rootScope, $location) {
        $rootScope.user = sessionStorage.getItem('bayard-user');
        $rootScope.eventSignInMode = false;

        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            $rootScope.authenticated = sessionStorage.getItem('bayard-user-authenticated');
            var eventSignInMode = sessionStorage.getItem('event-sign-in-mode');
            $rootScope.eventSignInMode = eventSignInMode == null || eventSignInMode == 'false' ? false : true;

            if ($rootScope.authenticated == null || $rootScope.authenticated == 'false') {
                event.preventDefault();
                $rootScope.$evalAsync(function() {
                    $location.path('/login');
                });
            } else if ($rootScope.eventSignInMode) {
                console.log('Event Sign In id:' + $rootScope.eventSignInId); //TODO remove
                event.preventDefault();
                $rootScope.$evalAsync(function() {
                    var eventId = sessionStorage.getItem('event-sign-in-id');
                    $location.path('/events/event/' + eventId + '/sign-in');
                });
            }
        });

        $rootScope.booleanToString = function (value) {
            if (value) {
                return "Yes";
            }
            return "No";
        };
    })



}());