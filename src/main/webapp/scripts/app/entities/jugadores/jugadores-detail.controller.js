'use strict';

angular.module('ligaBasket01App')
    .controller('JugadoresDetailController', function ($scope, $rootScope, $stateParams, entity, Jugadores) {
        $scope.jugadores = entity;
        $scope.load = function (id) {
            Jugadores.get({id: id}, function(result) {
                $scope.jugadores = result;
            });
        };
        var unsubscribe = $rootScope.$on('ligaBasket01App:jugadoresUpdate', function(event, result) {
            $scope.jugadores = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
