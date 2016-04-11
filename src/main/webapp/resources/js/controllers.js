(function () {
    'use strict';

    function ResponseErrorInterpreter(response) {
        this.response = response;
        this.message = response.data.message;
        this.id = response.data.id;
        this.type = response.data.type;

        this.isConstraintViolation = function () {
            return this.type == "Constraint Violation";
        };

        this.isSecurityConstraint = function () {
            return this.type == "Security Constraint"
        };

        this.isAccessDenied = function () {
            return this.type == "Access Denied";
        };

        this.isNullReference = function () {
            return this.type == "Null Reference";
        };
    }

    var controllers = angular.module('controllers', []);

    controllers.controller('ContactsCtrl', ['$scope', 'ContactService', '$location', function ($scope, ContactService, $location) {

        ContactService.findAll({}, function (data) {
            $scope.contacts = data;
        }, function (err) {
            console.log(err);
        });

    }]);

    controllers.controller('MainCtrl', ['$scope', '$location', 'ConfigService', function($scope, $location, ConfigService) {

    }]);

    controllers.controller('LogoutCtrl', ['$scope', '$location', '$rootScope', function ($scope, $location, $rootScope) {
        console.log("Logging out");
        sessionStorage.setItem('bayard-user-authenticated', 'false');
        sessionStorage.setItem('bayard-user', {});
        sessionStorage.removeItem('event-sign-in-mode');
        sessionStorage.removeItem('event-sign-in-id');
        $rootScope.authenticated = null;
        $rootScope.eventSignInMode = false;
        $location.path("/login");
    }]);

    controllers.controller('CreateContactCtrl', ['$scope', 'ContactService', '$location', '$timeout', '$window', function ($scope, ContactService, $location, $timeout, $window) {

        $scope.crudRequest = {
            success: false,
            failure: false,
            constraintViolation: null,
            clashingDomainId: null,
            clear: function () {
                this.success = false;
                this.failure = false;
                this.constraintViolation = null;
                this.clashingDomainId = null;
            }
        };

        $scope.submit = function () {

            ContactService.create({}, $scope.newContact, function (data) {
                $scope.newContactForm.$setPristine();
                $scope.newContact = {};
                $scope.crudRequest.success = true;
                $scope.crudRequest.constraintViolation = null;
                $scope.crudRequest.clashingDomainId = null;
                $timeout(function () {
                    $scope.crudRequest.success = false;
                    $scope.crudRequest.clear();
                }, 3000);
            }, function (err) {
                handleCrudError(err);
            });
        };

        var handleCrudError = function (err) {
            var errorResponse = new ResponseErrorInterpreter(err);
            if (errorResponse.isConstraintViolation()) {
                $scope.crudRequest.constraintViolation = errorResponse.message;
                if (errorResponse.id != null) {
                    $scope.crudRequest.clashingDomainId = errorResponse.id;
                }
            }
            $scope.crudRequest.failure = true;
            $timeout(function () {
                $scope.crudRequest.failure = false;
            }, 3000);
        };

        $scope.submitAndViewDetails = function () {

            ContactService.create({}, $scope.newContact, function (postSuccess) {
                postSuccess.$promise.then(function (response) {
                    $scope.viewDetails(response.id);
                });
            }, function (err) {
                handleCrudError(err);
            });

        };

        $scope.viewDetails = function (contactId) {
            var detailsPath = "/contacts/contact/" + contactId;
            $location.path(detailsPath);
        };

    }]);

    controllers.controller('DetailsCtrl', ['$scope', '$routeParams', 'ContactService', '$timeout', '$location', 'OrganizationService',
        'EventService', 'CommitteeService', 'DateFormatter', '$window', 'EncounterTypeService', 'GroupService', 'DemographicService', 'RouteChangeService',
        function ($scope, $routeParams, ContactService, $timeout, $location, OrganizationService, EventService, CommitteeService, DateFormatter,
                  $window, EncounterTypeService, GroupService, DemographicService, RouteChangeService) {

            var setup = function () {
                $scope.errorMessage = "";
                $scope.addingEncounter = false;
                $scope.encounterSuccess = true;
                $scope.initiator = null;
                $scope.organizations = null;
                $scope.addOrganization = {hidden: true};
                $scope.newOrganization = {hidden: true};
                $scope.addEvent = {hidden: true};
                $scope.addCommittee = {hidden: true};
                $scope.modelHolder = {
                    encounterModel: {},
                    organizationModel: {},
                    donationModel: {}
                };

                $scope.donorPanel = {
                    showingPanel : false,
                    creatingDonation : false,
                    creatingSustainerPeriod: false,
                };

                $scope.basicDetailsPanel = {
                    editingBasicDetails: false,
                    updateRequest: {
                        success: false,
                        failure: false,
                        constraintViolation: null,
                        clashingDomainId: null
                    },
                    wipeErrors: function () {
                        this.updateRequest.failure = false;
                        this.updateRequest.constraintViolation = null;
                        this.updateRequest.clashingDomainId = null;
                    }
                };

                $scope.memberInfoPanel = {
                    showingPanel: false, updateRequest: {success: false, failure: false},
                    showingMemberInfo: false, editingMemberInfo: false,
                    standings: {
                        good: {
                            label: "Good",
                            value: 1
                        },
                        bad: {
                            label: "Bad",
                            value: 2
                        },
                        other: {
                            label: "Other",
                            value: 3
                        }
                    }
                };

                //TODO: decouple this knowledge
                $scope.assessmentRange = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

                ContactService.find({id: $routeParams.id}, function (data) {
                    $scope.contact = data;
                }, function (err) {
                    console.log(err);
                });

                $scope.modelHolder = {
                    donationModel : {
                        dates: {
                            dateOfDeposit: new Date(),
                            dateOfReceipt: new Date()
                        }
                    }
                };

                $scope.formHolder = {};

            };

            var establishBasicDetails = function (id) {
                ContactService.find({id: id}, function (data) {
                    $scope.contact = data;
                }, function (err) {
                    console.log(err);
                });
            };

            setup();
            establishBasicDetails($routeParams.id);

            /*Basic details*/
            $scope.updateBasicDetails = function () {
                ContactService.update({id: $scope.contact.id}, $scope.contact, function (data) {
                    establishBasicDetails($scope.contact.id);
                    $scope.basicDetailsPanel.updateRequest.success = true;
                    $timeout(function () {
                        $scope.basicDetailsPanel.updateRequest.success = false;
                    }, 3000);
                    $scope.basicDetailsPanel.wipeErrors();
                    $scope.basicDetailsPanel.editingBasicDetails = false;
                }, function (err) {
                    var errorResponse = new ResponseErrorInterpreter(err);
                    if (errorResponse.isConstraintViolation()) {
                        $scope.basicDetailsPanel.updateRequest.constraintViolation = errorResponse.message;
                        if (errorResponse.id != null) {
                            $scope.basicDetailsPanel.updateRequest.clashingDomainId = errorResponse.id;
                        }
                    }
                    console.log(err);
                });
            };

            $scope.cancelUpdateBasicDetails = function () {
                $scope.basicDetailsPanel.editingBasicDetails = false;
                $scope.basicDetailsPanel.wipeErrors();
                establishBasicDetails($scope.contact.id);
            };


            $scope.viewDetails = function (id) {
                var path = "/contacts/contact/" + id;
                $location.path(path);
            };

            /*Encounters*/
            $scope.showEncounterForm = function () {
                $scope.addingEncounter = !$scope.addingEncounter;
                populateInitiatorAndEncounterTypeLists();

            };

            var populateInitiatorAndEncounterTypeLists = function () {
                if ($scope.initiators == null) {

                    ContactService.getInitiators({}, function (data) {
                        $scope.initiators = data;

                    }, function (err) {
                        console.log(err);
                    });
                }

                if ($scope.encounterTypes == null) {
                    EncounterTypeService.findAll({}, function (data) {
                        $scope.encounterTypes = data;
                    }, function (err) {
                        console.log(err);
                    });

                }
            };


            $scope.addEncounter = function () {

                $scope.modelHolder.encounterModel.encounterDate = DateFormatter.formatDate($scope.modelHolder.encounterModel.jsDate);

                ContactService.createEncounter({id: $scope.contact.id}, $scope.modelHolder.encounterModel, function (data) {
                    ContactService.getEncounters({id: $scope.contact.id}, function (encounters) {
                        $scope.newEncounterRequestSuccess = true;
                        $scope.modelHolder.encounterModel = {};
                        $scope.formHolder.encounterForm.$setPristine();

                        $timeout(function () {
                            $scope.newEncounterRequestSuccess = false;
                        }, 3000);

                        $scope.encountersTable = encounters;
                        $scope.addingEncounter = false;

                    }, function (err) {
                        $scope.newEncounterRequestFailure = true;
                        $timeout(function () {
                            $scope.newEncounterRequestFailure = false;
                        }, 3000);
                        console.log(err);
                    });

                    ContactService.find({id: $scope.contact.id}, function (contact) {
                        $scope.contact = contact;
                    }, function (err) {
                        console.log(err);
                    })

                }, function (err) {
                    $scope.newEncounterRequestFailure = true;
                    $timeout(function () {
                        $scope.newEncounterRequestFailure = false;
                    }, 3000);
                    console.log(err);
                });
            };

            $scope.cancelAddEncounter = function () {
                $scope.modelHolder.encounterModel = {};
                $scope.formHolder.encounterForm.$setPristine();
                $scope.addingEncounter = false;
            };

            $scope.showUpdateEncounterForm = function () {
                $scope.updatingEncounter = true;
                populateInitiatorAndEncounterTypeLists();
            };

            $scope.deleteEncounter = function (encounterId) {

                var deleteConfirmed = $window.confirm('Are you sure you want to delete this encounter?');
                if (deleteConfirmed) {
                    ContactService.deleteEncounter({id: $scope.contact.id, entityId: encounterId}, function (succ) {
                        $('#encounterModal').modal('hide');
                        ContactService.getEncounters({id: $scope.contact.id}, function (encounters) {
                            $scope.encountersTable = encounters;
                        }, function (err) {
                            console.log(err);
                        });
                    }, function (err) {
                        console.log(err);
                    });
                }
            };

            $scope.updateEncounter = function () {
                $scope.modelHolder.encounterModel.encounterDate = DateFormatter.formatDate($scope.modelHolder.encounterModel.jsDate);
                ContactService.updateEncounter({
                    id: $scope.contact.id,
                    entityId: $scope.modelHolder.encounterModel.id
                }, $scope.modelHolder.encounterModel, function (succ) {
                    $scope.updatingEncounter = false;
                    //Fetch updated assessment and follow up
                    ContactService.find({id: $scope.contact.id}, function (contact) {
                        $scope.contact = contact;
                    }, function (err) {
                        console.log(err);
                    })
                }, function (err) {
                    console.log(err);
                })
            };

            $scope.cancelUpdateEncounter = function () {
                $scope.updatingEncounter = false;
            };

            var createEncounterDetails = function (encounter, initiator) {
                var encounterDetails = encounter;
                var initiatorName = initiator.firstName + " " + initiator.lastName;
                encounterDetails["initiatorName"] = initiatorName;
                encounterDetails["initiatorId"] = initiator.id;
                encounterDetails["jsDate"] = DateFormatter.asDate(encounter.encounterDate);
                return encounterDetails;
            };

            $scope.viewEncounterDetails = function (encounter) {

                ContactService.find({id: encounter.initiator.id}, function (initiator) {
                    $scope.modelHolder.encounterModel = createEncounterDetails(encounter, initiator);
                }, function (err) {
                    console.log(err);
                });

            };

            $scope.displayEncounters = function () {
                $scope.showingEncounters = !$scope.showingEncounters;

                if ($scope.showingEncounters == true) {
                    ContactService.getEncounters({id: $scope.contact.id}, function (data) {
                        $scope.encountersTable = data;
                    }, function (err) {
                        console.log(err);
                    });
                }
            };

            /*Events */

            $scope.toggleShowingEvents = function () {
                ContactService.getEvents({id: $scope.contact.id}, function (data) {
                    $scope.contact.attendedEvents = data;
                    $scope.eventsTable = $scope.contact.attendedEvents;

                }, function (err) {
                    console.log(err);
                });
                $scope.showingEvents = !$scope.showingEvents;
            };

            $scope.getEvents = function () {
                $scope.addEvent.hidden = false;

                EventService.findAll({}, function (response) {
                    console.log(response);
                    $scope.events = response;
                }, function (err) {
                    console.log(err);
                });

            };

            $scope.attendEvent = function (eventId) {

                var idDto = {id: eventId};

                ContactService.attend({id: $scope.contact.id}, idDto, function (response) {
                    ContactService.getEvents({id: $scope.contact.id}, function (data) {
                        $scope.contact.attendedEvents = data;
                        $scope.eventsTable = $scope.contact.attendedEvents;
                    }, function (err) {
                        console.log(err);
                    });

                    $scope.addEvent.hidden = true;
                    $timeout(function () {
                        $scope.requestSuccess = false;
                    }, 3000);

                }, function (err) {
                    console.log(err);
                });

            };

            $scope.removeFromEvent = function (eventId) {
                ContactService.removeFromEvent({id: $scope.contact.id, entityId: eventId}, function (success) {
                    ContactService.getEvents({id: $scope.contact.id}, function (data) {
                        $scope.contact.attendedEvents = data;
                        $scope.eventsTable = $scope.contact.attendedEvents;
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    console.log(err);
                });
            };


            /* Organizations */

            $scope.getContactOrganizations = function () {
                $scope.showingOrganizations = !$scope.showingOrganizations;

                if ($scope.showingOrganizations == true) {
                    ContactService.getOrganizations({id: $scope.contact.id}, function (data) {
                        $scope.contact.organizations = data
                    }, function (err) {
                        console.log(err);
                    });
                }
            };

            $scope.getOrganizations = function () {
                $scope.addOrganization.hidden = !$scope.addOrganization.hidden;

                if ($scope.organizations == null) {
                    OrganizationService.findAll({}, function (data) {
                        $scope.organizations = data;
                    }, function (err) {
                        console.log(err);
                    });
                }
            };

            $scope.addToOrganization = function (index) {
                $scope.organizationSuccess = true;
                var organization = $scope.organizations[index];

                ContactService.addToOrganization({id: $scope.contact.id}, {id: organization.id},
                    function (data) {
                        ContactService.getOrganizations({id: $scope.contact.id}, function (data) {
                            $scope.contact.organizations = data
                        }, function (err) {
                            console.log(err);
                        });

                    }, function (err) {
                        console.log(err);
                    });

            };

            $scope.createAndAddToOrganization = function () {
                OrganizationService.create($scope.modelHolder.organizationModel, function (data) {
                    //Add this contact to the newly-created Organization
                    ContactService.addToOrganization({id: $scope.contact.id}, {id: data.id}, function (data) {
                        //Refresh organizations the contact is now a member of
                        ContactService.getOrganizations({id: $scope.contact.id}, function (data) {
                            $scope.contactUpdated = true;
                            $scope.contact.organizations = data;
                            $scope.addOrganization.hidden = true;
                            $scope.newOrganization.hidden = true;
                            $scope.modelHolder.organizationModel = {};
                        }, function (err) {
                            console.log(err);
                        });
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    var errorResponse = new ResponseErrorInterpreter(err);
                    if (errorResponse.isConstraintViolation()) {
                        $scope.modelHolder.organizationModel.constraintViolation = errorResponse.message;
                    }
                    console.log(err);
                });

                //Refresh list of all organizations known to the app
                OrganizationService.findAll(function (allOrgs) {
                    $scope.organizations = allOrgs;
                }, function (err) {
                    console.log(err);
                });
            };

            $scope.removeFromOrganization = function (id) {

                ContactService.removeFromOrganization({id: $scope.contact.id, entityId: id}, function (resp) {
                    ContactService.getOrganizations({id: $scope.contact.id}, function (orgs) {
                        $scope.contact.organizations = orgs;
                    }, function (err) {
                        console.log(err);
                    })
                }, function (err) {
                    console.log(err);
                })
            };

            /* Groups */
            $scope.getContactGroups = function () {
                $scope.showingGroups = !$scope.showingGroups;
                if ($scope.contact.groups == null) {
                    ContactService.getGroups({id: $scope.contact.id}, function (groups) {
                        $scope.contact.groups = groups;
                    }, function (err) {
                        console.log(err);
                    })
                }
            };

            $scope.showAddingGroup = function () {
                $scope.addingGroup = !$scope.addingGroup;
                if ($scope.groups == null) {
                    GroupService.findAll({}, function (groups) {
                        $scope.groups = groups;
                    }, function (err) {
                        console.log(err);
                    })
                }
            };

            $scope.addToGroup = function (groupId) {

                ContactService.addToGroup({id: $scope.contact.id}, {id: groupId}, function (succ) {
                    ContactService.getGroups({id: $scope.contact.id}, function (groups) {
                        $scope.contact.groups = groups;
                        $scope.addingGroup = false;
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    console.log(err);
                })

            };

            $scope.removeFromGroup = function (groupId) {

                ContactService.removeFromGroup({id: $scope.contact.id, entityId: groupId}, function (succ) {
                    ContactService.getGroups({id: $scope.contact.id}, function (groups) {
                        $scope.contact.groups = groups;
                    }, function (err) {
                        console.log(err);
                    })
                }, function (err) {
                    console.log(err);
                })
            };

            /* Committees */



            $scope.getContactCommittees = function () {
                $scope.showingCommittees = !$scope.showingCommittees;

                if ($scope.contact.committees == null) {
                    ContactService.getCommittees({id: $scope.contact.id}, function (data) {
                        $scope.contact.committees = data
                    }, function (err) {
                        console.log(err);
                    });
                }
            };
            $scope.getCommittees = function () {
                $scope.addCommittee.hidden = !$scope.addCommittee.hidden;

                if ($scope.committees == null) {
                    CommitteeService.findAll({}, function (data) {
                        $scope.committees = data;
                    }, function (err) {
                        console.log(err);
                    });
                }
            };


            $scope.addToCommittee = function (index) {
                $scope.committeeSuccess = true;
                var committee = $scope.committees[index];

                ContactService.addToCommittee({id: $scope.contact.id}, {id: committee.id},
                    function (data) {

                        ContactService.getCommittees({id: $scope.contact.id}, function (data) {
                            $scope.contact.committees = data
                        }, function (err) {
                            console.log(err);
                        });

                    }, function (err) {
                        console.log(err);
                    });
            };

            $scope.removeFromCommittee = function (committeeId) {

                ContactService.removeFromCommittee({
                    id: $scope.contact.id,
                    entityId: committeeId
                }, function (success) {
                    ContactService.getCommittees({id: $scope.contact.id}, function (committees) {
                        $scope.contact.committees = committees;
                    }, function (err) {
                        console.log(err);
                    })
                }, function (err) {
                    console.log(err);
                })

            };

            /* MEMBERINFO */

            $scope.toggleMemberInfoPanel = function () {
                $scope.memberInfoPanel.showingPanel = !$scope.memberInfoPanel.showingPanel;

                if ($scope.contact.member == true) {
                    $scope.displayMemberInfo();
                } else {
                    $scope.promptNoMemberInfo();
                }
            };

            $scope.promptNoMemberInfo = function () {
                $scope.promptNotMember = true;
            };

            $scope.becomeMember = function () {
                $scope.displayMemberInfo();
                $scope.toggleEditingMemberInfo();
            };

            $scope.updateMembershipInfo = function () {
                ContactService.updateMemberInfo({id: $scope.contact.id}, $scope.memberInfo, function (data) {
                    ContactService.getMemberInfo({id: $scope.contact.id}, function (data) {
                        $scope.contact.member = true;
                        $scope.promptNotMember = false;
                        $scope.memberInfoPanel.editingMemberInfo = false;
                        $scope.memberInfoPanel.updateRequest.success = true;
                        $timeout(function () {
                            $scope.memberInfoPanel.updateRequest.success = false;
                        }, 3000);
                    }, function (err) {
                        console.log(err);
                        $scope.memberInfoPanel.updateRequest.failure = true;
                        $timeout(function () {
                            $scope.memberInfoPanel.updateRequest.failure = false;
                        }, 3000);
                    })
                }, function (err) {
                    console.log(err);
                    $scope.memberInfoPanel.updateRequest.failure = true;
                    $timeout(function () {
                        $scope.memberInfoPanel.updateRequest.failure = false;
                    }, 3000);
                })
            };


            $scope.displayMemberInfo = function () {

                $scope.memberInfoPanel.showingMemberInfo = !$scope.memberInfoPanel.showingMemberInfo;

                if ($scope.memberInfoPanel.showingMemberInfo) {
                    ContactService.getMemberInfo({id: $scope.contact.id}, function (data) {
                        $scope.memberInfo = data;
                    }, function (err) {
                        console.log(err);
                    });
                }
            };

            $scope.toggleEditingMemberInfo = function () {
                $scope.memberInfoPanel.editingMemberInfo = !$scope.memberInfoPanel.editingMemberInfo;
            };

            $scope.cancelUpdateMemberInfo = function () {
                ContactService.getMemberInfo({id: $scope.contact.id}, function (data) {
                    $scope.memberInfo = data;
                    $scope.memberInfoPanel.editingMemberInfo = false;
                    if (!$scope.contact.member) {
                        $scope.memberInfoPanel.showingMemberInfo = false;
                    }
                }, function (err) {
                    console.log(err);
                });
            };

            $scope.toggleShowingDonorInfoPanel = function() {
                $scope.donorPanel.showingDonorInfo = !$scope.donorPanel.showingDonorInfo;
                if ($scope.donorPanel.showingDonorInfo) {
                    ContactService.getDonations({id: $scope.contact.id}, function(donations) {
                        $scope.donations = donations;
                    }, function(err) {
                        console.log(err);
                    });
                    ContactService.getSustainerPeriods({id: $scope.contact.id}, function(periods) {
                        $scope.sustainerPeriods = periods;
                    }, function(err) {
                        console.log(err);
                    });
                }
            };

            $scope.createDonation = function() {
                $scope.modelHolder.donationModel.dateOfReceipt = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfReceipt);
                $scope.modelHolder.donationModel.dateOfDeposit = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfDeposit);

                ContactService.addDonation({id: $scope.contact.id}, $scope.modelHolder.donationModel, function(succ) {
                    $scope.cancelCreateDonation();
                    ContactService.getDonations({id: $scope.contact.id}, function(donations) {
                        $scope.donations = donations;
                    }, function(err) {
                        console.log(err);
                    })
                }, function(err) {
                    console.log(err);
                })
            };

            $scope.cancelCreateDonation = function() {
                $scope.donorPanel.creatingDonation = false;
                $scope.modelHolder.donationModel = {};
            };

            $scope.createSustainerPeriod = function() {
                $scope.modelHolder.sustainerPeriodModel.periodStartDate= DateFormatter.formatDate($scope.modelHolder.sustainerPeriodModel.dates.periodStartDate);
                $scope.modelHolder.sustainerPeriodModel.cancelDate = DateFormatter.formatDate($scope.modelHolder.sustainerPeriodModel.dates.cancelDate);

                ContactService.addSustainerPeriod({id: $scope.contact.id}, $scope.modelHolder.sustainerPeriodModel, function(succ) {
                    $scope.cancelCreateSustainerPeriod();
                    ContactService.getSustainerPeriods({id: $scope.contact.id}, function(periods) {
                        $scope.sustainerPeriods = periods;
                    }, function(err) {
                        console.log(err);
                    })
                }, function(err) {
                    console.log(err);
                })

            };

            $scope.cancelCreateSustainerPeriod = function (){
                $scope.donorPanel.creatingSustainerPeriod = false;
                $scope.modelHolder.sustainerPeriodModel = {};
            };

            $scope.viewSustainerPeriod = function(id) {
                RouteChangeService.set($scope.contact);
                $location.path("/sustainerPeriod/"+id);
            };

            $scope.currentEntity = "Contact";
            $scope.deleteWarning = "Deleting a Contact will delete any Donations, Sustainer Periods, Encounters and memberships they may have. ";

            $scope.deleteCurrentEntity = function() {
                var deleteConfirmed = $window.confirm('WARNING: Are you sure you want to delete this contact?');
                if (deleteConfirmed) {
                    ContactService.delete({id: $scope.contact.id}, function () {
                        $location.path("/contacts");
                    }, function (err) {
                        console.log(err);
                    });
                }
            }

        }]);

    controllers.controller('EventsCtrl', ['$scope', 'EventService', 'CommitteeService', 'DateFormatter', function ($scope, EventService, CommitteeService, DateFormatter) {

        $scope.addEvent = {hidden: true};
        $scope.formHolder = {};
        $scope.modelHolder = {};

        var populateEvents = function () {
            EventService.findAll({}, function (response) {
                $scope.eventsTable = response;
            }, function (err) {
                console.log(err);
            });
        };

        populateEvents();

        $scope.createEvent = function () {

            $scope.modelHolder.eventModel.dateHeld = DateFormatter.formatDate($scope.modelHolder.eventModel.jsDate);

            $scope.modelHolder.eventModel.attendees = [];
            EventService.create({}, $scope.modelHolder.eventModel, function (response) {
                $scope.addEvent = {hidden: true};
                $scope.clashingName = false;
                populateEvents();
                $scope.modelHolder.eventModel = {};
            }, function (err) {
                var errorResponse = new ResponseErrorInterpreter(err);
                if (errorResponse.isConstraintViolation()) {
                    $scope.modelHolder.eventModel.constraintViolation = errorResponse.message;
                }
                console.log(err);
            });
        };

    }]);


    controllers.controller('EventDetailsCtrl', ['$scope', '$rootScope', 'EventService', '$routeParams', 'CommitteeService', '$timeout', '$window', '$location', 'DateFormatter',
        function ($scope, $rootScope, EventService, $routeParams, CommitteeService, $timeout, $window, $location, DateFormatter) {

            $scope.modelHolder = {
                donationModel : {
                    dates: {
                        dateOfDeposit: new Date(),
                        dateOfReceipt: new Date()
                    }
                }
            };

            $scope.formHolder = {};

            var formatEvent = function (event) {
                event.jsDate = DateFormatter.asDate(event.dateHeld);
                if (event.attendees == null) {
                    event.attendees = [];
                }
                $scope.contactCollection = event.attendees;
                return event;
            };

            EventService.find({id: $routeParams.id}, function (event) {
                $scope.modelHolder.eventModel = formatEvent(event);
                $scope.event = $scope.modelHolder.eventModel;
            }, function (err) {
                console.log(err);
            });

            if ($rootScope.showingDevelopment) {
                EventService.getDonations({id: $routeParams.id}, function(donations) {
                    $scope.donations = donations;
                }, function(err) {
                    console.log(err);
                });
            }
            
            $scope.showUpdateForm = function () {
                $scope.updatingEventDetails = true;
            };

            $scope.submitUpdate = function () {

                $scope.modelHolder.eventModel.dateHeld = DateFormatter.formatDate($scope.modelHolder.eventModel.jsDate);

                EventService.update({id: $scope.modelHolder.eventModel.id}, $scope.modelHolder.eventModel, function (success) {
                    EventService.find({id: $scope.modelHolder.eventModel.id}, function (event) {
                        $scope.modelHolder.eventModel = formatEvent(event);
                        $scope.requestSuccess = true;
                        $scope.updatingEventDetails = false;
                        $timeout(function () {
                            $scope.requestSuccess = false;
                        }, 3000)
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    var errorResponse = new ResponseErrorInterpreter(err);
                    if (errorResponse.isConstraintViolation()) {
                        $scope.modelHolder.eventModel.constraintViolation = errorResponse.message;
                    }
                    $scope.requestFail = true;
                    $timeout(function () {
                        $scope.requestFail = false;
                    }, 3000);
                    console.log(err);
                });

            };

            $scope.cancelUpdate = function () {
                $scope.updatingEventDetails = false;
                EventService.find({id: $scope.modelHolder.eventModel.id}, function (event) {
                    $scope.modelHolder.eventModel = formatEvent(event)
                }, function (err) {
                    console.log(err);
                });
            };

            $scope.deleteEvent = function () {

                var deleteConfirmed = $window.confirm('Are you sure you want to delete this event?');
                if (deleteConfirmed) {
                    EventService.delete({id: $scope.modelHolder.eventModel.id}, function () {
                        $location.path("/events");
                    }, function (err) {
                        console.log(err);
                    });
                }

            };

            $scope.showDonationForm= function() {
                $scope.addingDonation = true;
            };

            $scope.addDonationToEvent = function() {
                $scope.modelHolder.donationModel.dateOfReceipt = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfReceipt);
                $scope.modelHolder.donationModel.dateOfDeposit = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfDeposit);

                EventService.addDonation({id: $scope.event.id}, $scope.modelHolder.donationModel, function(succ) {
                    $scope.cancelAddDonation();
                    EventService.getDonations({id: $scope.event.id}, function(donations) {
                        $scope.donations = donations;
                    }, function(err) {
                        console.log(err);
                    })
                }, function(err) {
                    console.log(err);
                })


            };

            $scope.cancelAddDonation = function() {
                $scope.addingDonation = false;
                $scope.modelHolder.donationModel = {};
            }

        }]);

    controllers.controller('EventFormCtrl', ['$scope', 'CommitteeService', function ($scope, CommitteeService) {

        CommitteeService.findAll({}, function (data) {
            $scope.committees = data;
            $scope.committees.push({id: null, name: "None"});
        }, function (err) {
            console.log(err);
        });

    }]);

    controllers.controller('EventSignInCtrl', ['$scope', 'EventService', '$routeParams', 'ContactService', '$rootScope', '$route', '$timeout',
        function ($scope, EventService, $routeParams, ContactService, $rootScope, $route, $timeout) {

            $scope.contactId = null;
            $scope.validated = false;
            $scope.contactValidated = true;
            $scope.contactForm = null;
            $scope.contact = {};
            $scope.notFound = false;
            $scope.found = false;
            $scope.error = false;
            $scope.success = false;

            EventService.find({id: $routeParams.id}, function (data) {
                $scope.event = data;
                $rootScope.eventSignInMode = true;
                sessionStorage.setItem('event-sign-in-id', $scope.event.id);
                sessionStorage.setItem('event-sign-in-mode', true);
                console.log(data)
            }, function (err) {
                console.log(err);
            });

            $scope.getMatch = function () {
                ContactService.findBySignInDetails({}, $scope.contactForm, function (data) {
                    $scope.found = true;
                    $scope.contact = data;
                    $scope.contact.lastName = $scope.contact.lastName == null ? $scope.contactForm.lastName : $scope.contact.lastName;
                }, function (err) {

                    if (err.status == 404) {
                        $scope.notFound = true;
                        $scope.contact.firstName = $scope.contactForm.firstName;
                        $scope.contact.lastName = $scope.contactForm.lastName;
                        $scope.contact.email = $scope.contactForm.email;
                        $scope.contact.phoneNumber1 = $scope.contactForm.phoneNumber;

                        ContactService.create({}, $scope.contact, function (data) {
                            $scope.contact.id = data.id;

                        }, function (err) {
                            $scope.error = true;
                            console.log(err);
                        });

                        /*Some other error has occurred*/
                    } else {
                        $scope.error = true;
                        console.log(err);
                    }
                });
            };

            $scope.validate = function () {
                $scope.validated = $scope.contactForm.firstName != null && $scope.contactForm.lastName != null &&
                    ($scope.contactForm.email != null || $scope.contactForm.phoneNumber != null);
            };

            $scope.validateContact = function () {
                $scope.contactValidated = $scope.contact.firstName != null && $scope.contact.lastName != null &&
                    ($scope.contact.email != null || $scope.contact.phoneNumber1 != null);
            };

            $scope.submit = function () {
                ContactService.update({id: $scope.contact.id}, $scope.contact, function (data) {
                    attendEvent();
                }, function (err) {
                    displayAlertAndReload()
                });

            };

            var displayAlertAndReload = function (success) {
                $scope.found = false;
                $scope.notFound = false;
                if (success) {
                    $scope.success = true;
                } else {
                    $scope.error = true;
                }

                $timeout(function () {
                    $route.reload();
                }, 5000);
            };

            var attendEvent = function () {
                ContactService.attend({id: $scope.contact.id}, {id: $scope.event.id}, function (data) {
                    displayAlertAndReload(true);
                }, function (err) {
                    console.log("Error with attending event: " + err);
                    displayAlertAndReload(false);
                });
            };
        }]);

    controllers.controller('OrganizationFormCtrl', ['$scope', function ($scope) {

    }]);

    controllers.controller('DemographicsCtrl', ['$scope','DemographicService','ContactService','DateFormatter','$timeout', function ($scope, DemographicService, ContactService, DateFormatter, $timeout) {

        $scope.toggleEditingDemographics = function () {
            $scope.demographicPanel.editingDemographics = !$scope.demographicPanel.editingDemographics;
        };

        var retrieveContactDemographics = function () {
            return ContactService.getDemographics({id: $scope.contact.id}, function (demographics) {
                $scope.demographics = demographics;
                $scope.demographics.dobAsDate = DateFormatter.asDate($scope.demographics.dateOfBirth);
                return true;
            }, function (err) {
                console.log(err);
                return false;
            });
        };

        var retrieveDemographicOptions = function () {
            DemographicService.findAll({}, function (categories) {
                formatDemographicOptions(categories);
            }, function (err) {
                console.log(err);
            })
        };

        var formatDemographicOptions = function (categories) {
            for (var i = 0; i < categories.length; i++) {
                var key = categories[i].name;
                var value = categories[i].options;
                $scope[key] = value;
            }
        };

        $scope.displayDemographics = function () {
            $scope.demographicPanel.showingDemographics = !$scope.demographicPanel.showingDemographics;
            retrieveContactDemographics();
            retrieveDemographicOptions();

        };

        $scope.showAddDemographicOption = function (addingCategoryFlag) {
            $scope[addingCategoryFlag] = true;
        };

        $scope.addDemographicOption = function (addingCategoryFlag, category, newOption) {
            console.log("Category: " + category);
            console.log("Option: " + newOption);
            DemographicService.createOption({categoryName: category}, {name: newOption}, function (succ) {
                $scope[addingCategoryFlag] = false;
                newOption = null;
                retrieveDemographicOptions();
            }, function (err) {
                console.log(err);
            })
        };

        $scope.cancelAddDemographicOption = function (addingCategoryFlag) {
            $scope[addingCategoryFlag] = false;
        };

        $scope.updateDemographics = function () {

            $scope.demographics.dateOfBirth = DateFormatter.formatDate($scope.demographics.dobAsDate);
            ContactService.updateDemographics({id: $scope.contact.id}, $scope.demographics, function (data) {
                if (retrieveContactDemographics()) {
                    $scope.demographicPanel.editingDemographics = false;
                    $scope.demographicPanel.updateRequest.success = true;

                    $timeout(function() {
                        $scope.$digest();
                    });

                    $timeout(function () {
                        $scope.demographicPanel.updateRequest.success = false;
                    }, 3000);
                } else {
                    $scope.demographicPanel.updateRequest.failure = true;
                    $timeout(function () {
                        $scope.demographicPanel.updateRequest.failure = false;
                    }, 3000);
                }
            }, function (err) {
                console.log(err);
                $scope.demographicPanel.updateRequest.failure = true;
                $timeout(function () {
                    $scope.demographicPanel.updateRequest.failure = false;
                }, 3000);
            })
        };

        $scope.cancelUpdateDemographics = function () {
            if (retrieveContactDemographics()) {
                $scope.demographicPanel.editingDemographics = false;
            }
        };

        var setup =  function () {
             $scope.demographicPanel = {
             updateRequest: {success: false, failure: false},
             editingDemographics: false,
             showingDemographics: false,
             addingOption: {
             race: false,
             ethnicity: false,
             gender: false,
             incomeBracket: false,
             sexualOrientation: false
             }
             };

            $scope.displayDemographics();

        };

        setup();
    }]);



    controllers.controller('OrganizationsCtrl', ['$scope', 'OrganizationService', function ($scope, OrganizationService) {

        $scope.modelHolder = {};
        $scope.formHolder = {};
        $scope.hideForm = true;

        OrganizationService.findAll({}, function (data) {
            $scope.organizations = data;
        }, function (err) {
            console.log(err);
        });

        $scope.showOrganizationForm = function () {
            $scope.hideForm = false;
        };

        $scope.createOrganization = function () {
            $scope.modelHolder.organizationModel.members = [];
            OrganizationService.create($scope.modelHolder.organizationModel, function (data) {
                $scope.hideForm = true;
                $scope.modelHolder.organizationModel = {};
                OrganizationService.findAll({}, function (data) {
                    $scope.organizations = data;
                }, function (err) {
                    console.log(err);
                });
            }, function (err) {
                var errorResponse = new ResponseErrorInterpreter(err);
                if (errorResponse.isConstraintViolation()) {
                    $scope.modelHolder.organizationModel.constraintViolation = errorResponse.message;
                }
                console.log(err);
            });
        };

        $scope.cancelCreateOrganization = function () {
            $scope.hideForm = true;
            $scope.modelHolder.organizationModel = null;
        };

    }]);

    controllers.controller('DonationFormCtrl', ['$scope', 'DonationService', function($scope, DonationService) {
        DonationService.getBudgetItems({}, function(items) {
            $scope.budgetItems = items;
            $scope.budgetItems.push({name: "None"});
        }, function(err) {
           console.log(err);
        });
        $scope.donationMethodOptions = ['PayPal', 'Cash', 'Check', 'Credit Card', 'In-kind'];

    }]);

    controllers.controller('OrganizationDetailsCtrl', ['$scope', '$rootScope', 'OrganizationService', '$routeParams', '$location', '$window', '$timeout', 'DateFormatter',
        function ($scope, $rootScope, OrganizationService, $routeParams, $location, $window, $timeout, DateFormatter) {

            $scope.formHolder = {};
            $scope.modelHolder = {
                donationModel : {
                    dates: {
                        dateOfDeposit: new Date(),
                        dateOfReceipt: new Date()
                    }
                }
            };

            $scope.contactCollection = {};

            var establishDetails = function (id) {
                OrganizationService.find({id: id}, function (data) {
                    $scope.modelHolder.organizationModel = data;
                    if ($scope.modelHolder.organizationModel.members == null) {
                        $scope.modelHolder.organizationModel.members = [];
                    }
                    $scope.organization = $scope.modelHolder.organizationModel;
                    $scope.contactCollection = $scope.organization.members;

                    if ($rootScope.showingDevelopment) {
                        OrganizationService.getDonations({id: $scope.modelHolder.organizationModel.id}, function(donations) {
                            $scope.donations = donations;
                        }, function(err) {
                            console.log(err);
                        });
                    }
                }, function (err) {
                    console.log(err);
                });
            };

            establishDetails($routeParams.id);
            $scope.cancelUpdate = function () {
                $scope.updatingOrganizationDetails = false;
                establishDetails($scope.modelHolder.organizationModel.id);
            };

            $scope.deleteOrganization = function () {
                var deleteConfirmed = $window.confirm('Are you sure you want to delete this organization?');
                if (deleteConfirmed) {
                    OrganizationService.delete({id: $scope.modelHolder.organizationModel.id}, function () {
                        $location.path("/organizations");
                    }, function (err) {
                        console.log(err);
                    });
                }

            };

            $scope.submitUpdate = function () {
                OrganizationService.update({id: $scope.modelHolder.organizationModel.id}, $scope.modelHolder.organizationModel, function (data) {
                    $scope.updatingOrganizationDetails = false;
                    establishDetails($scope.modelHolder.organizationModel.id);
                    $scope.requestSuccess = true;
                    $timeout(function () {
                        $scope.requestSuccess = false;
                    }, 3000)
                }, function (err) {
                    var errorResponse = new ResponseErrorInterpreter(err);
                    if (errorResponse.isConstraintViolation()) {
                        $scope.modelHolder.organizationModel.constraintViolation = errorResponse.message;
                    }
                    $scope.requestFail = true;
                    $timeout(function () {
                        $scope.requestFail = false;
                    }, 3000);
                    console.log(err);
                })
            };

            $scope.showDonationForm = function() {
                $scope.addingDonation = true;
            };

            $scope.addDonationToOrganization = function() {
                $scope.modelHolder.donationModel.dateOfReceipt = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfReceipt);
                $scope.modelHolder.donationModel.dateOfDeposit = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfDeposit);

                OrganizationService.addDonation({id: $scope.modelHolder.organizationModel.id}, $scope.modelHolder.donationModel, function(succ) {
                    $scope.cancelAddDonation();
                    OrganizationService.getDonations({id: $scope.modelHolder.organizationModel.id}, function(donations) {
                        $scope.donations = donations;
                    }, function(err) {
                        console.log(err);
                    })
                }, function(err) {
                    console.log(err);
                })
            };

            $scope.cancelAddDonation = function() {
                $scope.modelHolder.donationModel = {};
                $scope.addingDonation = false;
            };

        }]);

    controllers.controller('CommitteesCtrl', ['$scope', 'CommitteeService', '$window', function ($scope, CommitteeService, $window) {

        var setup = function () {
            $scope.hideForm = true;
            $scope.newCommittee = {};
            CommitteeService.findAll({}, function (data) {
                $scope.committees = data;
            }, function (err) {
                console.log(err);
            });
        };

        setup();

        $scope.showCommitteeForm = function () {
            $scope.hideForm = false;
        };

        $scope.cancelCreateCommittee = function () {
            $scope.newCommittee = {};
            $scope.hideForm = true;
        };

        $scope.createCommittee = function () {

            CommitteeService.create({name: $scope.newCommittee.name, members: []}, function (succ) {
                $scope.hideForm = true;
                $scope.newCommittee = {};
                CommitteeService.findAll({}, function (committees) {
                    $scope.committees = committees;
                }, function (err) {
                    console.log(err);
                });
            }, function (err) {
                var errorResponse = new ResponseErrorInterpreter(err);
                if (errorResponse.isConstraintViolation()) {
                    $scope.newCommittee.constraintViolation = errorResponse.message;
                }
                console.log(err);
            });

        };

    }]);

    controllers.controller('CommitteeDetailsCtrl', ['$scope', 'CommitteeService', '$location', '$routeParams', '$window', function ($scope, CommitteeService, $location, $routeParams, $window) {

        var establishCommittee = function (id) {
            CommitteeService.find({id: id}, function (committee) {
                $scope.committee = committee;
                $scope.contactCollection = $scope.committee.members;
            }, function (err) {
                console.log(err);
            });
        };

        var setup = function () {
            $scope.updatingCommittee = false;
            establishCommittee($routeParams.id)
        };

        var handleFailedUpdate = function (err) {
            var errorResponse = new ResponseErrorInterpreter(err);
            if (errorResponse.isConstraintViolation()) {
                $scope.committee.constraintViolation = errorResponse.message;
            }
            $scope.requestFail = true;
            $timeout(function () {
                $scope.requestFail = false;
            }, 3000);

            console.log(err);
        };

        setup();

        $scope.showUpdateForm = function () {
            $scope.updatingCommittee = true;
        };

        $scope.submitUpdate = function () {
            CommitteeService.update({id: $scope.committee.id}, $scope.committee, function (succ) {
                setup();
            }, function (err) {
                handleFailedUpdate(err);
            })
        };

        $scope.cancelUpdate = function () {
            setup();
        };

        $scope.deleteCommittee = function() {
            var deleteConfirmed = $window.confirm('Are you sure you want to delete this committee?');
            if (deleteConfirmed) {
                CommitteeService.delete({id: $scope.committee.id}, function () {
                    $location.path("/committees");
                }, function (err) {
                    console.log(err);
                });
            }
        }

    }]);

    controllers.controller('CommitteeEventsCtrl', ['$scope', '$routeParams', 'CommitteeService', function ($scope, $routeParams, CommitteeService) {

        CommitteeService.find({id: $routeParams.id}, function (committee) {
            $scope.committee = committee;
            $scope.eventsTable = committee.events;
        }, function (err) {
            console.log(err);
        });

    }]);


    controllers.controller('LoginCtrl', ['$scope', '$rootScope', '$location', 'UserService', '$http', 'ConfigService', function ($scope, $rootScope, $location, UserService, $http, ConfigService) {

        $scope.error = false;

        var authenticate = function (credentials, callback) {

            var headers = credentials ? {
                authorization: "Basic "
                + btoa(credentials.username + ":" + credentials.password)
            } : {};

            $http.get('/users/authenticate', {headers: headers}).success(function (data) {
                if (data.email) {

                    sessionStorage.setItem('bayard-user-authenticated', 'true');
                    sessionStorage.setItem('bayard-user', data);
                    sessionStorage.setItem('event-sign-in-mode', $rootScope.eventSignInMode);
                    sessionStorage.setItem('event-sign-in-id', $rootScope.eventSignInId);

                    $rootScope.authenticated = true;
                    $rootScope.user = data;
                } else {
                    sessionStorage.setItem('bayard-user-authenticated', 'false');
                    $rootScope.authenticated = false;
                }
                callback && callback();
            }).error(function () {
                sessionStorage.setItem('bayard-user-authenticated', 'false');
                $rootScope.authenticated = false;
                callback && callback();
            });

        };

        authenticate();
        $scope.credentials = {};

        $scope.login = function () {
            authenticate($scope.credentials, function () {
                if (sessionStorage.getItem('bayard-user-authenticated') == 'true') {
                    $location.path("/");
                    $scope.error = false;
                } else {
                    $location.path("/login");
                    $scope.error = true;
                }
            });
        };
    }]);

    controllers.controller('ConfigurationCtrl', ['$scope', 'EncounterTypeService', function ($scope, EncounterTypeService) {

    }]);

    controllers.controller('InteractionRecordTypeCtrl', ['$scope', 'InteractionService', function ($scope, InteractionService) {

        $scope.showingInteractionTypes = true;

        var setup = function() {
            $scope.newInteractionType = "";
            InteractionService.getInteractionTypes({}, function(types) {
                $scope.interactionTypes = types;
            }, function(err) {
                console.log(err);
            })
        };

        setup();

        $scope.showCreateInteractionType = function() {
            $scope.creatingInteractionType = true;
        };

        $scope.createInteractionType = function() {
            InteractionService.createType({}, {name: $scope.newInteractionType}, function(succ) {
                setup();
            }, function(err) {
                console.log(err);
            });
        };

        $scope.cancelCreateInteractionType = function() {
            $scope.creatingInteractionType = false;
            $scope.newInteractionType = "";
        };

        $scope.deleteInteractionType = function(interactionType) {
            InteractionService.deleteType({typeId: interactionType.id}, function(succ) {
                setup();
            }, function(err) {
                console.log(err);
            })
        }

    }]);

    controllers.controller('BudgetItemCtrl', ['$scope', 'DonationService', function ($scope, DonationService) {

        $scope.showingBudgetItems = true;

        var setup = function() {
            DonationService.getBudgetItems({}, function(items) {
                $scope.budgetItems = items;
                $scope.newBudgetItem = "";
            }, function(err) {
                console.log(err);
            })
        };

        setup();

        $scope.showCreateBudgetItem = function() {
            $scope.creatingBudgetItem = true;
        };

        $scope.createBudgetItem = function() {
            DonationService.createBudgetItem({}, {name: $scope.newBudgetItem}, function(succ) {
                $scope.creatingBudgetItem = false;
                setup();
            }, function(err) {
               console.log(err);
            });
        };

        $scope.cancelCreateBudgetItem = function() {
            $scope.showCreateBudgetItem = false;
            $scope.newBudgetItem = "";
        };

        $scope.deleteBudgetItem = function(budgetItem) {
            DonationService.deleteBudgetItem({budgetItemId: budgetItem.id}, function(succ) {
                setup();
            }, function(err) {
                console.log(err);
            })
        };

    }]);



    controllers.controller('EncounterTypeCtrl', ['$scope', 'EncounterTypeService', function ($scope, EncounterTypeService) {

        $scope.newEncounterType = "";

        $scope.showingEncounterTypes = true;

        var setup = function () {
            EncounterTypeService.findAll({}, function (data) {
                $scope.encounterTypes = data;
            }, function (err) {
                console.log(err);
            });
        };

        setup();

        $scope.showCreateEncounterType = function() {
            $scope.creatingEncounterType = true;
        };

        $scope.createEncounterType = function (name) {
            var encounterType = {name: name};
            EncounterTypeService.create(encounterType, function (data) {
                $scope.cancelCreateEncounterType();
                setup();
            }, function (err) {
                console.log(err);
            });

        };

        $scope.cancelCreateEncounterType = function() {
            $scope.creatingEncounterType = false;
            $scope.newEncounterType = "";
        };

        $scope.deleteEncounterType = function (encounterType) {

            EncounterTypeService.delete({id: encounterType.id}, function () {
                setup();
            }, function (err) {
                console.log(err);
            });
        };

    }]);

    controllers.controller('GroupsCtrl', ['$scope', 'GroupService', function ($scope, GroupService) {

        $scope.modelHolder = {};
        $scope.formHolder = {};
        $scope.hideForm = true;

        GroupService.findAll({}, function (data) {
            $scope.groups = data;
        }, function (err) {
            console.log(err);
        });

        $scope.showGroupForm = function () {
            $scope.hideForm = false;
        };

        $scope.createGroup = function () {

            GroupService.create($scope.modelHolder.groupModel, function (data) {
                $scope.hideForm = true;
                $scope.modelHolder.groupModel = {};
                GroupService.findAll({}, function (data) {
                    $scope.groups = data;
                }, function (err) {
                    console.log(err);
                });
            }, function (err) {
                var errorResponse = new ResponseErrorInterpreter(err);
                if (errorResponse.isConstraintViolation()) {
                    $scope.modelHolder.groupModel.constraintViolation = errorResponse.message;
                }
                console.log(err);
            });
        };

        $scope.cancelCreateGroup = function () {
            $scope.hideForm = true;
            $scope.modelHolder.groupModel = null;
        };

    }]);

    controllers.controller('GroupDetailsCtrl', ['$scope', 'GroupService', '$routeParams', '$location', '$timeout', '$window', 'OrganizationService', 'CommitteeService', 'EventService',
        function ($scope, GroupService, $routeParams, $location, $timeout, $window, OrganizationService, CommitteeService, EventService) {

            $scope.formHolder = {};
            $scope.modelHolder = {};

            var establishGroupMembers = function () {
                GroupService.getAllContacts({id: $scope.modelHolder.groupModel.id}, function (contacts) {
                    $scope.modelHolder.groupModel.groupMembers = contacts;
                    $scope.contactCollection = contacts;

                    if ($scope.modelHolder.groupModel.aggregations.length == 0 && $scope.modelHolder.groupModel.groupMembers.length == 0) {
                        $scope.showAggregationForm();
                    }

                }, function (err) {
                    console.log(err);
                })
            };

            var establishGroup = function (groupId) {
                GroupService.find({id: groupId}, function (group) {
                    $scope.modelHolder.groupModel = group;
                    establishGroupMembers();
                }, function (err) {
                    console.log(err);
                });
            };

            var establishGroupDetails = function (groupId) {
                GroupService.find({id: groupId}, function (group) {
                    $scope.modelHolder.groupModel.groupName = group.groupName;
                    GroupService.getAllContacts({id: $routeParams.id}, function (members) {
                        $scope.modelHolder.groupModel.topLevelMembers = members;
                    }, function (err) {
                        console.log(err);
                    });
                }, function (err) {
                    console.log(err);
                });
            };

            establishGroup($routeParams.id);

            OrganizationService.findAll({}, function (orgs) {
                $scope.organizations = orgs;
            }, function (err) {
                console.log(err);
            });

            EventService.findAll({}, function (events) {
                $scope.events = events;
            }, function (err) {
                console.log(err);
            });

            CommitteeService.findAll({}, function (comms) {
                $scope.committees = comms;
            }, function (err) {
                console.log(err);
            });

            $scope.deleteGroup = function () {
                var deleteConfirmed = $window.confirm('Are you sure you want to delete this group?');
                if (deleteConfirmed) {
                    GroupService.deleteGroup({id: $scope.modelHolder.groupModel.id}, function (resp) {
                        $location.path("/groups");
                    }, function (err) {
                        console.log(err);
                    })
                }
            };

            $scope.showUpdateForm = function () {
                $scope.updatingGroup = true;
                $scope.updatingGroupName = true;
            };

            $scope.cancelUpdate = function () {
                $scope.updatingGroup = false;
                $scope.updatingGroupName = false;
                establishGroupDetails($scope.modelHolder.groupModel.id);
            };

            $scope.showAggregationForm = function () {
                $scope.updatingGroup = true;
                $scope.updatingAggregations = true;
            };

            $scope.cancelUpdateAggregations = function () {
                $scope.updatingGroup = false;
                $scope.updatingAggregations = false;
            };

            $scope.submitUpdate = function () {
                GroupService.update({id: $scope.modelHolder.groupModel.id}, {groupName: $scope.modelHolder.groupModel.groupName}, function (succ) {
                    handleSuccessfulUpdate();
                }, function (err) {
                    handleFailedUpdate(err);
                });
            };

            var handleFailedUpdate = function (err) {
                var errorResponse = new ResponseErrorInterpreter(err);
                if (errorResponse.isConstraintViolation()) {
                    $scope.modelHolder.groupModel.constraintViolation = errorResponse.message;
                }
                $scope.requestFail = true;
                $timeout(function () {
                    $scope.requestFail = false;
                }, 3000);
                console.log(err);
            };

            var handleSuccessfulUpdate = function () {
                $scope.updatingGroup = false;
                $scope.updatingGroupName = false;
                establishGroupDetails($scope.modelHolder.groupModel.id);
                $scope.requestSuccess = true;
                $timeout(function () {
                    $scope.requestSuccess = false;
                }, 3000)
            };

            var addAggregation = function (id) {
                GroupService.addAggregation({id: $scope.modelHolder.groupModel.id, entityId: id}, function (succ) {
                    $scope.updatingGroup = false;
                    $scope.updatingAggregations = false;
                    $scope.requestSuccess = true;
                    $timeout(function () {
                        $scope.requestSuccess = false;
                    }, 3000);
                    establishGroup($scope.modelHolder.groupModel.id);
                    $scope.newAggregation = null;
                }, function (err) {
                    handleFailedUpdate(err);
                });
            };

            $scope.addCommittee = function (id) {
                addAggregation(id);
            };

            $scope.addOrganization = function (id) {
                addAggregation(id);
            };

            $scope.addEvent = function (id) {
                addAggregation(id);
            };

        }]);

    controllers.controller('FoundationListCtrl', ['$scope', 'FoundationService', function($scope, FoundationService) {

        $scope.newFoundation = {};

        var setup = function () {
            $scope.creatingFoundation = false;
            $scope.newFoundation = {};

            FoundationService.findAll({}, function(foundations) {
                $scope.foundations = foundations;
            }, function(err) {
                console.log(err);
            });
        };

        setup();

        $scope.createFoundation = function() {
            FoundationService.create($scope.newFoundation, function(succ) {
                $scope.newFoundation = {};
                setup();
            }, function(err) {
                var error = ResponseErrorInterpreter(err);
                if (error.isConstraintViolation()) {
                    $scope.newFoundation.constraintViolation = error.message;
                }
                console.log(err);
            })
        };

        $scope.cancelCreateFoundation = function() {
            $scope.creatingFoundation = false;
            $scope.newFoudation = {};
        };

    }]);

    controllers.controller('FoundationDetailsCtrl', ['$scope', 'FoundationService', 'InteractionService', '$routeParams', '$timeout', '$location', 'DateFormatter', '$window', function($scope, FoundationService, InteractionService, $routeParams, $timeout, $location, DateFormatter, $window) {

        $scope.showingBasicDetails = true;
        $scope.foundation = {};
        $scope.newInteraction = {};
        $scope.interactionRecords = {};

        InteractionService.getInteractionTypes({}, function(types) {
            $scope.interactionTypes = types;
            $scope.interactionTypes.push({name: "None"});
        }, function(err) {
            console.log(err);
        });

        var fetchInteractions = function(id) {
            FoundationService.getInteractionRecords({id: id}, function(interactions) {
                $scope.interactionRecords = interactions;
            }, function(err) {
                console.log(err);
            })
        };

        var establishDetails = function(id) {
            FoundationService.find({id : id}, function(data) {
                $scope.foundation = data;
                $scope.grants = $scope.foundation.grants;

                fetchInteractions(id);
            }, function(err) {
                console.log(err);
            });
        };

        establishDetails($routeParams.id);

        $scope.toggleEditingBasicDetails = function() {
            $scope.editingBasicDetails = !$scope.editingBasicDetails
        };

        $scope.toggleEditingContactInfo = function() {
            $scope.editingContactInfo = !$scope.editingContactInfo
        };

        $scope.updateBasicDetails = function() {
            FoundationService.update({id: $scope.foundation.id}, $scope.foundation, function(succ) {
                $scope.requestSuccess = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);
                $scope.editingBasicDetails = false;
                establishDetails($scope.foundation.id);
            }, function(err) {
                var error = new ResponseErrorInterpreter(err);
                if (error.isConstraintViolation()) {
                    $scope.foundation.constraintViolation = error.message;
                }
            });
        };

        $scope.cancelUpdateBasicDetails = function() {
            $scope.editingBasicDetails = false;
            establishDetails($scope.foundation.id);
        };

        $scope.updateContactInfo = function() {
            FoundationService.update({id: $scope.foundation.id}, $scope.foundation, function(succ) {
                $scope.requestSuccess = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);
                $scope.editingContactInfo = false;
                establishDetails($scope.foundation.id);
            }, function(err) {
                var error = new ResponseErrorInterpreter(err);
                if (error.isConstraintViolation()) {
                    $scope.foundation.constraintViolation = error.message;
                }
            });
        };

            $scope.createNewGrant = function() {
                $location.path("/grants/create/"+$scope.foundation.id)
            };

        $scope.cancelUpdateContactInfo = function() {
            $scope.editingContactInfo = false;
            establishDetails($scope.foundation.id);
        };

            $scope.showInteractionForm = function() {
                $scope.creatingInteraction = true;
            };

            $scope.createInteractionRecord = function() {
                formatInteractionDates($scope.newInteraction);
                FoundationService.createInteractionRecord({id: $scope.foundation.id}, $scope.newInteraction, function(succ) {
                    fetchInteractions($scope.foundation.id);
                    $scope.cancelCreateInteractionRecord();
                    $scope.requestSuccess = true;
                    $timeout(function() {
                        $scope.requestSuccess = false;
                    }, 3000);
                }, function(err) {
                    console.log(err);
                });
            };

            $scope.cancelCreateInteractionRecord = function() {
                $scope.creatingInteraction = false;
                $scope.newInteraction = {};
            };

            var formatInteractionDates = function(interaction) {
                interaction.dateOfInteraction = DateFormatter.formatDate(interaction.dates.dateOfInteraction);
            };

        $scope.getInteractionTypeDescriptor = function(type) {
            if (null == type) {
                return "Unknown";
            }
            return type.name;
        };

        $scope.currentEntity = "Foundation";
        $scope.deleteWarning = "Deleting a Foundation will delete any Grants or Interaction Records associated with them. ";

        $scope.deleteCurrentEntity = function() {
            var deleteConfirmed = $window.confirm('Are you sure you want to delete this foundation?');
            if (deleteConfirmed) {
                FoundationService.delete({id: $scope.foundation.id}, function () {
                    $location.path("/foundations");
                }, function (err) {
                    console.log(err);
                });
            }
        }


    }]);

    controllers.controller('GrantListCtrl', ['$scope', '$routeParams', 'GrantService', 'FoundationService', 'DateFormatter',
        function($scope, $routeParams, GrantService, FoundationService, DateFormatter) {

        $scope.newGrant = {};
            $scope.unformatted = {};

        if ($routeParams.foundationId != null) {
            $scope.creatingGrant = true;
            $scope.preselectedFoundation = true;
            FoundationService.find({id: $routeParams.foundationId}, function(foundation) {
                $scope.newGrant.foundationId = foundation.id;
            }, function(err) {
                console.log(err);
            })
        }

        FoundationService.findAll({}, function(foundations) {
            $scope.foundations = foundations;
        }, function(err) {
            console.log(err);
        });

        var getGrants = function() {
            GrantService.findAll({}, function(grants) {
                $scope.grants = grants;
            }, function(err) {
              console.log(err);
            })
        };

        getGrants();

        $scope.createGrant = function() {

            $scope.newGrant.startPeriod = DateFormatter.formatDate($scope.unformatted.startPeriod);
            $scope.newGrant.endPeriod = DateFormatter.formatDate($scope.unformatted.endPeriod);
            $scope.newGrant.intentDeadline = DateFormatter.formatDate($scope.unformatted.intentDeadline);
            $scope.newGrant.applicationDeadline = DateFormatter.formatDate($scope.unformatted.applicationDeadline);
            $scope.newGrant.reportDeadline = DateFormatter.formatDate($scope.unformatted.reportDeadline);

            GrantService.create({foundationId: $scope.newGrant.foundationId}, $scope.newGrant, function(succ) {
                $scope.creatingGrant = false;
                $scope.newGrant = {};
                getGrants();
            }, function(err) {
                console.log(err);
            })
        };

        $scope.cancelCreateGrant = function() {
            $scope.newGrant = {};
            $scope.creatingGrant = false;
        };

    }]);

    controllers.controller('GrantDetailsCtrl', ['$scope', 'FoundationService', 'GrantService', '$routeParams', '$timeout', 'DateFormatter', '$window', function($scope, FoundationService, GrantService, $routeParams, $timeout, DateFormatter, $window) {

        var createGrantDates = function(grant) {
            grant.dates = {
                startPeriod: DateFormatter.asDate(grant.startPeriod),
                endPeriod: DateFormatter.asDate(grant.endPeriod),
                applicationDeadline: DateFormatter.asDate(grant.applicationDeadline),
                intentDeadline: DateFormatter.asDate(grant.intentDeadline),
                reportDeadline: DateFormatter.asDate(grant.reportDeadline)
            };
            return grant;
        };

        var convertGrantDatesToStrings = function(grant) {
            grant.startPeriod = DateFormatter.formatDate(grant.dates.startPeriod),
                grant.endPeriod = DateFormatter.formatDate(grant.dates.endPeriod),
                grant.applicationDeadline = DateFormatter.formatDate(grant.dates.applicationDeadline),
                grant.intentDeadline = DateFormatter.formatDate(grant.dates.intentDeadline),
                grant.reportDeadline = DateFormatter.formatDate(grant.dates.reportDeadline)
            return grant;
        };

        var establishDetails = function(id) {
            GrantService.find({id: id}, function(grant) {
                $scope.grant = createGrantDates(grant);
            }, function(err) {
                console.log(err);
            })
        };

        establishDetails($routeParams.id);

        $scope.updateGrantDetails = function() {
            $scope.grant = convertGrantDatesToStrings($scope.grant);
            GrantService.update({id: $scope.grant.id}, $scope.grant, function(succ) {
                $scope.editingGrantDetails = false;
                $scope.requestSuccess = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);
                establishDetails($scope.grant.id);
            }, function(err) {
                console.log(err);
            })
        };

        $scope.cancelUpdateGrantDetails = function() {
            $scope.editingGrantDetails = false;
            establishDetails($scope.grant.id);
        }

    }]);

    controllers.controller('InteractionDetailsCtrl', ['$scope', 'InteractionService', 'DateFormatter', '$timeout', '$routeParams', '$window', function($scope, InteractionService, DateFormatter, $timeout, $routeParams, $window) {

        var establishDetails = function(id) {
            InteractionService.find({id: id}, function(interaction) {
                $scope.interaction = interaction;
                $scope.interaction.dates = {};
                $scope.interaction.dates.dateOfInteraction = DateFormatter.asDate($scope.interaction.dateOfInteraction);
                console.log("Interaction: "+$scope.interaction.dateOfInteraction+" as Date: "+$scope.interaction.dates.dateOfInteraction)
            }, function(err) {
               console.log(err);
            });
        };

        establishDetails($routeParams.id);

        $scope.updateInteractionDetails = function() {
            $scope.interaction.dateOfInteraction = DateFormatter.formatDate($scope.interaction.dates.dateOfInteraction);
            InteractionService.update({id: $scope.interaction.id}, $scope.interaction, function(succ) {
                $scope.requestSuccess = true;
                $scope.editingInteractionDetails = false;
                establishDetails($scope.interaction.id);
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);
            }, function(err) {
                console.log(err);
            });
        };

        $scope.cancelUpdateInteractionDetails = function() {
            $scope.editingInteractionDetails = false;
            establishDetails($scope.interaction.id);
        };

        $scope.currentEntity = "Interaction Record";
        $scope.deleteWarning = "Deleting an Interaction Record will delete any Files associated with them.";

        $scope.deleteCurrentEntity = function() {
            var deleteConfirmed = $window.confirm('WARNING: Are you sure you want to delete this Interaction Record?');
            if (deleteConfirmed) {
                InteractionService.delete({id: $scope.interaction.id}, function () {
                    $window.history.back();
                }, function (err) {
                    console.log(err);
                });
            }
        };



    }]);


    controllers.controller('GrantListCtrl', ['$scope', '$routeParams', 'GrantService', 'FoundationService', 'DateFormatter',
        function($scope, $routeParams, GrantService, FoundationService, DateFormatter) {

        $scope.newGrant = {};
            $scope.unformatted = {};

        if ($routeParams.foundationId != null) {
            $scope.creatingGrant = true;
            $scope.preselectedFoundation = true;
            FoundationService.find({id: $routeParams.foundationId}, function(foundation) {
                $scope.newGrant.foundationId = foundation.id;
            }, function(err) {
                console.log(err);
            })
        }

        FoundationService.findAll({}, function(foundations) {
            $scope.foundations = foundations;
        }, function(err) {
            console.log(err);
        });

        var getGrants = function() {
            GrantService.findAll({}, function(grants) {
                $scope.grants = grants;
            }, function(err) {
              console.log(err);
            })
        };

        getGrants();

        $scope.createGrant = function() {

            $scope.newGrant.startPeriod = DateFormatter.formatDate($scope.unformatted.startPeriod);
            $scope.newGrant.endPeriod = DateFormatter.formatDate($scope.unformatted.endPeriod);
            $scope.newGrant.intentDeadline = DateFormatter.formatDate($scope.unformatted.intentDeadline);
            $scope.newGrant.applicationDeadline = DateFormatter.formatDate($scope.unformatted.applicationDeadline);
            $scope.newGrant.reportDeadline = DateFormatter.formatDate($scope.unformatted.reportDeadline);

            GrantService.create({foundationId: $scope.newGrant.foundationId}, $scope.newGrant, function(succ) {
                $scope.creatingGrant = false;
                $scope.newGrant = {};
                getGrants();
            }, function(err) {
                console.log(err);
            })
        };

        $scope.cancelCreateGrant = function() {
            $scope.newGrant = {};
            $scope.creatingGrant = false;
        };

    }]);

    controllers.controller('GrantDetailsCtrl', ['$scope', 'FoundationService', 'GrantService', '$routeParams', '$timeout', 'DateFormatter', '$window', function($scope, FoundationService, GrantService, $routeParams, $timeout, DateFormatter, $window) {

        var createGrantDates = function(grant) {
            grant.dates = {
                startPeriod: DateFormatter.asDate(grant.startPeriod),
                endPeriod: DateFormatter.asDate(grant.endPeriod),
                applicationDeadline: DateFormatter.asDate(grant.applicationDeadline),
                intentDeadline: DateFormatter.asDate(grant.intentDeadline),
                reportDeadline: DateFormatter.asDate(grant.reportDeadline)
            };
            return grant;
        };

        var convertGrantDatesToStrings = function(grant) {
            grant.startPeriod = DateFormatter.formatDate(grant.dates.startPeriod),
                grant.endPeriod = DateFormatter.formatDate(grant.dates.endPeriod),
                grant.applicationDeadline = DateFormatter.formatDate(grant.dates.applicationDeadline),
                grant.intentDeadline = DateFormatter.formatDate(grant.dates.intentDeadline),
                grant.reportDeadline = DateFormatter.formatDate(grant.dates.reportDeadline)
            return grant;
        };

        var establishDetails = function(id) {
            GrantService.find({id: id}, function(grant) {
                $scope.grant = createGrantDates(grant);
            }, function(err) {
                console.log(err);
            })
        };

        establishDetails($routeParams.id);

        $scope.updateGrantDetails = function() {
            $scope.grant = convertGrantDatesToStrings($scope.grant);
            GrantService.update({id: $scope.grant.id}, $scope.grant, function(succ) {
                $scope.editingGrantDetails = false;
                $scope.requestSuccess = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);
                establishDetails($scope.grant.id);
            }, function(err) {
                console.log(err);
            })
        };

        $scope.cancelUpdateGrantDetails = function() {
            $scope.editingGrantDetails = false;
            establishDetails($scope.grant.id);
        };

        $scope.currentEntity = "Grant";
        $scope.deleteWarning = "Deleting a Grant will delete any Files associated with them.";

        $scope.deleteCurrentEntity = function() {
            var deleteConfirmed = $window.confirm('WARNING: Are you sure you want to delete this Grant?');
            if (deleteConfirmed) {
                GrantService.delete({id: $scope.grant.id}, function () {
                    $window.history.back();
                }, function (err) {
                    console.log(err);
                });
            }
        };

    }]);

    controllers.controller('DonationListCtrl', ['$scope', 'DonationService', 'DateFormatter', '$location', function($scope, DonationService, DateFormatter, $location) {

        $scope.donationTable = {};

        $scope.queries = {
            byDepositDate: "By deposit date",
            byReceiptDate: "By receipt date",
            byBudgetItem: "By budget item"
        };

        $scope.activeQuery = "";

        $scope.donations = [];
        $scope.donationTable = {};
        $scope.currentPage = 0;

        $scope.selectedOptions = {
            fromDepositDate: new Date(),
            toDepositDate: new Date(),
            fromReceiptDate: new Date(),
            toReceiptDate: new Date(),
            budgetItemId : ""
        };

        $scope.parameters = {
            fromDepositDateParameter : "",
            toDepositDateParameter : "",
            fromReceiptDateParameter : "",
            toReceiptDateParameter : "",
            budgetitemIdParameter : ""
        };

        $scope.totalQueryElements = 0;
        $scope.numberElementsShown = 0;

        var defaultPageSize = 10;

        $scope.loadMoreDonations = function() {
            $scope.currentPage += 1;
            switch ($scope.activeQuery) {
                case $scope.queries.byDepositDate:
                    getDepositDatePage();
                    break;
                case $scope.queries.byReceiptDate:
                    getReceiptDatePage();
                    break;
                case $scope.queries.byBudgetItem:
                    getBudgetItemPage();
                    break;
                default:
                    break;
            }
        };

        var getReceiptDatePage = function() {
            var params = rangeQueryParameter($scope.parameters.fromReceiptDateParameter, $scope.parameters.toReceiptDateParameter, $scope.currentPage);
            DonationService.getDonationsByReceiptRange(params, mergePageResponse, logError);
        };

        var getDepositDatePage = function() {
            var params = rangeQueryParameter($scope.parameters.fromDepositDateParameter, $scope.parameters.toDepositDateParameter, $scope.currentPage);
            DonationService.getDonationsByDepositRange(params, mergePageResponse,logError);
        };

        var getBudgetItemPage = function() {
            var params = {item: $scope.parameters.budgetitemIdParameter, page:$scope.currentPage, size:defaultPageSize};
            DonationService.getDonationsByBudgetItem(params, mergePageResponse,logError);
        };

        //TODO find a smarter way to handle these various showing conditions
        $scope.showReceiptOptions = function() {
            $scope.showingReceiptDateQuery = !$scope.showingReceiptDateQuery;
            $scope.showingDepositDateQuery = false;
            $scope.showingBudgetItemQuery = false;
        };

        $scope.showDepositOptions = function() {
            $scope.showingDepositDateQuery = !$scope.showingDepositDateQuery;
            $scope.showingReceiptDateQuery = false;
            $scope.showingBudgetItemQuery = false;
        };

        $scope.showBudgetItemOptions = function() {
            $scope.showingBudgetItemQuery = !$scope.showingBudgetItemQuery;
            $scope.showingReceiptDateQuery = false;
            $scope.showingDepositDateQuery = false;

        };

        $scope.submitNewReceiptDateQuery = function() {
            $scope.activeQuery = $scope.queries.byReceiptDate;
            $scope.donationTable.orderingProperty = 'dateOfReceipt';
            $scope.donationTable.reverseSort = true;
            $scope.parameters.fromReceiptDateParameter = DateFormatter.formatDate($scope.selectedOptions.fromReceiptDate);
            $scope.parameters.toReceiptDateParameter = DateFormatter.formatDate($scope.selectedOptions.toReceiptDate);

            $scope.currentPage = 0;
            $scope.donations = [];

            getReceiptDatePage();
        };

        $scope.submitNewDepositDateQuery = function() {
            $scope.activeQuery = $scope.queries.byDepositDate;
            $scope.donationTable.orderingProperty = 'dateOfDeposit';
            $scope.donationTable.reverseSort = true;
            $scope.parameters.fromDepositDateParameter = DateFormatter.formatDate($scope.selectedOptions.fromDepositDate);
            $scope.parameters.toDepositDateParameter = DateFormatter.formatDate($scope.selectedOptions.toDepositDate);

            $scope.currentPage = 0;
            $scope.donations = [];

            getDepositDatePage();
        };

        $scope.submitNewBudgetItemQuery = function() {
            $scope.activeQuery = $scope.queries.byBudgetItem;
            $scope.donationTable.orderingProperty = 'budgetItem.name';
            $scope.donationTable.reverseSort = true;
            $scope.parameters.budgetitemIdParameter = $scope.selectedOptions.budgetItemId;

            $scope.currentPage = 0;
            $scope.donations = [];

            getBudgetItemPage();
        };

        var rangeQueryParameter = function(from, to, page, size) {
            if (null == page) {
                page = 0
            }
            if (null == size) {
                size = defaultPageSize
            }
            return {
                from: DateFormatter.formatDate(from),
                to: DateFormatter.formatDate(to),
                page: page,
                size: size
            }
        };

        var mergePageResponse = function(page) {
            $scope.donations = $scope.donations.concat(page.content);
            $scope.totalQueryElements = page.totalElements;
            $scope.numberElementsShown = $scope.donations.length;
        };

        var logError = function(err) {
            console.log(err);
        };

        var initialSetup = function() {
            $scope.activeQuery = $scope.queries.byDepositDate;
            $scope.selectedOptions.fromDepositDate = new Date(new Date().setDate(new Date().getDate()-30));
            $scope.selectedOptions.toDepositDate = new Date();
            $scope.submitNewDepositDateQuery();
        };

        DonationService.getBudgetItems({}, function(items) {
            $scope.budgetItems = items;
        }, logError);

        initialSetup();

    }]);

    controllers.controller('DonationDetailsCtrl', ['$scope', 'DonationService', '$routeParams', '$timeout', 'DateFormatter', '$window', function($scope, DonationService, $routeParams, $timeout, DateFormatter, $window) {

        $scope.modelHolder = {};
        $scope.formHolder = {};

        var establishDetails = function(id) {
            DonationService.find({id: id}, function(donation) {
                $scope.modelHolder.donationModel = donation;
                $scope.modelHolder.donationModel.dates = {
                    dateOfDeposit : DateFormatter.asDate(donation.dateOfDeposit),
                    dateOfReceipt : DateFormatter.asDate(donation.dateOfReceipt)
                };

                console.log($scope.modelHolder.donationModel);
                console.log(donation);
                if (null != donation.budgetItem) {
                    $scope.modelHolder.donationModel.budgetItemId = donation.budgetItem.id;
                }
            }, function(err) {
                console.log(err);
            })
        };

        establishDetails($routeParams.id);

        $scope.updateDonationDetails = function() {
            $scope.modelHolder.donationModel.dateOfReceipt = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfReceipt);
            $scope.modelHolder.donationModel.dateOfDeposit = DateFormatter.formatDate($scope.modelHolder.donationModel.dates.dateOfDeposit);

            DonationService.update({id: $scope.modelHolder.donationModel.id}, $scope.modelHolder.donationModel, function(succ) {
                $scope.editingDonationDetails = false;
                $scope.requestSuccess = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);
                establishDetails($scope.modelHolder.donationModel.id);
            }, function(err) {
                console.log(err);
            })
        };

        $scope.cancelUpdateDonationDetails = function() {
            $scope.editingDonationDetails = false;
            establishDetails($scope.modelHolder.donationModel.id);
        };

        $scope.deleteDonation = function() {
            var deleteConfirmed = $window.confirm('Are you sure you want to delete this donation?');
            if (deleteConfirmed) {
                DonationService.delete({id: $scope.modelHolder.donationModel.id}, function (succ) {
                    $window.history.back();
                }, function (err) {
                    console.log(err);
                })
            }
        }

    }]);

    controllers.controller('DonationTableCtrl', ['$scope', '$location', function($scope, $location) {

        $scope.viewDonationDetails = function(donation) {
            $location.path("/donations/"+donation.id);
        }

    }]);


    controllers.controller('DonorListCtrl', ['$scope', 'ContactService', 'DateFormatter', '$location', function($scope, ContactService, DateFormatter, $location) {

        var defaultPageSize = 10;

        $scope.contactTable = {
            quantity : defaultPageSize
        };

        $scope.queries = {
            byCurrentSustainer: "By current sustainer"
        };

        $scope.activeQuery = "";

        $scope.totalElements = 0;
        $scope.numberElementsShown = 0;

        $scope.viewMoreDonors = function() {
            $scope.contactTable.quantity += defaultPageSize;
            $scope.numberElementsShown = ($scope.contactTable.quantity >= $scope.totalElements) ? $scope.totalElements : $scope.contactTable.quantity;
        };

        var getAllCurrentSustainers = function() {
            ContactService.getAllCurrentSustainers({}, function(contacts) {
                $scope.contactTable.contacts = contacts;
                $scope.totalElements = $scope.contactTable.contacts.length;
                $scope.numberElementsShown = ($scope.contactTable.quantity >= $scope.totalElements) ? $scope.totalElements : $scope.contactTable.quantity;
            }, logError);
        };

        $scope.showCurrentSustainers = function() {
            $scope.showingCurrentSustainerQuery = !$scope.showingCurrentSustainerQuery;
            if ($scope.showingCurrentSustainerQuery) {
                $scope.activeQuery = $scope.queries.byCurrentSustainer;
                $scope.contactTable.orderByField = 'lastName';
                $scope.contactTable.reverseSort = true;

                getAllCurrentSustainers();
            }
        };

        var rangeQueryParameter = function(from, to, page, size) {
            if (null == page) {
                page = 0
            }
            if (null == size) {
                size = defaultPageSize
            }
            return {
                from: DateFormatter.formatDate(from),
                to: DateFormatter.formatDate(to),
                page: page,
                size: size
            }
        };

        var logError = function(err) {
            console.log(err);
        };

        var initialSetup = function() {
            $scope.showCurrentSustainers();
        };

        initialSetup();

    }]);

    controllers.controller('UserCtrl', ['$scope', '$rootScope', '$location', '$timeout', '$window', 'UserService', 'PermissionService',
        function ($scope, $rootScope, $location, $timeout, $window, UserService, PermissionService) {

        $scope.userPermissionLevel = PermissionService.getPermissionInterpreter($rootScope.user);
        $scope.newUser = {};
        $scope.passwordChange = {};
        $scope.viewingUser = true;
        $scope.violations = {};

        $scope.roles = ["ROLE_USER"];

        if ($scope.userPermissionLevel.isSuperUser()) {
            $scope.roles.push("ROLE_SUPERUSER");
        }
        
        if ($scope.userPermissionLevel.isElevatedUser()) {
            $scope.roles.push("ROLE_ELEVATED");
        }
        
        if ($scope.userPermissionLevel.isDevelopmentUser()) {
            $scope.roles.push("ROLE_DEVELOPMENT");
        }

        $scope.getUserList = function () {
            if ($scope.userPermissionLevel.isElevatedUser()) {
                UserService.findAll({}, function (users) {
                    $scope.users = users;
                }, function (err) {
                    console.log(err);
                })
            }
        };

        $scope.getUserList();

        $scope.viewInDetail = function (user) {
            $scope.userInDetail = user;
        };
        $scope.viewInDetail($rootScope.user);

        $scope.showNewUserForm = function () {
            $scope.creatingUser = true;
        };

        $scope.cancelNewUserForm = function () {
            $scope.violations = {};
            $scope.creatingUser = false;
            $scope.newUser = {};
        };

        $scope.createNewUser = function () {

            UserService.create({}, $scope.newUser, function (succ) {
                $scope.violations = {};
                $scope.requestSuccess = true;
                $timeout(function () {
                    $scope.requestSuccess = false;
                    $scope.creatingUser = false;
                    $scope.newUser = {}
                }, 3000);
                $scope.getUserList();
            }, function (err) {
                handleCrudError(err);
                console.log(err);
            })
        };

        var handleCrudError = function (err) {
            var errorResponse = new ResponseErrorInterpreter(err);
            if (errorResponse.isConstraintViolation()) {
                $scope.violations.constraintViolation = errorResponse.message;
            }
            if (errorResponse.isSecurityConstraint()) {
                $scope.violations.securityViolation = errorResponse.message;
            }
            $scope.requestFail = true;
            $timeout(function () {
                $scope.requestFail = false;
            }, 3000);
        };

        $scope.submitUpdate = function () {

            UserService.updateDetails({id: $scope.userInDetail.id}, $scope.userInDetail, function (succ) {
                $scope.requestSuccess = true;
                $scope.violations = {};
                $timeout(function () {
                    $scope.requestSuccess = false;
                    $scope.updatingUser = false;
                    $scope.viewingUser = true;
                }, 3000);
                UserService.find({id: $scope.userInDetail.id}, function (user) {
                    $scope.userInDetail = user;
                    $scope.getUserList();
                }, function (err) {
                    console.log(err);
                })
            }, function (err) {
                handleCrudError(err);
                console.log(err);
            })

        };

        $scope.cancelUpdate = function () {
            $scope.updatingUser = false;
            $scope.viewingUser = true;
            $scope.violations = {};
            UserService.find({id: $scope.userInDetail.id}, function (user) {
                $scope.userInDetail = user;
            }, function (err) {
                console.log(err);
            })
        };

        $scope.deleteUser = function (userId) {

            var deleteConfirmed = $window.confirm('Are you sure you want to delete this user?');
            if (deleteConfirmed) {
                UserService.delete({id: userId}, function (succ) {
                    $scope.viewInDetail($rootScope.user);
                    $scope.getUserList();
                }, function (err) {
                    handleCrudError(err);
                    console.log(err);
                })
            }

        };

        $scope.showPasswordChangeForm = function () {
            $scope.changingPassword = true;
            $scope.viewingUser = false;
        };

        $scope.submitPasswordChange = function () {
            UserService.changePassword({id: $scope.userInDetail.id}, $scope.passwordChange, function (succ) {
                $scope.violations = {};
                $scope.requestSuccess = true;
                $timeout(function () {
                    $scope.requestSuccess = false;
                    $scope.changingPassword = false;
                    $scope.viewingUser = true;
                    $scope.passwordChange = {};
                }, 3000);
            }, function (err) {
                handleCrudError(err);
                console.log(err);
            })
        };

        $scope.cancelPasswordChange = function () {
            $scope.violations = {};
            $scope.changingPassword = false;
            $scope.passwordChange = {};
        };

    }]);

    controllers.controller('TableCtrl', ['$scope', function ($scope) {
        $scope.exportData = function () {
            var blob = new Blob([document.getElementById('exportable').innerHTML], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
            });
            saveAs(blob, "table.xls");
        };
    }]);

    controllers.controller('SustainerPeriodDetailsCtrl', ['$scope', 'ContactService', '$routeParams', '$timeout', 'DateFormatter', 'RouteChangeService', '$window',
        function($scope, ContactService, $routeParams, $timeout, DateFormatter, RouteChangeService, $window) {

        $scope.contact = RouteChangeService.get();

        $scope.modelHolder = {};
        $scope.formHolder = {};

        $scope.updateSustainerPeriod = function() {
            $scope.modelHolder.sustainerPeriodModel.periodStartDate= DateFormatter.formatDate($scope.modelHolder.sustainerPeriodModel.dates.periodStartDate);
            if ($scope.modelHolder.sustainerPeriodModel.dates.cancelDate == null) {
                $scope.modelHolder.sustainerPeriodModel.cancelDate = null;
            } else {
                $scope.modelHolder.sustainerPeriodModel.cancelDate = DateFormatter.formatDate($scope.modelHolder.sustainerPeriodModel.dates.cancelDate);
            }

            console.log($scope.modelHolder.sustainerPeriodModel.dates.cancelDate);

            ContactService.updateSustainerPeriod({id: $scope.contact.id, entityId: $scope.modelHolder.sustainerPeriodModel.id}, $scope.modelHolder.sustainerPeriodModel, function(succ) {
                $scope.requestSuccess = true;
                $timeout(function() {
                    $scope.requestSuccess = false;
                }, 3000);
                $scope.cancelUpdateSustainerPeriod();
            }, function(err) {
                console.log(err);
            })
        };

        var establishDetails = function(id) {
            ContactService.getSustainerPeriod({id: $scope.contact.id, entityId: id}, function(period) {
                $scope.modelHolder.sustainerPeriodModel = period;
                console.log($scope.modelHolder.sustainerPeriodModel);
                $scope.modelHolder.sustainerPeriodModel.dates = {
                    periodStartDate : DateFormatter.asDate(period.periodStartDate),
                    cancelDate : DateFormatter.asDate(period.cancelDate)
                };
                if (null == period.cancelDate) {
                    $scope.modelHolder.sustainerPeriodModel.dates.cancelDate = null;
                }
            }, function(err) {
                console.log(err);
            })
        };

        establishDetails($routeParams.id);

        $scope.cancelUpdateSustainerPeriod = function() {
            $scope.editingSustainerPeriod = false;
            establishDetails($scope.modelHolder.sustainerPeriodModel.id);
        };

        $scope.closePeriod = function() {
            $scope.modelHolder.sustainerPeriodModel.dates.cancelDate = new Date();
            $scope.updateSustainerPeriod();
        };

        $scope.openPeriod = function() {
            $scope.modelHolder.sustainerPeriodModel.dates.cancelDate = null;
            $scope.updateSustainerPeriod();
        };

        $scope.deleteSustainerPeriod = function() {
            var deleteConfirmed = $window.confirm('Are you sure you want to delete this sustainer period?');
            if (deleteConfirmed) {
                ContactService.deleteSustainerPeriod({id: $scope.contact.id, entityId: $scope.modelHolder.sustainerPeriodModel.id}, function (succ) {
                    $window.history.back();
                }, function (err) {
                    console.log(err);
                })
            }
        }

    }]);

    controllers.controller('ContactTableCtrl', ['$scope', function($scope) {
        $scope.searchValue = "";
        $scope.orderByField = null;
        $scope.reverseSort = false;

        $scope.fields = [
            {label:"All", field:"ALL"},
            {label: "Assessment" , field:"assessment"},
            {label: "City" , field:"city"},
            {label: "Email" , field:"email"},
            {label: "First Name" , field:"firstName"},
            {label: "Last Name" , field:"lastName"},
            {label: "Middle Name" , field:"middleName"},
            {label: "Occupation" , field:"occupation"},
            {label: "Phone Number 1" , field:"phoneNumber1"},
            {label: "Phone Number 2" , field:"phoneNumber2"},
            {label: "Preferred Language" , field:"language"},
            {label: "State" , field:"state"},
            {label: "Street Address" , field:"streetAddress"},
            {label: "Zip Code" , field:"zipCode"}
        ];

        $scope.selectedField = $scope.fields[0].field;

    }]);

}());



