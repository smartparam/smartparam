
var services = angular.module('smartparam.services');

services.factory('Param', ['$resource', function($resource) {

    function Param() {
        
    }

    var parametersResource = $resource('/smartparam-demo/smartparam/param', {}, {
        list: {method:'POST', params: {}, isArray:true}
    });

    Param.list = function(callback) {
        parametersResource.list(callback);
    };

    return Param;
}]);