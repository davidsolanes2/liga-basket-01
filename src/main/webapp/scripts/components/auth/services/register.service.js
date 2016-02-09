'use strict';

angular.module('ligaBasket01App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


