
var module = angular.module('ui.bootstrap.linktab', []);

module.controller('LinkTabController', ['$scope', '$element', function($scope, $element) {
    var tabs = $scope.tabs = [];

    this.select = $scope.select = function selectTab(tab) {
        angular.forEach(tabs, function(tab) {
            tab.selected = false;
        });
        tab.selected = true;
    };

    this.addTab = function(tab) {
        if(!tabs.length) {
            $scope.select(tab);
        }
        tabs.push(tab);
    };

    this.removeTab = function removeTab(tab) {
        var index = tabs.indexOf(tab);
        tabs.splice(index, 1);

        if (tab.selected && tabs.length > 0) {
            $scope.select(tabs[index < tabs.length ? index : index-1]);
        }
    };
}]);

module.directive('linktabs', function() {
    return {
        restrict: 'E',
        replace: true,
        transclude: true,
        controller: 'LinkTabController',
        scope: {},
        template: '<div>' +
                    '<ul class="nav nav-tabs" ng-transclude>' +
                    '</ul>' +
                '</div>'
    };
})
.directive('linktab', ['$parse', function($parse) {
    return {
        restrict: 'E',
        require: '^linktabs',
        replace: true,
        transclude: true,
        scope: {
            location: '@'
        },
        template: '<li ng-class="{active: selected}">' +
                    '<a ng-href="{{location}}" ng-click="select()" ng-transclude></a>' +
                '</li>',
        link: function(scope, element, attrs, controller) {
            var getSelected, setSelected;
            scope.selected = false;
            scope.tab = scope;

            scope.select = function() {
                controller.select(scope);
            };

            if(attrs.active) {
                getSelected = $parse(attrs.active);
                setSelected = getSelected.assign;
                scope.$watch(
                    function watchSelected() {return getSelected(scope.$parent);},
                    function updateSelected(value) {scope.selected = value;}
                );
                scope.selected = getSelected ? getSelected(scope.$parent) : false;
            }

            scope.$watch('selected', function(selected) {
                if(selected) {
                    controller.select(scope);
                }
                if(setSelected) {
                    setSelected(scope.$parent, selected);
                }
            });

            controller.addTab(scope);
            scope.$on('$destroy', function() {
                controller.removeTab(scope);
            });
        }
    };
}]);
