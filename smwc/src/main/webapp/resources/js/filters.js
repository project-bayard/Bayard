(function () {
    'use strict';

    /* Filters */

    var filters = angular.module('filters', []);

    filters.filter('byMembers', function () {
        return function (collection) {

            var newCollection = angular.copy(collection)
            for (var i = 0; i < newCollection.length; i++) {

                if (newCollection[i].member == false) {
                    newCollection.splice(i,1);
                }
            }

            return newCollection;
        };
    });

    filters.filter('byAlreadyMemberOf', function () {
        return function (collection, contactCollection) {
            if (contactCollection == null || contactCollection == 0) {
                return collection;
            }
            var newCollection = angular.copy(collection);


            for (var i = 0; i < contactCollection.length; i++) {
                var item = contactCollection[i];

                for (var j = 0; j < newCollection.length; j++) {

                    var collectionItem = newCollection[j];

                    if(collectionItem.id == item.id) {
                        newCollection.splice(j,1);
                        break;
                    }
                }
            }

            return newCollection;
        };
    });

    filters.filter('byAlreadyAttended', function() {
        return function(collection, contactCollection) {
            if (contactCollection == null || contactCollection == 0) {
                return collection;
            }
            var newCollection = angular.copy(collection);

        }
    })


}());