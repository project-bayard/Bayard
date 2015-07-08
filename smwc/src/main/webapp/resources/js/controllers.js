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
                $scope.contact = {};

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

    controllers.controller('DetailsCtrl', ['$scope','$routeParams', 'ContactService', '$timeout','$location','OrganizationService',
        'EventService', 'CommitteeService',
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
            $scope.newOrganization = {hidden : true};
            $scope.addEvent = {hidden : true};
            $scope.addCommittee = {hidden : true};
            $scope.newEncounter = {};
            $scope.showingDemographics = false;
            $scope.demographicPanel = { updateRequest : {success : false,  failure : false }, editingDemographics : false, showingDemographics : false };
            $scope.memberInfoPanel = { showingPanel : false, updateRequest : {success : false,  failure : false },
                showingMemberInfo : false, editingMemberInfo : false,
                standings : {
                    good : {
                        label : "Good",
                        value : 1
                    },
                    bad : {
                        label : "Bad",
                        value : 2
                    },
                    other : {
                        label : "Other",
                        value : 3
                    }
                }};

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

        /*Basic details*/
        $scope.updateBasicDetails = function() {

            ContactService.update({id : $scope.contact.id}, $scope.contact, function(data) {
                //indicate success
                $scope.contactUpdated = true;
            }, function(err) {
                console.log(err);
            });
        };


        $scope.viewDetails = function (id) {
            var path = "/contacts/contact/" + id ;
            $location.path(path);
        };

        /*Encounters*/
        $scope.showEncounterForm = function () {
            $scope.addingEncounter = !$scope.addingEncounter;

            if ($scope.initiators == null) {

                ContactService.getInitiators({}, function(data) {
                    $scope.initiators = data;

                }, function(err) {
                    console.log(err);
                });
            }
        };


        $scope.addEncounter = function() {

            ContactService.createEncounter({id: $scope.contact.id}, $scope.newEncounter, function(data) {
                ContactService.getEncounters({id : $scope.contact.id}, function(encounters) {
                    $scope.newEncounterRequestSuccess = true;
                    $scope.newEncounter = {};

                    $timeout(function() {
                        $scope.newEncounterRequestSuccess = false;
                    }, 3000);

                    $scope.encountersTable = encounters;
                    $scope.addingEncounter = false;

                }, function(err) {
                    $scope.newEncounterRequestFailure = true;
                    $timeout(function() {
                        $scope.newEncounterRequestFailure = false;
                    }, 3000);
                    console.log(err);
                });

            }, function(err) {
                $scope.newEncounterRequestFailure = true;
                $timeout(function() {
                    $scope.newEncounterRequestFailure = false;
                }, 3000);
                console.log(err);
            });
        };


            $scope.viewEncounterDetails = function(encounter) {

                ContactService.find({id : encounter.initiator.id}, function(initiator) {
                    var initiatorName = initiator.firstName + " " + initiator.lastName;
                    $scope.encounterDetails = { initiatorId : initiator.id, notes : encounter.notes, initiatorName : initiatorName};

                }, function(err) {
                    console.log(err);
                });

            };

            $scope.displayEncounters = function () {
                $scope.showingEncounters = !$scope.showingEncounters;

                if ($scope.showingEncounters == true) {
                    ContactService.getEncounters({id : $scope.contact.id}, function(data) {
                        $scope.encountersTable =  data;
                    }, function(err) {
                        console.log(err);
                    });
                }
            };

        /*Events */

        $scope.toggleShowingEvents = function() {
            ContactService.getEvents({id : $scope.contact.id}, function(data) {
                $scope.contact.attendedEvents = data;
                $scope.eventsTable = $scope.contact.attendedEvents;

            }, function(err) {
                console.log(err);
            });
            $scope.showingEvents=!$scope.showingEvents;
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

            var idDto = { id : eventId };

            ContactService.attend({id : $scope.contact.id}, idDto, function(response) {
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


        /* Organizations */

        $scope.getContactOrganizations = function () {
            $scope.showingOrganizations = !$scope.showingOrganizations;

            if ($scope.showingOrganizations == true) {
                ContactService.getOrganizations({id: $scope.contact.id},function(data) {
                    $scope.contact.organizations = data
                }, function(err) {
                    console.log(err);
                });
            }
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

            ContactService.addToOrganization({id: $scope.contact.id},{id: organization.id},
                function(data) {
                if (data.status == 'SUCCESS') {
                    ContactService.getOrganizations({id: $scope.contact.id},function(data) {
                        $scope.contact.organizations = data
                    }, function(err) {
                        console.log(err);
                    });
                } else {
                    console.log(data.message)
                }
            }, function(err) {
                console.log(err);
            });

        };

        $scope.createAndAddToOrganization = function(organization) {
            OrganizationService.create( organization, function(data) {
                //Add this contact to the newly-created Organization
                ContactService.addToOrganization({id: $scope.contact.id},{id : data.id}, function(data) {
                    //Refresh organizations the contact is now a member of
                    ContactService.getOrganizations({id:$scope.contact.id}, function(data) {
                        $scope.contactUpdated = true;
                        $scope.contact.organizations = data;
                        $scope.addOrganization.hidden = true;
                        $scope.newOrganization.hidden = true;
                    }, function(err) {
                        console.log(err);
                        $scope.organizationSuccess = false;
                    });
                }, function (err) {
                    console.log(err);
                    $scope.organizationSuccess = false;
                });
            }, function(err) {
                console.log(err);
                $scope.organizationSuccess = false;
            });

            //Refresh list of all organizations known to the app
            OrganizationService.getOrganizations(function(allOrgs) {
                $scope.organizations = allOrgs;
            }, function(err) {
                console.log(err);
            });
        };

            /* Committees */
        $scope.getContactCommittees = function () {
            $scope.showingCommittees = !$scope.showingCommittees;

            if ($scope.contact.committees == null) {
                ContactService.getCommittees({id: $scope.contact.id},function(data) {
                    $scope.contact.committees = data
                }, function(err) {
                    console.log(err);
                });
            }
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

            ContactService.addToCommittee({id: $scope.contact.id},{id: committee.id},
                function(data) {
                    if (data.status == 'SUCCESS') {
                        ContactService.getCommittees({id: $scope.contact.id},function(data) {
                            $scope.contact.committees = data
                        }, function(err) {
                            console.log(err);
                        });
                    } else {
                        console.log(data.message)
                    }
                }, function(err) {
                    console.log(err);
                });
        };

            /* Demographics*/

            $scope.toggleEditingDemographics = function() {
                $scope.demographicPanel.editingDemographics = !$scope.demographicPanel.editingDemographics;
            };

            $scope.booleanToString = function(value) {
                if (value) {
                    return "Yes";
                }
                return "No";
            };

            $scope.displayDemographics = function() {

                $scope.demographicPanel.showingDemographics = !$scope.demographicPanel.showingDemographics;

                ContactService.getDemographics({id : $scope.contact.id}, function(data) {
                    $scope.demographics = formatDemographics(data);
                }, function(err) {
                    console.log(err);
                });

            };

            $scope.updateDemographics = function() {

                console.log($scope.demographics);

                ContactService.updateDemographics({id: $scope.contact.id}, $scope.demographics, function(data) {
                    ContactService.getDemographics({id : $scope.contact.id}, function(demographics) {
                        $scope.demographics = formatDemographics(demographics);
                        $scope.demographicPanel.editingDemographics = false;
                        $scope.demographicPanel.updateRequest.success = true;
                        $timeout(function() {
                            $scope.demographicPanel.updateRequest.success = false;
                        }, 3000);

                    }, function(err) {
                        console.log(err);
                        $scope.demographicPanel.updateRequest.failure = true;
                        $timeout(function() {
                            $scope.demographicPanel.updateRequest.failure = false;
                        }, 3000);
                    });
                }, function(err) {
                    console.log(err);
                    $scope.demographicPanel.updateRequest.failure = true;
                    $timeout(function() {
                        $scope.demographicPanel.updateRequest.failure = false;
                    }, 3000);
                })
            };


            $scope.cancelUpdateDemographics = function() {
                ContactService.getDemographics({id : $scope.contact.id}, function(data) {
                    $scope.demographics = formatDemographics(data);
                    $scope.demographicPanel.editingDemographics = false;
                }, function(err) {
                    console.log(err);
                });
            };


            var formatDemographics = function(demographics) {
                demographics.dateOfBirth = new Date(demographics.dateOfBirth);
                return demographics;
            };

            /* MEMBERINFO */

            $scope.checkIfMember = function() {
                $scope.memberInfoPanel.showingPanel = !$scope.memberInfoPanel.showingPanel;

                if($scope.contact.member == true) {
                    $scope.displayMemberInfo();
                } else {
                    $scope.promptNoMemberInfo();
                }
            };

            $scope.promptNoMemberInfo = function() {
                $scope.promptNotMember = true;
            };

            $scope.becomeMember = function() {
                $scope.displayMemberInfo();
                $scope.toggleEditingMemberInfo();
            };

            $scope.updateMembershipInfo = function() {
                ContactService.updateMemberInfo({id: $scope.contact.id}, $scope.memberInfo, function(data) {
                    ContactService.getMemberInfo({id: $scope.contact.id}, function(data) {
                        $scope.contact.member = true;
                        $scope.promptNotMember = false;
                        $scope.memberInfoPanel.editingMemberInfo = false;
                        $scope.memberInfoPanel.updateRequest.success = true;
                        $timeout(function() {
                            $scope.memberInfoPanel.updateRequest.success = false;
                        }, 3000);
                    }, function(err) {
                        console.log(err);
                        $scope.memberInfoPanel.updateRequest.failure = true;
                        $timeout(function() {
                            $scope.memberInfoPanel.updateRequest.failure = false;
                        }, 3000);
                    })
                }, function(err) {
                    console.log(err);
                    $scope.memberInfoPanel.updateRequest.failure = true;
                    $timeout(function() {
                        $scope.memberInfoPanel.updateRequest.failure = false;
                    }, 3000);                })
            };


            $scope.displayMemberInfo = function() {
                console.log("Hit displayMemberInfo");

                $scope.memberInfoPanel.showingMemberInfo = !$scope.memberInfoPanel.showingMemberInfo;

                if ($scope.memberInfoPanel.showingMemberInfo) {
                    ContactService.getMemberInfo({id : $scope.contact.id}, function(data) {
                        $scope.memberInfo = data;
                    }, function(err) {
                        console.log(err);
                    });
                }
            };

            $scope.toggleEditingMemberInfo = function() {
                $scope.memberInfoPanel.editingMemberInfo = !$scope.memberInfoPanel.editingMemberInfo;
            };

            $scope.cancelUpdateMemberInfo = function() {
                ContactService.getMemberInfo({id : $scope.contact.id}, function(data) {
                    $scope.memberInfo = data;
                    $scope.memberInfoPanel.editingMemberInfo = false;
                    $scope.checkIfMember();
                }, function(err) {
                    console.log(err);
                });
            };


        }]);



    controllers.controller('EventsCtrl', ['$scope', 'EventService', 'CommitteeService', function($scope, EventService, CommitteeService) {

        $scope.addEvent = {hidden: true};
        $scope.newEvent = {};

        CommitteeService.findAll({}, function(data) {
           $scope.committees = data;
            $scope.committees.push({id: null, name: "None"});
        }, function(err) {
            console.log(err);
        });

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

        $scope.createOrganization = function(organization) {
            organization.members = [];
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



