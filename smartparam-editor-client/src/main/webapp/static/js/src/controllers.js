

function HomeController($scope) {
    
    return HomeController;
}

function ParamListController($scope, Param) {
    var self = this;
    $scope.params = [];

    this.paramsFetched = function(paramList) {
        $scope.params = paramList;
    };

    this.fetchParams = function() {
        Param.list(self.paramsFetched);
    };

    this.fetchParams();
    return ParamListController;
}