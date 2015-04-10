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

controllers.controller('CreateContactCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {

    $scope.errorMessage = "";
    $scope.success = null;

    $scope.submit = function() {
        $http.post("../contacts", $scope.contact)
            .success(function (response) {
                console.log(response);
                $scope.success = true;
                $scope.newContact.$setPristine();
                $scope.contact = "";
                $location.path($location.path());

            }).error(function(response) {
                console.log(response);
                $scope.errorMessage = response;
                $scope.success = false;
                $location.path($location.path());
            });
    };

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

