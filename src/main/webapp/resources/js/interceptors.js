var interceptors = angular.module('interceptors', []);

interceptors.factory("baseInterceptor", ["$q", function ($q) {
        return {
            'request': function (config) {
                return config;
            },
            'requestError': function (rejection) {
                return $q.reject(rejection);
            },
            'response': function (response) {
                var result = response.resource;
                if (result != null) {
                    result.$status = response.status;
                    return result;
                } else {
                    return response;
                }
            },
            'responseError': function (rejection) {
                var result = rejection.resource;
                if (result != null) {
                    result.$status = rejection.status;
                    return $q.reject(result);
                } else {
                    return $q.reject(rejection);
                }
            }
        };
    }]);
