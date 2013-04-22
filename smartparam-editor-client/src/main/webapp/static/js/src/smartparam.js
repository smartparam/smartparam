angular.module('smartparam.services', ['ngResource'])

var app = angular.module('smartparam', ['smartparam.services', 'ui.bootstrap.navbar']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'view/partials/home', controller: HomeController});
    $routeProvider.when('/param', {templateUrl: 'view/partials/param/paramList', controller: ParamListController});
}]);