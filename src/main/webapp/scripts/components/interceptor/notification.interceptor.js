 'use strict';

angular.module('ligaBasket01App')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-ligaBasket01App-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-ligaBasket01App-params')});
                }
                return response;
            }
        };
    });
