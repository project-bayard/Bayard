(function () {
    'use strict';

    /* Services */

    var services = angular.module('services', ['ngResource']);

    services.factory('DateFormatter', ['$filter', function($filter) {
        return {
            formatDate : function(date) {
                return $filter('date')(date, 'yyyy-MM-dd');
            },
            asDate: function(string) {
                //prevent html input date from misinterpreting midnight
                var date = new Date(string);
                var localTime = date.getTime();
                var localOffset = date.getTimezoneOffset() * 60000;
                return new Date(localTime + localOffset);
            },
            getDateFormat : function() {
                return 'yyyy-MM-dd';
            }
        }
    }]);

    services.factory('RouteChangeService', function() {
        var routeChangeData = {};
        function set(data) {
            routeChangeData = data;
        }
        function get() {
            return routeChangeData;
        }

        return {
            set: set,
            get: get
        }
    });

    services.factory('LoginService', ['$http', function($http) {

        return {
            authenticate: function(credentials, callback) {
                var headers = credentials ? {
                    authorization: "Basic "
                    + btoa(credentials.username + ":" + credentials.password)
                } : {};

                $http.get('/users/authenticate', {headers: headers}).success(function (data) {
                    if (data.email) {
                        sessionStorage.setItem('bayard-user-authenticated', 'true');
                        sessionStorage.setItem('bayard-user', data);
                        console.log(data);
                    } else {
                        sessionStorage.setItem('bayard-user-authenticated', 'false');
                    }
                    callback && callback();
                }).error(function () {
                    sessionStorage.setItem('bayard-user-authenticated', 'false');
                    callback && callback();
                });
            }
        }
    }]);



    services.factory('ContactService',[ '$resource', function ($resource) {
        return $resource('../contacts/:id', {id : '@id', entityId : '@entityId'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            delete : {
                method: 'DELETE',
                params : {
                    id : '@id'
                }
            },
            attend: {
                method: 'PUT',
                url: "../contacts/:id/events"
            },
            addToGroup: {
                method: 'PUT',
                url: "../contacts/:id/groups"
            },
            removeFromGroup: {
                method: 'DELETE',
                url: "../contacts/:id/groups/:entityId"
            },
            getGroups: {
                method: 'GET',
                url: "../contacts/:id/groups",
                isArray: true
            },
            removeFromEvent : {
                method: 'DELETE',
                url: "../contacts/:id/events/:entityId"
            },
            getEncounters : {
                method: 'GET',
                isArray : true,
                url: "../contacts/:id/encounters"
            },
            getInitiators : {
                method: 'GET',
                isArray : true,
                url: "../contacts/initiators"
            },
            getEvents : {
                method: 'GET',
                isArray : true,
                url: "../contacts/:id/events"
            },
            getOrganizations : {
                method: 'GET',
                isArray : true,
                url : "/contacts/:id/organizations"
            },
            addToOrganization : {
                method: 'PUT',
                url : "/contacts/:id/organizations"
            },
            createEncounter : {
                method : 'PUT',
                url: "/contacts/:id/encounters"
            },
            updateEncounter : {
                method : 'PUT',
                url: "/contacts/:id/encounters/:entityId"
            },
            deleteEncounter : {
                method: 'DELETE',
                url: '/contacts/:id/encounters/:entityId'
            },
            getDemographics : {
                method : 'GET',
                url: "/contacts/:id/demographics"
            },
            updateDemographics : {
                method: 'PUT',
                url: "/contacts/:id/demographics"
            },
            addToCommittee : {
                method : 'PUT',
                url : "/contacts/:id/committees"
            },
            getCommittees : {
                method : 'GET',
                isArray : true,
                url : "/contacts/:id/committees"
            },
            removeFromCommittee: {
                method : 'DELETE',
                url: "/contacts/:id/committees/:entityId"
            },
            getMemberInfo : {
                method : "GET",
                url : "/contacts/:id/memberinfo"
            },
            updateMemberInfo : {
                method : "PUT",
                url : "/contacts/:id/memberinfo"
            },
            removeFromOrganization : {
                method: "DELETE",
                url : "/contacts/:id/organizations/:entityId"
            },
            findBySignInDetails : {
                method : "POST",
                url : "/contacts/find"
            },
            addDonation : {
                method : "POST",
                url : "/contacts/:id/donations"
            },
            getDonations : {
                method : "GET",
                isArray: true,
                url : "/contacts/:id/donations"
            },
            addSustainerPeriod : {
                method : "POST",
                url : "/contacts/:id/sustainer"
            },
            updateSustainerPeriod: {
                method: 'PUT',
                url: "/contacts/:id/sustainer/:entityId"
            },
            getSustainerPeriods: {
                method: 'GET',
                isArray: true,
                url: "/contacts/:id/sustainer"
            },
            getSustainerPeriod: {
                method: 'GET',
                url: "/contacts/:id/sustainer/:entityId"
            },
            deleteSustainerPeriod: {
                method: 'DELETE',
                url: "/contacts/:id/sustainer/:entityId"
            },
            getAllCurrentSustainers: {
                method: 'GET',
                url: "/contacts/currentSustainers",
                isArray: true
            },
            getByQuery : {
                method : 'POST',
                url : "/contacts/query",
                isArray: true
            }
        });
    }]);

    services.factory('GrantService',[ '$resource', function ($resource) {
        return $resource('../grants/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST',
                url: "../foundations/:foundationId/grants",
                params: {
                    foundationId : "@foundationId"
                }
            },
            delete : {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            }
        });
    }]);

    services.factory('GrantService',[ '$resource', function ($resource) {
        return $resource('../grants/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST',
                url: "../foundations/:foundationId/grants",
                params: {
                    foundationId : "@foundationId"
                }
            },
            delete : {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            }
        });
    }]);


    services.factory('DonationService',[ '$resource', function ($resource) {
        return $resource('../donations/:id', {id : '@id', budgetItemId: '@budgetItemId'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            delete : {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            },
            getBudgetItems : {
                method: 'GET',
                url: '../donations/budgetitems',
                isArray: true
            },
            createBudgetItem : {
                method: 'POST',
                url: '../donations/budgetitems'
            },
            deleteBudgetItem : {
                method: 'DELETE',
                url: '../donations/budgetitems/:budgetItemId'
            },
            getDonationsByDepositRange : {
                method: 'GET',
                url: '../donations/bydepositdate'
            },
            getDonationsByReceiptRange : {
                method: 'GET',
                url: '../donations/byreceiptdate'
            },
            getDonationsByBudgetItem : {
                method: 'GET',
                url: '../donations/bybudgetitem'
            }
        });
    }]);

    services.factory('FoundationService',[ '$resource', function ($resource) {
        return $resource('../foundations/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            delete : {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            },
            createInteractionRecord: {
                method: 'POST',
                url: "/foundations/:id/interactions"
            },
            getInteractionRecords: {
                method: 'GET',
                url: "/foundations/:id/interactions",
                isArray : true,
                params: {
                    id: "@id"
                }
            }
        });
    }]);

    services.factory('InteractionService',[ '$resource', function ($resource) {
        return $resource('../interactions/:id', {id : '@id', typeId: '@typeId'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            delete : {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            },
            getInteractionTypes : {
                method: 'GET',
                url: '/interactions/interactiontypes',
                isArray : true
            },
            createType : {
                method: 'POST',
                url: '/interactions/interactiontypes'
            },
            deleteType : {
                method: 'DELETE',
                url: '/interactions/interactiontypes/:typeId',
                params: {
                    typeId: "@typeId"
                }
            }
        });
    }]);

    services.factory('GrantService',[ '$resource', function ($resource) {
        return $resource('../grants/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST',
                url: "../foundations/:foundationId/grants",
                params: {
                    foundationId : "@foundationId"
                }
            },
            delete : {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            }
        });
    }]);

    services.factory('OrganizationService',[ '$resource', function ($resource) {
        return $resource('../organizations/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            delete : {
                method: 'DELETE'
            },
            addDonation : {
                method: 'POST',
                url: '/organizations/:id/donations'
            },
            getDonations: {
                method: 'GET',
                isArray: true,
                url: '/organizations/:id/donations'
            }
        });
    }]);

    services.factory('EventService',[ '$resource', function ($resource) {
        return $resource('../events/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            delete : {
                method: 'DELETE'
            },
            addDonation : {
                method: 'POST',
                url: '/events/:id/donations'
            },
            getDonations : {
                method : 'GET',
                isArray : true,
                url: '/events/:id/donations'
            }
        });
    }]);

    services.factory('CommitteeService',[ '$resource', function ($resource) {
        return $resource('../committees/:id', {id : '@id'}, {
            update : {
                method: 'PUT',
                params: {
                    id: "@id"
                }
            },
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            delete : {
                method: 'DELETE'
            }
        });
    }]);

    services.factory('UserService',[ '$resource', function ($resource) {
        return $resource('../users/:id', {id : '@id'}, {
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            createStartupUser : {
                method: 'POST',
                url : '/users/startup'
            },
            delete : {
                method: 'DELETE',
                url: '/users/:id'
            },
            updateDetails : {
                method: 'PUT',
                url: '/users/:id'
            },
            changePassword : {
                method: 'PATCH',
                url: '/users/:id/password'
            },
            authenticate : {
                method : 'GET',
                url : '/users/authenticate'
            }
        });
    }]);

    services.factory('PermissionService', function() {

        function PermissionInterpreter(currentUser) {
            this.user = currentUser;

            this.getId = function () {
                return this.user.id;
            };

            this.isSuperUser = function () {
                return this.user.role == "ROLE_SUPERUSER";
            };

            this.isElevatedUser = function () {
                return this.user.role == "ROLE_ELEVATED" || this.isSuperUser();
            };

            this.isDevelopmentUser = function() {
                return this.user.role == "ROLE_DEVELOPMENT" || this.isSuperUser() || this.isElevatedUser();
            };

            this.isUser = function () {
                return this.user.role == "ROLE_USER" || this.isElevatedUser() || this.isSuperUser();
            };

            this.canChangeRole = function (other) {
                var otherPermissions = new PermissionInterpreter(other);
                if (this.isSuperUser()) {
                    return true;
                }
                return this.isElevatedUser() && !otherPermissions.isSuperUser();
            }

        }

        return {
            getPermissionInterpreter : function(user) {
                return new PermissionInterpreter(user);
            }
        };

    });

    services.factory('GroupService',[ '$resource', function ($resource) {
        return $resource('../groups/:id', {id : '@id', entityId : '@entityId'}, {
            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            update : {
                method : 'PUT',
                url : '/groups/:id'
            },
            deleteGroup : {
                method : 'DELETE',
                url : '/groups/:id'
            },
            getAllContacts : {
                method : 'GET',
                url : '/groups/:id/all',
                isArray : true
            },
            addAggregation : {
                method : 'PUT',
                url : '/groups/:id/aggregations/:entityId'
            },
            removeAggregation : {
                method : 'PUT',
                url : '/groups/:id/aggregations/:entityId'
            }

        });
    }]);

    services.factory('EncounterTypeService',[ '$resource', function ($resource) {
        return $resource('../encounterTypes/:id', {id : '@id'}, {

            find : {
                method: 'GET',
                params: {
                    id : "@id"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            create : {
                method: 'POST'
            },
            delete : {
                method: 'DELETE',
                params: {
                    id :"@id"
                }
            }

        });
    }]);

    services.factory('DemographicService',[ '$resource', function ($resource) {
        return $resource('../demographics/:categoryName', {categoryName : '@categoryName'}, {
            find : {
                method: 'GET',
                params: {
                    categoryName : "@categoryName"
                }
            },
            findAll : {
                method: 'GET',
                isArray : true
            },
            createOption : {
                method: 'POST',
                url: '/demographics/:categoryName/options'
            }
        });
    }]);

    services.factory('ConfigService',[ '$resource', function ($resource) {
        return $resource('../config/:option', {categoryName : '@categoryName'}, {
            getImplementationConfig : {
                method: 'GET'
            },
            getStartupMode : {
                method: 'GET',
                url: "/config/startup"
            },
            updateConfig : {
                method: 'PUT'
            }
        });
    }]);

}());