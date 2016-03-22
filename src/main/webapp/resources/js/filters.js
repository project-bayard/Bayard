(function () {
    'use strict';

    /* Filters */

    var filters = angular.module('filters', []);

    filters.filter('byMembers', function () {
        return function (collection) {

            var newCollection = angular.copy(collection);
            for (var i = 0; i < newCollection.length; i++) {

                if (newCollection[i].member == false) {
                    newCollection.splice(i,1);
                }
            }

            return newCollection;
        };
    });

    filters.filter('byAlreadyBelongsTo', function () {
        return function (collection, contactCollection) {

            if (collection == null && contactCollection == null ) {
                return [];

            } else if (collection == null) {
                return contactCollection;

            } else if (contactCollection == null) {
                return collection;
            }



            for (var i = 0; i < contactCollection.length; i++) {
                var item = contactCollection[i];

                for (var j = 0; j < collection.length; j++) {

                    var collectionItem = collection[j];

                    if(collectionItem.id == item.id) {
                        collection.splice(j,1);
                        break;
                    }
                }
            }

            return collection;
        };
    });

    filters.filter('removeItemById', function() {
       return function (collection, id) {

           if (collection == null) {
               return [];
           }

           for (var i = 0; i < collection.length; i++) {

               if (collection[i].id == id) {
                   collection.splice(i,1);
               }
           }

           return collection;
       }
    });

    filters.filter('byContactField', function() {
       return function (contacts, field, value) {

           var filtered = [];
           if (field == 'ALL') {
               angular.forEach(contacts, function(contact) {

                   for (var prop in contact) {
                       var val = contact[prop] + "";
                       if (val.indexOf(value) >= 0){
                           filtered.push(contact);
                           break;
                       }
                   }
               });

           } else {
               angular.forEach(contacts, function(contact) {
                   var contactField = contact[field] + "";

                   if(contactField.indexOf(value) >= 0) {
                       filtered.push(contact);
                   }
               });
           }

           return filtered;

       }
    });


}());