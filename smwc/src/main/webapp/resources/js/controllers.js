(function () {
    'use strict';



var controllers = angular.module('controllers', []);

controllers.controller('ContactsCtrl', ['$scope', 'ContactService', function($scope, ContactService) {


    $scope.contacts = ContactService.query(function () {
        $scope.error = false;
    }, function () {
        $scope.error = true;
    });



}]);

controllers.controller('MainCtrl', ['$scope', function($scope) {
    $scope.testMessage = "Check out our home!";
}]);

controllers.controller('CreateContactCtrl', ['$scope', 'ContactService', '$location','$http', function($scope, ContactService, $location, $http) {

    $scope.errorMessage = "";
    $scope.success = null;


    $scope.submit = function() {

        $http.post("../contacts", $scope.contact)
            .success(function (response) {
                console.log(response);
                $scope.success = true;
                $scope.newContactForm.$setPristine();
                $scope.contact = "";
                $location.path($location.path());

            }).error(function(response) {
                console.log(response);
                $scope.errorMessage = response;
                $scope.success = false;
                $location.path($location.path());
            });
    };


/*
    $scope.submit = function() {

        ContactService.save(function(response) {
            console.log(response);
            $scope.success = true;
            $scope.newContactForm.$setPristine();
            $scope.contact = "";
            $location.path($location.path());

        }, function (response) {

            $scope.success = false;
            $scope.errorMessage = response;
            console.log(response);
        });
    }*/

}]);

controllers.controller('DetailsCtrl', ['$scope', '$http','$routeParams', function($scope, $http, $routeParams) {
    $scope.edit = false;
    $scope.success = null;
    $scope.errorMessage = "";

    $http.get('../contacts/contact/' + $routeParams.id)
        .success(function (response) {
            $scope.contact = response;
        })
        .error(function (response) {
            //TODO
        });

    $scope.submit = function() {
        $http.put("../contacts/" + $scope.contact.id, $scope.contact)
            .success(function (response) {
                console.log(response);
                $scope.success = true;

            }).error(function(response) {
                console.log(response);
                $scope.errorMessage = response;
                $scope.success = false;
            });
    };
}]);

}());

