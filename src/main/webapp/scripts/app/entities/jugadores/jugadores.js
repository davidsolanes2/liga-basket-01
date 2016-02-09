'use strict';

angular.module('ligaBasket01App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jugadores', {
                parent: 'entity',
                url: '/jugadoress',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ligaBasket01App.jugadores.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jugadores/jugadoress.html',
                        controller: 'JugadoresController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jugadores');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jugadores.detail', {
                parent: 'entity',
                url: '/jugadores/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ligaBasket01App.jugadores.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jugadores/jugadores-detail.html',
                        controller: 'JugadoresDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jugadores');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Jugadores', function($stateParams, Jugadores) {
                        return Jugadores.get({id : $stateParams.id});
                    }]
                }
            })
            .state('jugadores.new', {
                parent: 'jugadores',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jugadores/jugadores-dialog.html',
                        controller: 'JugadoresDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombreJugador: null,
                                    fechaNacimiento: null,
                                    numCanastas: null,
                                    numAsistencias: null,
                                    numRebotes: null,
                                    posicion: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('jugadores', null, { reload: true });
                    }, function() {
                        $state.go('jugadores');
                    })
                }]
            })
            .state('jugadores.edit', {
                parent: 'jugadores',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jugadores/jugadores-dialog.html',
                        controller: 'JugadoresDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Jugadores', function(Jugadores) {
                                return Jugadores.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jugadores', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('jugadores.delete', {
                parent: 'jugadores',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jugadores/jugadores-delete-dialog.html',
                        controller: 'JugadoresDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Jugadores', function(Jugadores) {
                                return Jugadores.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jugadores', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
