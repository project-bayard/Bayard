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

controllers.controller('CreateContactCtrl', ['$scope', 'ContactService', '$location', function($scope, ContactService, $location) {

    $scope.errorMessage = "";
    $scope.success = null;

    $scope.submit = function() {

        ContactService.create({}, $scope.contact, function(data) {
            console.log(data);
            $scope.success = true;
            $scope.newContactForm.$setPristine();
            $scope.contact = "";
            $location.path($location.path());
        }, function (err) {
            console.log(err);
            $scope.errorMessage = err;
            $scope.success = false;
            $location.path($location.path());
        });
    };

}]);

controllers.controller('DetailsCtrl', ['$scope','$routeParams', 'ContactService', function($scope, $routeParams, ContactService) {

    var setup = function() {
        $scope.edit = false;
        $scope.success = null;
        $scope.errorMessage = "";
        $scope.addingEncounter = false;

        //TODO: decouple this knowledge
        $scope.assessmentRange = [0,1,2,3,4,5,6,7,8,9,10];

        //TODO: decouple this knowledge
        $scope.encounterTypes = ["Call", "Other"];

        ContactService.find({id : $routeParams.id}, function(data) {
            $scope.contact = data;
        }, function(err) {
            console.log(err);
        });
    };

    setup();

    $scope.addEncounter = function() {

        //TODO: use a selected initiator
        $scope.newEncounter.initiator = null;

        $scope.newEncounter.contact = $scope.contact.id;

        if (null === $scope.contact.encounters) {
            $scope.contact.encounters = [];
        }
        $scope.contact.encounters.push($scope.newEncounter);

        ContactService.update({id: $scope.contact.id}, $scope.contact, function(data) {
            $scope.encounterSuccess = true;
            $scope.newEncounterForm.$setPristine();
        }, function(err) {
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
}]);

}());

