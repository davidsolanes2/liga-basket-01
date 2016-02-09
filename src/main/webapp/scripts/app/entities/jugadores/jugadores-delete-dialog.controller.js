'use strict';

angular.module('ligaBasket01App')
	.controller('JugadoresDeleteController', function($scope, $uibModalInstance, entity, Jugadores) {

        $scope.jugadores = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Jugadores.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
