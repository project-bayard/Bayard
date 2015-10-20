# Bayard

A simple platform for community organizing.

Check us out on github.io: [project-bayard.github.io/Bayard]

# Change Log

## [October15Milestone] - 2015-10-20
### Added
- Addition of Groups, which define a set of Organizations, Committees, and/or Events
- Customizable options for certain demographic categories


## [September30Milestone] - 2015-07-26
### Added
- Client code presentation of constraint violation responses

### Fixed
- Default form action when creating new contacts
- Encounters: form set pristine on success or cancellation
- Encounters: dismissing the detail modal no longer breaks scrolling
- Authentication is maintained throughout browser session

## [August31stMilestone] - 2015-08-31
### Added
- Method-level authorization
- Back end validation of domain entities
- Front end validation
- EncounterTypes to support a dynamic, defined set of encounter types

### Changed
- Encounters now track whether a follow-up is needed
- Uniqueness is now enforced for several domain classes
- Refactored REST and service layer to proactively throw exceptions up to a global exception handler
- Refactored our Response class to accomodate exception handling strategy
- Reorganized the UI to us dropdowns in the nav bar
- Removed JsonIdentityInfo id generators from domain classes to simplify client's access to JSON object graph

### Fixed
- Various UI improvements

## [July29thMilestone] - 2015-07-29
### Added
- Basic superuser authentication
- Support for updates and deletes of core domain entities
- Migrated to PostgreSQL

### Changed
- Contact's address now includes State

### Fixed
- Various UI improvements

[project-bayard.github.io/Bayard]: http://project-bayard.github.io/Bayard/

[October15Milestone]: https://github.com/project-bayard/Bayard/compare/July29th2015Milestone...project-bayard:October15Milestone

[July29thMilestone]: https://github.com/ScottKimball/SMWC/compare/July8th2015Milestone...July29th2015Milestone

[August31stMilestone]: https://github.com/ScottKimball/SMWC/compare/July29th2015Milestone...August31Milestone

[September30Milestone]: https://github.com/ScottKimball/SMWC/compare/August31Milestone...September30Milestone