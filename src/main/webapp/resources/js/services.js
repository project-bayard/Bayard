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
            }
        });
    }]);

}());