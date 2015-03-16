var smwcControllers = angular.module('smwcControllers', []);

smwcControllers.controller('ContactsController', ['$scope', function($scope) {
    $scope.testMessage = "Look at all of our contacts!";
}]);

smwcControllers.controller('MainController', ['$scope', function($scope) {
    $scope.testMessage = "Check out our home!";
}]);

smwcControllers.controller('CreateContactController', ['$scope', function($scope) {
    $scope.testMessage = "Create a new Contact here!";
}]);