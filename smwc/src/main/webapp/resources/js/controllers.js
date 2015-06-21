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



        $scope.submitAndViewDetails = function() {

            ContactService.create({}, $scope.contact, function(postSuccess) {
                postSuccess.$promise.then(function(createdId) {
                    var detailsPath = "/contacts/contact/" + createdId.id;
                    $location.path(detailsPath);
                });
            }, function(err) {
                console.log(err);
                $scope.errorMessage = err;
                $scope.requestError = true;
                $timeout(function() {
                    $scope.requestError = false;
                }, 3000)
            });

        }

    }]);

    controllers.controller('DetailsCtrl', ['$scope','$routeParams', 'ContactService', '$timeout','$location','OrganizationService', 'EventService', 'CommitteeService',
        function($scope, $routeParams, ContactService, $timeout, $location, OrganizationService, EventService, CommitteeService) {

        var setup = function() {
            $scope.edit = false;
            $scope.success = null;
            $scope.errorMessage = "";
            $scope.addingEncounter = false;
            $scope.encounterSuccess = true;
            $scope.initiator = null;
            $scope.organizations = null;
            $scope.addOrganization = {hidden : true};
            $scope.addEvent = {hidden : true};
            $scope.addCommittee = {hidden : true};
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

            /*
        $scope.setEncounterInitiator = function(id) {
            $scope.initiator = {firstName: "" , lastName: ""};

            ContactService.find({id :id}, function(data) {
                $scope.initiator = data;
            }, function(err) {
                console.log(err);
            });
        };
        */

        $scope.viewDetails = function (id) {
            var path = "/contacts/contact/" + id ;
            $location.path(path);
        };

        $scope.toggleShowingEvents = function() {
            ContactService.getEvents({id : $scope.contact.id}, function(data) {
                $scope.contact.attendedEvents = data;
                $scope.eventsTable = $scope.contact.attendedEvents;
<<<<<<< HEAD
                $scope.showingEvents=!$scope.showingEvents;
            }, function (err) {
                console.log(err);
            });

=======
            }, function(err) {
                console.log(err);
            });
            $scope.showingEvents=!$scope.showingEvents;
>>>>>>> stripHashingConcerns
        };

        $scope.getEvents = function() {
            $scope.addEvent.hidden = false;

            EventService.findAll({}, function(response) {
                console.log(response);
                $scope.events = response;
            }, function(err) {
                console.log(err);
            });

        };

        $scope.attendEvent = function (eventId) {

<<<<<<< HEAD
            var dto = { id : eventId };

            ContactService.attend({id : $scope.contact.id}, dto, function(response) {
=======
            var idDto = { id : eventId };

            ContactService.attend({id : $scope.contact.id}, idDto, function(response) {
>>>>>>> stripHashingConcerns
                ContactService.getEvents({id : $scope.contact.id}, function(data) {
                    $scope.contact.attendedEvents = data;
                    $scope.eventsTable = $scope.contact.attendedEvents;
                }, function(err) {
                    console.log(err);
                });

                $scope.addEvent.hidden = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);

            }, function(err) {
                console.log(err);
            });

        };

        $scope.getOrganizations = function() {
            $scope.addOrganization.hidden = !$scope.addOrganization.hidden;

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
            if (organization.members == null) {
                organization.members = [];
            }

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
                $scope.organizations.push(organization);
                $scope.addOrganization.hidden = true;
            }, function(err) {
                console.log(err);
                $scope.organizationSuccess = false;
            });

        };

        $scope.getCommittees = function() {
            $scope.addCommittee.hidden = !$scope.addCommittee.hidden;

            if ($scope.committees == null) {
                CommitteeService.findAll({}, function(data) {
                    $scope.committees = data;
                }, function(err) {
                    console.log(err);
                });
            }
        };


        $scope.addToCommittee = function (index) {
            $scope.committeeSuccess = true;
            var committee = $scope.committees[index];

            if ($scope.contact.committees == null) {
                $scope.contact.committees = [];

            }


            var members = [$scope.contact.id];
            if (committee.members == null) {
                committee.members = [];
            }

            for (var i = 0; i < committee.members.length; i++) {
                members.push(committee.members[i].id);
            }

            $scope.contact.committees.push({id : committee.id, name: committee.name, members: members });

            ContactService.update({id: $scope.contact.id}, $scope.contact, function(data) {
                $scope.addCommittee.hidden = true;

                $timeout(function() {
                    $scope.requestSuccess = false;

                }, 3000);

            }, function(err) {
                console.log(err);
                $scope.committeeSuccess = false;

            });

        };

        $scope.getEncounters = function () {
            $scope.showingEncounters= !$scope.showingEncounters;

            if ($scope.contact.encounters == null) {
                ContactService.getEncounters({id : $scope.contact.id}, function(data) {
                    $scope.contact.encounters = data;
                }, function(err) {
                    console.log(err);
                });
            }

        }

        }]);

    controllers.controller('EventsCtrl', ['$scope', 'EventService', function($scope, EventService) {

        $scope.addEvent = {hidden: true};
        $scope.newEvent = {};

        var populateEvents = function() {
            EventService.findAll({}, function(response) {
                $scope.eventsTable = response;
            }, function(err) {
                console.log(err);
            });
        };

        populateEvents();

        $scope.createEvent = function() {

            $scope.newEvent.attendees = [];

            EventService.create({}, $scope.newEvent, function(response) {
                $scope.addEvent = {hidden : true};
                populateEvents();
                $scope.newEventForm.$setPristine();
                $scope.newEvent = {};
            }, function(err) {
                console.log(err);
            });
        };
    }]);


    controllers.controller('EventDetailsCtrl', ['$scope', 'EventService', '$routeParams', function($scope, EventService, $routeParams) {

        EventService.find({id : $routeParams.id}, function(data) {
            $scope.event = data;
            if ($scope.event.attendees == null) {
                $scope.event.attendees = [];
            }
        }, function(err) {
            console.log(err);
        });
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
                $scope.addOrganization = {hidden: true};

                OrganizationService.findAll({}, function(data) {
                    $scope.organizations = data;
                }, function(err) {
                    console.log(err);
                });
            }, function(err) {
                console.log(err);
            });
        };


    }]);

    controllers.controller('OrganizationDetailsCtrl', ['$scope', 'OrganizationService', '$routeParams', function($scope, OrganizationService, $routeParams) {

        OrganizationService.find({id : $routeParams.id}, function(data) {
            $scope.organization = data;
            if ($scope.organization.members == null) {
                $scope.organization.members = [];
            }
        }, function(err) {
            console.log(err);
        });
    }]);

    controllers.controller ('CommitteesCtrl',['$scope', 'CommitteeService', function($scope, CommitteeService){

        var setup = function () {
            $scope.panels = [];
            $scope.addCommittee = {hidden: true};

            CommitteeService.findAll({}, function(data) {
                $scope.committees = data;
                for (var i = 0; i < $scope.committees.size; i++) {
                    $scope.panels.push({"hidden" : true});
                }

            }, function(err) {
                console.log(err);
            });
        };

        setup();

        $scope.createCommittee = function(name) {
            var committee = {name: name, members: []};

            CommitteeService.create( committee, function(data) {
                $scope.addCommittee = {hidden: true};
                setup();

            }, function(err) {
                console.log(err);
            });
        }

    }]);

}());



