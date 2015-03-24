var smwcControllers = angular.module('smwcControllers', []);

smwcControllers.controller('ContactsController', ['$scope', '$http', function($scope, $http) {

    $http.get('../contacts')
        .success(function (response) {
            console.log(response);
            $scope.contacts = response;
        })
        .error(function (response) {
            console.log(response);
            //TODO
        });

}]);

smwcControllers.controller('MainController', ['$scope', function($scope) {
    $scope.testMessage = "Check out our home!";
}]);

smwcControllers.controller('CreateContactController', ['$scope', '$http', '$location', function($scope, $http, $location) {
    $scope.contact = "";
    $scope.errorMessage = "";

    $scope.submit = function() {
        $http.post("../contacts", $scope.contact)
            .success(function (response) {
                console.log(response);
            }).error(function(response) {
                console.log(response);
                $scope.errorMessage = response;
            });
    };

}]);

smwcControllers.controller('DetailsCtrl', ['$scope', '$http','$routeParams', function($scope, $http, $routeParams) {
    $http.get('../contacts/contact/' + $routeParams.id)
        .success(function (response) {
            $scope.contact = response;
        })
        .error(function (response) {
            //TODO
        });
}]);