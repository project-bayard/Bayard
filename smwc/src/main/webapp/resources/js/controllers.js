(function () {
    'use strict';

    var controllers = angular.module('controllers', []);

    controllers.controller('ContactsCtrl', ['$scope', 'ContactService', function($scope, ContactService) {

        ContactService.findAll({}, function(data) {
            $scope.contacts = data;
        }, function(err) {
            console.log(err);
        });

    }]);

    controllers.controller('MainCtrl', ['$scope', function($scope) {
        $scope.testMessage = "Check out our home!";
    }]);

    controllers.controller('CreateContactCtrl', ['$scope', 'ContactService', '$location', '$timeout', function($scope, ContactService, $location, $timeout) {

        $scope.errorMessage = "";
        $scope.success = null;

        $scope.submit = function() {

            ContactService.create({}, $scope.contact, function(data) {
                console.log(data);
                $scope.newContactForm.$setPristine();
                $scope.contact = "";

                $scope.requestSuccess = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000)
            }, function (err) {
                console.log(err);
                $scope.errorMessage = err;

                $scope.requestError = true;
                $timeout(function() {
                    $scope.requestError = false;
                }, 3000)
            });
        };

    }]);

    controllers.controller('DetailsCtrl', ['$scope','$routeParams', 'ContactService', '$timeout','$location','OrganizationService', function($scope, $routeParams, ContactService, $timeout, $location, OrganizationService) {

        var setup = function() {
            $scope.edit = false;
            $scope.success = null;
            $scope.errorMessage = "";
            $scope.addingEncounter = false;
            $scope.encounterSuccess = true;
            $scope.initiator = null;
            $scope.organizations = null;
            $scope.addOrganization = {hidden : true};
            $scope.newEncounter = {};

            //TODO: decouple this knowledge
            $scope.assessmentRange = [0,1,2,3,4,5,6,7,8,9,10];

            //TODO: decouple this knowledge
            $scope.encounterTypes = ["Call", "Other"];

            ContactService.find({id : $routeParams.id}, function(data) {
                $scope.contact = data;
            }, function(err) {
                console.log(err);
            });


            ContactService.findAll({}, function(data) {
                $scope.contacts = data;
            }, function(err) {
                console.log(err);
            });
        };

        setup();

        $scope.addEncounter = function() {
            $scope.newEncounter.contact = $scope.contact.id;

            if (null === $scope.contact.encounters) {
                $scope.contact.encounters = [];
            }
            $scope.contact.encounters.push($scope.newEncounter);

            ContactService.update({id: $scope.contact.id}, $scope.contact, function(data) {
                $scope.requestSuccess = true;
                $scope.newEncounter = {};
                $scope.addingEncounter = false;
                $scope.encounterSuccess = true;

                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);

            }, function(err) {
                $scope.encounterSuccess = false;
                console.log(err);
            });
        };

        $scope.updateBasicDetails = function() {

            ContactService.update({id : $scope.contact.id}, $scope.contact, function(data) {
                //indicate success
                $scope.contactUpdated = true;
            }, function(err) {
                console.log(err);
            });
        };

        $scope.setEncounterInitiator = function(id) {
            $scope.initiator = {firstName: "" , lastName: ""};

            ContactService.find({id :id}, function(data) {
                $scope.initiator = data;
            }, function(err) {
                console.log(err);
            });
        };

        $scope.viewDetails = function (id) {
            var path = "/contacts/contact/" + id ;
            $location.path(path);
        };

        $scope.getOrganizations = function() {
            $scope.addOrganization.hidden = false;

            if ($scope.organizations == null) {
                OrganizationService.findAll({}, function(data) {
                    $scope.organizations = data;
                }, function(err) {
                    console.log(err);
                });
            }
        };

        $scope.addToOrganization = function (index) {
            $scope.organizationSuccess = true;
            var organization = $scope.organizations[index];

            if ($scope.contact.organizations == null) {
                $scope.contact.organizations = [];

            }

            var members = [$scope.contact.id];
            for (var i = 0; i < organization.members.length; i++) {
                members.push(organization.members[i].id);
            }

            $scope.contact.organizations.push({id : organization.id, name: organization.name, members: members });

            ContactService.update({id: $scope.contact.id}, $scope.contact, function(data) {
                $scope.addOrganization.hidden = true;

                $timeout(function() {
                    $scope.requestSuccess = false;

                }, 3000);

            }, function(err) {
                console.log(err);
                $scope.organizationSuccess = false;

            });

        };

        $scope.addToNewOrganization = function(name) {
            var organization = {name: name, members : [$scope.contact.id]};

            OrganizationService.create( organization, function(data) {
                $scope.contactUpdated = true;
                $scope.contact.organizations.push(organization);
            }, function(err) {
                console.log(err);
                $scope.organizationSuccess = false;
            });

        }



    }]);


    controllers.controller('OrganizationsCtrl', ['$scope', 'OrganizationService', function($scope, OrganizationService) {

        $scope.addOrganization = {hidden: true};

        OrganizationService.findAll({}, function(data) {
            $scope.organizations = data;
        }, function(err) {
            console.log(err);
        });

        $scope.createOrganization = function(name) {
            var organization = {name: name, members: []};
            OrganizationService.create( organization, function(data) {
                $scope.organizations.push(organization);
                $scope.addOrganization = {hidden: true};
            }, function(err) {
                console.log(err);
            });
        }


    }]);


}());



