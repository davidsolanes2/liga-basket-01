'use strict';

angular.module('ligaBasket01App').controller('JugadoresDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Jugadores',
        function($scope, $stateParams, $uibModalInstance, entity, Jugadores) {

        $scope.jugadores = entity;
        $scope.load = function(id) {
            Jugadores.get({id : id}, function(result) {
                $scope.jugadores = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ligaBasket01App:jugadoresUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.jugadores.id != null) {
                Jugadores.update($scope.jugadores, onSaveSuccess, onSaveError);
            } else {
                Jugadores.save($scope.jugadores, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
