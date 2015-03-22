var smwcControllers = angular.module('smwcControllers', []);

smwcControllers.controller('ContactsController', ['$scope', '$http', function($scope, $http) {

    $http.get('../contacts')
        .success(function (response) {
            $scope.contacts = response;
        })
        .error(function (response) {
            //TODO
        });

}]);

smwcControllers.controller('MainController', ['$scope', function($scope) {
    $scope.testMessage = "Check out our home!";
}]);

smwcControllers.controller('CreateContactController', ['$scope', function($scope) {
    $scope.testMessage = "Create a new Contact here!";
}]);